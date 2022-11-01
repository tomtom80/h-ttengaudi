package de.klingbeil.hutparty.event;

import de.klingbeil.hutparty.domain.model.DomainEvent;
import de.klingbeil.hutparty.serializer.AbstractSerializer;

public class EventSerializer extends AbstractSerializer {

    private static EventSerializer eventSerializer;

    public static synchronized EventSerializer instance() {
        if (EventSerializer.eventSerializer == null) {
            EventSerializer.eventSerializer = new EventSerializer();
        }

        return EventSerializer.eventSerializer;
    }

    public EventSerializer(boolean isCompact) {
        this(false, isCompact);
    }

    public EventSerializer(boolean isPretty, boolean isCompact) {
        super(isPretty, isCompact);
    }

    public String serialize(DomainEvent aDomainEvent) {

        return this.gson().toJson(aDomainEvent);
    }

    public <T extends DomainEvent> T deserialize(String aSerialization, final Class<T> aType) {

        return this.gson().fromJson(aSerialization, aType);
    }

    private EventSerializer() {
        this(false, false);
    }
}
