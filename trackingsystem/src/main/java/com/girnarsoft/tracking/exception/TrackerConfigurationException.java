package com.girnarsoft.tracking.exception;

public class TrackerConfigurationException extends RuntimeException {
    public TrackerConfigurationException() {
    }

    public TrackerConfigurationException(String detailMessage) {
        super(detailMessage);
    }

    public TrackerConfigurationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public TrackerConfigurationException(Throwable throwable) {
        super(throwable);
    }
}
