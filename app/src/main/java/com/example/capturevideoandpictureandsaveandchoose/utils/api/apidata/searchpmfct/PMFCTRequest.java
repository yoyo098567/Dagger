package com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchpmfct;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PMFCTRequest {
    @SerializedName("AuthorizedId")
    @Expose
    private String authorizedId;
    @SerializedName("IdNo")
    @Expose
    private String idNo;
    @SerializedName("MNTCO")
    @Expose
    private String mMNTCO;
    @SerializedName("MNTFCT")
    @Expose
    private String mMNTFCT;
    public PMFCTRequest(String authorizedId, String idNo,String mMNTCO, String mMNTFCT) {
        this.authorizedId=authorizedId;
        this.idNo=idNo;
        this.mMNTCO=mMNTCO;
        this.mMNTFCT=mMNTFCT;
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
}
