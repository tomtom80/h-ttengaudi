package de.klingbeil.hutparty.event.sourceing;

import java.util.List;

import de.klingbeil.hutparty.domain.model.DomainEvent;

public interface EventStream {

    public List<DomainEvent> events();

    public int version();
}
