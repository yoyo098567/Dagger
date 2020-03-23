package com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchco;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class COResultList {
    @SerializedName("ResultList")
    @Expose
    private List<COResponse> cOResponseList;

    public List<COResponse> getcOResponseList() {
        return cOResponseList;
    }

    public void setcOResponseList(List<COResponse> cOResponseList) {
        this.cOResponseList = cOResponseList;
    }
}
