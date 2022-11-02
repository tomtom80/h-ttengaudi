package de.klingbeil.hutparty.iam.domain.model.identity;

import java.io.Serial;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import de.klingbeil.hutparty.domain.model.ConcurrencySafeEntity;

public class RegistrationInvitation extends ConcurrencySafeEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private String description;
    private InvitationId invitationId;
    private Instant startingOn;
    private TenantId tenantId;
    private Instant until;

    public String description() {
        return this.description;
    }

    public InvitationId invitationId() {
        return this.invitationId;
    }

    public boolean isAvailable() {
        boolean isAvailable = false;
        if (this.startingOn() == null && this.until() == null) {
            isAvailable = true;
        } else {
            Instant time = Instant.now();
            if ((time.isAfter(this.startingOn) ||
                time.equals(this.startingOn())) &&
                (time.isBefore(this.until) ||
                    time.equals(this.until()))) {
                isAvailable = true;
            }
        }
        return isAvailable;
    }

    public boolean isIdentifiedBy(String invitationIdentifier) {
        boolean isIdentified = this.invitationId().id().equals(invitationIdentifier);
        if (!isIdentified && this.description() != null) {
            isIdentified = this.description().equals(invitationIdentifier);
        }
        return isIdentified;
    }

    public RegistrationInvitation openEnded() {
        this.setStartingOn(null);
        this.setUntil(null);
        return this;
    }

    public RegistrationInvitation redefineAs() {
        this.setStartingOn(null);
        this.setUntil(null);
        return this;
    }

    public Instant startingOn() {
        return this.startingOn;
    }

    public RegistrationInvitation startingOn(Instant aDate) {
        if (this.until() != null) {
            throw new IllegalStateException("Cannot set starting-on date after until date.");
        }

        this.setStartingOn(aDate);

        // temporary if until() properly follows, but
        // prevents illegal state if until() doesn't follow
        this.setUntil(Instant.now().truncatedTo(ChronoUnit.DAYS).plus(60, ChronoUnit.DAYS));

        return this;
    }

    public TenantId tenantId() {
        return this.tenantId;
    }

    public InvitationDescriptor toDescriptor() {
        return
            new InvitationDescriptor(
                this.tenantId(),
                this.invitationId(),
                this.description(),
                this.startingOn(),
                this.until());
    }

    public Instant until() {
        return this.until;
    }

    public RegistrationInvitation until(Instant aDate) {
        if (this.startingOn() == null) {
            throw new IllegalStateException("Cannot set until date before setting starting-on date.");
        }

        this.setUntil(aDate);

        return this;
    }

    @Override
    public boolean equals(Object anObject) {
        boolean equalObjects = false;

        if (anObject != null && this.getClass() == anObject.getClass()) {
            RegistrationInvitation typedObject = (RegistrationInvitation) anObject;
            equalObjects =
                this.tenantId().equals(typedObject.tenantId()) &&
                    this.invitationId().equals(typedObject.invitationId());
        }

        return equalObjects;
    }

    @Override
    public int hashCode() {
        return (6325 * 233)
            + this.tenantId().hashCode()
            + this.invitationId().hashCode();
    }


    @Override
    public String toString() {
        return "RegistrationInvitation ["
            + "tenantId=" + tenantId
            + ", description=" + description
            + ", invitationId=" + invitationId
            + ", startingOn=" + startingOn
            + ", until=" + until + "]";
    }

    protected RegistrationInvitation(
        TenantId tenantId,
        InvitationId invitationId,
        String description) {

        this();

        this.setDescription(description);
        this.setInvitationId(invitationId);
        this.setTenantId(tenantId);

        this.assertValidInvitationDates();
    }

    protected RegistrationInvitation() {
        super();
    }

    protected void assertValidInvitationDates() {
        // either both dates must be null, or both dates must be set
        if (this.startingOn() == null && this.until() == null) {
            // valid
        } else if (this.startingOn() == null || this.until() == null &&
            this.startingOn() != this.until()) {
            throw new IllegalStateException("This is an invalid open-ended invitation.");
        } else if (this.startingOn().isAfter(this.until())) {
            throw new IllegalStateException("The starting date and time must be before the until date and time.");
        }
    }

    protected void setDescription(String aDescription) {
        this.assertArgumentNotEmpty(aDescription, "The invitation description is required.");
        this.assertArgumentLength(aDescription, 1, 100, "The invitation description must be 100 characters or less.");

        this.description = aDescription;
    }

    protected void setInvitationId(InvitationId invitationId) {
        this.invitationId = invitationId;
    }

    protected void setStartingOn(Instant aStartingOn) {
        this.startingOn = aStartingOn;
    }

    protected void setTenantId(TenantId aTenantId) {
        this.assertArgumentNotNull(aTenantId, "The tenantId is required.");

        this.tenantId = aTenantId;
    }

    protected void setUntil(Instant anUntil) {
        this.until = anUntil;
    }
}
