package com.example.capturevideoandpictureandsaveandchoose.di.component.login;

import com.example.capturevideoandpictureandsaveandchoose.di.annotation.LoginScoped;
import com.example.capturevideoandpictureandsaveandchoose.di.component.BaseComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.module.login.LoginModule;
import com.example.capturevideoandpictureandsaveandchoose.login.LoginActivity;

import dagger.Component;
@LoginScoped
@Component(
        dependencies = BaseComponent.class,
        modules = {
                LoginModule.class
        }
)
public interface LoginComponent {
    void inject(LoginActivity activity);
}
