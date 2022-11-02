package de.klingbeil.hutparty.iam.domain.model.access;

import java.time.Instant;

import de.klingbeil.hutparty.domain.model.DomainEvent;
import de.klingbeil.hutparty.iam.domain.model.identity.TenantId;

public class UserUnassignedFromRole implements DomainEvent {

    private final int eventVersion;
    private final Instant occurredOn;
    private final String roleName;
    private final TenantId tenantId;
    private final String username;

    public UserUnassignedFromRole(
        TenantId aTenantId,
        String aRoleName,
        String aUsername) {

        super();

        this.eventVersion = 1;
        this.occurredOn = Instant.now();
        this.roleName = aRoleName;
        this.tenantId = aTenantId;
        this.username = aUsername;
    }

    @Override
    public int eventVersion() {
        return this.eventVersion;
    }

    @Override
    public Instant occurredOn() {
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
