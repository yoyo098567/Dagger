package com.example.capturevideoandpictureandsaveandchoose.ui.login;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import com.example.capturevideoandpictureandsaveandchoose.databinding.ActivityLoginBinding;
import com.example.capturevideoandpictureandsaveandchoose.di.component.login.DaggerLoginComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.module.login.LoginModule;
import com.example.capturevideoandpictureandsaveandchoose.ui.main.MainActivity;
import com.example.capturevideoandpictureandsaveandchoose.R;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseActivity;
import com.example.capturevideoandpictureandsaveandchoose.di.component.login.LoginComponent;
import com.example.capturevideoandpictureandsaveandchoose.Application;
import com.example.capturevideoandpictureandsaveandchoose.utils.sharepreferences.LoginPreferencesProvider;

import javax.inject.Inject;

public class LoginActivity extends BaseActivity implements LoginContract.View {
    @Inject
    LoginContract.Presenter<LoginContract.View> mPresenter;

    @Inject
    LoginPreferencesProvider loginPreferencesProvider;
    private String  account = "";
    private String  pwd = "";
    private LoginComponent mLoginActivityComponent;
    public static int loginStatus = 0;
    private static final int REQUEST_PERMISSIONS_CODE=20200410;
    private String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA};
     ActivityLoginBinding activityLoginBinding;
     private LoginData loginData;
    private Button button;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        requestPermissions(permissions, REQUEST_PERMISSIONS_CODE);
        mPresenter.onAttached(this);
        getAccount();
        if(loginStatus==1){
            if (account.substring(0,2).contains("PP")){
                loginPreferencesProvider.setPersonId(0);
                mPresenter.onCNAutoLogin(account,pwd);
            }else if (account.substring(0,2).contains("N")){
                loginPreferencesProvider.setPersonId(1);
                mPresenter.onAutoLogin(account,pwd);
            }
        }
    }

    @Override
    public void init() {
        loginStatus = 0;
        mLoginActivityComponent = DaggerLoginComponent.builder()
                .loginModule(new LoginModule(this))
                .baseComponent(((Application) getApplication()).getApplicationComponent())
                .build();
        mLoginActivityComponent.inject(this);

        loginData=new LoginData();
        activityLoginBinding= DataBindingUtil.setContentView(this,R.layout.activity_login);
        activityLoginBinding.setView(this);
        activityLoginBinding.setData(loginData);

    }


    @Override
    public void onLoginClick() {
        if(loginStatus == 1){
            if(onCheckUserisEmpty()){
                account=loginData.getAccount();
                if (account.substring(0,2).contains("PP")){
                    loginPreferencesProvider.setPersonId(0);
                    mPresenter.onCNLogin(account,loginData.getPw());
                }else if (account.substring(0,2).contains("N")){
                    loginPreferencesProvider.setPersonId(1);
                    mPresenter.onLogin(account,loginData.getPw());
                }
                loginPreferencesProvider.setAccount(loginData.getAccount());

            }
        }else{
            if(onCheckUserisEmpty()){
                account=loginData.getAccount();
                if (account.substring(0,2).contains("PP")){
                    loginPreferencesProvider.setPersonId(0);
                    mPresenter.onCNLogin(account,loginData.getPw());
                }else if (account.substring(0,2).contains("N")){
                    loginPreferencesProvider.setPersonId(1);
                    mPresenter.onLogin(account,loginData.getPw());
                }
                loginPreferencesProvider.setAccount(loginData.getAccount());

            }
        }
    }


    private boolean onCheckUserisEmpty(){
        Log.v("LoginStatus","8888888888888");
        if ("".equals(loginData.getAccount())) {
            showDialogMessage(getResourceString(R.string.login_account_hint));
            return false;
        }
        if ("".equals(loginData.getPw())) {
            showDialogMessage(getResourceString(R.string.login_password_hint));
            return false;
        }
        return true;
    }

    public void getAccount() {
        account = "";
        pwd = "";
        String CONTENT_STRING = "content://tw.com.efpg.processe_equip.provider.ShareCloud/ShareCloud";
        Uri uri = Uri.parse(CONTENT_STRING);

        Cursor cursor = this.getContentResolver().query(
                uri,
                null,
                "LoginStatus",
                null, null
        );
        if(cursor==null){
            showDialogCaveatMessage(getResourceString(R.string.auto_login_error_message));
        }else{
            while (cursor.moveToNext()) {
                String isSuccess = cursor.getString(cursor.getColumnIndexOrThrow("IsSuccess"));
                String message = cursor.getString(cursor.getColumnIndexOrThrow("Message"));
                if(isSuccess.equals("true")){
                    account = cursor.getString(cursor.getColumnIndexOrThrow("Account"));
                    pwd = cursor.getString(cursor.getColumnIndexOrThrow("PWD"));
                }

                if(account.equals("") && pwd.equals("")){
                    loginStatus = 0;
                }else{
                    loginStatus = 1;
                }
            }
        }
    }



    @Override
    public void onCompleteLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("AccessToken",mPresenter.getAccessToken());
        intent.putExtra("account",loginData.getAccount());
        startActivity(intent);
    }

    @Override
    public void onCoompleteAutoLogin(String account) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("AccessToken",mPresenter.getAccessToken());
        intent.putExtra("account",account);
        startActivity(intent);
    }
}
