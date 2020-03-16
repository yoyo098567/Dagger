package com.example.capturevideoandpictureandsaveandchoose.base;

import com.example.capturevideoandpictureandsaveandchoose.utils.api.ApiService;
import com.example.capturevideoandpictureandsaveandchoose.utils.rxjava.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class BasePresenter<V extends BaseView> implements BaseAttacher<V> {
    private V mView;
    private ApiService api;
    private SchedulerProvider schedulerProvider;
    private CompositeDisposable compositeDisposable;
    @Inject
    public BasePresenter(ApiService api,
                         SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable) {
        this.api = api;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = compositeDisposable;
    }

    public V getView() {
        return mView;
    }

    @Override
    public void onAttached(V view) {
        mView = view;
    }

    @Override
    public void onDetached() {
        mView = null;
    }

    public ApiService getApiService() {
        return api;
    }

    public SchedulerProvider getSchedulerProvider() {
        return schedulerProvider;
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

}
