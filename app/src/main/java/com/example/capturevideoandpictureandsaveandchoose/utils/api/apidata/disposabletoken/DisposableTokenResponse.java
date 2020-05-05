package com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.disposabletoken;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DisposableTokenResponse {
    @SerializedName("Result")
    @Expose
    private String mResult;
    @SerializedName("DisposableToken")
    @Expose
    private String mDisposableToken;
    @SerializedName("ErrMsg")
    @Expose
    private String mErrMsg;

    public String getmResult() {
        return mResult;
    }

    public void setmResult(String mResult) {
        this.mResult = mResult;
    }

    public String getmDisposableToken() {
        return mDisposableToken;
    }

    public void setmDisposableToken(String mDisposableToken) {
        this.mDisposableToken = mDisposableToken;
    }

    public String getmErrMsg() {
        return mErrMsg;
    }

    public void setmErrMsg(String mErrMsg) {
        this.mErrMsg = mErrMsg;
    }
}
