package com.amazon.spapi.documents;

import com.amazon.spapi.documents.exception.HttpResponseException;
import com.amazon.spapi.documents.impl.OkHttpTransferClient;

import java.io.File;
import java.io.IOException;

/**
 * Helper for downloading encrypted documents.
 */
public class DownloadHelper {
    private final HttpTransferClient httpTransferClient;
    private final String tmpFilePrefix;
    private final String tmpFileSuffix;
    private final File tmpFileDirectory;

    private DownloadHelper(HttpTransferClient httpTransferClient, String tmpFilePrefix, String tmpFileSuffix,
                           File tmpFileDirectory) {
        this.httpTransferClient = httpTransferClient;
        this.tmpFilePrefix = tmpFilePrefix;
        this.tmpFileSuffix = tmpFileSuffix;
        this.tmpFileDirectory = tmpFileDirectory;
    }

    /**
     * Download the specified document's encrypted contents to a temporary file on disk. It is the responsibility of the
     * caller to call <code>close</code> on the returned {@link AutoCloseable} {@link DownloadBundle}.
     *
     * Common reasons for receiving a 403 response include:
     * <li> The signed URL has expired
     *
     * @param spec The specification for the download
     * @return The closeable {@link DownloadBundle}
     * @throws HttpResponseException On failure HTTP response
     * @throws IOException IO Exception
     */
    public DownloadBundle download(DownloadSpecification spec) throws HttpResponseException, IOException {

        File tmpFile = File.createTempFile(tmpFilePrefix, tmpFileSuffix, tmpFileDirectory);

        try {
            tmpFile.deleteOnExit();

            String contentType = httpTransferClient.download(spec.getUrl(), tmpFile);

            return new DownloadBundle(
                    spec.getCompressionAlgorithm(), contentType, spec.getCryptoStreamFactory(), tmpFile);
        } catch (Exception e) {
            tmpFile.delete();
            throw e;
        }
    }

    /**
     * Use this to create an instance of a {@link DownloadHelper}.
     */
    public static class Builder {
        private HttpTransferClient httpTransferClient = null;
        private String tmpFilePrefix = "SPAPI";
        private String tmpFileSuffix = null;
        private File tmpFileDirectory = null;

        /**
         * The HTTP transfer client.
         *
         * @param httpTransferClient The HTTP transfer client
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
         * @return The helper
         */
        public DownloadHelper build() {
            if (httpTransferClient == null) {
                httpTransferClient = new OkHttpTransferClient.Builder().build();
            }

            return new DownloadHelper(httpTransferClient, tmpFilePrefix, tmpFileSuffix, tmpFileDirectory);
        }
    }
}
