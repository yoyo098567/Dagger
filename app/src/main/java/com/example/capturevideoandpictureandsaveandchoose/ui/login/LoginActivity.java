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

import com.example.capturevideoandpictureandsaveandchoose.di.component.login.DaggerLoginComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.module.login.LoginModule;
import com.example.capturevideoandpictureandsaveandchoose.ui.main.MainActivity;
import com.example.capturevideoandpictureandsaveandchoose.R;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseActivity;
import com.example.capturevideoandpictureandsaveandchoose.di.component.login.LoginComponent;
import com.example.capturevideoandpictureandsaveandchoose.Application;
import com.example.capturevideoandpictureandsaveandchoose.utils.sharepreferences.LoginPreferencesProvider;

import javax.inject.Inject;

public class LoginActivity extends BaseActivity implements LoginContract.View,View.OnClickListener {
    @Inject
    LoginContract.Presenter<LoginContract.View> mPresenter;

    private EditText editAccount, editPassword;
    private Button btnLogin;
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
            mPresenter.onAutoLogin(account,pwd);
        }
    }

    @Override
    public void init() {
        loginStatus = 0;
        editAccount = findViewById(R.id.edit_account);
        editPassword = findViewById(R.id.edit_password);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        mLoginActivityComponent = DaggerLoginComponent.builder()
                .loginModule(new LoginModule(this))
                .baseComponent(((Application) getApplication()).getApplicationComponent())
                .build();
        mLoginActivityComponent.inject(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
//                Intent intentCentralCloud = new Intent();
//                intentCentralCloud.setAction(Intent.ACTION_VIEW);
//                intentCentralCloud.setData(Uri.parse("https://cloud.fpcetg.com.tw/FPC/WEB/MTN/MTN_EQPT/Default.aspx?" +
//                        "CO=" + "1" + "&" +
//                        "PMFCT=" + "A3" + "&" +
//                        "MNTCO=" + "1" + "&" +
//                        "MNTFCT=" +"123" + "&" +
//                        "EQNO=" + "P-166"+ "&" +
//                        "token=" + "wwwww"));
//                startActivity(intentCentralCloud);
                if(loginStatus == 1){
                    if(onCheckUserisEmpty()){
                        mPresenter.onLogin(editAccount.getText().toString(),editPassword.getText().toString());
                    }
                }else{
                    if(onCheckUserisEmpty()){
                        mPresenter.onLogin(editAccount.getText().toString(),editPassword.getText().toString());
                    }
                }
                break;
        }
    }
    private boolean onCheckUserisEmpty(){
        Log.v("LoginStatus","8888888888888");
        if ("".equals(editAccount.getText().toString())) {
            showDialogMessage(getResourceString(R.string.login_account_hint));
            return false;
        }
        if ("".equals(editPassword.getText().toString())) {
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
                Log.v("LoginStatus","isSuccess:" + isSuccess);
                Log.v("LoginStatus","message:" + message);
                Log.v("LoginStatus","account:" + account);
                Log.v("LoginStatus","pwd:" + pwd);

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
        intent.putExtra("account",account);
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
