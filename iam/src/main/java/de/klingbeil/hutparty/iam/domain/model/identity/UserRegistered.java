package de.klingbeil.hutparty.iam.domain.model.identity;

import java.time.Instant;

import de.klingbeil.hutparty.domain.model.DomainEvent;

public class UserRegistered implements DomainEvent {

    private final EmailAddress emailAddress;
    private final int eventVersion;
    private final FullName name;
    private final Instant occurredOn;
    private final TenantId tenantId;
    private String username;

    public UserRegistered(
        TenantId tenantId,
        String username,
        FullName name,
        EmailAddress emailAddress) {

        super();

        this.emailAddress = emailAddress;
        this.eventVersion = 1;
        this.name = name;
        this.occurredOn = Instant.now();
        this.tenantId = tenantId;
        this.username = username;
    }

    public EmailAddress emailAddress() {
        return this.emailAddress;
    }

    @Override
    public int eventVersion() {
        return this.eventVersion;
    }

    public FullName name() {
        return this.name;
    }

    @Override
    public Instant occurredOn() {
        return this.occurredOn;
    }

    public TenantId tenantId() {
        return this.tenantId;
    }

    public String username() {
        return this.username;
    }
}
