package com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.upload;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadRequest {
    @SerializedName("AuthorizedId")
    @Expose
    private String authorizedId;
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
    @SerializedName("EQKD")
    @Expose
    private String nEQKD;
    @SerializedName("EQKDNM")
    @Expose
    private String mEQKDNM;
    @SerializedName("EQNO")
    @Expose
    private String mEQNO;
    @SerializedName("EQNM")
    @Expose
    private String mEQNM;
    @SerializedName("RecordDate")
    @Expose
    private String mRecordDate;
    @SerializedName("RecordSubject")
    @Expose
    private String mRecordSubject;
    @SerializedName("UploadEMP")
    @Expose
    private String mUploadEMP;
    @SerializedName("UploadNM")
    @Expose
    private String mUploadNM;
    @SerializedName("UploadDATETM")
    @Expose
    private String mUploadDATETM;

    public String getAuthorizedId() {
        return authorizedId;
    }

    public void setAuthorizedId(String authorizedId) {
        this.authorizedId = authorizedId;
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

    public String getnEQKD() {
        return nEQKD;
    }

    public void setnEQKD(String nEQKD) {
        this.nEQKD = nEQKD;
    }

    public String getmEQKDNM() {
        return mEQKDNM;
    }

    public void setmEQKDNM(String mEQKDNM) {
        this.mEQKDNM = mEQKDNM;
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

    public String getmRecordDate() {
        return mRecordDate;
    }

    public void setmRecordDate(String mRecordDate) {
        this.mRecordDate = mRecordDate;
    }

    public String getmRecordSubject() {
        return mRecordSubject;
    }

    public void setmRecordSubject(String mRecordSubject) {
        this.mRecordSubject = mRecordSubject;
    }

    public String getmUploadEMP() {
        return mUploadEMP;
    }

    public void setmUploadEMP(String mUploadEMP) {
        this.mUploadEMP = mUploadEMP;
    }

    public String getmUploadNM() {
        return mUploadNM;
    }

    public void setmUploadNM(String mUploadNM) {
        this.mUploadNM = mUploadNM;
    }

    public String getmUploadDATETM() {
        return mUploadDATETM;
    }

    public void setmUploadDATETM(String mUploadDATETM) {
        this.mUploadDATETM = mUploadDATETM;
    }
}
