//   Copyright 2012,2013 Vaughn Vernon
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

package de.klingbeil.hutparty.iam.domain.model.identity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import de.klingbeil.hutparty.domain.model.DomainEventPublisher;
import de.klingbeil.hutparty.domain.model.DomainEventSubscriber;
import de.klingbeil.hutparty.iam.domain.model.DomainRegistry;
import de.klingbeil.hutparty.iam.domain.model.IdentityAccessTest;
import de.klingbeil.hutparty.iam.infrastructure.TestConfig;
import de.klingbeil.hutparty.iam.infrastructure.test.DomainEventTestExtension;

@SpringBootTest
@Import(TestConfig.class)
@ExtendWith(DomainEventTestExtension.class)
class UserTest extends IdentityAccessTest {

    private boolean wasCalled;

    public UserTest() {
        super();
    }

    @Test
    void testUserActivationEnabled() throws Exception {
        User user = this.userAggregate();

        assertTrue(user.isEnabled());
    }

    @Test
    void testUserActivationDisabled() throws Exception {
        final User user = this.userAggregate();
        DomainEventPublisher
            .instance()
            .subscribe(new DomainEventSubscriber<UserActivationChanged>() {
                public void handleEvent(UserActivationChanged domainEvent) {
                    assertEquals(domainEvent.username(), user.username());
                    wasCalled = true;
                }

                public Class<UserActivationChanged> subscribedToEventType() {
                    return UserActivationChanged.class;
                }
            });

        user.defineActivation(new Activation(false, null, null));

        assertFalse(user.isEnabled());
        assertTrue(wasCalled);
    }

    @Test
    void testUserActivationWithinStartEndDates() throws Exception {
        final User user = this.userAggregate();
        DomainEventPublisher
            .instance()
            .subscribe(new DomainEventSubscriber<UserActivationChanged>() {
                public void handleEvent(UserActivationChanged domainEvent) {
                    assertEquals(domainEvent.username(), user.username());
                    wasCalled = true;
                }

                public Class<UserActivationChanged> subscribedToEventType() {
                    return UserActivationChanged.class;
                }
            });

        user.defineActivation(
            new Activation(
                true,
                this.today(),
                this.tomorrow()));

        assertTrue(user.isEnabled());
        assertTrue(wasCalled);
    }

    @Test
    void testUserActivationOutsideStartEndDates() throws Exception {
        final User user = this.userAggregate();
        DomainEventPublisher
            .instance()
            .subscribe(new DomainEventSubscriber<UserActivationChanged>() {
                public void handleEvent(UserActivationChanged domainEvent) {
                    assertEquals(domainEvent.username(), user.username());
                    wasCalled = true;
                }

                public Class<UserActivationChanged> subscribedToEventType() {
                    return UserActivationChanged.class;
                }
            });

        user.defineActivation(
            new Activation(
                true,
                this.dayBeforeYesterday(),
                this.yesterday()));

        assertFalse(user.isEnabled());
        assertTrue(wasCalled);
    }

    @Test
    void testUserActivationUnsequencedDates() throws Exception {
        final User user = this.userAggregate();
        DomainEventPublisher
            .instance()
            .subscribe(new DomainEventSubscriber<UserActivationChanged>() {
                public void handleEvent(UserActivationChanged domainEvent) {
                    assertEquals(domainEvent.username(), user.username());
                    wasCalled = true;
                }

                public Class<UserActivationChanged> subscribedToEventType() {
                    return UserActivationChanged.class;
                }
            });

        try {
            user.defineActivation(
                new Activation(
                    true,
                    this.tomorrow(),
                    this.today()));
            fail("unsequenced dates must throw exception");
        } catch (Exception t) {
            assertThat(t.getMessage()).isEqualTo("Enablement start and/or end date is invalid.");
        }

        assertFalse(wasCalled);
    }

    @Test
    void testUserDescriptor() throws Exception {
        User user = this.userAggregate();

        UserDescriptor userDescriptor =
            user.userDescriptor();

        assertNotNull(userDescriptor.emailAddress());
        assertEquals(FIXTURE_USER_EMAIL_ADDRESS, userDescriptor.emailAddress());
        assertNotNull(userDescriptor.tenantId());
        assertEquals(userDescriptor.tenantId(), user.tenantId());
        assertNotNull(userDescriptor.username());
        assertEquals(FIXTURE_USERNAME, userDescriptor.username());
    }

    @Test
    void testUserChangePassword() throws Exception {
        final User user = this.userAggregate();
        DomainEventPublisher
            .instance()
            .subscribe(new DomainEventSubscriber<UserPasswordChanged>() {
                public void handleEvent(UserPasswordChanged domainEvent) {
                    assertEquals(domainEvent.username(), user.username());
                    assertEquals(domainEvent.tenantId(), user.tenantId());
                    wasCalled = true;
                }

                public Class<UserPasswordChanged> subscribedToEventType() {
                    return UserPasswordChanged.class;
                }
            });

        user.changePassword(FIXTURE_PASSWORD, "ThisIsANewPassword.");

        assertTrue(wasCalled);
    }

    @Test
    void testUserChangePasswordFails() throws Exception {
        User user = this.userAggregate();

        try {
            user.changePassword("no clue", "ThisIsANewP4ssw0rd.");
            fail("unequal passwords must fail");
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Current password not confirmed.");
        }
    }

    @Test
    void testUserPasswordHashedOnConstruction() throws Exception {
        User user = this.userAggregate();

        assertNotEquals(FIXTURE_PASSWORD, user.password());
    }

    @Test
    void testUserPasswordHashedOnChange() throws Exception {
        User user = this.userAggregate();
        String strongPassword = DomainRegistry.passwordService().generateStrongPassword();

        user.changePassword(FIXTURE_PASSWORD, strongPassword);

        assertNotEquals(FIXTURE_PASSWORD, user.password());
        assertNotEquals(strongPassword, user.password());
    }

    @Test
    void testUserPersonalContactInformationChanged() throws Exception {
        final User user = this.userAggregate();
        DomainEventPublisher
            .instance()
            .subscribe(new DomainEventSubscriber<PersonContactInformationChanged>() {
                public void handleEvent(PersonContactInformationChanged domainEvent) {
                    assertEquals(domainEvent.username(), user.username());
                    wasCalled = true;
                }

                public Class<PersonContactInformationChanged> subscribedToEventType() {
                    return PersonContactInformationChanged.class;
                }
            });

        user.changePersonalContactInformation(
            new ContactInformation(
                new EmailAddress(FIXTURE_USER_EMAIL_ADDRESS2),
                new PostalAddress(
                    "123 Mockingbird Lane",
                    "Boulder",
                    "CO",
                    "80301",
                    "US"),
                new Telephone("303-555-1210"),
                new Telephone("303-555-1212")));

        assertEquals(new EmailAddress(FIXTURE_USER_EMAIL_ADDRESS2), user.person().emailAddress());
        assertEquals("123 Mockingbird Lane", user.person().contactInformation().postalAddress().streetAddress());
        assertTrue(wasCalled);
    }

    @Test
    void testUserPersonNameChanged() throws Exception {
        final User user = this.userAggregate();
        DomainEventPublisher
            .instance()
            .subscribe(new DomainEventSubscriber<PersonNameChanged>() {
                public void handleEvent(PersonNameChanged domainEvent) {
                    assertEquals(domainEvent.username(), user.username());
                    assertEquals("Joe", domainEvent.name().firstName());
                    assertEquals("Smith", domainEvent.name().lastName());
                    wasCalled = true;
                }

                public Class<PersonNameChanged> subscribedToEventType() {
                    return PersonNameChanged.class;
                }
            });

        user.changePersonalName(new FullName("Joe", "Smith"));

        assertTrue(wasCalled);
    }
}
