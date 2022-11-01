package de.klingbeil.hutparty.iam.domain.model.identity;

import java.time.Instant;

import de.klingbeil.hutparty.AssertionConcern;

public final class InvitationDescriptor extends AssertionConcern {

    private String description;
    private String invitationId;
    private Instant startingOn;
    private TenantId tenantId;
    private Instant until;

    public InvitationDescriptor(
        TenantId aTenantId,
        String anInvitationId,
        String aDescription,
        Instant aStartingOn,
        Instant anUntil) {

        super();

        this.setDescription(aDescription);
        this.setInvitationId(anInvitationId);
        this.setStartingOn(aStartingOn);
        this.setTenantId(aTenantId);
        this.setUntil(anUntil);
    }

    public InvitationDescriptor(InvitationDescriptor anInvitationDescriptor) {
        this(anInvitationDescriptor.tenantId(),
            anInvitationDescriptor.invitationId(),
            anInvitationDescriptor.description(),
            anInvitationDescriptor.startingOn(),
            anInvitationDescriptor.until());
    }

    public String description() {
        return this.description;
    }

    public String invitationId() {
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
    public boolean equals(Object anObject) {
        boolean equalObjects = false;

        if (anObject != null && this.getClass() == anObject.getClass()) {
            InvitationDescriptor typedObject = (InvitationDescriptor) anObject;
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
        return +(23279 * 199)
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

    private void setDescription(String aDescription) {
        this.assertArgumentNotEmpty(aDescription, "The invitation description is required.");

        this.description = aDescription;
    }

    private void setInvitationId(String anInvitationId) {
        this.assertArgumentNotEmpty(anInvitationId, "The invitationId is required.");

        this.invitationId = anInvitationId;
    }

    private void setStartingOn(Instant aStartingOn) {
        this.startingOn = aStartingOn;
    }

    private void setTenantId(TenantId aTenantId) {
        this.assertArgumentNotNull(aTenantId, "The tenantId is required.");

        this.tenantId = aTenantId;
    }

    private void setUntil(Instant anUntil) {
        this.until = anUntil;
    }
}
