package com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchpmfct;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PMFCTResponse {
    @SerializedName("MNTCO")
    @Expose
    private String mMNTCO;
    @SerializedName("MNTFCT")
    @Expose
    private String mMNTFCT;
    @SerializedName("CO")
    @Expose
    private String mCO;
    @SerializedName("PMFCT")
    @Expose
    private String mPMFCT;
    @SerializedName("PMFCTNM")
    @Expose
    private String mPMFCTNM;

    public String getmMNTCO() {
        return mMNTCO;
    }

    public void setmMNTCO(String mMNTCO) {
        this.mMNTCO = mMNTCO;
    }

    public String getmMNTFCT() {
        return mMNTFCT;
    }

    public void setmMNTFCT(String mMNTFCT) {
        this.mMNTFCT = mMNTFCT;
    }

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

    public String getmPMFCTNM() {
        return mPMFCTNM;
    }

    public void setmPMFCTNM(String mPMFCTNM) {
        this.mPMFCTNM = mPMFCTNM;
    }
}
