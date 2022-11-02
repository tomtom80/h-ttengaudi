package de.klingbeil.hutparty.iam.domain.model.identity;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GroupMemberService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public boolean confirmUser(Group aGroup, User aUser) {
        boolean userConfirmed = true;

        User confirmedUser =
                this.userRepository
                    .userWithUsername(aGroup.tenantId(), aUser.username());

        if (confirmedUser == null || !confirmedUser.isEnabled()) {
            userConfirmed = false;
        }

        return userConfirmed;
    }

    public boolean isMemberGroup(Group group, GroupMember groupMember) {
        boolean isMember = false;

        Iterator<GroupMember> iter =
            group.groupMembers().iterator();

        while (!isMember && iter.hasNext()) {
            GroupMember member = iter.next();
            if (member.isGroup()) {
                if (groupMember.equals(member)) {
                    isMember = true;
                } else {
                    // find recursion in nested groups
                    Group matchingGroup =
                        this.groupRepository
                            .groupNamed(member.tenantId(), member.name());
                    if (matchingGroup != null) {
                        isMember = this.isMemberGroup(matchingGroup, groupMember);
                    }
                }
            }
        }

        return isMember;
    }

    public boolean isUserInNestedGroup(Group aGroup, User aUser) {
        boolean isInNestedGroup = false;

        Iterator<GroupMember> iter =
            aGroup.groupMembers().iterator();

        while (!isInNestedGroup && iter.hasNext()) {
            GroupMember member = iter.next();
            if (member.isGroup()) {
                Group group =
                        this.groupRepository
                            .groupNamed(member.tenantId(), member.name());
                if (group != null) {
                    isInNestedGroup = group.isMember(aUser, this);
                }
            }
        }

        return isInNestedGroup;
    }
}
