package de.klingbeil.hutparty.iam.domain.model.identity;

import java.io.Serial;
import java.io.Serializable;
import java.util.regex.Pattern;

import de.klingbeil.hutparty.AssertionConcern;

public final class Telephone extends AssertionConcern implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String number;

    public Telephone(String number) {
        this();

        this.setNumber(number);
    }

    public Telephone(Telephone telephone) {
        this(telephone.number());
    }

    public String number() {
        return this.number;
    }

    @Override
    public boolean equals(Object object) {
        boolean equalObjects = false;

        if (object != null && this.getClass() == object.getClass()) {
            Telephone typedObject = (Telephone) object;
            equalObjects = this.number().equals(typedObject.number());
        }

        return equalObjects;
    }

    @Override
    public int hashCode() {
        return (35137 * 239)
            + this.number().hashCode();
    }

    @Override
    public String toString() {
        return "Telephone [number=" + number + "]";
    }

    public Telephone() {
        super();
    }

    private void setNumber(String number) {
        this.assertArgumentNotEmpty(number, "Telephone number is required.");
        this.assertArgumentLength(number, 5, 20, "Telephone number may not be more than 20 characters.");
        this.assertArgumentTrue(
            Pattern.matches("((\\(\\d{3}\\))|(\\d{3}-))\\d{3}-\\d{4}", number),
            "Telephone number or its format is invalid.");

        this.number = number;
    }
}
