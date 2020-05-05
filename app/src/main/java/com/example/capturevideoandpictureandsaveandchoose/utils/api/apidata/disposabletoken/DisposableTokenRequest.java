package com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.disposabletoken;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DisposableTokenRequest {
    @SerializedName("AuthorizedId")
    @Expose
    private String authorizedId;
    @SerializedName("AccessToken")
    @Expose
    private String mAccessToken;
    @SerializedName("DeviceId")
    @Expose
    private String mDeviceId;

    public DisposableTokenRequest(String authorizedId, String mAccessToken, String mDeviceId) {
        this.authorizedId=authorizedId;
        this.mAccessToken=mAccessToken;
        this.mDeviceId=mDeviceId;
    }
    public String getAuthorizedId() {
        return authorizedId;
    }

    public void setAuthorizedId(String authorizedId) {
        this.authorizedId = authorizedId;
    }

    public String getmAccessToken() {
        return mAccessToken;
    }

    public void setmAccessToken(String mAccessToken) {
        this.mAccessToken = mAccessToken;
    }

    public String getmDeviceId() {
        return mDeviceId;
    }

    public void setmDeviceId(String mDeviceId) {
        this.mDeviceId = mDeviceId;
    }
}
