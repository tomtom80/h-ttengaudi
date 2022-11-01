package de.klingbeil.hutparty.event;

import java.util.List;

import de.klingbeil.hutparty.domain.model.DomainEvent;

public interface EventStore {

    public List<StoredEvent> allStoredEventsBetween(long aLowStoredEventId, long aHighStoredEventId);

    public List<StoredEvent> allStoredEventsSince(long aStoredEventId);

    public StoredEvent append(DomainEvent aDomainEvent);

    public void close();

    public long countStoredEvents();
}
