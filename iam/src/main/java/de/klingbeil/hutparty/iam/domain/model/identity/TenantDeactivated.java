package de.klingbeil.hutparty.iam.domain.model.identity;

import java.util.Date;

import de.klingbeil.hutparty.domain.model.DomainEvent;

public class TenantDeactivated implements DomainEvent {

    private int eventVersion;
    private Date occurredOn;
    private TenantId tenantId;

    public TenantDeactivated(TenantId aTenantId) {
        super();

        this.eventVersion = 1;
        this.occurredOn = new Date();
        this.tenantId = aTenantId;
    }

    @Override
    public int eventVersion() {
        return this.eventVersion;
    }

    @Override
    public Date occurredOn() {
        return this.occurredOn;
    }

    public TenantId tenantId() {
        return this.tenantId;
    }
}
