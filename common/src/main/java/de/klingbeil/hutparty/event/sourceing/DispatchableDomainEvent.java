package de.klingbeil.hutparty.event.sourceing;

import de.klingbeil.hutparty.domain.model.DomainEvent;

public class DispatchableDomainEvent {

    private DomainEvent domainEvent;
    private long eventId;

    public DispatchableDomainEvent(long anEventId, DomainEvent aDomainEvent) {
        super();

        this.domainEvent = aDomainEvent;
        this.eventId = anEventId;
    }

    public DomainEvent domainEvent() {
        return this.domainEvent;
    }

    public long eventId() {
        return this.eventId;
    }
}
