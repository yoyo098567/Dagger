package com.example.capturevideoandpictureandsaveandchoose.utils.sharepreferences;

/**
 * Created by 5*N on 2018/1/1.
 */

public interface LoginPreferencesProvider {

    void setOrgId(String mOrgId);

    void setPersonId(String mPersonId);

    void setPersonName(String mName);

    void setFactoryNumber(String mNumber);

    String getOrgId();

    String getPersonId();

    String getPersonName();

    String getFactoryNumber();
}
