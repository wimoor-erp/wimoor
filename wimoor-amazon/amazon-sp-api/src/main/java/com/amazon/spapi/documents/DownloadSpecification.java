package com.amazon.spapi.documents;

import com.google.common.base.Preconditions;

/**
 * Specification for {@link DownloadHelper#download(DownloadSpecification)}.
 */
public class DownloadSpecification {
    private final CompressionAlgorithm compressionAlgorithm;
    private final CryptoStreamFactory cryptoStreamFactory;
    private final String url;

    private DownloadSpecification(CompressionAlgorithm compressionAlgorithm, CryptoStreamFactory cryptoStreamFactory,
                                  String url) {
        this.compressionAlgorithm = compressionAlgorithm;
        this.cryptoStreamFactory = cryptoStreamFactory;
        this.url = url;
    }

    CompressionAlgorithm getCompressionAlgorithm() {
        return compressionAlgorithm;
    }

    CryptoStreamFactory getCryptoStreamFactory() {
        return cryptoStreamFactory;
    }

    String getUrl() {
        return url;
    }

    /**
     * Use this to create an instance of a {@link DownloadSpecification}.
     */
    public static class Builder {
        private final CryptoStreamFactory cryptoStreamFactory;
        private final String url;

        private CompressionAlgorithm compressionAlgorithm = null;

        /**
         * Create the builder.
         *
         * @param cryptoStreamFactory The crypto stream factory
         * @param url The url to download the encrypted document from
         */
        public Builder(CryptoStreamFactory cryptoStreamFactory, String url) {
            Preconditions.checkArgument(cryptoStreamFactory != null, "cryptoStreamFactory is required");
            Preconditions.checkArgument(url != null, "url is required");

            this.cryptoStreamFactory = cryptoStreamFactory;
            this.url = url;
        }

        /**
         * The compression algorithm.
         *
         * @param compressionAlgorithm The compression algorithm
         * @return this
         */
        public Builder withCompressionAlgorithm(CompressionAlgorithm compressionAlgorithm) {
            this.compressionAlgorithm = compressionAlgorithm;
            return this;
        }

        /**
         * Create the specification.
         *
         * @return The specification
         */
        public DownloadSpecification build() {
            return new DownloadSpecification(compressionAlgorithm, cryptoStreamFactory, url);
        }

    }
}
