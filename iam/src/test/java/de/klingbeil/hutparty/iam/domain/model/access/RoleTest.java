package de.klingbeil.hutparty.iam.domain.model.access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import de.klingbeil.hutparty.domain.model.DomainEventPublisher;
import de.klingbeil.hutparty.domain.model.DomainEventSubscriber;
import de.klingbeil.hutparty.iam.domain.model.DomainRegistry;
import de.klingbeil.hutparty.iam.domain.model.IdentityAccessTest;
import de.klingbeil.hutparty.iam.domain.model.identity.Group;
import de.klingbeil.hutparty.iam.domain.model.identity.GroupGroupAdded;
import de.klingbeil.hutparty.iam.domain.model.identity.GroupGroupRemoved;
import de.klingbeil.hutparty.iam.domain.model.identity.GroupUserAdded;
import de.klingbeil.hutparty.iam.domain.model.identity.GroupUserRemoved;
import de.klingbeil.hutparty.iam.domain.model.identity.Tenant;
import de.klingbeil.hutparty.iam.domain.model.identity.User;
import de.klingbeil.hutparty.iam.infrastructure.TestConfig;
import de.klingbeil.hutparty.iam.infrastructure.test.DomainEventTestExtension;

@SpringBootTest
@Import(TestConfig.class)
@ExtendWith(DomainEventTestExtension.class)
class RoleTest extends IdentityAccessTest {

    private int groupSomethingAddedCount;
    private int groupSomethingRemovedCount;
    private int roleSomethingAssignedCount;
    private int roleSomethingUnassignedCount;

    public RoleTest() {
        super();
    }

    @Test
    void testProvisionRole() {
        Tenant tenant = this.tenantAggregate();

        Role role = tenant.provisionRole("Manager", "A manager role.");
        DomainRegistry.roleRepository().add(role);

        assertEquals(1, DomainRegistry.roleRepository().allRoles(tenant.tenantId()).size());
    }

    @Test
    void testRoleUniqueness() {
        Tenant tenant = this.tenantAggregate();
        Role role1 = tenant.provisionRole("Manager", "A manager role.");
        DomainRegistry.roleRepository().add(role1);

        try {
            Role role2 = tenant.provisionRole("Manager", "A manager role.");
            DomainRegistry.roleRepository().add(role2);
            fail("Should have thrown exception for nonuniqueness.");
        } catch (IllegalStateException e) {
            assertEquals("Duplicate key.", e.getMessage());
        }
    }

    @Test
    void testUserIsInRole() {
        Tenant tenant = this.tenantAggregate();
        User user = this.userAggregate();
        DomainRegistry.userRepository().add(user);
        Role managerRole = tenant.provisionRole("Manager", "A manager role.", true);
        Group group = new Group(user.tenantId(), "Managers", "A group of managers.");
        DomainRegistry.groupRepository().add(group);
        managerRole.assignGroup(group, DomainRegistry.groupMemberService());
        DomainRegistry.roleRepository().add(managerRole);
        group.addUser(user);

        assertTrue(group.isMember(user, DomainRegistry.groupMemberService()));
        assertTrue(managerRole.isInRole(user, DomainRegistry.groupMemberService()));
    }

    @Test
    void testUserIsNotInRole() {
        Tenant tenant = this.tenantAggregate();
        User user = this.userAggregate();
        DomainRegistry.userRepository().add(user);
        Role managerRole = tenant.provisionRole("Manager", "A manager role.", true);
        Group group = tenant.provisionGroup("Managers", "A group of managers.");
        DomainRegistry.groupRepository().add(group);
        managerRole.assignGroup(group, DomainRegistry.groupMemberService());
        DomainRegistry.roleRepository().add(managerRole);
        Role accountantRole = new Role(user.tenantId(), "Accountant", "An accountant role.");
        DomainRegistry.roleRepository().add(accountantRole);

        assertFalse(managerRole.isInRole(user, DomainRegistry.groupMemberService()));
        assertFalse(accountantRole.isInRole(user, DomainRegistry.groupMemberService()));
    }

    @Test
    void testNoRoleInternalGroupsInFindGroupByName() {
        Tenant tenant = this.tenantAggregate();
        Role roleA = tenant.provisionRole("RoleA", "A role of A.");
        DomainRegistry.roleRepository().add(roleA);

        try {
            DomainRegistry
                .groupRepository()
                .groupNamed(
                    tenant.tenantId(),
                    roleA.group().name());
            fail("Should have thrown exception for invalid group name.");
        } catch (Exception e) {
           assertEquals("May not find internal groups.", e.getMessage());
        }
    }

    @Test
    void testInternalGroupAddedEventsNotPublished() {
        DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<GroupAssignedToRole>() {
            @Override
            public void handleEvent(GroupAssignedToRole domainEvent) {
                ++roleSomethingAssignedCount;
            }

            @Override
            public Class<GroupAssignedToRole> subscribedToEventType() {
                return GroupAssignedToRole.class;
            }
        });
        DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<GroupGroupAdded>() {
            @Override
            public void handleEvent(GroupGroupAdded domainEvent) {
                ++groupSomethingAddedCount;
            }

            @Override
            public Class<GroupGroupAdded> subscribedToEventType() {
                return GroupGroupAdded.class;
            }
        });
        DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<UserAssignedToRole>() {
            @Override
            public void handleEvent(UserAssignedToRole domainEvent) {
                ++roleSomethingAssignedCount;
            }

            @Override
            public Class<UserAssignedToRole> subscribedToEventType() {
                return UserAssignedToRole.class;
            }
        });
        DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<GroupUserAdded>() {
            @Override
            public void handleEvent(GroupUserAdded domainEvent) {
                ++groupSomethingAddedCount;
            }

            @Override
            public Class<GroupUserAdded> subscribedToEventType() {
                return GroupUserAdded.class;
            }
        });
        Tenant tenant = this.tenantAggregate();
        User user = this.userAggregate();
        DomainRegistry.userRepository().add(user);
        Role managerRole = tenant.provisionRole("Manager", "A manager role.", true);
        Group group = new Group(user.tenantId(), "Managers", "A group of managers.");
        DomainRegistry.groupRepository().add(group);
        managerRole.assignGroup(group, DomainRegistry.groupMemberService());
        managerRole.assignUser(user);
        DomainRegistry.roleRepository().add(managerRole);
        group.addUser(user); // legal add

        assertEquals(2, roleSomethingAssignedCount);
        assertEquals(1, groupSomethingAddedCount);
    }

    @Test
    void testInternalGroupRemovedEventsNotPublished() {
        DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<GroupUnassignedFromRole>() {
            @Override
            public void handleEvent(GroupUnassignedFromRole domainEvent) {
                ++roleSomethingUnassignedCount;
            }

            @Override
            public Class<GroupUnassignedFromRole> subscribedToEventType() {
                return GroupUnassignedFromRole.class;
            }
        });
        DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<GroupGroupRemoved>() {
            @Override
            public void handleEvent(GroupGroupRemoved domainEvent) {
                ++groupSomethingRemovedCount;
            }

            @Override
            public Class<GroupGroupRemoved> subscribedToEventType() {
                return GroupGroupRemoved.class;
            }
        });
        DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<UserUnassignedFromRole>() {
            @Override
            public void handleEvent(UserUnassignedFromRole domainEvent) {
                ++roleSomethingUnassignedCount;
            }

            @Override
            public Class<UserUnassignedFromRole> subscribedToEventType() {
                return UserUnassignedFromRole.class;
            }
        });
        DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<GroupUserRemoved>() {
            @Override
            public void handleEvent(GroupUserRemoved domainEvent) {
                ++groupSomethingRemovedCount;
            }

            @Override
            public Class<GroupUserRemoved> subscribedToEventType() {
                return GroupUserRemoved.class;
            }
        });

        Tenant tenant = this.tenantAggregate();
        User user = this.userAggregate();
        DomainRegistry.userRepository().add(user);
        Role managerRole = tenant.provisionRole("Manager", "A manager role.", true);
        Group group = new Group(user.tenantId(), "Managers", "A group of managers.");
        DomainRegistry.groupRepository().add(group);
        managerRole.assignUser(user);
        managerRole.assignGroup(group, DomainRegistry.groupMemberService());
        DomainRegistry.roleRepository().add(managerRole);

        managerRole.unassignUser(user);
        managerRole.unassignGroup(group);

        assertEquals(2, roleSomethingUnassignedCount);
        assertEquals(0, groupSomethingRemovedCount);
    }
}
