package com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice;

import com.example.capturevideoandpictureandsaveandchoose.base.BaseAttacher;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseView;
import com.example.capturevideoandpictureandsaveandchoose.ui.login.LoginContract;

public interface ChooseDeviceContract {
    interface View extends BaseView {
        void setCurrentItem(int position);
    }

    interface Presenter<V extends ChooseDeviceContract.View> extends BaseAttacher<V> {
    }
}
