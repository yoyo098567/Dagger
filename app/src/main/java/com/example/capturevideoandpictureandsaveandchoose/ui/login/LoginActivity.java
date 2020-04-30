package com.example.capturevideoandpictureandsaveandchoose.ui.login;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
    EditText editAccount, editPassword;
    Button btnLogin;


    @Inject
    LoginContract.Presenter<LoginContract.View> mPresenter;

    private LoginComponent mLoginActivityComponent;
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
    }

    @Override
    public void init() {
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
//                if(onCheckUserisEmpty()){
                    mPresenter.onLogin(editAccount.getText().toString(),editPassword.getText().toString());
//                }
                break;
        }
    }
    private boolean onCheckUserisEmpty(){
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
    @Override
    public void onCompleteLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
