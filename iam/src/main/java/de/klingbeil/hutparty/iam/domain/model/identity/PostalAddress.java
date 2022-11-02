package de.klingbeil.hutparty.iam.domain.model.identity;

import java.io.Serial;
import java.io.Serializable;

import de.klingbeil.hutparty.AssertionConcern;

public class PostalAddress extends AssertionConcern implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String city;
    private String countryCode;
    private String postalCode;
    private String stateProvince;
    private String streetAddress;

    public PostalAddress(
        String streetAddress,
        String city,
        String stateProvince,
        String postalCode,
        String countryCode) {

        super();

        this.setCity(city);
        this.setCountryCode(countryCode);
        this.setPostalCode(postalCode);
        this.setStateProvince(stateProvince);
        this.setStreetAddress(streetAddress);
    }

    public PostalAddress(PostalAddress postalAddress) {
        this(postalAddress.streetAddress(),
            postalAddress.city(),
            postalAddress.stateProvince(),
            postalAddress.postalCode(),
            postalAddress.countryCode());
    }

    public String city() {
        return this.city;
    }

    public String countryCode() {
        return this.countryCode;
    }

    public String postalCode() {
        return this.postalCode;
    }

    public String stateProvince() {
        return this.stateProvince;
    }

    public String streetAddress() {
        return this.streetAddress;
    }

    @Override
    public boolean equals(Object object) {
        boolean equalObjects = false;

        if (object != null && this.getClass() == object.getClass()) {
            PostalAddress typedObject = (PostalAddress) object;
            equalObjects =
                this.streetAddress().equals(typedObject.streetAddress()) &&
                    this.city().equals(typedObject.city()) &&
                    this.stateProvince().equals(typedObject.stateProvince()) &&
                    this.postalCode().equals(typedObject.postalCode()) &&
                    this.countryCode().equals(typedObject.countryCode());
        }

        return equalObjects;
    }

    @Override
    public int hashCode() {
        return (31589 * 227)
            + this.streetAddress().hashCode()
            + this.city().hashCode()
            + this.stateProvince().hashCode()
            + this.postalCode().hashCode()
            + this.countryCode().hashCode();
    }

    @Override
    public String toString() {
        return "PostalAddress [streetAddress=" + streetAddress
            + ", city=" + city + ", stateProvince=" + stateProvince
            + ", postalCode=" + postalCode
            + ", countryCode=" + countryCode + "]";
    }

    protected PostalAddress() {
        super();
    }

    private void setCity(String city) {
        this.assertArgumentNotEmpty(city, "The city is required.");
        this.assertArgumentLength(city, 1, 100, "The city must be 100 characters or less.");

        this.city = city;
    }

    private void setCountryCode(String countryCode) {
        this.assertArgumentNotEmpty(countryCode, "The country is required.");
        this.assertArgumentLength(countryCode, 2, 2, "The country code must be two characters.");

        this.countryCode = countryCode;
    }

    private void setPostalCode(String postalCode) {
        this.assertArgumentNotEmpty(postalCode, "The postal code is required.");
        this.assertArgumentLength(postalCode, 5, 12, "The postal code must be 12 characters or less.");

        this.postalCode = postalCode;
    }

    private void setStateProvince(String stateProvince) {
        this.assertArgumentNotEmpty(stateProvince, "The state/province is required.");
        this.assertArgumentLength(stateProvince, 2, 100, "The state/province must be 100 characters or less.");

        this.stateProvince = stateProvince;
    }

    private void setStreetAddress(String streetAddress) {
        this.assertArgumentNotEmpty(streetAddress, "The street address is required.");
        this.assertArgumentLength(streetAddress, 1, 100, "The street address must be 100 characters or less.");

        this.streetAddress = streetAddress;
    }
}
