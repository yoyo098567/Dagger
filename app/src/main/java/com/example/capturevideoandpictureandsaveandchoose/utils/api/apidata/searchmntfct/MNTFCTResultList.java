package com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchmntfct;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MNTFCTResultList {
    @SerializedName("ResultList")
    @Expose
    private List<MNTFCTResponse> mNTFCTResponse;

    public List<MNTFCTResponse> getmNTFCTResponse() {
        return mNTFCTResponse;
    }

    public void setmNTFCTResponse(List<MNTFCTResponse> mNTFCTResponse) {
        this.mNTFCTResponse = mNTFCTResponse;
    }
}
