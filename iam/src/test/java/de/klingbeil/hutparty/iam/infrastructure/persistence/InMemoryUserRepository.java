package de.klingbeil.hutparty.iam.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.klingbeil.hutparty.iam.domain.model.identity.FullName;
import de.klingbeil.hutparty.iam.domain.model.identity.TenantId;
import de.klingbeil.hutparty.iam.domain.model.identity.User;
import de.klingbeil.hutparty.iam.domain.model.identity.UserRepository;
import de.klingbeil.hutparty.persistence.CleanableStore;

public class InMemoryUserRepository implements UserRepository, CleanableStore {

    private final Map<String, User> repository;

    public InMemoryUserRepository() {
        super();

        this.repository = new HashMap<>();
    }

    @Override
    public void add(User aUser) {
        String key = this.keyOf(aUser);

        if (this.repository().containsKey(key)) {
            throw new IllegalStateException("Duplicate key.");
        }

        this.repository().put(key, aUser);
    }

    @Override
    public Collection<User> allSimilarlyNamedUsers(
        TenantId aTenantId,
        String aFirstNamePrefix,
        String aLastNamePrefix) {

        Collection<User> users = new ArrayList<>();

        aFirstNamePrefix = aFirstNamePrefix.toLowerCase();
        aLastNamePrefix = aLastNamePrefix.toLowerCase();

        for (User user : this.repository().values()) {
            if (user.tenantId().equals(aTenantId)) {
                FullName name = user.person().name();
                if (name.firstName().toLowerCase().startsWith(aFirstNamePrefix)) {
                    if (name.lastName().toLowerCase().startsWith(aLastNamePrefix)) {
                        users.add(user);
                    }
                }
            }
        }

        return users;
    }

    @Override
    public void remove(User aUser) {
        String key = this.keyOf(aUser);

        this.repository().remove(key);
    }

    @Override
    public User userFromAuthenticCredentials(
        TenantId aTenantId,
        String aUsername,
        String anEncryptedPassword) {

        for (User user : this.repository().values()) {
            if (user.tenantId().equals(aTenantId)) {
                if (user.username().equals(aUsername)) {
                    if (user.internalAccessOnlyEncryptedPassword().equals(anEncryptedPassword)) {
                        return user;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public User userWithUsername(TenantId aTenantId, String aUsername) {
        for (User user : this.repository().values()) {
            if (user.tenantId().equals(aTenantId)) {
                if (user.username().equals(aUsername)) {
                    return user;
                }
            }
        }

        return null;
    }

    @Override
    public void clean() {
        this.repository().clear();
    }

    private String keyOf(TenantId aTenantId, String aUsername) {
        return aTenantId.value().toString() + "#" + aUsername;
    }

    private String keyOf(User aUser) {
        return this.keyOf(aUser.tenantId(), aUser.username());
    }

    private Map<String, User> repository() {
        return this.repository;
    }
}
