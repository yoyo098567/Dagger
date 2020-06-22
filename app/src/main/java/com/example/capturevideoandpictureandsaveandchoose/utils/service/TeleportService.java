package com.example.capturevideoandpictureandsaveandchoose.utils.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;

import com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice.ChooseDeviceItemData;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.ApiService;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.addchkInfo.AddChkInfoRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.addchkInfo.AddChkInfoResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private Handler handler;
    private String account = "";
    private boolean isEnd = false;
    private boolean isStart=true;
    private int currentDataCount;
    private int error_api_times=0;
    private Retrofit retrofit;
    private Handler refreshHandle;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        handler = new Handler();
        refreshHandle = new Handler();
        isEnd = false;
        Log.e("ooooooooooooo", "" + currentDataCount);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isStart){
            account = intent.getStringExtra("account");
            if(account==null){
                Log.e("aaaaaaaaa","null");
            }
            Log.e("gggg", "" + account);
            currentDataCount = Integer.valueOf(intent.getStringExtra("currentDataCount"));
            Log.e("uuuuuuuuuuuu", "" + currentDataCount);
            onCreateApi();
            handler.post(periodicUpdate);
            refreshHandle.post(refreshRunnable);
            isStart=false;
        }

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
        Log.e("gggg", "onDestroy");
        handler.removeCallbacks(periodicUpdate);
        refreshHandle.removeCallbacks(refreshRunnable);
        // TODO Auto-generated method stub
    }

    private Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("datatest");
            broadcastIntent.putExtra("refresh", "" + currentDataCount);
            sendBroadcast(broadcastIntent);
            refreshHandle.postDelayed(refreshRunnable, 1000); // schedule next wake up 10 second
        }
    };
    private Runnable periodicUpdate = new Runnable() {
        @Override
        public void run() {
            getCurrentDataList();
            if (isEnd) {
                handler.postDelayed(periodicUpdate, 2000); // schedule next wake up 10 second
            } else {
                handler.postDelayed(periodicUpdate, 1000); // schedule next wake up 10 second
            }
        }
    };

    private void getCurrentDataList() {
        String CONTENT_STRING = "content://tw.com.efpg.processe_equip.provider.ShareCloud/ShareCloud";
        Calendar mCal = Calendar.getInstance();
        CharSequence currentDate;
        error_api_times=0;
        currentDate = DateFormat.format("yyyy/MM/dd", mCal.getTime());
        Uri uri = Uri.parse(CONTENT_STRING);
        Cursor cursor = this.getContentResolver().query(
                uri,
                null,
                "CurrentJob",
                null, null
        );
        ArrayList<ChooseDeviceItemData> tempDataList = new ArrayList<ChooseDeviceItemData>();
        if (cursor == null) {
        } else {
            while (cursor.moveToNext()) {
                String WAYID = cursor.getString(cursor.getColumnIndexOrThrow("WAYID"));
                String EQNO = cursor.getString(cursor.getColumnIndexOrThrow("EQNO"));
                ChooseDeviceItemData mChooseDeviceItemData = new ChooseDeviceItemData();
                mChooseDeviceItemData.setOPCO(cursor.getString(cursor.getColumnIndexOrThrow("OPCO")));
                mChooseDeviceItemData.setOPPLD(cursor.getString(cursor.getColumnIndexOrThrow("OPPLD")));
                mChooseDeviceItemData.setPMFCT(cursor.getString(cursor.getColumnIndexOrThrow("PMFCT")));
                //MNTFCT=OPPLD
                mChooseDeviceItemData.setMNTFCT(cursor.getString(cursor.getColumnIndexOrThrow("OPPLD")));
                mChooseDeviceItemData.setWAYID(WAYID);
                mChooseDeviceItemData.setWAYNM(cursor.getString(cursor.getColumnIndexOrThrow("WAYNM")));
                mChooseDeviceItemData.setEQNO(EQNO);
                mChooseDeviceItemData.setRecordDate(currentDate.toString());
                mChooseDeviceItemData.setEQNM(cursor.getString(cursor.getColumnIndexOrThrow("EQNM")));
                mChooseDeviceItemData.setEQKD(cursor.getString(cursor.getColumnIndexOrThrow("EQKD")));
                mChooseDeviceItemData.setProgress(cursor.getInt(cursor.getColumnIndexOrThrow("Progress")));
                mChooseDeviceItemData.setCO(cursor.getString(cursor.getColumnIndexOrThrow("CO")));
                mChooseDeviceItemData.setCONM(cursor.getString(cursor.getColumnIndexOrThrow("CONM")));
                mChooseDeviceItemData.setPMFCTNM(cursor.getString(cursor.getColumnIndexOrThrow("PMFCTNM")));
                mChooseDeviceItemData.setUploadNM("測試");
                mChooseDeviceItemData.setUploadEMP(account);
                mChooseDeviceItemData.setChcekDataFromAPP(true);
                tempDataList.add(mChooseDeviceItemData);
            }
            if (currentDataCount == tempDataList.size()) {
                isEnd = true;
                onAddChkInfo(tempDataList.get(currentDataCount - 1));
            } else {
                if (tempDataList.get(currentDataCount).getProgress() == 100) {
                    onAddChkInfo(tempDataList.get(currentDataCount));
                }
            }
        }
    }

    public void onAddChkInfo(final ChooseDeviceItemData mChooseDeviceItemData) {
        String authorizedId = "e1569364-6066-48af-8f47-8f11bb4916dd";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
        String str = formatter.format(curDate);
        if (isEnd) {
            mChooseDeviceItemData.setEQNO("end");
        }
        AddChkInfoRequest mAddChkInfoRequest = new AddChkInfoRequest(authorizedId,
                mChooseDeviceItemData.getOPCO(),
                mChooseDeviceItemData.getOPPLD(),
                mChooseDeviceItemData.getWAYID(),
                mChooseDeviceItemData.getWAYNM(),
                mChooseDeviceItemData.getCO(),
                mChooseDeviceItemData.getCONM(),
                mChooseDeviceItemData.getPMFCT(),
                mChooseDeviceItemData.getPMFCTNM(),
                mChooseDeviceItemData.getEQNO(),
                account,
                mChooseDeviceItemData.getUploadNM(),
                str);
        ApiService apiService = retrofit.create(ApiService.class);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(apiService.onAddChkInfo("AddChkInfo", mAddChkInfoRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<AddChkInfoResponse>() {

                            @Override
                            public void onNext(AddChkInfoResponse addChkInfoResponse) {
                                Log.e("ggggg", "" + addChkInfoResponse.getMessage());
                                if (isEnd) {
                                    stopSelf();
//                            Intent broadcastIntent = new Intent();
//                            broadcastIntent.setAction("datatest");
//                            broadcastIntent.putExtra("end", "true");
//                            sendBroadcast(broadcastIntent);
                                } else {
                                    currentDataCount++;
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("gggg", "error:" + e);
                                if(error_api_times<3){
                                    onAddChkInfo(mChooseDeviceItemData);
                                }
                                error_api_times++;
                            }

                            @Override
                            public void onComplete() {

                            }
                        })
        );
    }


    private void onCreateApi() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return chain.proceed(chain.request().newBuilder()
                        .header("Content-Type", "application/json")
                        .build());
            }
        };
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addNetworkInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://cloud.fpcetg.com.tw/FPC/API/MTN/API_MTN/MTN/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }
}
