package de.klingbeil.hutparty.iam.domain.model.identity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import de.klingbeil.hutparty.domain.model.DomainEventPublisher;
import de.klingbeil.hutparty.domain.model.DomainEventSubscriber;
import de.klingbeil.hutparty.iam.domain.model.DomainRegistry;
import de.klingbeil.hutparty.iam.domain.model.IdentityAccessTest;
import de.klingbeil.hutparty.iam.infrastructure.TestConfig;
import de.klingbeil.hutparty.iam.infrastructure.test.DomainEventTestExtension;

@SpringBootTest
@Import(TestConfig.class)
@ExtendWith(DomainEventTestExtension.class)
class TenantTest extends IdentityAccessTest {

    private boolean provisionedCalled;
    private boolean adminRegisteredCalled;

    @Test
    void testProvisionTenant() {
        DomainEventPublisher
            .instance()
            .subscribe(new DomainEventSubscriber<TenantProvisioned>() {
                public void handleEvent(TenantProvisioned aDomainEvent) {
                    provisionedCalled = true;
                }

                public Class<TenantProvisioned> subscribedToEventType() {
                    return TenantProvisioned.class;
                }
            });
        DomainEventPublisher
            .instance()
            .subscribe(new DomainEventSubscriber<TenantAdministratorRegistered>() {
                public void handleEvent(TenantAdministratorRegistered aDomainEvent) {
                    adminRegisteredCalled = true;
                }

                public Class<TenantAdministratorRegistered> subscribedToEventType() {
                    return TenantAdministratorRegistered.class;
                }
            });

        Tenant tenant =
            DomainRegistry.
                tenantProvisioningService()
                .provisionTenant(
                    FIXTURE_TENANT_NAME,
                    FIXTURE_TENANT_DESCRIPTION,
                    new FullName("John", "Doe"),
                    new EmailAddress(FIXTURE_USER_EMAIL_ADDRESS),
                    new PostalAddress(
                        "123 Pearl Street",
                        "Boulder",
                        "CO",
                        "80301",
                        "US"),
                    new Telephone("303-555-1210"),
                    new Telephone("303-555-1212"));

        assertTrue(provisionedCalled);
        assertTrue(adminRegisteredCalled);
        assertNotNull(tenant.tenantId());
        assertNotNull(tenant.tenantId().value());
        assertEquals(36, tenant.tenantId().id().length());
        assertEquals(FIXTURE_TENANT_NAME, tenant.name());
        assertEquals(FIXTURE_TENANT_DESCRIPTION, tenant.description());
    }

    @Test
    void testCreateOpenEndedInvitation() {
        Tenant tenant = this.tenantAggregate();

        tenant
            .offerRegistrationInvitation("Open-Ended")
            .openEnded();

        assertNotNull(tenant.redefineRegistrationInvitationAs("Open-Ended"));
    }

    @Test
    void testOpenEndedInvitationAvailable() {
        Tenant tenant = this.tenantAggregate();

        tenant
            .offerRegistrationInvitation("Open-Ended")
            .openEnded();

        assertTrue(tenant.isRegistrationAvailableThrough("Open-Ended"));
    }

    @Test
    void testClosedEndedInvitationAvailable() {
        Tenant tenant = this.tenantAggregate();

        tenant
            .offerRegistrationInvitation("Today-and-Tomorrow")
            .startingOn(this.today())
            .until(this.tomorrow());

        assertTrue(tenant.isRegistrationAvailableThrough("Today-and-Tomorrow"));
    }

    @Test
    void testClosedEndedInvitationNotAvailable() {
        Tenant tenant = this.tenantAggregate();

        tenant
            .offerRegistrationInvitation("Tomorrow-and-Day-After-Tomorrow")
            .startingOn(this.tomorrow())
            .until(this.dayAfterTomorrow());

        assertFalse(tenant.isRegistrationAvailableThrough("Tomorrow-and-Day-After-Tomorrow"));
    }

    @Test
    void testAvailableInvitationDescriptor() {
        Tenant tenant = this.tenantAggregate();
        tenant
            .offerRegistrationInvitation("Open-Ended")
            .openEnded();

        tenant
            .offerRegistrationInvitation("Today-and-Tomorrow")
            .startingOn(this.today())
            .until(this.tomorrow());

        assertEquals(2, tenant.allAvailableRegistrationInvitations().size());
    }

    @Test
    void testUnavailableInvitationDescriptor() {
        Tenant tenant = this.tenantAggregate();

        tenant
            .offerRegistrationInvitation("Tomorrow-and-Day-After-Tomorrow")
            .startingOn(this.tomorrow())
            .until(this.dayAfterTomorrow());

        assertEquals(1, tenant.allUnavailableRegistrationInvitations().size());
    }

    @Test
    void testRegisterUser() throws Exception {
        Tenant tenant = this.tenantAggregate();
        RegistrationInvitation registrationInvitation =
            this.registrationInvitationEntity(tenant);

        User user =
            tenant.registerUser(
                registrationInvitation.invitationId(),
                FIXTURE_USERNAME,
                FIXTURE_PASSWORD,
                new Activation(true, null, null),
                this.personEntity(tenant));

        assertNotNull(user);
        DomainRegistry.userRepository().add(user);
        assertNotNull(user.activation());
        assertNotNull(user.person());
        assertNotNull(user.userDescriptor());
    }
}
