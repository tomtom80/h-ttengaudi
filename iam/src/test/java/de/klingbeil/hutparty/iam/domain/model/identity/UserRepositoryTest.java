package de.klingbeil.hutparty.iam.domain.model.identity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Collection;

import org.junit.jupiter.api.Test;

import de.klingbeil.hutparty.iam.domain.model.DomainRegistry;
import de.klingbeil.hutparty.iam.domain.model.IdentityAccessTest;

class UserRepositoryTest extends IdentityAccessTest {
    @Test
    void testAddUser() {

        User user = this.userAggregate();

        DomainRegistry.userRepository().add(user);

        assertNotNull(DomainRegistry
            .userRepository()
            .userWithUsername(user.tenantId(), user.username()));
    }

    @Test
    void testFindUserByUsername() {

        User user = this.userAggregate();

        DomainRegistry.userRepository().add(user);

        assertNotNull(DomainRegistry
            .userRepository()
            .userWithUsername(user.tenantId(), user.username()));
    }

    @Test
    void testRemoveUser() {

        User user = this.userAggregate();

        DomainRegistry.userRepository().add(user);

        assertNotNull(DomainRegistry
            .userRepository()
            .userWithUsername(user.tenantId(), user.username()));

        DomainRegistry.userRepository().remove(user);

        assertNull(DomainRegistry
            .userRepository()
            .userWithUsername(user.tenantId(), user.username()));
    }

    @Test
    void testFindSimilarlyNamedUsers() {

        User user = this.userAggregate();
        DomainRegistry.userRepository().add(user);

        User user2 = this.userAggregate2();
        DomainRegistry.userRepository().add(user2);

        FullName name = user.person().name();

        Collection<User> users =
            DomainRegistry
                .userRepository()
                .allSimilarlyNamedUsers(
                    user.tenantId(),
                    "",
                    name.lastName().substring(0, 2));

        assertEquals(2, users.size());
    }

}
