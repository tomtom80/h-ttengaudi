package de.klingbeil.hutparty.iam.domain.model.access;

import java.util.Collection;

import de.klingbeil.hutparty.iam.domain.model.identity.TenantId;

public interface RoleRepository {

    public void add(Role aRole);

    public Collection<Role> allRoles(TenantId aTenantId);

    public void remove(Role aRole);

    public Role roleNamed(TenantId aTenantId, String aRoleName);
}
