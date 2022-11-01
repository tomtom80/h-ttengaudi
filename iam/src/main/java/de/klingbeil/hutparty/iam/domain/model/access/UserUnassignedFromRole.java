package de.klingbeil.hutparty.iam.domain.model.access;

import java.util.Date;

import de.klingbeil.hutparty.domain.model.DomainEvent;
import de.klingbeil.hutparty.iam.domain.model.identity.TenantId;

public class UserUnassignedFromRole implements DomainEvent {

    private int eventVersion;
    private Date occurredOn;
    private String roleName;
    private TenantId tenantId;
    private String username;

    public UserUnassignedFromRole(
        TenantId aTenantId,
        String aRoleName,
        String aUsername) {

        super();

        this.eventVersion = 1;
        this.occurredOn = new Date();
        this.roleName = aRoleName;
        this.tenantId = aTenantId;
        this.username = aUsername;
    }

    @Override
    public int eventVersion() {
        return this.eventVersion;
    }

    @Override
    public Date occurredOn() {
        return this.occurredOn;
    }

    public String roleName() {
        return this.roleName;
    }

    public TenantId tenantId() {
        return this.tenantId;
    }

    public String username() {
        return this.username;
    }
}
