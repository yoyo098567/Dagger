package com.example.capturevideoandpictureandsaveandchoose.utils.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;

import android.util.Log;
import android.widget.Toast;

import com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice.ChooseDeviceItemData;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.ApiService;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.addchkInfo.AddChkInfoRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.addchkInfo.AddChkInfoResponse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
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

import static java.lang.String.format;
import static java.text.DateFormat.*;

public class TeleportService extends Service {
    private Handler handler;
    private String account = "";
    private boolean isEnd = false;
    private boolean isStart=true;
    private int totalData=0;
    private int currentDataCount;
    private Retrofit retrofit;
    private Handler refreshHandle;
    private DateFormat dateFormat;

    //創建log.txt
    public void generateLogTxt(String sBody) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM_dd");
            Date date = new Date();
             File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/capturevideoandpictureandsaveandchoose/");
            if (!root.exists()) {
                root.mkdirs();
            }
            String sFileName="log"+sdf.format(date)+".txt";
            File logttt = new File(root, sFileName);
            FileWriter writer = new FileWriter(logttt,true);
            writer.append(sBody);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

         dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        if (isStart){
            generateLogTxt("service onStartCommand 開始"+"，時間為"+dateFormat.format(Calendar.getInstance().getTime())+"\n");
            account = intent.getStringExtra("account");
            if(account==null){
            }
            currentDataCount = Integer.valueOf(intent.getStringExtra("currentDataCount"));
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
        generateLogTxt("service onDestroy 停止"+"，時間為"+dateFormat.format(Calendar.getInstance().getTime())+"\n");
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
            refreshHandle.postDelayed(refreshRunnable, 2000); // schedule next wake up 10 second
        }
    };
    private Runnable periodicUpdate = new Runnable() {
        @Override
        public void run() {

            getCurrentDataList();
            if (isEnd) {
               //handler.postDelayed(periodicUpdate, 10000); // schedule next wake up 10 second
                handler.postDelayed(periodicUpdate, 300000); // schedule next wake up 10 second
            } else {
               //handler.postDelayed(periodicUpdate, 5000); // schedule next wake up 10 second
                handler.postDelayed(periodicUpdate, 180000); // schedule next wake up 10 second
            }
        }
    };

    private void getCurrentDataList() {
        generateLogTxt("service getCurrentDataList"+"，時間為"+dateFormat.format(Calendar.getInstance().getTime())+"\n");
        String CONTENT_STRING = "content://tw.com.efpg.processe_equip.provider.ShareCloud/ShareCloud";
        Calendar mCal = Calendar.getInstance();
        CharSequence currentDate;
        currentDate = format("yyyy/MM/dd", mCal.getTime());
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
                mChooseDeviceItemData.setUploadNM("");
                mChooseDeviceItemData.setUploadEMP(account);
                mChooseDeviceItemData.setChcekDataFromAPP(true);
                tempDataList.add(mChooseDeviceItemData);
            }
            totalData=tempDataList.size();
            if(currentDataCount<=tempDataList.size()){
                if (currentDataCount == tempDataList.size()) {
                    generateLogTxt("service 準備打最後一個API"+"，時間為"+dateFormat.format(Calendar.getInstance().getTime())+"\n");
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
                    String str = formatter.format(curDate);
                    tempDataList.get(tempDataList.size() - 1).setRecordDate(str);
                    tempDataList.get(tempDataList.size() - 1).setEQNO("end");
                    tempDataList.get(tempDataList.size() - 1).setPosition(currentDataCount);
                    onAddChkInfo(tempDataList.get(tempDataList.size() - 1));
                    currentDataCount++;
                } else {
                    if (tempDataList.get(currentDataCount).getProgress() == 100) {
                        generateLogTxt("service 準備打目前 currentDataCount:"+currentDataCount+" + "+tempDataList.get(currentDataCount).getEQNO()+tempDataList.get(currentDataCount).getEQNM()+"API，時間為"+dateFormat.format(Calendar.getInstance().getTime())+"\n");
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
                        String str = formatter.format(curDate);
                        tempDataList.get(currentDataCount).setRecordDate(str);
                        tempDataList.get(currentDataCount).setPosition(currentDataCount);
                        onAddChkInfo(tempDataList.get(currentDataCount));
                        currentDataCount++;
                    }
                }
            }
            if(currentDataCount >= tempDataList.size()){
                isEnd = true;
            }
        }
    }

    public void onAddChkInfo(final ChooseDeviceItemData mChooseDeviceItemData) {
        String authorizedId = "e1569364-6066-48af-8f47-8f11bb4916dd";

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
                mChooseDeviceItemData.getRecordDate());
        ApiService apiService = retrofit.create(ApiService.class);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(apiService.onAddChkInfo("AddChkInfo", mAddChkInfoRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<AddChkInfoResponse>() {

                            @Override
                            public void onNext(AddChkInfoResponse addChkInfoResponse) {
                              generateLogTxt("打api onNext 成功 設備為:"+mChooseDeviceItemData.getEQNO()+mChooseDeviceItemData.getEQNM()+"，時間為"+dateFormat.format(Calendar.getInstance().getTime())+"\n");
                                if (mChooseDeviceItemData.getPosition() >= totalData) {
                                    generateLogTxt("service position > totalDate 停止"+"，時間為"+dateFormat.format(Calendar.getInstance().getTime())+"\n");
                                    stopSelf();
//                            Intent broadcastIntent = new Intent();
//                            broadcastIntent.setAction("datatest");
//                            broadcastIntent.putExtra("end", "true");
//                            sendBroadcast(broadcastIntent);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                generateLogTxt("打api失敗，原因為"+e.toString()+"\n");
                                onAddChkInfo(mChooseDeviceItemData);
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
