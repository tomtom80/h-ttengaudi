package de.klingbeil.hutparty.iam.domain.model.identity;

import java.time.Instant;

import de.klingbeil.hutparty.domain.model.DomainEvent;

public class GroupUserAdded implements DomainEvent {

    private final int eventVersion;
    private final String groupName;
    private final Instant occurredOn;
    private final TenantId tenantId;
    private final String username;

    public GroupUserAdded(TenantId aTenantId, String aGroupName, String aUsername) {
        super();

        this.eventVersion = 1;
        this.groupName = aGroupName;
        this.occurredOn = Instant.now();
        this.tenantId = aTenantId;
        this.username = aUsername;
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

    public TenantId tenantId() {
        return this.tenantId;
    }

    public String username() {
        return this.username;
    }
}
