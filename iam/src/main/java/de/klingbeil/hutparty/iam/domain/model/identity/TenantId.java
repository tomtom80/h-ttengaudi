package de.klingbeil.hutparty.iam.domain.model.identity;

import java.util.UUID;

public record TenantId(UUID value) {
    public String id() {
        return value.toString().toUpperCase();
    }
}
