package de.klingbeil.hutparty.iam.domain.model.access;

import java.time.Instant;

import de.klingbeil.hutparty.domain.model.DomainEvent;
import de.klingbeil.hutparty.iam.domain.model.identity.TenantId;

public class UserAssignedToRole implements DomainEvent {

    private final String emailAddress;
    private final int eventVersion;
    private final String firstName;
    private final String lastName;
    private final Instant occurredOn;
    private final String roleName;
    private final TenantId tenantId;
    private final String username;

    public UserAssignedToRole(
        TenantId aTenantId,
        String aRoleName,
        String aUsername,
        String aFirstName,
        String aLastName,
        String anEmailAddress) {

        super();

        this.emailAddress = anEmailAddress;
        this.eventVersion = 1;
        this.firstName = aFirstName;
        this.lastName = aLastName;
        this.occurredOn = Instant.now();
        this.roleName = aRoleName;
        this.tenantId = aTenantId;
        this.username = aUsername;
    }

    public String emailAddress() {
        return this.emailAddress;
    }

    @Override
    public int eventVersion() {
        return this.eventVersion;
    }

    public String firstName() {
        return this.firstName;
    }

    public String lastName() {
        return this.lastName;
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
