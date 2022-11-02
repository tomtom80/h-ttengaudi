package de.klingbeil.hutparty.iam.domain.model.identity;

import java.time.Instant;

import de.klingbeil.hutparty.domain.model.DomainEvent;

public class UserActivationChanged implements DomainEvent {

    private final Activation activation;
    private final int eventVersion;
    private final Instant occurredOn;
    private final TenantId tenantId;
    private final String username;

    public UserActivationChanged(
        TenantId tenantId,
        String username,
        Activation activation) {

        super();

        this.activation = activation;
        this.eventVersion = 1;
        this.occurredOn = Instant.now();
        this.tenantId = tenantId;
        this.username = username;
    }

    public Activation activation() {
        return this.activation;
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
