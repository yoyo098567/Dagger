package com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchmntfct;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MNTFCTResponse {
    @SerializedName("MNTCO")
    @Expose
    private String mNTCO;
    @SerializedName("MNTFCT")
    @Expose
    private String mNTFCT;
    @SerializedName("MNTFCTNM")
    @Expose
    private String mNTFCTNM;

    public String getmNTCO() {
        return mNTCO;
    }

    public void setmNTCO(String mNTCO) {
        this.mNTCO = mNTCO;
    }

    public String getmNTFCT() {
        return mNTFCT;
    }

    public void setmNTFCT(String mNTFCT) {
        this.mNTFCT = mNTFCT;
    }

    public String getmNTFCTNM() {
        return mNTFCTNM;
    }

    public void setmNTFCTNM(String mNTFCTNM) {
        this.mNTFCTNM = mNTFCTNM;
    }
}
