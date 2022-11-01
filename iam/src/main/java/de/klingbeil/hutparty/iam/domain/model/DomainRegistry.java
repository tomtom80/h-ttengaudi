package de.klingbeil.hutparty.iam.domain.model;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import de.klingbeil.hutparty.iam.domain.model.access.AuthorizationService;
import de.klingbeil.hutparty.iam.domain.model.access.RoleRepository;
import de.klingbeil.hutparty.iam.domain.model.identity.AuthenticationService;
import de.klingbeil.hutparty.iam.domain.model.identity.EncryptionService;
import de.klingbeil.hutparty.iam.domain.model.identity.GroupMemberService;
import de.klingbeil.hutparty.iam.domain.model.identity.GroupRepository;
import de.klingbeil.hutparty.iam.domain.model.identity.PasswordService;
import de.klingbeil.hutparty.iam.domain.model.identity.TenantProvisioningService;
import de.klingbeil.hutparty.iam.domain.model.identity.TenantRepository;
import de.klingbeil.hutparty.iam.domain.model.identity.UserRepository;

public class DomainRegistry implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static AuthenticationService authenticationService() {
        return (AuthenticationService) applicationContext.getBean("authenticationService");
    }

    public static AuthorizationService authorizationService() {
        return (AuthorizationService) applicationContext.getBean("authorizationService");
    }

    public static EncryptionService encryptionService() {
        return (EncryptionService) applicationContext.getBean("encryptionService");
    }

    public static GroupMemberService groupMemberService() {
        return (GroupMemberService) applicationContext.getBean("groupMemberService");
    }

    public static GroupRepository groupRepository() {
        return (GroupRepository) applicationContext.getBean("groupRepository");
    }

    public static PasswordService passwordService() {
        return (PasswordService) applicationContext.getBean("passwordService");
    }

    public static RoleRepository roleRepository() {
        return (RoleRepository) applicationContext.getBean("roleRepository");
    }

    public static TenantProvisioningService tenantProvisioningService() {
        return (TenantProvisioningService) applicationContext.getBean("tenantProvisioningService");
    }

    public static TenantRepository tenantRepository() {
        return (TenantRepository) applicationContext.getBean("tenantRepository");
    }

    public static UserRepository userRepository() {
        return (UserRepository) applicationContext.getBean("userRepository");
    }

    @Override
    public synchronized void setApplicationContext(
        ApplicationContext anApplicationContext)
        throws BeansException {

        if (DomainRegistry.applicationContext == null) {
            DomainRegistry.applicationContext = anApplicationContext;
        }
    }
}
