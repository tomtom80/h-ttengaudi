package de.klingbeil.hutparty.iam.infrastructure.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.klingbeil.hutparty.iam.domain.model.identity.Tenant;
import de.klingbeil.hutparty.iam.domain.model.identity.TenantId;
import de.klingbeil.hutparty.iam.domain.model.identity.TenantRepository;
import de.klingbeil.hutparty.persistence.CleanableStore;

public class InMemoryTenantRepository implements TenantRepository, CleanableStore {

    private final Map<String, Tenant> repository;

    public InMemoryTenantRepository() {
        super();

        this.repository = new HashMap<>();
    }

    @Override
    public void add(Tenant aTenant) {
        String key = this.keyOf(aTenant);

        if (this.repository().containsKey(key)) {
            throw new IllegalStateException("Duplicate key.");
        }

        this.repository().put(key, aTenant);
    }

    @Override
    public TenantId nextIdentity() {
        return new TenantId(UUID.randomUUID());
    }

    @Override
    public Tenant tenantNamed(String aName) {
        for (Tenant tenant : this.repository().values()) {
            if (tenant.name().equals(aName)) {
                return tenant;
            }
        }

        return null;
    }

    @Override
    public Tenant tenantOfId(TenantId aTenantId) {
        return this.repository().get(this.keyOf(aTenantId));
    }

    @Override
    public void remove(Tenant aTenant) {
        String key = this.keyOf(aTenant);

        this.repository().remove(key);
    }

    @Override
    public void clean() {
        this.repository().clear();
    }

    private String keyOf(TenantId aTenantId) {
        return aTenantId.value().toString();
    }

    private String keyOf(Tenant aTenant) {
        return this.keyOf(aTenant.tenantId());
    }

    private Map<String, Tenant> repository() {
        return this.repository;
    }
}
