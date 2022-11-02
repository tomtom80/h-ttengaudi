package de.klingbeil.hutparty.iam.domain.model.identity;

import java.time.Instant;

import de.klingbeil.hutparty.domain.model.DomainEvent;

public class PersonNameChanged implements DomainEvent {

    private final int eventVersion;
    private final FullName name;
    private final Instant occurredOn;
    private final TenantId tenantId;
    private final String username;

    public PersonNameChanged(TenantId tenantId, String username, FullName name) {
        super();

        this.eventVersion = 1;
        this.name = name;
        this.occurredOn = Instant.now();
        this.tenantId = tenantId;
        this.username = username;
    }

    @Override
    public int eventVersion() {
        return this.eventVersion;
    }

    public FullName name() {
        return this.name;
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
