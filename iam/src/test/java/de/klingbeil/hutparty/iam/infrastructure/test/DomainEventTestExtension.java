package de.klingbeil.hutparty.iam.infrastructure.test;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import de.klingbeil.hutparty.domain.model.DomainEventPublisher;

public class DomainEventTestExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(final ExtensionContext context)  {
        DomainEventPublisher.instance().reset();
    }
}
