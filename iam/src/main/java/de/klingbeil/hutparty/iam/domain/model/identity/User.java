package de.klingbeil.hutparty.iam.domain.model.identity;


import java.io.Serial;

import de.klingbeil.hutparty.domain.model.ConcurrencySafeEntity;
import de.klingbeil.hutparty.domain.model.DomainEventPublisher;
import de.klingbeil.hutparty.iam.domain.model.DomainRegistry;

public class User extends ConcurrencySafeEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private Activation activation;
    private String password;
    private Person person;
    private TenantId tenantId;
    private String username;

    public void changePassword(String currentPassword, String changedPassword) {
        this.assertArgumentNotEmpty(currentPassword, "Current and new password must be provided.");

        this.assertArgumentEquals(this.password(), this.asEncryptedValue(currentPassword), "Current password not confirmed.");

        this.protectPassword(currentPassword, changedPassword);

        DomainEventPublisher.instance().publish(new UserPasswordChanged(this.tenantId(), this.username()));
    }

    public void changePersonalContactInformation(ContactInformation contactInformation) {
        this.person().changeContactInformation(contactInformation);
    }

    public void changePersonalName(FullName personalName) {
        this.person().changeName(personalName);
    }

    public void defineActivation(Activation activation) {
        this.setActivation(activation);

        DomainEventPublisher.instance().publish(new UserActivationChanged(this.tenantId(), this.username(), this.activation()));
    }

    public boolean isEnabled() {
        return this.activation().isActivationEnabled();
    }

    public Person person() {
        return this.person;
    }

    public TenantId tenantId() {
        return this.tenantId;
    }

    public UserDescriptor userDescriptor() {
        return new UserDescriptor(this.tenantId(), this.username(), this.person().emailAddress().address());
    }

    public String username() {
        return this.username;
    }

    @Override
    public boolean equals(Object anObject) {
        boolean equalObjects = false;

        if (anObject != null && this.getClass() == anObject.getClass()) {
            User typedObject = (User) anObject;
            equalObjects = this.tenantId().equals(typedObject.tenantId()) && this.username().equals(typedObject.username());
        }

        return equalObjects;
    }

    @Override
    public int hashCode() {

        return (45217 * 269) + this.tenantId().hashCode() + this.username().hashCode();
    }

    @Override
    public String toString() {
        return "User [tenantId=" + tenantId + ", username=" + username + ", person=" + person + ", enablement=" + activation + "]";
    }

    protected User(TenantId tenantId, String username, String password, Activation activation, Person person) {

        this();

        this.setActivation(activation);
        this.setPerson(person);
        this.setTenantId(tenantId);
        this.setUsername(username);

        this.protectPassword("", password);

        person.internalOnlySetUser(this);

        DomainEventPublisher.instance().publish(new UserRegistered(this.tenantId(), username, person.name(), person.contactInformation().emailAddress()));
    }

    protected User() {
        super();
    }

    protected String asEncryptedValue(String plainTextPassword) {
        return DomainRegistry.encryptionService().encryptedValue(plainTextPassword);
    }

    protected void assertPasswordsNotSame(String currentPassword, String changedPassword) {
        this.assertArgumentNotEquals(currentPassword, changedPassword, "The password is unchanged.");
    }

    protected void assertPasswordNotWeak(String aPlainTextPassword) {
        this.assertArgumentFalse(DomainRegistry.passwordService().isWeak(aPlainTextPassword), "The password must be stronger.");
    }

    protected void assertUsernamePasswordNotSame(String plainTextPassword) {
        this.assertArgumentNotEquals(this.username(), plainTextPassword, "The username and password must not be the same.");
    }

    protected Activation activation() {
        return this.activation;
    }

    protected void setActivation(Activation activation) {
        this.assertArgumentNotNull(activation, "The activation is required.");

        this.activation = activation;
    }

    public String internalAccessOnlyEncryptedPassword() {
        return this.password();
    }

    protected String password() {
        return this.password;
    }

    protected void setPassword(String aPassword) {
        this.password = aPassword;
    }

    protected void setPerson(Person aPerson) {
        this.assertArgumentNotNull(aPerson, "The person is required.");

        this.person = aPerson;
    }

    protected void protectPassword(String aCurrentPassword, String aChangedPassword) {
        this.assertPasswordsNotSame(aCurrentPassword, aChangedPassword);

        this.assertPasswordNotWeak(aChangedPassword);

        this.assertUsernamePasswordNotSame(aChangedPassword);

        this.setPassword(this.asEncryptedValue(aChangedPassword));
    }

    protected void setTenantId(TenantId aTenantId) {
        this.assertArgumentNotNull(aTenantId, "The tenantId is required.");

        this.tenantId = aTenantId;
    }

    protected GroupMember toGroupMember() {
        return new GroupMember(this.tenantId(), this.username(), GroupMemberType.User);
    }

    protected void setUsername(String aUsername) {
        this.assertArgumentNotEmpty(aUsername, "The username is required.");
        this.assertArgumentLength(aUsername, 3, 250, "The username must be 3 to 250 characters.");

        this.username = aUsername;
    }
}
