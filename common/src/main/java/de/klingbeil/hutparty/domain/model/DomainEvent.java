package de.klingbeil.hutparty.domain.model;

import java.time.Instant;

public interface DomainEvent {

    int eventVersion();

    Instant occurredOn();
}
