package com.example.capturevideoandpictureandsaveandchoose.di.component.adddevice;

import com.example.capturevideoandpictureandsaveandchoose.di.annotation.AddDiviceScoped;
import com.example.capturevideoandpictureandsaveandchoose.di.component.BaseComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.module.adddevice.AddDeviceModule;
import com.example.capturevideoandpictureandsaveandchoose.ui.adddevice.AddDeviceActivity;

import dagger.Component;

@AddDiviceScoped
@Component(
        dependencies = BaseComponent.class,
        modules = {
               AddDeviceModule.class
        }
)
public interface AddDeviceComponent {
    void inject(AddDeviceActivity activity);

}
