package com.example.capturevideoandpictureandsaveandchoose.utils.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice.ChooseDeviceItemData;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.ApiService;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.addchkInfo.AddChkInfoRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.addchkInfo.AddChkInfoResponse;

import java.io.IOException;
import java.util.ArrayList;
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
    private Retrofit retrofit;
    private Handler handler;
    private int useApiCount;
    private int deviceCount;
    private ArrayList<AddChkInfoRequest> apiDataList;
    private String AUTHORIZED_ID="e1569364-6066-48af-8f47-8f11bb4916dd";
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
        apiDataList=new ArrayList<>();
        ArrayList<ChooseDeviceItemData> chooseDeviceItemDataList = (ArrayList<ChooseDeviceItemData>) intent.getSerializableExtra("chooseDeviceData");
        for(ChooseDeviceItemData chooseDeviceItemData:chooseDeviceItemDataList){
            AddChkInfoRequest mData=new AddChkInfoRequest();
            mData.setmCO(chooseDeviceItemData.getCompanyId());
            mData.setmCONM(chooseDeviceItemData.getCompany());
            mData.setmPMFCT(chooseDeviceItemData.getProductionPlant());
            mData.setmPMFCTNM(chooseDeviceItemData.getProductionPlantId());
            mData.setmEQNO(chooseDeviceItemData.getDeciceId());
            //要取巡檢APP資料的 暫時代替
            mData.setmOPCO(chooseDeviceItemData.getCompanyId());
            mData.setmOPPLD(chooseDeviceItemData.getCompanyId());
        }
        deviceCount=0;
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

    public int getNowDevice(){
        return deviceCount;
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
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("datatest");
            broadcastIntent.putExtra("Data", "Broadcast Data");
            sendBroadcast(broadcastIntent);
            ApiService apiService=retrofit.create(ApiService.class);
            AddChkInfoRequest mAddChkInfoRequest=new AddChkInfoRequest();
            CompositeDisposable compositeDisposable=new CompositeDisposable();
            compositeDisposable.add(apiService.onAddChkInfo("AddChkInfo", mAddChkInfoRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<AddChkInfoResponse>() {

                        @Override
                        public void onNext(AddChkInfoResponse addChkInfoResponse) {
                            Log.e("ggggg",""+addChkInfoResponse.getMessage());
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    })
            );

        }
    };
}
