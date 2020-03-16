package com.example.capturevideoandpictureandsaveandchoose.utils.rxjava;


import io.reactivex.Scheduler;

/**
 * Created by 5*N on 2017/12/31
 */

public interface SchedulerProvider {
    Scheduler ui();

    Scheduler computation();

    Scheduler io();

    Scheduler newThread();
}
