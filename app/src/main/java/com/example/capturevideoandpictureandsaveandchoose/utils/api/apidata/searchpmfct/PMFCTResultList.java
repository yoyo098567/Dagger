package com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchpmfct;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PMFCTResultList {
    @SerializedName("ResultList")
    @Expose
    private List<PMFCTResponse> mPMFCTResponseList;

    public List<PMFCTResponse> getmPMFCTResponseList() {
        return mPMFCTResponseList;
    }

    public void setmPMFCTResponseList(List<PMFCTResponse> mPMFCTResponseList) {
        this.mPMFCTResponseList = mPMFCTResponseList;
    }
}
