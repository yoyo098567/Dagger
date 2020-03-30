package com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqno;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EQNOResponse {
    @SerializedName("CO")
    @Expose
    private String mCO;
    @SerializedName("PMFCT")
    @Expose
    private String mPMFCT;
    @SerializedName("EQKD")
    @Expose
    private String mEQKD;
    @SerializedName("EQNO")
    @Expose
    private String mEQNO;
    @SerializedName("EQNM")
    @Expose
    private String mEQNM;

    public String getmCO() {
        return mCO;
    }

    public void setmCO(String mCO) {
        this.mCO = mCO;
    }

    public String getmPMFCT() {
        return mPMFCT;
    }

    public void setmPMFCT(String mPMFCT) {
        this.mPMFCT = mPMFCT;
    }

    public String getmEQKD() {
        return mEQKD;
    }

    public void setmEQKD(String mEQKD) {
        this.mEQKD = mEQKD;
    }

    public String getmEQNO() {
        return mEQNO;
    }

    public void setmEQNO(String mEQNO) {
        this.mEQNO = mEQNO;
    }

    public String getmEQNM() {
        return mEQNM;
    }

    public void setmEQNM(String mEQNM) {
        this.mEQNM = mEQNM;
    }
}
