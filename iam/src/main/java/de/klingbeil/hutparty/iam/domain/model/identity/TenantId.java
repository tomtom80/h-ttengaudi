package de.klingbeil.hutparty.iam.domain.model.identity;

import java.io.Serializable;
import java.util.UUID;

import lombok.Value;

@Value
public final class TenantId implements Serializable {
    private UUID value;
}
