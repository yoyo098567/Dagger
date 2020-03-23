package com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqkd;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EQKDResponse {
    @SerializedName("CO")
    @Expose
    private String mCO;
    @SerializedName("PMFCT")
    @Expose
    private String mPMFCT;
    @SerializedName("EQKD")
    @Expose
    private String mEQKD;
    @SerializedName("EQKDNM")
    @Expose
    private String mEQKDNM;

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

    public String getmEQKDNM() {
        return mEQKDNM;
    }

    public void setmEQKDNM(String mEQKDNM) {
        this.mEQKDNM = mEQKDNM;
    }
}
