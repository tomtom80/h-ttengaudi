package de.klingbeil.hutparty.iam.domain.model.identity;

import java.time.Instant;

import de.klingbeil.hutparty.domain.model.DomainEvent;

public class TenantAdministratorRegistered implements DomainEvent {

    private final FullName administratorName;
    private final EmailAddress emailAddress;
    private final int eventVersion;
    private final Instant occurredOn;
    private final String temporaryPassword;
    private final TenantId tenantId;
    private final String tenantName;
    private final String username;

    public TenantAdministratorRegistered(
            TenantId aTenantId,
            String aTenantName,
            FullName anAdministratorName,
            EmailAddress anEmailAddress,
            String aUsername,
            String aTemporaryPassword) {

        super();

        this.administratorName = anAdministratorName;
        this.emailAddress = anEmailAddress;
        this.eventVersion = 1;
        this.occurredOn = Instant.now();
        this.temporaryPassword = aTemporaryPassword;
        this.tenantId = aTenantId;
        this.tenantName = aTenantName;
        this.username = aUsername;
    }

    public FullName administratorName() {
        return this.administratorName;
    }

    public EmailAddress emailAddress() {
        return this.emailAddress;
    }

    @Override
    public int eventVersion() {
        return this.eventVersion;
    }

    @Override
    public Instant occurredOn() {
        return this.occurredOn;
    }

    public String temporaryPassword() {
        return this.temporaryPassword;
    }

    public TenantId tenantId() {
        return this.tenantId;
    }

    public String getTenantName() {
        return this.tenantName;
    }

    public String username() {
        return this.username;
    }
}
