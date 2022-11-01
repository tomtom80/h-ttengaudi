package de.klingbeil.hutparty.iam.infrastructure;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import de.klingbeil.hutparty.event.EventStore;
import de.klingbeil.hutparty.iam.domain.model.DomainRegistry;
import de.klingbeil.hutparty.iam.domain.model.access.AuthorizationService;
import de.klingbeil.hutparty.iam.domain.model.access.RoleRepository;
import de.klingbeil.hutparty.iam.domain.model.identity.AuthenticationService;
import de.klingbeil.hutparty.iam.domain.model.identity.EncryptionService;
import de.klingbeil.hutparty.iam.domain.model.identity.GroupRepository;
import de.klingbeil.hutparty.iam.domain.model.identity.PasswordService;
import de.klingbeil.hutparty.iam.domain.model.identity.TenantProvisioningService;
import de.klingbeil.hutparty.iam.domain.model.identity.TenantRepository;
import de.klingbeil.hutparty.iam.domain.model.identity.UserRepository;
import de.klingbeil.hutparty.iam.infrastructure.persistence.InMemoryEventStore;
import de.klingbeil.hutparty.iam.infrastructure.persistence.InMemoryGroupRepository;
import de.klingbeil.hutparty.iam.infrastructure.persistence.InMemoryRoleRepository;
import de.klingbeil.hutparty.iam.infrastructure.persistence.InMemoryTenantRepository;
import de.klingbeil.hutparty.iam.infrastructure.persistence.InMemoryUserRepository;
import de.klingbeil.hutparty.iam.infrastructure.services.MD5EncryptionService;

@TestConfiguration
public class TestConfig {

    @Bean
    public DomainRegistry domainRegistry() {
        return new DomainRegistry();
    }

    @Bean
    public AuthenticationService authenticationService() {
        return new AuthenticationService(encryptionService(),tenantRepository(),userRepository());
    }

    @Bean
    public AuthorizationService authorizationService() {
        return new AuthorizationService(groupRepository(),roleRepository(),userRepository());
    }

    @Bean
    public EncryptionService encryptionService() {
        return new MD5EncryptionService();
    }

    @Bean
    public EventStore eventStore() {
        return new InMemoryEventStore();
    }

    @Bean
    public GroupRepository groupRepository() {
        return new InMemoryGroupRepository();
    }

    @Bean
    public RoleRepository roleRepository() {
        return new InMemoryRoleRepository();
    }

    @Bean
    public TenantRepository tenantRepository() {
        return new InMemoryTenantRepository();
    }

    @Bean
    public PasswordService passwordService() {
        return new PasswordService();
    }
    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository();
    }

    @Bean
    public TenantProvisioningService tenantProvisioningService() {
        return new TenantProvisioningService(roleRepository(),tenantRepository(),userRepository(),passwordService());
    }
}
