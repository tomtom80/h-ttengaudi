package de.klingbeil.hutparty.iam.domain.model.identity;

import de.klingbeil.hutparty.domain.model.ConcurrencySafeEntity;
import de.klingbeil.hutparty.domain.model.DomainEventPublisher;

public class Person extends ConcurrencySafeEntity {

    private static final long serialVersionUID = 1L;

    private ContactInformation contactInformation;
    private FullName name;
    private TenantId tenantId;
    private User user;

    public Person(
        TenantId aTenantId,
        FullName aName,
        ContactInformation aContactInformation) {

        this();

        this.setContactInformation(aContactInformation);
        this.setName(aName);
        this.setTenantId(aTenantId);
    }

    public void changeContactInformation(ContactInformation aContactInformation) {
        this.setContactInformation(aContactInformation);

        DomainEventPublisher
            .instance()
            .publish(new PersonContactInformationChanged(
                this.tenantId(),
                this.user().username(),
                this.contactInformation()));
    }

    public void changeName(FullName aName) {
        this.setName(aName);

        DomainEventPublisher
            .instance()
            .publish(new PersonNameChanged(
                this.tenantId(),
                this.user().username(),
                this.name()));
    }

    public ContactInformation contactInformation() {
        return this.contactInformation;
    }

    public EmailAddress emailAddress() {
        return this.contactInformation().emailAddress();
    }

    public FullName name() {
        return this.name;
    }

    @Override
    public boolean equals(Object anObject) {
        boolean equalObjects = false;

        if (anObject != null && this.getClass() == anObject.getClass()) {
            Person typedObject = (Person) anObject;
            equalObjects =
                this.tenantId().equals(typedObject.tenantId()) &&
                    this.user().username().equals(typedObject.user().username());
        }

        return equalObjects;
    }

    @Override
    public int hashCode() {
        return +(90113 * 223)
            + this.tenantId().hashCode()
            + this.user().username().hashCode();
    }

    @Override
    public String toString() {
        return "Person [tenantId=" + tenantId + ", name=" + name + ", contactInformation=" + contactInformation + "]";
    }

    protected Person() {
        super();
    }

    protected void setContactInformation(ContactInformation aContactInformation) {
        this.assertArgumentNotNull(aContactInformation, "The person contact information is required.");

        this.contactInformation = aContactInformation;
    }

    protected void setName(FullName aName) {
        this.assertArgumentNotNull(aName, "The person name is required.");

        this.name = aName;
    }

    protected TenantId tenantId() {
        return this.tenantId;
    }

    protected void setTenantId(TenantId aTenantId) {
        this.assertArgumentNotNull(aTenantId, "The tenantId is required.");

        this.tenantId = aTenantId;
    }

    protected User user() {
        return this.user;
    }

    public void internalOnlySetUser(User aUser) {
        this.user = aUser;
    }
}
