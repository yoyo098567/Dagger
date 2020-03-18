package com.example.capturevideoandpictureandsaveandchoose.ui.login;

import com.example.capturevideoandpictureandsaveandchoose.base.BaseAttacher;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseView;

public interface LoginContract {
    interface View extends BaseView {
    }

    interface Presenter<V extends View> extends BaseAttacher<V> {
        void onLogin();
    }
}
