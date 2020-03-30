package com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.addchkInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddChkInfoRequest {
    @SerializedName("AuthorizedId")
    @Expose
    private String authorizedId;
    @SerializedName("OPCO")
    @Expose
    private String mOPCO;
    @SerializedName("OPPLD")
    @Expose
    private String mOPPLD;
    @SerializedName("WAYID")
    @Expose
    private String mWAYID;
    @SerializedName("WAYNM")
    @Expose
    private String mWAYNM;
    @SerializedName("CO")
    @Expose
    private String mCO;
    @SerializedName("CONM")
    @Expose
    private String mCONM;
    @SerializedName("PMFCT")
    @Expose
    private String mPMFCT;
    @SerializedName("PMFCTNM")
    @Expose
    private String mPMFCTNM;
    @SerializedName("EQNO")
    @Expose
    private String mEQNO;
    @SerializedName("ChkEMP")
    @Expose
    private String mChkEMP;
    @SerializedName("ChkNM")
    @Expose
    private String mChkNM;
    @SerializedName("ChkDATETM")
    @Expose
    private String mChkDATETM;

    public String getAuthorizedId() {
        return authorizedId;
    }

    public void setAuthorizedId(String authorizedId) {
        this.authorizedId = authorizedId;
    }

    public String getmOPCO() {
        return mOPCO;
    }

    public void setmOPCO(String mOPCO) {
        this.mOPCO = mOPCO;
    }

    public String getmOPPLD() {
        return mOPPLD;
    }

    public void setmOPPLD(String mOPPLD) {
        this.mOPPLD = mOPPLD;
    }

    public String getmWAYID() {
        return mWAYID;
    }

    public void setmWAYID(String mWAYID) {
        this.mWAYID = mWAYID;
    }

    public String getmWAYNM() {
        return mWAYNM;
    }

    public void setmWAYNM(String mWAYNM) {
        this.mWAYNM = mWAYNM;
    }

    public String getmCO() {
        return mCO;
    }

    public void setmCO(String mCO) {
        this.mCO = mCO;
    }

    public String getmCONM() {
        return mCONM;
    }

    public void setmCONM(String mCONM) {
        this.mCONM = mCONM;
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

    public String getmEQNO() {
        return mEQNO;
    }

    public void setmEQNO(String mEQNO) {
        this.mEQNO = mEQNO;
    }

    public String getmChkEMP() {
        return mChkEMP;
    }

    public void setmChkEMP(String mChkEMP) {
        this.mChkEMP = mChkEMP;
    }

    public String getmChkNM() {
        return mChkNM;
    }

    public void setmChkNM(String mChkNM) {
        this.mChkNM = mChkNM;
    }

    public String getmChkDATETM() {
        return mChkDATETM;
    }

    public void setmChkDATETM(String mChkDATETM) {
        this.mChkDATETM = mChkDATETM;
    }
}
