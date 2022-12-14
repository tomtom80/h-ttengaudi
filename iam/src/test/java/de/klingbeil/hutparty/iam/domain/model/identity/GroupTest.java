package de.klingbeil.hutparty.iam.domain.model.identity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import de.klingbeil.hutparty.domain.model.DomainEventPublisher;
import de.klingbeil.hutparty.domain.model.DomainEventSubscriber;
import de.klingbeil.hutparty.iam.domain.model.DomainRegistry;
import de.klingbeil.hutparty.iam.domain.model.IdentityAccessTest;
import de.klingbeil.hutparty.iam.domain.model.access.Role;
import de.klingbeil.hutparty.iam.infrastructure.TestConfig;
import de.klingbeil.hutparty.iam.infrastructure.test.DomainEventTestExtension;

@SpringBootTest
@Import(TestConfig.class)
@ExtendWith(DomainEventTestExtension.class)
class GroupTest extends IdentityAccessTest {

    private int groupGroupAddedCount;
    private int groupGroupRemovedCount;
    private int groupUserAddedCount;
    private int groupUserRemovedCount;

    public GroupTest() {
        super();
    }

    @Test
    void testProvisionGroup() {
        Tenant tenant = this.tenantAggregate();

        Group groupA = tenant.provisionGroup("GroupA", "A group named GroupA");
        DomainRegistry.groupRepository().add(groupA);

        assertEquals(1, DomainRegistry.groupRepository().allGroups(tenant.tenantId()).size());
    }

    @Test
    void testAddGroup() {
        DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<GroupGroupAdded>() {
            @Override
            public void handleEvent(GroupGroupAdded domainEvent) {
                ++groupGroupAddedCount;
            }

            @Override
            public Class<GroupGroupAdded> subscribedToEventType() {
                return GroupGroupAdded.class;
            }
        });
        Tenant tenant = this.tenantAggregate();

        Group groupA = tenant.provisionGroup("GroupA", "A group named GroupA");
        DomainRegistry.groupRepository().add(groupA);
        Group groupB = tenant.provisionGroup("GroupB", "A group named GroupB");
        DomainRegistry.groupRepository().add(groupB);
        groupA.addGroup(groupB, DomainRegistry.groupMemberService());

        assertEquals(1, groupA.groupMembers().size());
        assertEquals(0, groupB.groupMembers().size());
        assertEquals(1, groupGroupAddedCount);
    }

    @Test
    void testAddUser() throws Exception {
        DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<GroupUserAdded>() {
            @Override
            public void handleEvent(GroupUserAdded domainEvent) {
                ++groupUserAddedCount;
            }

            @Override
            public Class<GroupUserAdded> subscribedToEventType() {
                return GroupUserAdded.class;
            }
        });
        Tenant tenant = this.tenantAggregate();
        Group groupA = tenant.provisionGroup("GroupA", "A group named GroupA");
        User user = this.userAggregate();
        DomainRegistry.userRepository().add(user);

        groupA.addUser(user);
        DomainRegistry.groupRepository().add(groupA);

        assertEquals(1, groupA.groupMembers().size());
        assertTrue(groupA.isMember(user, DomainRegistry.groupMemberService()));
        assertEquals(1, groupUserAddedCount);
    }

    @Test
    void testRemoveGroup() {
        DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<GroupGroupRemoved>() {
            @Override
            public void handleEvent(GroupGroupRemoved domainEvent) {
                ++groupGroupRemovedCount;
            }

            @Override
            public Class<GroupGroupRemoved> subscribedToEventType() {
                return GroupGroupRemoved.class;
            }
        });
        Tenant tenant = this.tenantAggregate();
        Group groupA = tenant.provisionGroup("GroupA", "A group named GroupA");
        DomainRegistry.groupRepository().add(groupA);
        Group groupB = tenant.provisionGroup("GroupB", "A group named GroupB");
        DomainRegistry.groupRepository().add(groupB);
        groupA.addGroup(groupB, DomainRegistry.groupMemberService());
        assertEquals(1, groupA.groupMembers().size());

        groupA.removeGroup(groupB);

        assertEquals(0, groupA.groupMembers().size());
        assertEquals(1, groupGroupRemovedCount);
    }

    @Test
    void testRemoveUser() throws Exception {
        DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<GroupUserRemoved>() {
            @Override
            public void handleEvent(GroupUserRemoved domainEvent) {
                ++groupUserRemovedCount;
            }

            @Override
            public Class<GroupUserRemoved> subscribedToEventType() {
                return GroupUserRemoved.class;
            }
        });
        Tenant tenant = this.tenantAggregate();
        Group groupA = tenant.provisionGroup("GroupA", "A group named GroupA");
        User user = this.userAggregate();
        DomainRegistry.userRepository().add(user);
        groupA.addUser(user);
        DomainRegistry.groupRepository().add(groupA);
        assertEquals(1, groupA.groupMembers().size());

        groupA.removeUser(user);

        assertEquals(0, groupA.groupMembers().size());
        assertEquals(1, groupUserRemovedCount);
    }

    @Test
    void testRemoveGroupReferencedUser() throws Exception {
        Tenant tenant = this.tenantAggregate();
        Group groupA = tenant.provisionGroup("GroupA", "A group named GroupA");
        User user = this.userAggregate();
        DomainRegistry.userRepository().add(user);
        groupA.addUser(user);
        DomainRegistry.groupRepository().add(groupA);
        assertEquals(1, groupA.groupMembers().size());
        assertTrue(groupA.isMember(user, DomainRegistry.groupMemberService()));

        DomainRegistry.userRepository().remove(user);

        Group reGrouped =
            DomainRegistry
                .groupRepository()
                .groupNamed(tenant.tenantId(), "GroupA");
        assertEquals("GroupA", reGrouped.name());
        assertEquals(1, reGrouped.groupMembers().size());
        assertFalse(reGrouped.isMember(user, DomainRegistry.groupMemberService()));
    }

    @Test
    void testRepositoryRemoveGroup() {
        Tenant tenant = this.tenantAggregate();
        Group groupA = tenant.provisionGroup("GroupA", "A group named GroupA");
        DomainRegistry.groupRepository().add(groupA);
        Group notNullGroup =
            DomainRegistry
                .groupRepository()
                .groupNamed(tenant.tenantId(), "GroupA");
        assertNotNull(notNullGroup);

        DomainRegistry.groupRepository().remove(groupA);

        Group nullGroup =
            DomainRegistry
                .groupRepository()
                .groupNamed(tenant.tenantId(), "GroupA");
        assertNull(nullGroup);
    }

    @Test
    void testUserIsMemberOfNestedGroup() throws Exception {
        DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<GroupGroupAdded>() {
            @Override
            public void handleEvent(GroupGroupAdded domainEvent) {
                ++groupGroupAddedCount;
            }

            @Override
            public Class<GroupGroupAdded> subscribedToEventType() {
                return GroupGroupAdded.class;
            }
        });
        Tenant tenant = this.tenantAggregate();
        Group groupA = tenant.provisionGroup("GroupA", "A group named GroupA");
        DomainRegistry.groupRepository().add(groupA);
        Group groupB = tenant.provisionGroup("GroupB", "A group named GroupB");
        DomainRegistry.groupRepository().add(groupB);
        groupA.addGroup(groupB, DomainRegistry.groupMemberService());
        User user = this.userAggregate();
        DomainRegistry.userRepository().add(user);

        groupB.addUser(user);

        assertTrue(groupB.isMember(user, DomainRegistry.groupMemberService()));
        assertTrue(groupA.isMember(user, DomainRegistry.groupMemberService()));
        assertEquals(1, groupGroupAddedCount);
    }

    @Test
    void testUserIsNotMember() throws Exception {
        User user = this.userAggregate();
        DomainRegistry.userRepository().add(user);
        // tests alternate creation via constructor
        Group groupA = new Group(user.tenantId(), "GroupA", "A group named GroupA");
        DomainRegistry.groupRepository().add(groupA);
        Group groupB = new Group(user.tenantId(), "GroupB", "A group named GroupB");
        DomainRegistry.groupRepository().add(groupB);
        groupA.addGroup(groupB, DomainRegistry.groupMemberService());

        assertFalse(groupB.isMember(user, DomainRegistry.groupMemberService()));
        assertFalse(groupA.isMember(user, DomainRegistry.groupMemberService()));
    }

    @Test
    void testNoRecursiveGroupings() throws Exception {
        DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<GroupGroupAdded>() {
            @Override
            public void handleEvent(GroupGroupAdded domainEvent) {
                ++groupGroupAddedCount;
            }

            @Override
            public Class<GroupGroupAdded> subscribedToEventType() {
                return GroupGroupAdded.class;
            }
        });
        User user = this.userAggregate();
        DomainRegistry.userRepository().add(user);
        // tests alternate creation via constructor
        Group groupA = new Group(user.tenantId(), "GroupA", "A group named GroupA");
        DomainRegistry.groupRepository().add(groupA);
        Group groupB = new Group(user.tenantId(), "GroupB", "A group named GroupB");
        DomainRegistry.groupRepository().add(groupB);
        Group groupC = new Group(user.tenantId(), "GroupC", "A group named GroupC");
        DomainRegistry.groupRepository().add(groupC);
        groupA.addGroup(groupB, DomainRegistry.groupMemberService());
        groupB.addGroup(groupC, DomainRegistry.groupMemberService());

        try {
            groupC.addGroup(groupA, DomainRegistry.groupMemberService());
            fail("no recursive groups");
        } catch (Exception e) {
            assertEquals("Group recurrsion.", e.getMessage());
        }

        assertEquals(2, groupGroupAddedCount);
    }

    @Test
    void testNoRoleInternalGroupsInFindAllGroups(){
        Tenant tenant = this.tenantAggregate();
        Group groupA = tenant.provisionGroup("GroupA", "A group named GroupA");
        DomainRegistry.groupRepository().add(groupA);
        Role roleA = tenant.provisionRole("RoleA", "A role of A.");
        DomainRegistry.roleRepository().add(roleA);
        Role roleB = tenant.provisionRole("RoleB", "A role of B.");
        DomainRegistry.roleRepository().add(roleB);
        Role roleC = tenant.provisionRole("RoleC", "A role of C.");
        DomainRegistry.roleRepository().add(roleC);

        Collection<Group> groups =
            DomainRegistry
                .groupRepository()
                .allGroups(tenant.tenantId());

        assertEquals(1, groups.size());
    }
}
