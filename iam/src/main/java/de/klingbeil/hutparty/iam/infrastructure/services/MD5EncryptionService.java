package de.klingbeil.hutparty.iam.infrastructure.services;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import de.klingbeil.hutparty.AssertionConcern;
import de.klingbeil.hutparty.iam.domain.model.identity.EncryptionService;

public class MD5EncryptionService extends AssertionConcern implements EncryptionService {

    public MD5EncryptionService() {
        super();
    }

    @Override
    public String encryptedValue(String aPlainTextValue) {
        this.assertArgumentNotEmpty(
            aPlainTextValue,
            "Plain text value to encrypt must be provided.");

        String encryptedValue = null;

        try {

            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.update(aPlainTextValue.getBytes(StandardCharsets.UTF_8));

            BigInteger bigInt = new BigInteger(1, messageDigest.digest());

            encryptedValue = bigInt.toString(16);

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        return encryptedValue;
    }
}
