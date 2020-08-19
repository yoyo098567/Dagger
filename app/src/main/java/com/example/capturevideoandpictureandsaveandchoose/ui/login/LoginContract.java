package com.example.capturevideoandpictureandsaveandchoose.ui.login;

import com.example.capturevideoandpictureandsaveandchoose.base.BaseAttacher;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseView;

public interface LoginContract {
    interface View extends BaseView {

        void onLoginClick();

        void onCompleteLogin();
        void onCoompleteAutoLogin(String account);
    }

    interface Presenter<V extends View> extends BaseAttacher<V> {

        //taiwan
        void onLogin(String account,String password);
        void onAutoLogin(String account,String password);
        String getAccessToken();

        //ningboa
        void onCNLogin(String account,String pw);
        void onCNAutoLogin(String account,String pw);
        String getCNAccessToken();
    }
}
