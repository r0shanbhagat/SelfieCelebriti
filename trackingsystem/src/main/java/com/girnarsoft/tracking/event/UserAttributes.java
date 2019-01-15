package com.girnarsoft.tracking.event;

import java.util.HashMap;
import java.util.Map;

public class UserAttributes {
    private Map<String, Object> attributes = new HashMap<>();

    private UserAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public static class Builder {
        private Map<String, Object> params = new HashMap<>();

        public Builder withDeviceId(String deviceId) {
            params.put("ID", deviceId);
            return this;
        }

        public Builder withAppVersion(int appVersion) {
            params.put("AppVersion", appVersion);
            return this;
        }

        public Builder withPlatform(String platform) {
            params.put("Platform", platform);
            return this;
        }

        public Builder withLocation(String location) {
            params.put("Location", location);
            return this;
        }


        public Builder withFirstName(String firstName) {
            params.put("FirstName", firstName);
            return this;
        }

        public Builder withLastName(String lastName) {
            params.put("LastName", lastName);
            return this;
        }

        public Builder withName(String name) {
            params.put("Name", name);
            return this;
        }

        public Builder withGender(String gender) {
            params.put("Gender", gender);
            return this;
        }


        public UserAttributes build() {
            return new UserAttributes(params);
        }
    }
}
