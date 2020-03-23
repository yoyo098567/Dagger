package com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqkd;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EQKDRequest {
    @SerializedName("AuthorizedId")
    @Expose
    private String authorizedId;
    @SerializedName("IdNo")
    @Expose
    private String idNo;
    @SerializedName("CO")
    @Expose
    private String mCO;
    @SerializedName("PMFCT")
    @Expose
    private String mPMFCT;
    public EQKDRequest(String authorizedId, String idNo, String mCO, String mPMFCT) {
        this.authorizedId=authorizedId;
        this.idNo=idNo;
        this.mCO=mCO;
        this.mPMFCT=mPMFCT;
    }

    public String getAuthorizedId() {
        return authorizedId;
    }

    public void setAuthorizedId(String authorizedId) {
        this.authorizedId = authorizedId;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
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
}
