package com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqno;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EQNORequest {
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
    @SerializedName("EQKD")
    @Expose
    private String mEQKD;
    public EQNORequest(String authorizedId, String idNo, String mCO, String mPMFCT,String mEQKD) {
        this.authorizedId=authorizedId;
        this.idNo=idNo;
        this.mCO=mCO;
        this.mPMFCT=mPMFCT;
        this.mEQKD=mEQKD;
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

    public String getmEQKD() {
        return mEQKD;
    }

    public void setmEQKD(String mEQKD) {
        this.mEQKD = mEQKD;
    }
}
