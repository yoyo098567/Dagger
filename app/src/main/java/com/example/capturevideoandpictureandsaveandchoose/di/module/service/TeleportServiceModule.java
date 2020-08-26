package com.example.capturevideoandpictureandsaveandchoose.di.module.service;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capturevideoandpictureandsaveandchoose.di.annotation.MainScoped;
import com.example.capturevideoandpictureandsaveandchoose.di.annotation.TeleportServiceScope;
import com.example.capturevideoandpictureandsaveandchoose.ui.main.MainContract;
import com.example.capturevideoandpictureandsaveandchoose.ui.main.MainPresenter;

import dagger.Module;
import dagger.Provides;
@Module
public class TeleportServiceModule {

    private Application activity;

    public TeleportServiceModule(Application activity) {
        this.activity = activity;
    }

    @Provides
    @TeleportServiceScope
    Application provideActivity() {
        return activity;
    }

    @Provides
    @TeleportServiceScope
    Context provideContext() {
        return activity;
    }


}
