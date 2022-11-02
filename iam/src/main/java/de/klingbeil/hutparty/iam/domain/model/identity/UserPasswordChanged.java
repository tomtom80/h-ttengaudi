package de.klingbeil.hutparty.iam.domain.model.identity;

import java.time.Instant;

import de.klingbeil.hutparty.domain.model.DomainEvent;

public class UserPasswordChanged implements DomainEvent {

    private final int eventVersion;
    private final Instant occurredOn;
    private final TenantId tenantId;
    private final String username;

    public UserPasswordChanged(TenantId tenantId, String username) {
        super();

        this.eventVersion = 1;
        this.occurredOn = Instant.now();
        this.tenantId = tenantId;
        this.username = username;
    }

    @Override
    public int eventVersion() {
        return this.eventVersion;
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
