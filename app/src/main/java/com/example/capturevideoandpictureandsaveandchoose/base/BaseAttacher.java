package com.example.capturevideoandpictureandsaveandchoose.base;

/**
 * Created by 5*N on 2017/12/22
 */

public interface BaseAttacher<V extends BaseView> {
    void onAttached(V view);

    void onDetached();
}
