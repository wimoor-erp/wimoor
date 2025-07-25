package com.amazon.spapi.documents.exception;

/**
 * The details of an HTTP response that indicates failure.
 */
public class HttpResponseException extends Exception {
    private final String body;
    private final int code;

    /**
     * {@inheritDoc}
     *
     * @param message The {@link Exception} message
     * @param body The body
     * @param code The HTTP status code
     */
    public HttpResponseException(String message, String body, int code) {
        super(message);
        this.body = body;
        this.code = code;
    }

    /**
     * {@inheritDoc}
     *
     * @param message The {@link Exception} message
     * @param cause The {@link Exception} cause
     * @param body The body
     * @param code The HTTP status code
     */
    public HttpResponseException(String message, Throwable cause, String body, int code) {
        super(message, cause);
        this.body = body;
        this.code = code;
    }

    /**
     * The body. To ensure that a remote server cannot overwhelm heap memory, the body may have been truncated.
     *
     * @return The body
     */
    public String getBody() {
        return body;
    }

    /**
     * The HTTP status code
     *
     * @return The HTTP status code
     */
    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return super.toString() + " {code="
                + getCode()
                + ", body="
                + getBody()
                + '}';
    }
}
