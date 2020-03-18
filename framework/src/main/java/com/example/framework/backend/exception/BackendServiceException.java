package com.example.framework.backend.exception;

public class BackendServiceException extends RuntimeException {

    private int errorCode;

    public BackendServiceException() {
    }

    public BackendServiceException(int errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public BackendServiceException(String message) {
        super(message);
    }

    public BackendServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public BackendServiceException(Throwable cause) {
        super(cause);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
