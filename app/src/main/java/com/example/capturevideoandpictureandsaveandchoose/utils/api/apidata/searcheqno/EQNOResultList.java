package com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqno;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EQNOResultList {
    @SerializedName("ResultList")
    @Expose
    private List<EQNOResponse> mEQNOResponseList;

    public List<EQNOResponse> getmEQNOResponseList() {
        return mEQNOResponseList;
    }

    public void setmEQNOResponseList(List<EQNOResponse> mEQNOResponseList) {
        this.mEQNOResponseList = mEQNOResponseList;
    }
}
