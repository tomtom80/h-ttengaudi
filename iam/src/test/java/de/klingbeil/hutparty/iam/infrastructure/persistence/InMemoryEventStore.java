package de.klingbeil.hutparty.iam.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;

import de.klingbeil.hutparty.domain.model.DomainEvent;
import de.klingbeil.hutparty.event.EventSerializer;
import de.klingbeil.hutparty.event.EventStore;
import de.klingbeil.hutparty.event.StoredEvent;
import de.klingbeil.hutparty.persistence.CleanableStore;

public class InMemoryEventStore implements EventStore, CleanableStore {

    private List<StoredEvent> storedEvents;

    public InMemoryEventStore() {
        super();

        this.storedEvents = new ArrayList<StoredEvent>();
    }

    @Override
    public List<StoredEvent> allStoredEventsBetween(
        long aLowStoredEventId,
        long aHighStoredEventId) {
        List<StoredEvent> events = new ArrayList<StoredEvent>();

        for (StoredEvent storedEvent : this.storedEvents) {
            if (storedEvent.eventId() >= aLowStoredEventId && storedEvent.eventId() <= aHighStoredEventId) {
                events.add(storedEvent);
            }
        }

        return events;
    }

    @Override
    public List<StoredEvent> allStoredEventsSince(long aStoredEventId) {
        List<StoredEvent> events = new ArrayList<StoredEvent>();

        for (StoredEvent storedEvent : this.storedEvents) {
            if (storedEvent.eventId() > aStoredEventId) {
                events.add(storedEvent);
            }
        }

        return events;
    }

    @Override
    public synchronized StoredEvent append(DomainEvent aDomainEvent) {
        String eventSerialization =
            EventSerializer.instance().serialize(aDomainEvent);

        StoredEvent storedEvent =
            new StoredEvent(
                aDomainEvent.getClass().getName(),
                aDomainEvent.occurredOn(),
                eventSerialization,
                this.storedEvents.size() + 1);

        this.storedEvents.add(storedEvent);

        return storedEvent;
    }

    @Override
    public void close() {
        this.clean();
    }

    @Override
    public long countStoredEvents() {
        return this.storedEvents.size();
    }

    @Override
    public void clean() {
        this.storedEvents = new ArrayList<StoredEvent>();
    }
}
