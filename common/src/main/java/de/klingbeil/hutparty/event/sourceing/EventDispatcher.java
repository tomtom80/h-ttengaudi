package de.klingbeil.hutparty.event.sourceing;

public interface EventDispatcher {

    public void dispatch(DispatchableDomainEvent aDispatchableDomainEvent);

    public void registerEventDispatcher(EventDispatcher anEventDispatcher);

    public boolean understands(DispatchableDomainEvent aDispatchableDomainEvent);
}
