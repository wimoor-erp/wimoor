package com.amazon.spapi.documents;

import com.amazon.spapi.documents.exception.CryptoException;
import com.amazon.spapi.documents.exception.MissingCharsetException;
import com.squareup.okhttp.MediaType;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

/**
 * Helper that contains and provides access to the downloaded contents of an encrypted document. {@link #close()} must
 * be called to delete the temporary file that contains the encrypted document contents.
 *
 * Multiple independent streams and readers (from which unencrypted data can be read while maintaining encryption at
 * rest on the filesystem) can be opened from an instance of {@link DownloadBundle}, but once {@link #close()} is
 * called the behavior of any open streams or readers from this instance will be unspecified as the underlying
 * temporary file will be deleted.
 */
public class DownloadBundle implements AutoCloseable {
    private final CompressionAlgorithm compressionAlgorithm;
    private final String contentType;
    private final CryptoStreamFactory cryptoStreamFactory;
    private final File document;

    DownloadBundle(CompressionAlgorithm compressionAlgorithm, String contentType,
                   CryptoStreamFactory cryptoStreamFactory, File document) {
        this.compressionAlgorithm = compressionAlgorithm;
        this.contentType = contentType;
        this.cryptoStreamFactory = cryptoStreamFactory;
        this.document = document;
    }

    /**
     * The content type of the unencrypted document contents.
     *
     * @return The content type
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Open an {@link InputStream} that allows the caller to read the decompressed (if applicable) and decrypted
     * contents of the downloaded document. It is the responsibility of the caller to close the returned
     * {@link InputStream}. The {@link InputStream}'s operation will be unspecified once this {@link DownloadBundle} is
     * closed.
     *
     * @return An {@link InputStream} that decompresses and decrypts the document's contents
     * @throws CryptoException Crypto exception
     * @throws IOException IO exception
     */
    public InputStream newInputStream() throws CryptoException, IOException {
        Closeable closeThis = null;
        try {
            InputStream inputStream = new FileInputStream(document);
            closeThis = inputStream;

            inputStream = cryptoStreamFactory.newDecryptStream(inputStream);
            closeThis = inputStream;

            if (compressionAlgorithm != null) {
                switch (compressionAlgorithm) {
                    case GZIP:
                        inputStream = new GZIPInputStream(inputStream);
                        closeThis = inputStream;
                }
            }

            closeThis = null;
            return inputStream;
        } finally {
            IOUtils.closeQuietly(closeThis);
        }
    }

    /**
     * Open a {@link BufferedReader} that allows the caller to read the decompressed (if applicable) and decrypted
     * contents of the downloaded document. The character set is parsed from {@link #getContentType()}. If the character
     * set could not be parsed, this method will fail with {@link MissingCharsetException}.
     *
     * It is the responsibility of the caller to close the returned {@link BufferedReader}. This {@link BufferedReader}
     * will become invalid once this {@link DownloadBundle} is closed.
     *
     * @return A {@link BufferedReader} that decompresses and decrypts the document's contents
     * @throws CryptoException Crypto exception
     * @throws IOException IO exception
     * @throws MissingCharsetException The character set could not be parsed from {@link #getContentType()}
     */
    public BufferedReader newBufferedReader() throws CryptoException, IOException, MissingCharsetException {
        return newBufferedReader(null);
    }

    /**
     * Open a {@link BufferedReader} that allows the caller to read the decompressed (if applicable) and decrypted
     * contents of the downloaded document. The character set is parsed from {@link #getContentType()}. If the character
     * set could not be parsed and <code>defaultCharset</code> is specified, this method will attempt to open the reader
     * with <code>defaultCharset</code>.  Otherwise, this method will fail with {@link MissingCharsetException}.
     *
     * It is the responsibility of the caller to close the returned {@link BufferedReader}. This {@link BufferedReader}
     * will become invalid once this {@link DownloadBundle} is closed.
     *
     * @param defaultCharset The default charset to use if a charset cannot be parsed from the content type.
     * @return A {@link BufferedReader} that decompresses and decrypts the document's contents
     * @throws CryptoException Crypto exception
     * @throws IOException IO exception
     * @throws MissingCharsetException The character set could not be parsed from {@link #getContentType()} and
     *                                  <code>defaultCharset</code> was not specified.
     */
    public BufferedReader newBufferedReader(Charset defaultCharset) throws CryptoException, IOException,
            MissingCharsetException {
        String contentType = getContentType();

        Charset charset = MediaType.parse(contentType).charset();
        if (charset == null) {
            charset = defaultCharset;
        }

        if (charset == null) {
            throw new MissingCharsetException(String.format(
                    "Could not parse character set from content type '%s' and no default provided", contentType));
        }

        Closeable closeThis = null;
        try {
            InputStream inputStream = newInputStream();
            closeThis = inputStream;

            InputStreamReader reader = new InputStreamReader(inputStream, charset);
            closeThis = reader;

            BufferedReader bufferedReader = new BufferedReader(reader);
            closeThis = null;
            return bufferedReader;
        } finally {
            IOUtils.closeQuietly(closeThis);
        }
    }

    /**
     * Closes this {@link DownloadBundle}, deleting the temporary file containing the encrypted document contents.
     */
    public void close() {
        document.delete();
    }
}
