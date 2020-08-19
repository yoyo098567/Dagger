package com.example.capturevideoandpictureandsaveandchoose.utils.sharepreferences;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Created by 5*N on 2018/1/1.
 */

@Singleton
public class LoginPreferences extends PreferencesHelper implements LoginPreferencesProvider {
    private final String SP_FiLE_NAME = LoginPreferences.class.getName();
    private final String PERSON_ID = "PERSON_ID";
    private final String PERSON_NAME = "PERSON_NAME";
    private final String FACTORY_NUMBER="FACTORY_NUMBER";
    private final String PERSON_PASSWORD="PERSON_PASSWORD";
    private final String TOKEN="TOKEN";
    @Inject
    public LoginPreferences(Context context) {
        super(context);
    }

    @Override
    public String getClassName() {
        return SP_FiLE_NAME;
    }

    @Override
    public void setPersonPassword(String mPassword) {
        save(Type.STRING, PERSON_PASSWORD, mPassword);
    }

    @Override
    public void setToken(String token) {
        save(Type.STRING, TOKEN, token);
    }

    @Override
    public void setAccount(String mPersonId) {
        save(Type.STRING, PERSON_ID, mPersonId);
    }

    @Override
    public void setPersonId(Integer mName) {
        save(Type.INT, PERSON_NAME, mName);
    }

    @Override
    public void setFactoryNumber(String mNumber) {
        save(Type.STRING, FACTORY_NUMBER, mNumber);
    }

    @Override
    public String getAccount() {
        return (String) get(Type.STRING, PERSON_ID);
    }

    @Override
    public Integer getPersonId() {
        return (Integer) get(Type.INT, PERSON_NAME);
    }

    @Override
    public String getFactoryNumber() {
        return (String) get(Type.STRING, FACTORY_NUMBER);
    }

    @Override
    public String getPersonPassword() {
        return (String) get(Type.STRING, PERSON_PASSWORD);    }

    @Override
    public String getToken() {
        return (String) get(Type.STRING, TOKEN);    }
}
