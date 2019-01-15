package com.base.commonframework.network.communication.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gautam Sharma on 13-03-2018.
 */

public class BaseRequestInfo {
    @SerializedName("deviceId")
    @Expose
    private String deviceId;

    @SerializedName("deviceManufacturer")
    @Expose
    private String deviceManufacturer;

    @SerializedName("deviceModel")
    @Expose
    private String deviceModel;

    @SerializedName("osVersion")
    @Expose
    private String osVersion;

    @SerializedName("platform")
    @Expose
    private String platform;

    @SerializedName("appVersion")
    @Expose
    private String appVersion;

    @SerializedName("appVersionCode")
    @Expose
    private int appVersionCode;

    @SerializedName("sessionId")
    @Expose
    private String sessionId;
    @SerializedName("eCode")
    @Expose
    private String eCode;
    @SerializedName("serverSyncTime")
    @Expose
    private long serverSyncTime;

    public long getServerSyncTime() {
        return serverSyncTime;
    }

    public void setServerSyncTime(long serverSyncTime) {
        this.serverSyncTime = serverSyncTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceManufacturer() {
        return deviceManufacturer;
    }

    public void setDeviceManufacturer(String deviceManufacturer) {
        this.deviceManufacturer = deviceManufacturer;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public int getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(int appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String geteCode() {
        return eCode;
    }

    public void seteCode(String eCode) {
        this.eCode = eCode;
    }
}
