package com.example.capturevideoandpictureandsaveandchoose.utils.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;

import com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice.ChooseDeviceItemData;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.ApiService;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.addchkInfo.AddChkInfoRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.addchkInfo.AddChkInfoResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NonInspectionService extends Service {
    private Handler handler;
    private String msg="";
    private boolean isEnd=false;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }
    private void init(){
        handler=new Handler();
        isEnd=false;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(periodicUpdate);
        // TODO Auto-generated method stub
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("gggg","onDestroy");
        handler.removeCallbacks(periodicUpdate);
        // TODO Auto-generated method stub
    }

    private Runnable periodicUpdate = new Runnable() {
        @Override
        public void run() {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("datatest");
            broadcastIntent.putExtra("NonInspectionServiceTime", "false");
            sendBroadcast(broadcastIntent);
            Log.e("CCCCCSIZE","wwwwww");
            handler.postDelayed(periodicUpdate, 5000); // schedule next wake up 10 second

        }
    };
}
