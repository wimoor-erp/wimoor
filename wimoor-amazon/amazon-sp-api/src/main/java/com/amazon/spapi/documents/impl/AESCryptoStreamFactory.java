package com.amazon.spapi.documents.impl;

import com.amazon.spapi.documents.CryptoStreamFactory;
import com.amazon.spapi.documents.exception.CryptoException;
import com.google.common.base.Preconditions;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * A crypto stream factory implementing AES encryption.
 */
public class AESCryptoStreamFactory implements CryptoStreamFactory {
    private static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";

    private final Key key;
    private final byte[] initializationVector;

    private AESCryptoStreamFactory(Key key, byte[] initializationVector) {
        this.key = key;
        this.initializationVector = initializationVector;
    }

    private Cipher createInitializedCipher(int mode) throws CryptoException {
        try {
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(mode, key, new IvParameterSpec(initializationVector));
            return cipher;
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new CryptoException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream newDecryptStream(InputStream source) throws CryptoException {
        return new CipherInputStream(source, createInitializedCipher(Cipher.DECRYPT_MODE));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream newEncryptStream(InputStream source) throws CryptoException {
        return new CipherInputStream(source, createInitializedCipher(Cipher.ENCRYPT_MODE));
    }

    /**
     * Use this to create an instance of an {@link AESCryptoStreamFactory}.
     */
    public static class Builder {
        private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();
        private static final String REQUIRED_KEY_ALGORITHM = "AES";

        private final String key;
        private final String initializationVector;

        /**
         * Create the builder.
         *
         * @param key The key
         * @param initializationVector The initialization vector
         */
        public Builder(String key, String initializationVector) {
            Preconditions.checkArgument(key != null, "key is required");
            Preconditions.checkArgument(initializationVector != null, "initializationVector is required");

            this.key = key;
            this.initializationVector = initializationVector;
        }

        /**
         * Create the crypto stream factory.
         *
         * @return the crypto stream factory
         */
        public AESCryptoStreamFactory build() {
            Key convertedKey = new SecretKeySpec(BASE64_DECODER.decode(key), REQUIRED_KEY_ALGORITHM);
            byte[] convertedInitializationVector = BASE64_DECODER.decode(initializationVector);

            return new AESCryptoStreamFactory(convertedKey, convertedInitializationVector);
        }
    }
}
