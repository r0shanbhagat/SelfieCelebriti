package com.base.commonframework.baselibrary.network.httpcache.statusmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("status")
    @Expose
    public StatusModel status = new StatusModel();

    public StatusModel getStatus() {
        return status;
    }

    public class StatusModel {
        @SerializedName("code")
        @Expose
        private int statusCode;
        @SerializedName("message")
        @Expose
        private String statusMessage;

        public int getStatusCode() {
            return statusCode;
        }

        public String getStatusMessage() {
            return statusMessage;
        }
    }
}

