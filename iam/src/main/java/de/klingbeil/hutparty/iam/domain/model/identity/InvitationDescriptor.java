package de.klingbeil.hutparty.iam.domain.model.identity;

import java.time.Instant;

import de.klingbeil.hutparty.AssertionConcern;

public final class InvitationDescriptor extends AssertionConcern {

    private String description;
    private InvitationId invitationId;
    private Instant startingOn;
    private TenantId tenantId;
    private Instant until;

    public InvitationDescriptor(
        TenantId tenantId,
        InvitationId invitationId,
        String description,
        Instant startingOn,
        Instant until) {

        super();

        this.setDescription(description);
        this.setInvitationId(invitationId);
        this.setStartingOn(startingOn);
        this.setTenantId(tenantId);
        this.setUntil(until);
    }

    public InvitationDescriptor(InvitationDescriptor invitationDescriptor) {
        this(invitationDescriptor.tenantId(),
            invitationDescriptor.invitationId(),
            invitationDescriptor.description(),
            invitationDescriptor.startingOn(),
            invitationDescriptor.until());
    }

    public String description() {
        return this.description;
    }

    public InvitationId invitationId() {
        return this.invitationId;
    }

    public boolean isOpenEnded() {
        return this.startingOn() == null && this.until() == null;
    }

    public Instant startingOn() {
        return this.startingOn;
    }

    public TenantId tenantId() {
        return this.tenantId;
    }

    public Instant until() {
        return this.until;
    }

    @Override
    public boolean equals(Object object) {
        boolean equalObjects = false;

        if (object != null && this.getClass() == object.getClass()) {
            InvitationDescriptor typedObject = (InvitationDescriptor) object;
            equalObjects =
                this.tenantId().equals(typedObject.tenantId()) &&
                    this.invitationId().equals(typedObject.invitationId()) &&
                    this.description().equals(typedObject.description()) &&
                    ((this.startingOn() == null && typedObject.startingOn() == null) ||
                        (this.startingOn() != null && this.startingOn().equals(typedObject.startingOn()))) &&
                    ((this.until() == null && typedObject.until() == null) ||
                        (this.until() != null && this.until().equals(typedObject.until())));
        }

        return equalObjects;
    }

    @Override
    public int hashCode() {
        return (23279 * 199)
            + this.tenantId().hashCode()
            + this.invitationId().hashCode()
            + this.description().hashCode()
            + (this.startingOn() == null ? 0 : this.startingOn().hashCode())
            + (this.until() == null ? 0 : this.until().hashCode());
    }

    @Override
    public String toString() {
        return "InvitationDescriptor [tenantId=" + tenantId
            + ", invitationId=" + invitationId
            + ", description=" + description
            + ", startingOn=" + startingOn + ", until=" + until + "]";
    }

    protected InvitationDescriptor() {
        super();
    }

    private void setDescription(String description) {
        this.assertArgumentNotEmpty(description, "The invitation description is required.");

        this.description = description;
    }

    private void setInvitationId(InvitationId invitationId) {
        this.assertArgumentNotNull(invitationId, "The invitationId is required.");

        this.invitationId = invitationId;
    }

    private void setStartingOn(Instant startingOn) {
        this.startingOn = startingOn;
    }

    private void setTenantId(TenantId tenantId) {
        this.assertArgumentNotNull(tenantId, "The tenantId is required.");

        this.tenantId = tenantId;
    }

    private void setUntil(Instant until) {
        this.until = until;
    }
}
