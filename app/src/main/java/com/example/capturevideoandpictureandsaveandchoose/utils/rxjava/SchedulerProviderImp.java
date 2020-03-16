package com.example.capturevideoandpictureandsaveandchoose.utils.rxjava;


import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 5*N on 2017/12/31
 */
@Singleton
public class SchedulerProviderImp implements SchedulerProvider {
    @Inject
    public SchedulerProviderImp() {
    }

    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @Override
    public Scheduler newThread() {
        return Schedulers.newThread();
    }
}
