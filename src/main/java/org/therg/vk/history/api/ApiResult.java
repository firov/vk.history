package org.therg.vk.history.api;

import com.google.gson.annotations.SerializedName;

public class ApiResult {
    @SerializedName("error_code")
    public int errorCode;

    @SerializedName("error_msg")
    public String errorMessage;

    @Override
    public String toString() {
        return String.format("api result(%s): error code=%d, message=\"%s\"", this.getClass().getSimpleName(), errorCode, errorMessage);
    }
}
