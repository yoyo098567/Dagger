package com.example.capturevideoandpictureandsaveandchoose.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.capturevideoandpictureandsaveandchoose.di.component.login.DaggerLoginComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.module.login.LoginModule;
import com.example.capturevideoandpictureandsaveandchoose.main.MainActivity;
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
    LoginPreferencesProvider mLoginPreferencesProvider;

    @Inject
    LoginContract.Presenter<LoginContract.View> mPresenter;

    private LoginComponent mLoginActivityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
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
                mPresenter.onLogin();
                onLogin();
                break;
        }
    }

    private void onLogin() {
        if ("".equals(editAccount.getText().toString())) {
            showDialogMessage(getResourceString(R.string.login_account_hint));
        }
        if ("".equals(editPassword.getText().toString())) {
            showDialogMessage(getResourceString(R.string.login_password_hint));
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
