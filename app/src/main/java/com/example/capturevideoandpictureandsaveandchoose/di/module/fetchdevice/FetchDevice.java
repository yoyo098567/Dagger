package com.example.capturevideoandpictureandsaveandchoose.di.module.fetchdevice;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capturevideoandpictureandsaveandchoose.di.annotation.MainScoped;
import com.example.capturevideoandpictureandsaveandchoose.ui.main.MainContract;
import com.example.capturevideoandpictureandsaveandchoose.ui.main.MainPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class FetchDevice {

    private AppCompatActivity activity;

    public FetchDevice(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    @MainScoped
    AppCompatActivity provideActivity() {
        return activity;
    }

    @Provides
    @MainScoped
    Context provideContext() {
        return activity;
    }


    @Provides
    @MainScoped
    MainContract.Presenter<MainContract.View> providePresenter(
            MainPresenter<MainContract.View> presenter) {
        return presenter;
    }
}
