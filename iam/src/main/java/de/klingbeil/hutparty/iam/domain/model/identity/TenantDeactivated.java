package de.klingbeil.hutparty.iam.domain.model.identity;

import java.time.Instant;

import de.klingbeil.hutparty.domain.model.DomainEvent;

public class TenantDeactivated implements DomainEvent {

    private final int eventVersion;
    private final Instant occurredOn;
    private final TenantId tenantId;

    public TenantDeactivated(TenantId aTenantId) {
        super();

        this.eventVersion = 1;
        this.occurredOn = Instant.now();
        this.tenantId = aTenantId;
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
}
