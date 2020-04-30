package com.example.capturevideoandpictureandsaveandchoose.utils.sharepreferences;

/**
 * Created by 5*N on 2018/1/1.
 */

public interface LoginPreferencesProvider {

    void setPersonPassword(String mPassword);
    void setToken(String token);

    void setPersonId(String mPersonId);

    void setPersonName(String mName);

    void setFactoryNumber(String mNumber);

    String getPersonId();

    String getPersonName();

    String getFactoryNumber();

    String getPersonPassword();

    String getToken();
}
