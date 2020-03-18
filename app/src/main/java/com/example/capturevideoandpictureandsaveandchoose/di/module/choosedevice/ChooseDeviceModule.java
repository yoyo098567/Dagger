package com.example.capturevideoandpictureandsaveandchoose.di.module.choosedevice;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice.ChooseDeviceContract;
import com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice.ChooseDevicePresenter;
import com.example.capturevideoandpictureandsaveandchoose.di.annotation.ChooseDeviceScoped;
import com.example.capturevideoandpictureandsaveandchoose.ui.login.LoginContract;
import com.example.capturevideoandpictureandsaveandchoose.ui.login.LoginPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ChooseDeviceModule {

    private AppCompatActivity activity;

    public ChooseDeviceModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ChooseDeviceScoped
    AppCompatActivity provideActivity() {
        return activity;
    }

    @Provides
    @ChooseDeviceScoped
    Context provideContext() {
        return activity;
    }

    @Provides
    @ChooseDeviceScoped
    ChooseDeviceContract.Presenter<ChooseDeviceContract.View> providePresenter(
            ChooseDevicePresenter<ChooseDeviceContract.View> presenter) {
        return presenter;
    }
}
