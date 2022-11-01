package de.klingbeil.hutparty.iam.domain.model.access;

import de.klingbeil.hutparty.AssertionConcern;
import de.klingbeil.hutparty.iam.domain.model.identity.GroupMemberService;
import de.klingbeil.hutparty.iam.domain.model.identity.GroupRepository;
import de.klingbeil.hutparty.iam.domain.model.identity.TenantId;
import de.klingbeil.hutparty.iam.domain.model.identity.User;
import de.klingbeil.hutparty.iam.domain.model.identity.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthorizationService extends AssertionConcern {
    private final GroupRepository groupRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public boolean isUserInRole(TenantId aTenantId, String aUsername, String aRoleName) {
        this.assertArgumentNotNull(aTenantId, "TenantId must not be null.");
        this.assertArgumentNotEmpty(aUsername, "Username must not be provided.");
        this.assertArgumentNotEmpty(aRoleName, "Role name must not be null.");

        User user = userRepository.userWithUsername(aTenantId, aUsername);

        return !(user == null) && this.isUserInRole(user, aRoleName);
    }

    public boolean isUserInRole(User aUser, String aRoleName) {
        this.assertArgumentNotNull(aUser, "User must not be null.");
        this.assertArgumentNotEmpty(aRoleName, "Role name must not be null.");

        boolean authorized = false;

        if (aUser.isEnabled()) {
            Role role = this.roleRepository.roleNamed(aUser.tenantId(), aRoleName);

            if (role != null) {
                GroupMemberService groupMemberService =
                    new GroupMemberService(
                        userRepository,
                        groupRepository);

                authorized = role.isInRole(aUser, groupMemberService);
            }
        }

        return authorized;
    }
}
