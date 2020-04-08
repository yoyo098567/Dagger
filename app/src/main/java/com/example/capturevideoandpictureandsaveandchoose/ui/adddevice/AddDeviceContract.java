package com.example.capturevideoandpictureandsaveandchoose.ui.adddevice;

import com.example.capturevideoandpictureandsaveandchoose.base.BaseAttacher;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseView;

public interface AddDeviceContract {
    interface View extends BaseView {
    }

    interface Presenter<V extends AddDeviceContract.View> extends BaseAttacher<V> {
    }
}
