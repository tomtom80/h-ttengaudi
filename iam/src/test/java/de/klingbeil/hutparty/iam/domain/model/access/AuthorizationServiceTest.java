package de.klingbeil.hutparty.iam.domain.model.access;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import de.klingbeil.hutparty.iam.domain.model.DomainRegistry;
import de.klingbeil.hutparty.iam.domain.model.IdentityAccessTest;
import de.klingbeil.hutparty.iam.domain.model.identity.Tenant;
import de.klingbeil.hutparty.iam.domain.model.identity.User;
import de.klingbeil.hutparty.iam.infrastructure.TestConfig;
import de.klingbeil.hutparty.iam.infrastructure.test.DomainEventTestExtension;

@SpringBootTest
@Import(TestConfig.class)
@ExtendWith(DomainEventTestExtension.class)
class AuthorizationServiceTest extends IdentityAccessTest {

    public AuthorizationServiceTest() {
        super();
    }

    @Test
    void testUserInRoleAuthorization() {
        Tenant tenant = this.tenantAggregate();
        User user = this.userAggregate();
        DomainRegistry.userRepository().add(user);
        Role managerRole = tenant.provisionRole("Manager", "A manager role.", true);
        managerRole.assignUser(user);
        DomainRegistry
            .roleRepository()
            .add(managerRole);

       assertTrue(DomainRegistry
            .authorizationService()
            .isUserInRole(user, "Manager"));
       assertFalse(DomainRegistry
           .authorizationService()
           .isUserInRole(user, "Director"));
    }

    @Test
    void testUsernameInRoleAuthorization() {
        Tenant tenant = this.tenantAggregate();
        User user = this.userAggregate();
        DomainRegistry.userRepository().add(user);
        Role managerRole = tenant.provisionRole("Manager", "A manager role.", true);
        managerRole.assignUser(user);
        DomainRegistry
            .roleRepository()
            .add(managerRole);

       assertTrue(DomainRegistry
            .authorizationService()
            .isUserInRole(tenant.tenantId(), user.username(), "Manager"));
       assertFalse(DomainRegistry
           .authorizationService()
           .isUserInRole(tenant.tenantId(), user.username(), "Director"));
    }
}
