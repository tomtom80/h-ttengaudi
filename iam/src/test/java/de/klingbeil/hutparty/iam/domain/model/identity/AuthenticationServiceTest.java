package de.klingbeil.hutparty.iam.domain.model.identity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.klingbeil.hutparty.iam.domain.model.DomainRegistry;
import de.klingbeil.hutparty.iam.domain.model.IdentityAccessTest;

class AuthenticationServiceTest extends IdentityAccessTest {

    public AuthenticationServiceTest() {
        super();
    }

    @Test
    void testAuthenticationSuccess() {
        User user = this.userAggregate();
        DomainRegistry
            .userRepository()
            .add(user);

        UserDescriptor userDescriptor =
            DomainRegistry
                .authenticationService()
                .authenticate(
                    user.tenantId(),
                    user.username(),
                    FIXTURE_PASSWORD);

        assertNotNull(userDescriptor);
        assertFalse(userDescriptor.isNullDescriptor());
        assertEquals(userDescriptor.tenantId(), user.tenantId());
        assertEquals(userDescriptor.username(), user.username());
        assertEquals(userDescriptor.emailAddress(), user.person().emailAddress().address());
    }

    @Test
    void testAuthenticationTenantFailure() {
        User user = this.userAggregate();
        DomainRegistry
            .userRepository()
            .add(user);

        UserDescriptor userDescriptor =
            DomainRegistry
                .authenticationService()
                .authenticate(
                    DomainRegistry.tenantRepository().nextIdentity(),
                    user.username(),
                    FIXTURE_PASSWORD);

        assertNotNull(userDescriptor);
        assertTrue(userDescriptor.isNullDescriptor());
    }

    @Test
    void testAuthenticationUsernameFailure() {
        User user = this.userAggregate();
        DomainRegistry
            .userRepository()
            .add(user);

        UserDescriptor userDescriptor =
            DomainRegistry
                .authenticationService()
                .authenticate(
                    user.tenantId(),
                    FIXTURE_USERNAME2,
                    user.password());

        assertNotNull(userDescriptor);
        assertTrue(userDescriptor.isNullDescriptor());
    }

    @Test
    void testAuthenticationPasswordFailure() {
        User user = this.userAggregate();
        DomainRegistry
            .userRepository()
            .add(user);

        UserDescriptor userDescriptor =
            DomainRegistry
                .authenticationService()
                .authenticate(
                    user.tenantId(),
                    user.username(),
                    FIXTURE_PASSWORD + "-");

        assertNotNull(userDescriptor);
        assertTrue(userDescriptor.isNullDescriptor());
    }
}
