package de.klingbeil.hutparty.iam.domain.model.identity;

import java.util.Date;

import de.klingbeil.hutparty.domain.model.DomainEvent;

public class GroupProvisioned implements DomainEvent {

    private int eventVersion;
    private String name;
    private Date occurredOn;
    private TenantId tenantId;

    public GroupProvisioned(TenantId aTenantId, String aName) {
        super();

        this.eventVersion = 1;
        this.name = aName;
        this.occurredOn = new Date();
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
    public Date occurredOn() {
        return this.occurredOn;
    }

    public TenantId tenantId() {
        return this.tenantId;
    }
}
