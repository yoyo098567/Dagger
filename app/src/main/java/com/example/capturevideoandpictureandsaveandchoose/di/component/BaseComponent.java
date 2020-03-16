package com.example.capturevideoandpictureandsaveandchoose.di.component;


import com.example.capturevideoandpictureandsaveandchoose.di.module.APIModule;
import com.example.capturevideoandpictureandsaveandchoose.di.module.ApplicationModule;
import android.app.Application;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.ApiService;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.ErpAPI;
import com.example.capturevideoandpictureandsaveandchoose.utils.rxjava.SchedulerProvider;
import com.example.capturevideoandpictureandsaveandchoose.utils.sharepreferences.LoginPreferencesProvider;

import javax.inject.Singleton;

import dagger.Component;
import io.reactivex.disposables.CompositeDisposable;

@Singleton
@Component(
        modules = {
                ApplicationModule.class,
                APIModule.class,
        }
)
public interface BaseComponent {
    void inject(Application application);

    ApiService getAPI();

    ErpAPI getErpAPI();

    SchedulerProvider getSchedulerProvider();

    CompositeDisposable getCompositeDisposable();

    LoginPreferencesProvider getLoginPreferencesProvider();

}
