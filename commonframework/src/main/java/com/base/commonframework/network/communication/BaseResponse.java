package com.base.commonframework.network.communication;

import com.base.commonframework.network.communication.beans.BaseStatusModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponse {
    @SerializedName("info")
    @Expose
    public Info info;
    @SerializedName("status")
    @Expose
    public BaseStatusModel status = new BaseStatusModel();

    /**
     * @return
     */
    public Info getInfo() {
        return info;
    }

    /**
     * @return
     */
    public BaseStatusModel getStatus() {
        return status;
    }

    public class Info {

        @SerializedName("vehicleType")
        @Expose
        private int vehicleType;
        @SerializedName("userRole")
        @Expose
        private int userRole;
        @SerializedName("sessionId")
        @Expose
        private String sessionId;

        public int getVehicleType() {
            return vehicleType;
        }

        public int getUserRole() {
            return userRole;
        }

        public String getSessionId() {
            return sessionId;
        }
    }


}
