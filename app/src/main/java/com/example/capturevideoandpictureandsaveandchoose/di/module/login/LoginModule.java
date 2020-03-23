package com.example.capturevideoandpictureandsaveandchoose.di.module.login;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capturevideoandpictureandsaveandchoose.di.annotation.LoginScoped;
import com.example.capturevideoandpictureandsaveandchoose.ui.login.LoginContract;
import com.example.capturevideoandpictureandsaveandchoose.ui.login.LoginPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {

    private AppCompatActivity activity;

    public LoginModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    @LoginScoped
    AppCompatActivity provideActivity() {
        return activity;
    }

    @Provides
    @LoginScoped
    Context provideContext() {
        return activity;
    }

    @Provides
    @LoginScoped
    LoginContract.Presenter<LoginContract.View> providePresenter(
            LoginPresenter<LoginContract.View> presenter) {
        return presenter;
    }
}
