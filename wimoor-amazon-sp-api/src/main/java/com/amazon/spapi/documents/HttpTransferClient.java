package com.amazon.spapi.documents;

import com.amazon.spapi.documents.exception.HttpResponseException;

import java.io.File;
import java.io.IOException;

/**
 * HTTP transfer client. Implementations of this interface must be thread-safe and reusable for multiple requests.
 */
public interface HttpTransferClient {
    /**
     * Perform an HTTP GET on the specified <code>url</code>, storing the response body to <code>destination</code>.
     *
     * @param url The url to perform an HTTP GET on
     * @param destination The file to write the HTTP GET response body to
     * @return The Content-Type header value extracted from the response to the HTTP GET
     * @throws HttpResponseException On failure HTTP response
     * @throws IOException IO exception
     */
    String download(String url, File destination) throws HttpResponseException, IOException;

    /**
     * Perform an HTTP PUT on the specified <code>url</code>, uploading the contents of <code>source</code> to the body
     * of the request.
     *
     * @param url The url to perform an HTTP PUT on
     * @param contentType The Content-Type header to be used for the HTTP PUT request
     * @param source The file to read the HTTP PUT body from
     * @throws HttpResponseException On failure HTTP response
     * @throws IOException IO exception
     */
    void upload(String url, String contentType, File source) throws HttpResponseException, IOException;
}
