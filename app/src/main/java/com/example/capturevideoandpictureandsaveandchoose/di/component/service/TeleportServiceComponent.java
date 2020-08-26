package com.example.capturevideoandpictureandsaveandchoose.di.component.service;

import com.example.capturevideoandpictureandsaveandchoose.di.annotation.MainScoped;
import com.example.capturevideoandpictureandsaveandchoose.di.annotation.TeleportServiceScope;
import com.example.capturevideoandpictureandsaveandchoose.di.component.BaseComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.module.main.MainModule;
import com.example.capturevideoandpictureandsaveandchoose.di.module.service.TeleportServiceModule;
import com.example.capturevideoandpictureandsaveandchoose.utils.service.TeleportService;

import dagger.Component;

@TeleportServiceScope
@Component(
        dependencies = BaseComponent.class,
        modules = {
                TeleportServiceModule.class
        }
)
public interface TeleportServiceComponent {
    void inject(TeleportService teleportService);
}
