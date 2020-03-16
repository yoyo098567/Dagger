package com.example.capturevideoandpictureandsaveandchoose.utils.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataResponse {
    @SerializedName("title")
    @Expose
    private String titlewefwefew;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("poster")
    @Expose
    private String poster;
    @SerializedName("url")
    @Expose
    private String url;

    public String getTitle() {
        return titlewefwefew;
    }

    public void setTitle(String title) {
        this.titlewefwefew = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
