package com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqkd;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EQKDResultList {
    @SerializedName("ResultList")
    @Expose
    private List<EQKDResponse> mEQKDResponseList;

    public List<EQKDResponse> getmEQKDResponseList() {
        return mEQKDResponseList;
    }

    public void setmEQKDResponseList(List<EQKDResponse> mEQKDResponseList) {
        this.mEQKDResponseList = mEQKDResponseList;
    }
}
