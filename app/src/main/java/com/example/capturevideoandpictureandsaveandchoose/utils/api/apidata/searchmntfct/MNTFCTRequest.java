package com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchmntfct;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MNTFCTRequest {
    @SerializedName("AuthorizedId")
    @Expose
    private String authorizedId;
    @SerializedName("IdNo")
    @Expose
    private String idNo;
    public MNTFCTRequest(String authorizedId, String idNo) {
        this.authorizedId=authorizedId;
        this.idNo=idNo;
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
}
