package de.klingbeil.hutparty.iam.domain.model.identity;

import java.io.Serial;
import java.io.Serializable;
import java.util.regex.Pattern;

import de.klingbeil.hutparty.AssertionConcern;

public final class FullName extends AssertionConcern implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;

    public FullName(String firstName, String lastName) {
        super();

        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    public FullName(FullName fullName) {
        this(fullName.firstName(), fullName.lastName());
    }

    public String asFormattedName() {
        return this.firstName() + " " + this.lastName();
    }

    public String firstName() {
        return this.firstName;
    }

    public String lastName() {
        return this.lastName;
    }

    public FullName withChangedFirstName(String firstName) {
        return new FullName(firstName, this.lastName());
    }

    public FullName withChangedLastName(String lastName) {
        return new FullName(this.firstName(), lastName);
    }

    @Override
    public boolean equals(Object object) {
        boolean equalObjects = false;

        if (object != null && this.getClass() == object.getClass()) {
            FullName typedObject = (FullName) object;
            equalObjects =
                this.firstName().equals(typedObject.firstName()) &&
                    this.lastName().equals(typedObject.lastName());
        }

        return equalObjects;
    }

    @Override
    public int hashCode() {
        return (59151 * 191)
            + this.firstName().hashCode()
            + this.lastName().hashCode();
    }

    @Override
    public String toString() {
        return "FullName [firstName=" + firstName + ", lastName=" + lastName + "]";
    }

    private FullName() {
        super();
    }

    private void setFirstName(String firstName) {
        this.assertArgumentNotEmpty(firstName, "First name is required.");
        this.assertArgumentLength(firstName, 1, 50, "First name must be 50 characters or less.");
        this.assertArgumentTrue(
            Pattern.matches("[A-Z][a-z]*", firstName),
            "First name must be at least one character in length, starting with a capital letter.");

        this.firstName = firstName;
    }

    private void setLastName(String lastName) {
        this.assertArgumentNotEmpty(lastName, "The last name is required.");
        this.assertArgumentLength(lastName, 1, 50, "The last name must be 50 characters or less.");
        this.assertArgumentTrue(
            Pattern.matches("^[a-zA-Z'][ a-zA-Z'-]*[a-zA-Z']?", lastName),
            "Last name must be at least one character in length.");

        this.lastName = lastName;
    }
}
