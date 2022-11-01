package de.klingbeil.hutparty.event.sourceing;

public class EventStoreVersionException extends EventStoreException {

    private static final long serialVersionUID = 1L;

    public EventStoreVersionException(String aMessage) {
        super(aMessage);
    }

    public EventStoreVersionException(String aMessage, Throwable aCause) {
        super(aMessage, aCause);
    }
}
