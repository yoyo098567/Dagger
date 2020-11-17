package com.example.capturevideoandpictureandsaveandchoose.utils.sharepreferences;

/**
 * Created by 5*N on 2018/1/1.
 */

public interface LoginPreferencesProvider {

    void setPersonPassword(String mPassword);
    void setToken(String token);


    void setAccount(String mPersonId);

    void setPersonId(Integer mName);

    void setFactoryNumber(String mNumber);

    String getAccount();

    //0是寧波，1是台灣
    Integer getPersonId();

    String getFactoryNumber();

    String getPersonPassword();

    String getToken();
}
