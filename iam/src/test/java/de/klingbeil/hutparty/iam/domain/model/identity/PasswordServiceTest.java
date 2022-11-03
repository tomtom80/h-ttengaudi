package de.klingbeil.hutparty.iam.domain.model.identity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.klingbeil.hutparty.iam.domain.model.DomainRegistry;
import de.klingbeil.hutparty.iam.domain.model.IdentityAccessTest;

class PasswordServiceTest extends IdentityAccessTest {

    public PasswordServiceTest() {
        super();
    }

    @Test
    void testGenerateStrongPassword() {
        String password =
            DomainRegistry
                .passwordService()
                .generateStrongPassword();

        assertTrue(DomainRegistry.passwordService().isStrong(password));
        assertFalse(DomainRegistry.passwordService().isWeak(password));
    }

    @Test
    void testIsStrongPassword() {
        final String password = "Th1sShudBStrong.";
        assertTrue(DomainRegistry.passwordService().isStrong(password));
        assertFalse(DomainRegistry.passwordService().isVeryStrong(password));
        assertFalse(DomainRegistry.passwordService().isWeak(password));
    }

    @Test
    void testIsVeryStrongPassword() {
        final String password = "Th1sSh0uldBV3ryStrong!";
        assertTrue(DomainRegistry.passwordService().isVeryStrong(password));
        assertTrue(DomainRegistry.passwordService().isStrong(password));
        assertFalse(DomainRegistry.passwordService().isWeak(password));
    }

    @Test
    void testIsWeakPassword() {
        final String password = "Weakness";
        assertFalse(DomainRegistry.passwordService().isVeryStrong(password));
        assertFalse(DomainRegistry.passwordService().isStrong(password));
        assertTrue(DomainRegistry.passwordService().isWeak(password));
    }
}
