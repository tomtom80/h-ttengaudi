package de.klingbeil.hutparty.iam.domain.model.identity;

import de.klingbeil.hutparty.domain.model.DomainEventPublisher;
import de.klingbeil.hutparty.iam.domain.model.access.Role;
import de.klingbeil.hutparty.iam.domain.model.access.RoleRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TenantProvisioningService {

    private final RoleRepository roleRepository;
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;

    private final PasswordService passwordService;

    public Tenant provisionTenant(
        String aTenantName,
        String aTenantDescription,
        FullName anAdministorName,
        EmailAddress anEmailAddress,
        PostalAddress aPostalAddress,
        Telephone aPrimaryTelephone,
        Telephone aSecondaryTelephone) {

        try {
            Tenant tenant = new Tenant(
                this.tenantRepository.nextIdentity(),
                aTenantName,
                aTenantDescription,
                true); // must be active to register admin

            this.tenantRepository.add(tenant);

            this.registerAdministratorFor(
                tenant,
                anAdministorName,
                anEmailAddress,
                aPostalAddress,
                aPrimaryTelephone,
                aSecondaryTelephone);

            DomainEventPublisher
                .instance()
                .publish(new TenantProvisioned(
                    tenant.tenantId()));

            return tenant;

        } catch (Throwable t) {
            throw new IllegalStateException(
                "Cannot provision tenant because: "
                    + t.getMessage());
        }
    }

    private void registerAdministratorFor(
        Tenant aTenant,
        FullName anAdministorName,
        EmailAddress anEmailAddress,
        PostalAddress aPostalAddress,
        Telephone aPrimaryTelephone,
        Telephone aSecondaryTelephone) {

        RegistrationInvitation invitation =
            aTenant.offerRegistrationInvitation("init").openEnded();

        String strongPassword =
            passwordService
                .generateStrongPassword();

        User admin =
            aTenant.registerUser(
                invitation.invitationId(),
                "admin",
                strongPassword,
                Enablement.indefiniteEnablement(),
                new Person(
                    aTenant.tenantId(),
                    anAdministorName,
                    new ContactInformation(
                        anEmailAddress,
                        aPostalAddress,
                        aPrimaryTelephone,
                        aSecondaryTelephone)));

        aTenant.withdrawInvitation(invitation.invitationId());

        this.userRepository.add(admin);

        Role adminRole =
            aTenant.provisionRole(
                "Administrator",
                "Default " + aTenant.name() + " administrator.");

        adminRole.assignUser(admin);

        this.roleRepository.add(adminRole);

        DomainEventPublisher.instance().publish(
            new TenantAdministratorRegistered(
                aTenant.tenantId(),
                aTenant.name(),
                anAdministorName,
                anEmailAddress,
                admin.username(),
                strongPassword));
    }

}
