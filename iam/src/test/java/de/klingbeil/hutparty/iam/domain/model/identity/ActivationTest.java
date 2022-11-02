package de.klingbeil.hutparty.iam.domain.model.identity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import de.klingbeil.hutparty.iam.domain.model.IdentityAccessTest;

class ActivationTest extends IdentityAccessTest {

    public ActivationTest() {
        super();
    }

    @Test
    void testActivationEnabled() {
        Activation enablement = Activation.indefiniteActivation();

        assertTrue(enablement.isActivationEnabled());
    }

    @Test
    void testActivationDisabled() {
        Activation enablement = new Activation(false, null, null);

        assertFalse(enablement.isActivationEnabled());
    }

    @Test
    void testActivationOutsideStartEndDates() {
        Activation enablement =
            new Activation(
                true,
                this.dayBeforeYesterday(),
                this.yesterday());

        assertFalse(enablement.isActivationEnabled());
    }

    @Test
    void testActivationUnsequencedDates() {

        try {
            new Activation(
                true,
                this.tomorrow(),
                this.today());
            fail("activation dates sequence wrong");
        } catch (Exception e) {
            assertEquals("Enablement start and/or end date is invalid.", e.getMessage());
        }
    }

    @Test
    void testEnablementEndsTimeExpired() {
        Activation enablement =
            new Activation(
                true,
                this.dayBeforeYesterday(),
                this.yesterday());

        assertTrue(enablement.isTimeExpired());
    }

    @Test
    void testEnablementHasNotBegunTimeExpired() {
        Activation enablement =
            new Activation(
                true,
                this.tomorrow(),
                this.dayAfterTomorrow());

        assertTrue(enablement.isTimeExpired());
    }
}
