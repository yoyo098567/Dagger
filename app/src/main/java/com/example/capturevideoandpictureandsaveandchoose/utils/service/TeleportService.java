package com.example.capturevideoandpictureandsaveandchoose.utils.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.capturevideoandpictureandsaveandchoose.R;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.ApiService;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchco.CORequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchco.COResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchco.COResultList;

import java.io.IOException;
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

public class TeleportService extends Service {
    private Retrofit retrofit;
    private Handler handler;
    private int useApiCount;
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
        useApiCount=0;
        handler=new Handler();
        onCreateApi();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(periodicUpdate);
        String a=intent.getStringExtra("aa");
        Log.e("ggggg",""+a);
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
        // TODO Auto-generated method stub
    }

    private void onCreateApi(){
        Interceptor interceptor=new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return chain.proceed(chain.request().newBuilder()
                        .header("Content-Type", "application/json")
                        .build());
            }
        };
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addNetworkInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        retrofit=new Retrofit.Builder()
                .baseUrl("https://cloud.fpcetg.com.tw/FPC/API/MTN/API_MTN/MTN/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
    private Runnable periodicUpdate = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(periodicUpdate, 10000); // schedule next wake up 10 second
            ApiService apiService=retrofit.create(ApiService.class);
//            final CORequest mCORequest = new CORequest("6c66fcbd-6dfe-45a2-ad6b-cbcda09b25bd","N123456789");
//            CompositeDisposable compositeDisposable=new CompositeDisposable();
//            compositeDisposable.add(apiService.getCO("SearchCO", mCORequest)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeWith(new DisposableObserver<COResultList>() {
//
//                        @Override
//                        public void onNext(COResultList mCOResultList) {
//                            for(COResponse mCOResponse :mCOResultList.getcOResponseList()){
//                                Log.e("wwwww","getcO:"+mCOResponse.getcO());
//                                Log.e("wwwww","getcONM:"+mCOResponse.getcONM());
//                            }
//                            useApiCount++;
//                            Log.e("wwwww","getcONM:"+useApiCount);
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            if(useApiCount>1){
//                                handler.removeCallbacks(periodicUpdate);
//                                stopSelf();
//                            }
//                        }
//                    })
//            );
        }
    };
}
