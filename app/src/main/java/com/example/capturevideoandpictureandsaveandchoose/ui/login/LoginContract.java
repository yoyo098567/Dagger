package com.example.capturevideoandpictureandsaveandchoose.ui.login;

import com.example.capturevideoandpictureandsaveandchoose.base.BaseAttacher;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseView;

public interface LoginContract {
    interface View extends BaseView {
        void onCompleteLogin();
    }

    interface Presenter<V extends View> extends BaseAttacher<V> {
        void onLogin(String account,String password);
        String getAccessToken();
    }
}
