package de.klingbeil.hutparty.iam.domain.model.identity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import de.klingbeil.hutparty.iam.domain.model.IdentityAccessTest;

class ContactInformationTest extends IdentityAccessTest {

    public ContactInformationTest() {
        super();
    }

    @Test
    void testContactInformation() {
        ContactInformation contactInformation = this.contactInformation();

        assertEquals(FIXTURE_USER_EMAIL_ADDRESS, contactInformation.emailAddress().address());
        assertEquals("Boulder", contactInformation.postalAddress().city());
        assertEquals("CO", contactInformation.postalAddress().stateProvince());
    }

    @Test
    void testChangeEmailAddress() {
        ContactInformation contactInformation = this.contactInformation();
        ContactInformation contactInformationCopy = new ContactInformation(contactInformation);

        ContactInformation contactInformation2 =
            contactInformation
                .changeEmailAddress(
                    new EmailAddress(FIXTURE_USER_EMAIL_ADDRESS2));

        assertEquals(contactInformationCopy, contactInformation);
        assertNotEquals(contactInformation, contactInformation2);
        assertNotEquals(contactInformationCopy, contactInformation2);
        assertEquals(FIXTURE_USER_EMAIL_ADDRESS, contactInformation.emailAddress().address());
        assertEquals(FIXTURE_USER_EMAIL_ADDRESS2, contactInformation2.emailAddress().address());
        assertEquals("Boulder", contactInformation.postalAddress().city());
        assertEquals("CO", contactInformation.postalAddress().stateProvince());
    }

    @Test
    void testChangePostalAddress() {
        ContactInformation contactInformation = this.contactInformation();
        ContactInformation contactInformationCopy = new ContactInformation(contactInformation);

        ContactInformation contactInformation2 =
            contactInformation
                .changePostalAddress(
                    new PostalAddress("321 Mockingbird Lane", "Denver", "CO", "81121", "US"));

        assertEquals(contactInformationCopy, contactInformation);
        assertNotEquals(contactInformation, contactInformation2);
        assertNotEquals(contactInformationCopy, contactInformation2);
        assertEquals("321 Mockingbird Lane", contactInformation2.postalAddress().streetAddress());
        assertEquals("Denver", contactInformation2.postalAddress().city());
        assertEquals("CO", contactInformation2.postalAddress().stateProvince());
    }

    @Test
    void testChangePrimaryTelephone() {
        ContactInformation contactInformation = this.contactInformation();
        ContactInformation contactInformationCopy = new ContactInformation(contactInformation);

        ContactInformation contactInformation2 =
            contactInformation
                .changePrimaryTelephone(
                    new Telephone("720-555-1212"));

        assertEquals(contactInformationCopy, contactInformation);
        assertNotEquals(contactInformation, contactInformation2);
        assertNotEquals(contactInformationCopy, contactInformation2);
        assertEquals("720-555-1212", contactInformation2.primaryTelephone().number());
        assertEquals("Boulder", contactInformation2.postalAddress().city());
        assertEquals("CO", contactInformation2.postalAddress().stateProvince());
    }

    @Test
    void testChangeSecondaryTelephone() {
        ContactInformation contactInformation = this.contactInformation();
        ContactInformation contactInformationCopy = new ContactInformation(contactInformation);

        ContactInformation contactInformation2 =
            contactInformation
                .changeSecondaryTelephone(
                    new Telephone("720-555-1212"));

        assertEquals(contactInformationCopy, contactInformation);
        assertNotEquals(contactInformation, contactInformation2);
        assertNotEquals(contactInformationCopy, contactInformation2);
        assertEquals("720-555-1212", contactInformation2.secondaryTelephone().number());
        assertEquals("Boulder", contactInformation2.postalAddress().city());
        assertEquals("CO", contactInformation2.postalAddress().stateProvince());
    }
}
