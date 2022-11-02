package de.klingbeil.hutparty.iam.domain.model.identity;

import java.io.Serial;
import java.io.Serializable;
import java.util.regex.Pattern;

import de.klingbeil.hutparty.AssertionConcern;

public final class EmailAddress extends AssertionConcern implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String address;

    public EmailAddress(String address) {
        super();

        this.setAddress(address);
    }

    public EmailAddress(EmailAddress emailAddress) {
        this(emailAddress.address());
    }

    public String address() {
        return this.address;
    }

    @Override
    public boolean equals(Object object) {
        boolean equalObjects = false;

        if (object != null && this.getClass() == object.getClass()) {
            EmailAddress typedObject = (EmailAddress) object;
            equalObjects = this.address().equals(typedObject.address());
        }

        return equalObjects;
    }

    @Override
    public int hashCode() {
        return (17861 * 179)
            + this.address().hashCode();
    }

    @Override
    public String toString() {
        return "EmailAddress [address=" + address + "]";
    }

    protected EmailAddress() {
        super();
    }

    private void setAddress(String address) {
        this.assertArgumentNotEmpty(address, "The email address is required.");
        this.assertArgumentLength(address, 1, 100, "Email address must be 100 characters or less.");
        this.assertArgumentTrue(
            Pattern.matches("\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*", address),
            "Email address format is invalid.");

        this.address = address;
    }
}
