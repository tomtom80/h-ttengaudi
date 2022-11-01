package de.klingbeil.hutparty.iam.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.klingbeil.hutparty.iam.domain.model.identity.Group;
import de.klingbeil.hutparty.iam.domain.model.identity.GroupRepository;
import de.klingbeil.hutparty.iam.domain.model.identity.TenantId;
import de.klingbeil.hutparty.persistence.CleanableStore;

public class InMemoryGroupRepository implements GroupRepository, CleanableStore {

    private Map<String, Group> repository;

    public InMemoryGroupRepository() {
        super();

        this.repository = new HashMap<String, Group>();
    }

    @Override
    public void add(Group aGroup) {
        String key = this.keyOf(aGroup);

        if (this.repository().containsKey(key)) {
            throw new IllegalStateException("Duplicate key.");
        }

        this.repository().put(key, aGroup);
    }

    @Override
    public Collection<Group> allGroups(TenantId aTenantId) {
        List<Group> groups = new ArrayList<Group>();

        for (Group group : this.repository().values()) {
            if (group.tenantId().equals(aTenantId)) {
                groups.add(group);
            }
        }

        return groups;
    }

    @Override
    public Group groupNamed(TenantId aTenantId, String aName) {
        if (aName.startsWith(Group.ROLE_GROUP_PREFIX)) {
            throw new IllegalArgumentException("May not find internal groups.");
        }

        String key = this.keyOf(aTenantId, aName);

        return this.repository().get(key);
    }

    @Override
    public void remove(Group aGroup) {
        String key = this.keyOf(aGroup);

        this.repository().remove(key);
    }

    @Override
    public void clean() {
        this.repository().clear();
    }

    private String keyOf(TenantId aTenantId, String aName) {
        String key = aTenantId.getValue().toString() + "#" + aName;

        return key;
    }

    private String keyOf(Group aGroup) {
        return this.keyOf(aGroup.tenantId(), aGroup.name());
    }

    private Map<String, Group> repository() {
        return this.repository;
    }
}
