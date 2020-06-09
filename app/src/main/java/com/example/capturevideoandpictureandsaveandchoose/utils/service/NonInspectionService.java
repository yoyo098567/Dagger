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
    private Retrofit retrofit;
    private Handler handler;
    private int useApiCount;
    private int deviceCount;
    private int deviceSize;
    Boolean flag = true;
    private ArrayList<AddChkInfoRequest> apiDataList;
    ArrayList<ChooseDeviceItemData> chooseDeviceItemDataList;
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
        chooseDeviceItemDataList = (ArrayList<ChooseDeviceItemData>) intent.getSerializableExtra("chooseDeviceData");
        deviceCount = intent.getIntExtra("position",0);
        //for(ChooseDeviceItemData chooseDeviceItemData :chooseDeviceItemDataList){
        //    AddChkInfoRequest mData=new AddChkInfoRequest();
        //    mData.setmCO(chooseDeviceItemData.getCO());
        //    mData.setmCONM(chooseDeviceItemData.getCONM());
        //    mData.setmPMFCT(chooseDeviceItemData.getPMFCTNM());
        //    mData.setmPMFCTNM(chooseDeviceItemData.getPMFCT());
        //    mData.setmEQNO(chooseDeviceItemData.getEQNO());
        //    //要取巡檢APP資料的 暫時代替
        //    mData.setmOPCO(chooseDeviceItemData.getOPCO());
        //    mData.setmOPPLD(chooseDeviceItemData.getOPPLD());
        //}
        deviceSize = chooseDeviceItemDataList.size();
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
        Log.e("gggg","onDestroyFlag");
        flag = false;
        super.onDestroy();
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
            Calendar mCal = Calendar.getInstance();
            CharSequence date = DateFormat.format("yyyy/MM/dd hh:mm:ss ", mCal.getTime());
            if(flag){
                if(deviceCount == chooseDeviceItemDataList.size()){
                    handler.postDelayed(periodicUpdate, 5000); // schedule next wake up 10 second
                }else{
                    handler.postDelayed(periodicUpdate, 1000); // schedule next wake up 10 second
                }
            }else{
                handler.removeCallbacks(periodicUpdate);
            }
            ApiService apiService=retrofit.create(ApiService.class);
            AddChkInfoRequest mAddChkInfoRequest;
            if(deviceCount == chooseDeviceItemDataList.size()){
                mAddChkInfoRequest=new AddChkInfoRequest(AUTHORIZED_ID,
                        chooseDeviceItemDataList.get(deviceCount-1).getOPCO(),
                        chooseDeviceItemDataList.get(deviceCount-1).getOPPLD(),
                        chooseDeviceItemDataList.get(deviceCount-1).getWAYID(),
                        chooseDeviceItemDataList.get(deviceCount-1).getWAYNM(),
                        chooseDeviceItemDataList.get(deviceCount-1).getCO(),
                        chooseDeviceItemDataList.get(deviceCount-1).getCONM(),
                        chooseDeviceItemDataList.get(deviceCount-1).getPMFCT(),
                        chooseDeviceItemDataList.get(deviceCount-1).getPMFCTNM(),
                        "end",
                        "N000054949",
                        chooseDeviceItemDataList.get(deviceCount-1).getUploadNM(),
                        date.toString());
            }else{
                mAddChkInfoRequest=new AddChkInfoRequest(AUTHORIZED_ID,
                        chooseDeviceItemDataList.get(deviceCount).getOPCO(),
                        chooseDeviceItemDataList.get(deviceCount).getOPPLD(),
                        chooseDeviceItemDataList.get(deviceCount).getWAYID(),
                        chooseDeviceItemDataList.get(deviceCount).getWAYNM(),
                        chooseDeviceItemDataList.get(deviceCount).getCO(),
                        chooseDeviceItemDataList.get(deviceCount).getCONM(),
                        chooseDeviceItemDataList.get(deviceCount).getPMFCT(),
                        chooseDeviceItemDataList.get(deviceCount).getPMFCTNM(),
                        chooseDeviceItemDataList.get(deviceCount).getEQNO(),
                        "N000054949",
                        chooseDeviceItemDataList.get(deviceCount).getUploadNM(),
                        date.toString());
            }


//            Log.v("ggggg7",AUTHORIZED_ID);
//            Log.v("ggggg7",chooseDeviceItemDataList.get(deviceCount).getOPCO());
//            Log.v("ggggg7",chooseDeviceItemDataList.get(deviceCount).getOPPLD());
//            Log.v("ggggg7",chooseDeviceItemDataList.get(deviceCount).getWAYID());
//            Log.v("ggggg7",chooseDeviceItemDataList.get(deviceCount).getWAYNM());
//            Log.v("ggggg7",chooseDeviceItemDataList.get(deviceCount).getCO());
//            Log.v("ggggg7",chooseDeviceItemDataList.get(deviceCount).getCONM());
//            Log.v("ggggg7",chooseDeviceItemDataList.get(deviceCount).getPMFCT());
//            Log.v("ggggg7",chooseDeviceItemDataList.get(deviceCount).getPMFCTNM());
//            Log.v("ggggg7",chooseDeviceItemDataList.get(deviceCount).getEQNO());
//            Log.v("ggggg7",chooseDeviceItemDataList.get(deviceCount).getUploadEMP());
//            Log.v("ggggg7",chooseDeviceItemDataList.get(deviceCount).getUploadNM());
//            Log.v("ggggg7",date.toString());
//            Log.v("ggggg7","" + chooseDeviceItemDataList.get(deviceCount).getProgress());
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
                            Log.e("ggggg",""+e);
                        }

                        @Override
                        public void onComplete() {

                        }
                    })
            );
            if(deviceCount<deviceSize){
                 if(Integer.toString(chooseDeviceItemDataList.get(deviceCount).getProgress()).equals("100")){
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("datatest");
                    broadcastIntent.putExtra("Data", "positionUpdate");
                    broadcastIntent.putExtra("position", deviceCount);
                    sendBroadcast(broadcastIntent);
                    deviceCount++;
                }
            }
            if(deviceCount == deviceSize){
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("datatest");
                broadcastIntent.putExtra("Data", "end");
                sendBroadcast(broadcastIntent);
                handler.removeCallbacks(periodicUpdate);
            }
        }
    };
}
