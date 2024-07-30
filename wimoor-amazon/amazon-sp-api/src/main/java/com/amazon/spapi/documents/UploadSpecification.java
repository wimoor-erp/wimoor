package com.amazon.spapi.documents;

import com.google.common.base.Preconditions;

import java.io.InputStream;

/**
 * Specification for {@link UploadHelper#upload(UploadSpecification)}.
 */
public class UploadSpecification {
    private final String contentType;
    private final CryptoStreamFactory cryptoStreamFactory;
    private final InputStream source;
    private final String url;

    private UploadSpecification(String contentType, CryptoStreamFactory cryptoStreamFactory, InputStream source,
                                String url) {
        this.contentType = contentType;
        this.cryptoStreamFactory = cryptoStreamFactory;
        this.source = source;
        this.url = url;
    }

    String getContentType() {
        return contentType;
    }

    CryptoStreamFactory getCryptoStreamFactory() {
        return cryptoStreamFactory;
    }

    InputStream getSource() {
        return source;
    }

    String getUrl() {
        return url;
    }

    /**
     * Use this to create an instance of an {@link UploadSpecification}.
     */
    public static class Builder {
        private final String contentType;
        private final CryptoStreamFactory cryptoStreamFactory;
        private final InputStream source;
        private final String url;

        /**
         * Create the builder.
         *
         * @param contentType The content type of the document to upload
         * @param cryptoStreamFactory The crypto stream factory
         * @param source The source of the unencrypted data to upload
         * @param url The url
         */
        public Builder(String contentType, CryptoStreamFactory cryptoStreamFactory, InputStream source, String url) {
            Preconditions.checkArgument(contentType != null, "contentType is required");
            Preconditions.checkArgument(cryptoStreamFactory != null, "cryptoStreamFactory is required");
            Preconditions.checkArgument(source != null, "source is required");
            Preconditions.checkArgument(url != null, "url is required");

            this.contentType = contentType;
            this.cryptoStreamFactory = cryptoStreamFactory;
            this.source = source;
            this.url = url;
        }

        /**
         * Create the specification.
         *
         * @return The specification
         */
        public UploadSpecification build() {
            return new UploadSpecification(contentType, cryptoStreamFactory, source, url);
        }

    }
}
