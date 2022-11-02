package de.klingbeil.hutparty.iam.domain.model.access;

import java.time.Instant;

import de.klingbeil.hutparty.domain.model.DomainEvent;
import de.klingbeil.hutparty.iam.domain.model.identity.TenantId;

public class RoleProvisioned implements DomainEvent {

    private final int eventVersion;
    private final String name;
    private final Instant occurredOn;
    private final TenantId tenantId;

    public RoleProvisioned(TenantId aTenantId, String aName) {
        super();

        this.eventVersion = 1;
        this.name = aName;
        this.occurredOn =Instant.now();
        this.tenantId = aTenantId;
    }

    @Override
    public int eventVersion() {
        return this.eventVersion;
    }

    public String name() {
        return this.name;
    }

    @Override
    public Instant occurredOn() {
        return this.occurredOn;
    }

    public TenantId tenantId() {
        return this.tenantId;
    }
}
