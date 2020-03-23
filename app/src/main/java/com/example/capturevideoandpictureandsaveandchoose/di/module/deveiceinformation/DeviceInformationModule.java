package com.example.capturevideoandpictureandsaveandchoose.di.module.deveiceinformation;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capturevideoandpictureandsaveandchoose.di.annotation.DeviceInformationScoped;
import dagger.Module;
import dagger.Provides;

@Module
public class DeviceInformationModule {

    private AppCompatActivity activity;

    public DeviceInformationModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    @DeviceInformationScoped
    AppCompatActivity provideActivity() {
        return activity;
    }

    @Provides
    @DeviceInformationScoped
    Context provideContext() {
        return activity;
    }

}
