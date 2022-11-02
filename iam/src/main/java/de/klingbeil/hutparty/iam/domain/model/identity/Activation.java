package de.klingbeil.hutparty.iam.domain.model.identity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import de.klingbeil.hutparty.AssertionConcern;

public final class Activation extends AssertionConcern implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private boolean enabled;
    private Instant endDate;
    private Instant startDate;

    public static Activation indefiniteActivation() {
        return new Activation(true, null, null);
    }

    public Activation(boolean enabled, Instant startDate, Instant endDate) {
        super();

        if (startDate != null || endDate != null) {
            this.assertArgumentNotNull(startDate, "The start date must be provided.");
            this.assertArgumentNotNull(endDate, "The end date must be provided.");
            this.assertArgumentFalse(startDate.isAfter(endDate), "Enablement start and/or end date is invalid.");
        }

        this.setEnabled(enabled);
        this.setEndDate(endDate);
        this.setStartDate(startDate);
    }

    public Activation(Activation activation) {
        this(activation.isEnabled(), activation.startDate(), activation.endDate());
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isActivationEnabled() {
        boolean enabled = false;

        if (this.isEnabled() && !this.isTimeExpired()) {
            enabled = true;
        }

        return enabled;
    }

    public Instant endDate() {
        return this.endDate;
    }

    public boolean isTimeExpired() {
        boolean timeExpired = false;

        if (this.startDate() != null && this.endDate() != null) {
            Instant today = Instant.now().truncatedTo(ChronoUnit.DAYS);
            if (today.isBefore(this.startDate()) ||
                today.isAfter(this.endDate())) {
                timeExpired = true;
            }
        }

        return timeExpired;
    }

    public Instant startDate() {
        return this.startDate;
    }

    @Override
    public boolean equals(Object object) {
        boolean equalObjects = false;

        if (object != null && this.getClass() == object.getClass()) {
            Activation typedObject = (Activation) object;
            equalObjects =
                this.isEnabled() == typedObject.isEnabled() &&
                    ((this.startDate() == null && typedObject.startDate() == null) ||
                        (this.startDate() != null && this.startDate().equals(typedObject.startDate()))) &&
                    ((this.endDate() == null && typedObject.endDate() == null) ||
                        (this.endDate() != null && this.endDate().equals(typedObject.endDate())));
        }

        return equalObjects;
    }

    @Override
    public int hashCode() {
        return (19563 * 181)
            + (this.isEnabled() ? 1 : 0)
            + (this.startDate() == null ? 0 : this.startDate().hashCode())
            + (this.endDate() == null ? 0 : this.endDate().hashCode());
    }

    @Override
    public String toString() {
        return "Activation [enabled=" + enabled + ", endDate=" + endDate + ", startDate=" + startDate + "]";
    }

    private Activation() {
        super();
    }

    private void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    private void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }
}
