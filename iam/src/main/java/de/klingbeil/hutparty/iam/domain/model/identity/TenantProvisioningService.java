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
        String tenantName,
        String tenantDescription,
        FullName administratorName,
        EmailAddress emailAddress,
        PostalAddress postalAddress,
        Telephone primaryTelephone,
        Telephone secondaryTelephone) {

        try {
            Tenant tenant = new Tenant(
                this.tenantRepository.nextIdentity(),
                tenantName,
                tenantDescription,
                true); // must be active to register admin

            this.tenantRepository.add(tenant);

            this.registerAdministratorFor(
                tenant,
                administratorName,
                emailAddress,
                postalAddress,
                primaryTelephone,
                secondaryTelephone);

            DomainEventPublisher
                .instance()
                .publish(new TenantProvisioned(
                    tenant.tenantId()));

            return tenant;

        } catch (Exception e) {
            throw new IllegalStateException(
                "Cannot provision tenant because: "
                    + e.getMessage());
        }
    }

    private void registerAdministratorFor(
        Tenant tenant,
        FullName administratorName,
        EmailAddress emailAddress,
        PostalAddress postalAddress,
        Telephone primaryTelephone,
        Telephone secondaryTelephone) {

        RegistrationInvitation invitation =
            tenant.offerRegistrationInvitation("init").openEnded();

        String strongPassword =
            passwordService
                .generateStrongPassword();

        User admin =
            tenant.registerUser(
                invitation.invitationId(),
                "admin",
                strongPassword,
                Activation.indefiniteActivation(),
                new Person(
                    tenant.tenantId(),
                    administratorName,
                    new ContactInformation(
                        emailAddress,
                        postalAddress,
                        primaryTelephone,
                        secondaryTelephone)));

        tenant.withdrawInvitation(invitation.invitationId().id());

        this.userRepository.add(admin);

        Role adminRole =
            tenant.provisionRole(
                "Administrator",
                "Default " + tenant.name() + " administrator.");

        adminRole.assignUser(admin);

        this.roleRepository.add(adminRole);

        DomainEventPublisher.instance().publish(
            new TenantAdministratorRegistered(
                tenant.tenantId(),
                tenant.name(),
                administratorName,
                emailAddress,
                admin.username(),
                strongPassword));
    }

}
