package com.amazon.spapi.documents;

import com.amazon.spapi.documents.exception.CryptoException;

import java.io.InputStream;

/**
 * Crypto stream factory interface.
 */
public interface CryptoStreamFactory {
    /**
     * Create a new {@link InputStream} that decrypts a stream of encrypted data.
     *
     * @param source The source for encrypted data to decrypt
     * @return A new {@link InputStream} from which decrypted data can be read
     * @throws CryptoException Crypto exception
     */
    InputStream newDecryptStream(InputStream source) throws CryptoException;

    /**
     * Create a new {@link InputStream} that encrypts a stream of unencrypted data.
     *
     * @param source The source for unencrypted data to encrypt
     * @return A new {@link InputStream} from which encrypted data can be read
     * @throws CryptoException Crypto exception
     */
    InputStream newEncryptStream(InputStream source) throws CryptoException;

}
