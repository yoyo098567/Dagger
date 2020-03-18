package com.example.capturevideoandpictureandsaveandchoose.di.component.deveiceinformation;

import com.example.capturevideoandpictureandsaveandchoose.di.annotation.DeviceInformationScoped;
import com.example.capturevideoandpictureandsaveandchoose.di.component.BaseComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.module.deveiceinformation.DeviceInformationModule;
import com.example.capturevideoandpictureandsaveandchoose.ui.deviceinformation.DeviceInformationActivity;

import dagger.Component;

@DeviceInformationScoped
@Component(
        dependencies = BaseComponent.class,
        modules = {
                DeviceInformationModule.class
        }
)
public interface DeviceInformationComponent {
    void inject(DeviceInformationActivity activity);
}
