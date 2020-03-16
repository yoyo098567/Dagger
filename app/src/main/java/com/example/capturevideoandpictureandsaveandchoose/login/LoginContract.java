package com.example.capturevideoandpictureandsaveandchoose.login;

import android.content.DialogInterface;

import com.example.capturevideoandpictureandsaveandchoose.base.BaseAttacher;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseView;

import java.util.List;

public interface LoginContract {
    interface View extends BaseView {
    }

    interface Presenter<V extends View> extends BaseAttacher<V> {
        void onLogin();
    }
}
