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
    private final String SP_ORG_ID = "SP_ORG_ID";
    private final String SP_PERSON_ID = "SP_PERSON_ID";
    private final String SP_PERSON_NAME = "SP_PERSON_NAME";
    private final String FACTORY_NUMBER="FACTORY_NUMBER";
    @Inject
    public LoginPreferences(Context context) {
        super(context);
    }

    @Override
    public String getClassName() {
        return SP_FiLE_NAME;
    }

    @Override
    public void setOrgId(String mOrgId) {
        save(Type.STRING, SP_ORG_ID, mOrgId);
    }

    @Override
    public void setPersonId(String mPersonId) {
        save(Type.STRING, SP_PERSON_ID, mPersonId);
    }

    @Override
    public void setPersonName(String mName) {
        save(Type.STRING, SP_PERSON_NAME, mName);
    }

    @Override
    public void setFactoryNumber(String mNumber) {
        save(Type.STRING, FACTORY_NUMBER, mNumber);
    }

    @Override
    public String getOrgId() {
        return (String) get(Type.STRING, SP_ORG_ID);
    }

    @Override
    public String getPersonId() {
        return (String) get(Type.STRING, SP_PERSON_ID);
    }

    @Override
    public String getPersonName() {
        return (String) get(Type.STRING, SP_PERSON_NAME);
    }

    @Override
    public String getFactoryNumber() {
        return (String) get(Type.STRING, FACTORY_NUMBER);
    }
}
