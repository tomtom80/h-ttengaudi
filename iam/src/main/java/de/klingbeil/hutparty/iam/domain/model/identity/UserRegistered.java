package de.klingbeil.hutparty.iam.domain.model.identity;

import java.util.Date;

import de.klingbeil.hutparty.domain.model.DomainEvent;

public class UserRegistered implements DomainEvent {

    private EmailAddress emailAddress;
    private int eventVersion;
    private FullName name;
    private Date occurredOn;
    private TenantId tenantId;
    private String username;

    public UserRegistered(
        TenantId aTenantId,
        String aUsername,
        FullName aName,
        EmailAddress anEmailAddress) {

        super();

        this.emailAddress = anEmailAddress;
        this.eventVersion = 1;
        this.name = aName;
        this.occurredOn = new Date();
        this.tenantId = aTenantId;
        this.username = aUsername;
    }

    public EmailAddress emailAddress() {
        return this.emailAddress;
    }

    @Override
    public int eventVersion() {
        return this.eventVersion;
    }

    public FullName name() {
        return this.name;
    }

    @Override
    public Date occurredOn() {
        return this.occurredOn;
    }

    public TenantId tenantId() {
        return this.tenantId;
    }

    public String username() {
        return this.username;
    }
}
