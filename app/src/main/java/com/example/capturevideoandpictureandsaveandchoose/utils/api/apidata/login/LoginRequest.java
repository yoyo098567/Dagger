package com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.login;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("AuthorizedId")
    @Expose
    private String authorizedId;
    @SerializedName("NotesId")
    @Expose
    private String mNotesId;
    @SerializedName("Password")
    @Expose
    private String mPassword;

    public LoginRequest(String authorizedId, String account, String password) {
        this.authorizedId=authorizedId;
        this.mNotesId=account;
        this.mPassword=password;
    }

    public String getAuthorizedId() {
        return authorizedId;
    }

    public void setAuthorizedId(String authorizedId) {
        this.authorizedId = authorizedId;
    }

    public String getmNotesId() {
        return mNotesId;
    }

    public void setmNotesId(String mNotesId) {
        this.mNotesId = mNotesId;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
