package com.example.capturevideoandpictureandsaveandchoose;


import android.content.Context;

import androidx.multidex.MultiDex;

import com.example.capturevideoandpictureandsaveandchoose.di.component.BaseComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.component.DaggerBaseComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.module.APIModule;
import com.example.capturevideoandpictureandsaveandchoose.di.module.ApplicationModule;



/**
 * Created by 5*N on 2017/12/24
 */


public class Application extends android.app.Application {
    private BaseComponent mApplicationComponent;

    public static Application get(Context context) {
        return (Application) context.getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(base);
    }
    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerBaseComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .aPIModule(new APIModule())
                .build();
        mApplicationComponent.inject(this);

    }

    public BaseComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
