package de.klingbeil.hutparty.iam.domain.model.identity;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import de.klingbeil.hutparty.domain.model.ConcurrencySafeEntity;
import de.klingbeil.hutparty.domain.model.DomainEventPublisher;
import de.klingbeil.hutparty.iam.domain.model.access.Role;
import de.klingbeil.hutparty.iam.domain.model.access.RoleProvisioned;

public class Tenant extends ConcurrencySafeEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private boolean active;
    private String description;
    private String name;
    private Set<RegistrationInvitation> registrationInvitations;
    private TenantId tenantId;

    public Tenant(TenantId tenantId, String name, String description, boolean active) {
        this();

        this.setActive(active);
        this.setDescription(description);
        this.setName(name);
        this.setTenantId(tenantId);
    }

    protected Tenant() {
        super();

        this.setRegistrationInvitations(new HashSet<>(0));
    }

    public void activate() {
        if (!this.isActive()) {

            this.setActive(true);

            DomainEventPublisher
                .instance()
                .publish(new TenantActivated(this.tenantId()));
        }
    }

    public Collection<InvitationDescriptor> allAvailableRegistrationInvitations() {
        this.assertStateTrue(this.isActive(), "Tenant is not active.");

        return this.allRegistrationInvitationsFor(true);
    }

    public Collection<InvitationDescriptor> allUnavailableRegistrationInvitations() {
        this.assertStateTrue(this.isActive(), "Tenant is not active.");

        return this.allRegistrationInvitationsFor(false);
    }

    public void deactivate() {
        if (this.isActive()) {

            this.setActive(false);

            DomainEventPublisher
                .instance()
                .publish(new TenantDeactivated(this.tenantId()));
        }
    }

    public String description() {
        return this.description;
    }

    public boolean isActive() {
        return this.active;
    }

    protected void setActive(boolean active) {
        this.active = active;
    }

    public boolean isRegistrationAvailableThrough(String invitationIdentifier) {
        this.assertStateTrue(this.isActive(), "Tenant is not active.");

        RegistrationInvitation invitation =
            this.invitation(invitationIdentifier);

        return invitation != null && invitation.isAvailable();
    }

    public String name() {
        return this.name;
    }

    public RegistrationInvitation offerRegistrationInvitation(String description) {
        this.assertStateTrue(this.isActive(), "Tenant is not active.");

        this.assertStateFalse(
            this.isRegistrationAvailableThrough(description),
            "Invitation already exists.");

        RegistrationInvitation invitation =
            new RegistrationInvitation(
                this.tenantId(),
                new InvitationId(UUID.randomUUID()),
                description);

        boolean added = this.registrationInvitations().add(invitation);

        this.assertStateTrue(added, "The invitation should have been added.");

        return invitation;
    }

    public Group provisionGroup(String aName, String aDescription) {
        this.assertStateTrue(this.isActive(), "Tenant is not active.");

        Group group = new Group(this.tenantId(), aName, aDescription);

        DomainEventPublisher
            .instance()
            .publish(new GroupProvisioned(
                this.tenantId(),
                aName));

        return group;
    }

    public Role provisionRole(String aName, String aDescription) {
        return this.provisionRole(aName, aDescription, false);
    }

    public Role provisionRole(String aName, String aDescription, boolean aSupportsNesting) {
        this.assertStateTrue(this.isActive(), "Tenant is not active.");

        Role role = new Role(this.tenantId(), aName, aDescription, aSupportsNesting);

        DomainEventPublisher
            .instance()
            .publish(new RoleProvisioned(
                this.tenantId(),
                aName));

        return role;
    }

    public RegistrationInvitation redefineRegistrationInvitationAs(String anInvitationIdentifier) {
        this.assertStateTrue(this.isActive(), "Tenant is not active.");

        RegistrationInvitation invitation =
            this.invitation(anInvitationIdentifier);

        if (invitation != null) {
            invitation.redefineAs().openEnded();
        }

        return invitation;
    }

    public User registerUser(
        InvitationId invitationIdentifier,
        String username,
        String password,
        Activation activation,
        Person person) {

        this.assertStateTrue(this.isActive(), "Tenant is not active.");

        User user = null;

        if (this.isRegistrationAvailableThrough(invitationIdentifier.id())) {

            // ensure same tenant
            person.setTenantId(this.tenantId());

            user = new User(
                this.tenantId(),
                username,
                password,
                activation,
                person);
        }

        return user;
    }

    public TenantId tenantId() {
        return this.tenantId;
    }

    public void withdrawInvitation(String invitationIdentifier) {
        RegistrationInvitation invitation =
            this.invitation(invitationIdentifier);

        if (invitation != null) {
            this.registrationInvitations().remove(invitation);
        }
    }

    @Override
    public boolean equals(Object anObject) {
        boolean equalObjects = false;

        if (anObject != null && this.getClass() == anObject.getClass()) {
            Tenant typedObject = (Tenant) anObject;
            equalObjects =
                this.tenantId().equals(typedObject.tenantId()) &&
                    this.name().equals(typedObject.name());
        }

        return equalObjects;
    }

    @Override
    public int hashCode() {
        return (48123 * 257)
            + this.tenantId().hashCode()
            + this.name().hashCode();
    }

    @Override
    public String toString() {
        return "Tenant [active=" + active + ", description=" + description
            + ", name=" + name + ", tenantId=" + tenantId + "]";
    }

    protected Collection<InvitationDescriptor> allRegistrationInvitationsFor(boolean isAvailable) {
        Set<InvitationDescriptor> allInvitations = new HashSet<>();

        for (RegistrationInvitation invitation : this.registrationInvitations()) {
            if (invitation.isAvailable() == isAvailable) {
                allInvitations.add(invitation.toDescriptor());
            }
        }

        return Collections.unmodifiableSet(allInvitations);
    }

    protected void setDescription(String description) {
        this.assertArgumentNotEmpty(description, "The tenant description is required.");
        this.assertArgumentLength(description, 1, 100, "The tenant description must be 100 characters or less.");

        this.description = description;
    }

    protected RegistrationInvitation invitation(String invitationId) {
        for (RegistrationInvitation invitation : this.registrationInvitations()) {
            if (invitation.isIdentifiedBy(invitationId)) {
                return invitation;
            }
        }

        return null;
    }

    protected void setName(String aName) {
        this.assertArgumentNotEmpty(aName, "The tenant name is required.");
        this.assertArgumentLength(aName, 1, 100, "The name must be 100 characters or less.");

        this.name = aName;
    }

    protected Set<RegistrationInvitation> registrationInvitations() {
        return this.registrationInvitations;
    }

    protected void setRegistrationInvitations(Set<RegistrationInvitation> aRegistrationInvitations) {
        this.registrationInvitations = aRegistrationInvitations;
    }

    protected void setTenantId(TenantId aTenantId) {
        this.assertArgumentNotNull(aTenantId, "TenentId is required.");

        this.tenantId = aTenantId;
    }
}
