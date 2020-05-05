package com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.login;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("Result")
    @Expose
    private String mResult;
    @SerializedName("Token")
    @Expose
    private String mToken;
    @SerializedName("ErrMsg")
    @Expose
    private String mErrMsg;

    public String getmResult() {
        return mResult;
    }

    public void setmResult(String mResult) {
        this.mResult = mResult;
    }

    public String getmToken() {
        return mToken;
    }

    public void setmToken(String mToken) {
        this.mToken = mToken;
    }

    public String getmErrMsg() {
        return mErrMsg;
    }

    public void setmErrMsg(String mErrMsg) {
        this.mErrMsg = mErrMsg;
    }
}
