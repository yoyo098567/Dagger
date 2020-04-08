package com.example.capturevideoandpictureandsaveandchoose.di.module.adddevice;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capturevideoandpictureandsaveandchoose.di.annotation.AddDiviceScoped;
import com.example.capturevideoandpictureandsaveandchoose.ui.adddevice.AddDeviceContract;
import com.example.capturevideoandpictureandsaveandchoose.ui.adddevice.AddDevicePresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class AddDeviceModule {

    private AppCompatActivity activity;

    public AddDeviceModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    @AddDiviceScoped
    AppCompatActivity provideActivity() {
        return activity;
    }

    @Provides
    @AddDiviceScoped
    Context provideContext() {
        return activity;
    }

    @Provides
    @AddDiviceScoped
    AddDeviceContract.Presenter<AddDeviceContract.View> providePresenter(
            AddDevicePresenter<AddDeviceContract.View> presenter) {
        return presenter;
    }
}
