package de.klingbeil.hutparty.iam.domain.model.access;

import java.time.Instant;

import de.klingbeil.hutparty.domain.model.DomainEvent;
import de.klingbeil.hutparty.iam.domain.model.identity.TenantId;

public class GroupAssignedToRole implements DomainEvent {

    private final int eventVersion;
    private final String groupName;
    private final Instant occurredOn;
    private final String roleName;
    private final TenantId tenantId;

    public GroupAssignedToRole(TenantId aTenantId, String aRoleName, String aGroupName) {
        super();

        this.eventVersion = 1;
        this.groupName = aGroupName;
        this.occurredOn = Instant.now();
        this.roleName = aRoleName;
        this.tenantId = aTenantId;
    }

    @Override
    public int eventVersion() {
        return this.eventVersion;
    }

    public String groupName() {
        return this.groupName;
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
}
