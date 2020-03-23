package com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchco;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class COResponse {
    @SerializedName("CO")
    @Expose
    private String cO;
    @SerializedName("CONM")
    @Expose
    private String cONM;

    public String getcO() {
        return cO;
    }

    public void setcO(String cO) {
        this.cO = cO;
    }

    public String getcONM() {
        return cONM;
    }

    public void setcONM(String cONM) {
        this.cONM = cONM;
    }
}
