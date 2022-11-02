package de.klingbeil.hutparty.iam.domain.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import de.klingbeil.hutparty.iam.domain.model.identity.Activation;
import de.klingbeil.hutparty.iam.domain.model.identity.ContactInformation;
import de.klingbeil.hutparty.iam.domain.model.identity.EmailAddress;
import de.klingbeil.hutparty.iam.domain.model.identity.FullName;
import de.klingbeil.hutparty.iam.domain.model.identity.Person;
import de.klingbeil.hutparty.iam.domain.model.identity.PostalAddress;
import de.klingbeil.hutparty.iam.domain.model.identity.RegistrationInvitation;
import de.klingbeil.hutparty.iam.domain.model.identity.Telephone;
import de.klingbeil.hutparty.iam.domain.model.identity.Tenant;
import de.klingbeil.hutparty.iam.domain.model.identity.TenantId;
import de.klingbeil.hutparty.iam.domain.model.identity.User;

public abstract class IdentityAccessTest {

    protected static final String FIXTURE_PASSWORD = "TopSecret!";
    protected static final String FIXTURE_TENANT_DESCRIPTION = "This is a test tenant.";
    protected static final String FIXTURE_TENANT_NAME = "Test Tenant";
    protected static final String FIXTURE_USER_EMAIL_ADDRESS = "jdoe@hutparty.de";
    protected static final String FIXTURE_USER_EMAIL_ADDRESS2 = "zdoe@hutparty.de";
    protected static final String FIXTURE_USERNAME = "jdoe";
    protected static final String FIXTURE_USERNAME2 = "zdoe";
    protected static final long TWENTY_FOUR_HOURS = (1000L * 60L * 60L * 24L);

    private Tenant tenant;

    public IdentityAccessTest() {
        super();
    }

    protected ContactInformation contactInformation() {
        return
            new ContactInformation(
                new EmailAddress(FIXTURE_USER_EMAIL_ADDRESS),
                new PostalAddress(
                    "123 Pearl Street",
                    "Boulder",
                    "CO",
                    "80301",
                    "US"),
                new Telephone("303-555-1210"),
                new Telephone("303-555-1212"));
    }

    protected Instant dayAfterTomorrow() {
        return this.today().plus(TWENTY_FOUR_HOURS * 2, ChronoUnit.HOURS);
    }

    protected Instant dayBeforeYesterday() {
        return today().minus(TWENTY_FOUR_HOURS * 2, ChronoUnit.HOURS);
    }

    protected Person personEntity(Tenant aTenant) {
        return new Person(
            aTenant.tenantId(),
            new FullName("John", "Doe"),
            this.contactInformation());
    }

    protected Person personEntity2(Tenant aTenant) {
        return new Person(
            aTenant.tenantId(),
            new FullName("Zoe", "Doe"),
            new ContactInformation(
                new EmailAddress(FIXTURE_USER_EMAIL_ADDRESS2),
                new PostalAddress(
                    "123 Pearl Street",
                    "Boulder",
                    "CO",
                    "80301",
                    "US"),
                new Telephone("303-555-1210"),
                new Telephone("303-555-1212")));
    }

    protected RegistrationInvitation registrationInvitationEntity(Tenant aTenant)  {

        Instant today = Instant.now().truncatedTo(ChronoUnit.DAYS);

        Instant tomorrow = today.plus(TWENTY_FOUR_HOURS, ChronoUnit.HOURS);

        return aTenant.offerRegistrationInvitation("Today-and-Tomorrow: " + System.nanoTime())
            .startingOn(today)
            .until(tomorrow);
    }

    protected Tenant tenantAggregate() {

        if (this.tenant == null) {
            TenantId tenantId =
                DomainRegistry.tenantRepository().nextIdentity();

            this.tenant =
                new Tenant(
                    tenantId,
                    FIXTURE_TENANT_NAME,
                    FIXTURE_TENANT_DESCRIPTION,
                    true);

            DomainRegistry.tenantRepository().add(tenant);
        }

        return this.tenant;
    }

    protected Instant today() {
        return Instant.now().truncatedTo(ChronoUnit.DAYS);
    }

    protected Instant tomorrow() {
        return today().plus(TWENTY_FOUR_HOURS, ChronoUnit.HOURS);
    }

    protected User userAggregate() throws Exception {
        Tenant tenant = this.tenantAggregate();

        RegistrationInvitation registrationInvitation =
            this.registrationInvitationEntity(tenant);

        return tenant.registerUser(
            registrationInvitation.invitationId(),
            FIXTURE_USERNAME,
            FIXTURE_PASSWORD,
            Activation.indefiniteActivation(),
            this.personEntity(tenant));
    }

    protected User userAggregate2() throws Exception {
        Tenant tenant = this.tenantAggregate();

        RegistrationInvitation registrationInvitation =
            this.registrationInvitationEntity(tenant);

        return tenant.registerUser(
            registrationInvitation.invitationId(),
            FIXTURE_USERNAME2,
            FIXTURE_PASSWORD,
            Activation.indefiniteActivation(),
            this.personEntity2(tenant));
    }

    protected Instant yesterday() {
        return today().minus(TWENTY_FOUR_HOURS, ChronoUnit.HOURS);
    }
}
