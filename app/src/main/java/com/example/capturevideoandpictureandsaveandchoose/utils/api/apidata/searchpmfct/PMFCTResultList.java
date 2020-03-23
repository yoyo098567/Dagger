package com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchpmfct;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PMFCTResultList {
    @SerializedName("ResultList")
    @Expose
    private List<PMFCTResponse> mNTFCTResponse;

    public List<PMFCTResponse> getmNTFCTResponse() {
        return mNTFCTResponse;
    }

    public void setmNTFCTResponse(List<PMFCTResponse> mNTFCTResponse) {
        this.mNTFCTResponse = mNTFCTResponse;
    }
}
