package com.girnarsoft.tracking.config;

public class TrackerConfiguration {
    private String flurryId;
    private String googleAnalyticTrackingId;
    private String deviceId;
    private boolean debugMode;
    private boolean existingUser;

    private TrackerConfiguration(String flurryId, String googleAnalyticTrackingId, String deviceId, boolean debugMode,
                                 boolean existingUser) {
        this.flurryId = flurryId;
        this.googleAnalyticTrackingId = googleAnalyticTrackingId;
        this.debugMode = debugMode;
        this.existingUser = existingUser;
        this.deviceId = deviceId;
    }

    public String getFlurryId() {
        return flurryId;
    }

    public String getGoogleAnalyticId() {
        return googleAnalyticTrackingId;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public boolean isExistingUser() {
        return existingUser;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public static class Builder {
        private String flurryId, googleAnalyticTrackingId;
        private boolean debugMode;
        private boolean existingUser;
        private String deviceId;

        public Builder() {
        }


        public Builder withFlurryId(String flurryId) {
            this.flurryId = flurryId;
            return this;
        }


        public Builder withGoogleAnalyticId(String googleAnalyticTrackingId) {
            this.googleAnalyticTrackingId = googleAnalyticTrackingId;
            return this;
        }

        public Builder withDeviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Builder withDebugMode(boolean debugMode) {
            this.debugMode = debugMode;
            return this;
        }

        public Builder withExistingUser(boolean existingUser) {
            this.existingUser = existingUser;
            return this;
        }

        public TrackerConfiguration build() {
            return new TrackerConfiguration(flurryId, googleAnalyticTrackingId, deviceId, debugMode, existingUser);
        }


    }
}

