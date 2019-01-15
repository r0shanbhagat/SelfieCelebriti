package com.girnarsoft.tracking.exception;

public class TrackerException extends RuntimeException {

    public TrackerException() {

    }

    public TrackerException(String msg) {
        super(msg);
    }

    public TrackerException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
