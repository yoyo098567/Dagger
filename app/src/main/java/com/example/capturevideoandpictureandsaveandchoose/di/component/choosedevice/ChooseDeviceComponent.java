package com.example.capturevideoandpictureandsaveandchoose.di.component.choosedevice;

import com.example.capturevideoandpictureandsaveandchoose.di.annotation.ChooseDeviceScoped;
import com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice.ChooseDeviceActivity;
import com.example.capturevideoandpictureandsaveandchoose.di.component.BaseComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.module.choosedevice.ChooseDeviceModule;

import dagger.Component;

@ChooseDeviceScoped
@Component(
        dependencies = BaseComponent.class,
        modules = {
                ChooseDeviceModule.class
        }
)
public interface ChooseDeviceComponent {
    void inject(ChooseDeviceActivity activity);
}
