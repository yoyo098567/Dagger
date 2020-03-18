package com.example.capturevideoandpictureandsaveandchoose.di.component.main;

import com.example.capturevideoandpictureandsaveandchoose.di.annotation.MainScoped;
import com.example.capturevideoandpictureandsaveandchoose.di.component.BaseComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.module.choosedevice.ChooseDeviceModule;
import com.example.capturevideoandpictureandsaveandchoose.di.module.main.MainModule;
import com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice.ChooseDeviceActivity;
import com.example.capturevideoandpictureandsaveandchoose.ui.main.MainActivity;

import dagger.Component;

@MainScoped
@Component(
        dependencies = BaseComponent.class,
        modules = {
                MainModule.class
        }
)
public interface MainComponent {
    void inject(MainActivity activity);
}
