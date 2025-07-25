package com.amazon.spapi.documents;

import com.amazon.spapi.documents.exception.CryptoException;
import com.amazon.spapi.documents.exception.HttpResponseException;
import com.amazon.spapi.documents.impl.OkHttpTransferClient;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Helper class for encrypting and uploading documents.
 */
public class UploadHelper {
    private final HttpTransferClient httpTransferClient;
    private final String tmpFilePrefix;
    private final String tmpFileSuffix;
    private final File tmpFileDirectory;

    private UploadHelper(HttpTransferClient httpTransferClient, String tmpFilePrefix, String tmpFileSuffix,
                         File tmpFileDirectory) {
        this.httpTransferClient = httpTransferClient;
        this.tmpFilePrefix = tmpFilePrefix;
        this.tmpFileSuffix = tmpFileSuffix;
        this.tmpFileDirectory = tmpFileDirectory;
    }

    /**
     * Perform the specified upload. This method will buffer the encrypted contents of the document in a temporary file
     * before uploading to the specified url.
     *
     * Common reasons for receiving a 403 response include:
     * <li> The signed URL has expired
     * <li> {@link UploadSpecification#getContentType()} does not match the content type the URL was signed with
     *
     * @param spec The specification for the upload
     * @throws CryptoException Crypto exception
     * @throws HttpResponseException On failure HTTP response
     * @throws IOException IO exception
     */
    public void upload(UploadSpecification spec) throws CryptoException, HttpResponseException, IOException {
        File tmpFile = File.createTempFile(tmpFilePrefix, tmpFileSuffix, tmpFileDirectory);

        try {
            tmpFile.deleteOnExit();

            try (InputStream inputStream = spec.getCryptoStreamFactory().newEncryptStream(spec.getSource())) {
                FileUtils.copyInputStreamToFile(inputStream, tmpFile);
            }

            httpTransferClient.upload(spec.getUrl(), spec.getContentType(), tmpFile);
        } finally {
            tmpFile.delete();
        }
    }

    /**
     * Use this to create an instance of an {@link UploadHelper}.
     */
    public static class Builder {
        private HttpTransferClient httpTransferClient = null;
        private String tmpFilePrefix = "SPAPI";
        private String tmpFileSuffix = null;
        private File tmpFileDirectory = null;

        /**
         * The HTTP transfer client.
         *
         * @param httpTransferClient The HTTP transfer client.
         * @return this
         */
        public Builder withHttpTransferClient(HttpTransferClient httpTransferClient) {
            this.httpTransferClient = httpTransferClient;
            return this;
        }

        /**
         * The tmp file prefix. If not specified, defaults to <code>"SPAPI"</code>.
         *
         * @param tmpFilePrefix The prefix string to be used in generating the tmp file's name; must be at least three
         *                      characters long
         * @return this
         */
        public Builder withTmpFilePrefix(String tmpFilePrefix) {
            if (tmpFilePrefix.length() < 3) {
                throw new IllegalArgumentException("Prefix string too short");
            }

            this.tmpFilePrefix = tmpFilePrefix;
            return this;
        }

        /**
         * The tmp file suffix. If not specified, defaults to null.
         *
         * @param tmpFileSuffix The suffix string to be used in generating the file's name; may be <code>null</code>, in
         *                      which case the suffix <code>".tmp"</code> will be used
         * @return this
         */
        public Builder withTmpFileSuffix(String tmpFileSuffix) {
            this.tmpFileSuffix = tmpFileSuffix;
            return this;
        }

        /**
         * The tmp file directory. If not specified, defaults to null.
         *
         * @param tmpFileDirectory The directory in which the file is to be created, or <code>null</code> if the default
         *                         temporary-file directory is to be used
         * @return this
         */
        public Builder withTmpFileDirectory(File tmpFileDirectory) {
            this.tmpFileDirectory = tmpFileDirectory;
            return this;
        }

        /**
         * Create the helper.
         *
         * @return The helper.
         */
        public UploadHelper build() {
            if (httpTransferClient == null) {
                httpTransferClient = new OkHttpTransferClient.Builder().build();
            }

            return new UploadHelper(httpTransferClient, tmpFilePrefix, tmpFileSuffix, tmpFileDirectory);
        }
    }
}
