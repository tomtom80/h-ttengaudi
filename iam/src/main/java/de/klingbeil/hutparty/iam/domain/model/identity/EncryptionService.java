package de.klingbeil.hutparty.iam.domain.model.identity;

public interface EncryptionService {

    public String encryptedValue(String aPlainTextValue);
}
