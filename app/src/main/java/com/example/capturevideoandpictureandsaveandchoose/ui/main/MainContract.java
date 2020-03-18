package com.example.capturevideoandpictureandsaveandchoose.ui.main;

import com.example.capturevideoandpictureandsaveandchoose.base.BaseAttacher;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseView;

public interface MainContract {
    interface View extends BaseView {
    }

    interface Presenter<V extends MainContract.View> extends BaseAttacher<V> {
    }
}
