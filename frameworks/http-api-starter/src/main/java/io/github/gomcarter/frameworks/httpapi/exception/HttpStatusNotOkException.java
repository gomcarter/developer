package io.github.gomcarter.frameworks.httpapi.exception;

/**
 * @author gomcarter on 2019-11-13 10:27:42
 */
public class HttpStatusNotOkException extends RuntimeException {

    private int statusCode;

    private String reason;

    public HttpStatusNotOkException(int statusCode, String reason) {
        this("statusCode=" + statusCode + ",reason:" + reason, statusCode, reason);
    }

    public HttpStatusNotOkException(String message, int statusCode, String reason) {
        super(message);
        this.statusCode = statusCode;
        this.reason = reason;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
