package de.klingbeil.hutparty.iam.domain.model.identity;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import de.klingbeil.hutparty.domain.model.ConcurrencySafeEntity;
import de.klingbeil.hutparty.domain.model.DomainEventPublisher;

public class Group extends ConcurrencySafeEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final String ROLE_GROUP_PREFIX = "ROLE-INTERNAL-GROUP: ";

    private String description;
    private Set<GroupMember> groupMembers;
    private String name;
    private TenantId tenantId;

    public Group(TenantId aTenantId, String aName, String aDescription) {
        this();

        this.setDescription(aDescription);
        this.setName(aName);
        this.setTenantId(aTenantId);
    }

    public void addGroup(Group group, GroupMemberService groupMemberService) {
        this.assertArgumentNotNull(group, "Group must not be null.");
        this.assertArgumentEquals(this.tenantId(), group.tenantId(), "Wrong tenant for this group.");
        this.assertArgumentFalse(groupMemberService.isMemberGroup(group, this.toGroupMember()), "Group recurrsion.");

        if (this.groupMembers().add(group.toGroupMember()) && !this.isInternalGroup()) {
            DomainEventPublisher
                .instance()
                .publish(new GroupGroupAdded(
                    this.tenantId(),
                    this.name(),
                    group.name()));
        }
    }

    public void addUser(User aUser) {
        this.assertArgumentNotNull(aUser, "User must not be null.");
        this.assertArgumentEquals(this.tenantId(), aUser.tenantId(), "Wrong tenant for this group.");
        this.assertArgumentTrue(aUser.isEnabled(), "User is not enabled.");

        if (this.groupMembers().add(aUser.toGroupMember()) && !this.isInternalGroup()) {
            DomainEventPublisher
                .instance()
                .publish(new GroupUserAdded(
                    this.tenantId(),
                    this.name(),
                    aUser.username()));
        }
    }

    public String description() {
        return this.description;
    }

    public Set<GroupMember> groupMembers() {
        return this.groupMembers;
    }

    public boolean isMember(User aUser, GroupMemberService aGroupMemberService) {
        this.assertArgumentNotNull(aUser, "User must not be null.");
        this.assertArgumentEquals(this.tenantId(), aUser.tenantId(), "Wrong tenant for this group.");
        this.assertArgumentTrue(aUser.isEnabled(), "User is not enabled.");

        boolean isMember =
            this.groupMembers().contains(aUser.toGroupMember());

        if (isMember) {
            isMember = aGroupMemberService.confirmUser(this, aUser);
        } else {
            isMember = aGroupMemberService.isUserInNestedGroup(this, aUser);
        }

        return isMember;
    }

    public String name() {
        return this.name;
    }

    public void removeGroup(Group aGroup) {
        this.assertArgumentNotNull(aGroup, "Group must not be null.");
        this.assertArgumentEquals(this.tenantId(), aGroup.tenantId(), "Wrong tenant for this group.");

        // not a nested remove, only direct member
        if (this.groupMembers().remove(aGroup.toGroupMember()) && !this.isInternalGroup()) {
            DomainEventPublisher
                .instance()
                .publish(new GroupGroupRemoved(
                    this.tenantId(),
                    this.name(),
                    aGroup.name()));
        }
    }

    public void removeUser(User aUser) {
        this.assertArgumentNotNull(aUser, "User must not be null.");
        this.assertArgumentEquals(this.tenantId(), aUser.tenantId(), "Wrong tenant for this group.");

        // not a nested remove, only direct member
        if (this.groupMembers().remove(aUser.toGroupMember()) && !this.isInternalGroup()) {
            DomainEventPublisher
                .instance()
                .publish(new GroupUserRemoved(
                    this.tenantId(),
                    this.name(),
                    aUser.username()));
        }
    }

    public TenantId tenantId() {
        return this.tenantId;
    }

    @Override
    public boolean equals(Object anObject) {
        boolean equalObjects = false;

        if (anObject != null && this.getClass() == anObject.getClass()) {
            Group typedObject = (Group) anObject;
            equalObjects =
                this.tenantId().equals(typedObject.tenantId()) &&
                    this.name().equals(typedObject.name());
        }

        return equalObjects;
    }

    @Override
    public int hashCode() {
        return (2061 * 193)
                + this.tenantId().hashCode()
                + this.name().hashCode();
    }

    @Override
    public String toString() {
        return "Group [description=" + description + ", name=" + name + ", tenantId=" + tenantId + "]";
    }

    protected Group() {
        super();

        this.setGroupMembers(new HashSet<>(0));
    }

    protected void setDescription(String aDescription) {
        this.assertArgumentNotEmpty(aDescription, "Group description is required.");
        this.assertArgumentLength(aDescription, 1, 250, "Group description must be 250 characters or less.");

        this.description = aDescription;
    }

    protected void setGroupMembers(Set<GroupMember> aGroupMembers) {
        this.groupMembers = aGroupMembers;
    }

    protected boolean isInternalGroup() {
        return this.isInternalGroup(this.name());
    }

    protected boolean isInternalGroup(String aName) {
        return aName.startsWith(ROLE_GROUP_PREFIX);
    }

    protected void setName(String aName) {
        this.assertArgumentNotEmpty(aName, "Group name is required.");
        this.assertArgumentLength(aName, 1, 100, "Group name must be 100 characters or less.");

        if (this.isInternalGroup(aName)) {
            String uuid = aName.substring(ROLE_GROUP_PREFIX.length());

            try {
                UUID.fromString(uuid);
            } catch (Exception e) {
                throw new IllegalArgumentException("The group name has an invalid format.");
            }
        }

        this.name = aName;
    }

    protected void setTenantId(TenantId aTenantId) {
        this.assertArgumentNotNull(aTenantId, "The tenantId must be provided.");

        this.tenantId = aTenantId;
    }

    protected GroupMember toGroupMember() {
        return new GroupMember(
                this.tenantId(),
                this.name(),
                GroupMemberType.Group);
    }
}
