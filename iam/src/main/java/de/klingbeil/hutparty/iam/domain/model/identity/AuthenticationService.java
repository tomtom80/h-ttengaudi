package de.klingbeil.hutparty.iam.domain.model.identity;

import de.klingbeil.hutparty.AssertionConcern;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthenticationService extends AssertionConcern {

    private final EncryptionService encryptionService;
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;

    public UserDescriptor authenticate(
        TenantId aTenantId,
        String aUsername,
        String aPassword) {

        this.assertArgumentNotNull(aTenantId, "TenantId must not be null.");
        this.assertArgumentNotEmpty(aUsername, "Username must be provided.");
        this.assertArgumentNotEmpty(aPassword, "Password must be provided.");

        UserDescriptor userDescriptor = UserDescriptor.nullDescriptorInstance();

        Tenant tenant = this.tenantRepository.tenantOfId(aTenantId);

        if (tenant != null && tenant.isActive()) {
            String encryptedPassword = this.encryptionService.encryptedValue(aPassword);

            User user =
                this.userRepository
                    .userFromAuthenticCredentials(
                        aTenantId,
                        aUsername,
                        encryptedPassword);

            if (user != null && user.isEnabled()) {
                userDescriptor = user.userDescriptor();
            }
        }

        return userDescriptor;
    }
}
