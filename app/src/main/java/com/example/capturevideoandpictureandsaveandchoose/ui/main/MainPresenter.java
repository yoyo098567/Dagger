package com.example.capturevideoandpictureandsaveandchoose.ui.main;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.example.capturevideoandpictureandsaveandchoose.R;
import com.example.capturevideoandpictureandsaveandchoose.base.BasePresenter;
import com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice.ChooseDeviceItemData;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.ApiService;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.ErpAPI;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.addchkInfo.AddChkInfoRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.addchkInfo.AddChkInfoResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.disposabletoken.DisposableTokenRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.disposabletoken.DisposableTokenResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqkd.EQKDRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqkd.EQKDResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqkd.EQKDResultList;
import com.example.capturevideoandpictureandsaveandchoose.utils.rxjava.SchedulerProvider;
import com.example.capturevideoandpictureandsaveandchoose.utils.sharepreferences.LoginPreferencesProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public class MainPresenter<V extends MainContract.View> extends BasePresenter<V> implements MainContract.Presenter<V> {
    @Inject
    LoginPreferencesProvider mLoginPreferencesProvider;
    private String disposableToken;
    private String KEY_SEARCH_EQKD = "378540a4-6d39-448d-ad34-1db12e61550a";
    private DateFormat dateFormat;

    @Inject
    public MainPresenter(ApiService api, ErpAPI erpAPI, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(api, erpAPI, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onGetDisposableToken(String DeviceId) {
        String authorizedId ="fec40e7e-48c2-4226-81ca-5044b72a8e1f";
        String url = getView().getResourceString(R.string.api_on_DisposableToken);
        DisposableTokenRequest mDisposableTokenRequest=new DisposableTokenRequest(authorizedId,mLoginPreferencesProvider.getToken(),DeviceId);
        getCompositeDisposable().add(getApiService().onDisposableToken(url, mDisposableTokenRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<DisposableTokenResponse>() {
                    @Override
                    public void onNext(DisposableTokenResponse disposableTokenResponse) {
                        disposableToken = disposableTokenResponse.getmDisposableToken();
                        Log.d("DisposableToken", "onNext: "+disposableToken);
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
    public void onAddChkInfo(final ChooseDeviceItemData mChooseDeviceItemData) {
        String authorizedId ="e1569364-6066-48af-8f47-8f11bb4916dd";
        AddChkInfoRequest mAddChkInfoRequest=new AddChkInfoRequest(authorizedId,
                mChooseDeviceItemData.getCO(),
                mChooseDeviceItemData.getMNTFCT(),
                mChooseDeviceItemData.getWAYID(),
                mChooseDeviceItemData.getWAYNM(),
                mChooseDeviceItemData.getCO(),
                mChooseDeviceItemData.getCONM(),
                mChooseDeviceItemData.getPMFCT(),
                mChooseDeviceItemData.getPMFCTNM(),
                mChooseDeviceItemData.getEQNO(),
                mChooseDeviceItemData.getUploadEMP(),
                mChooseDeviceItemData.getUploadNM(),
                mChooseDeviceItemData.getRecordDate());
        String url = getView().getResourceString(R.string.api_on_AddChkInfo);
        getCompositeDisposable().add(getApiService().onAddChkInfo(url, mAddChkInfoRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<AddChkInfoResponse>() {

                    @Override
                    public void onNext(AddChkInfoResponse addChkInfoResponse) {
                        generateLogTxt("Presenter 打API成功"+"\n");
                       // generateLogTxt("Presenter 打API成功:"+mChooseDeviceItemData.getEQNO()+mChooseDeviceItemData.getEQNM()+"API，時間為"+dateFormat.format(Calendar.getInstance().getTime())+"\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        generateLogTxt("Presenter 打API失敗:"+"\n");
                        //generateLogTxt("Presenter 打API失敗:"+mChooseDeviceItemData.getEQNO()+mChooseDeviceItemData.getEQNM()+"API，時間為"+dateFormat.format(Calendar.getInstance().getTime())+"\n");
                        onAddChkInfo(mChooseDeviceItemData);
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    @Override
    public void onAddEndChkInfo(final ChooseDeviceItemData mChooseDeviceItemData) {
        String authorizedId ="e1569364-6066-48af-8f47-8f11bb4916dd";
        AddChkInfoRequest mAddChkInfoRequest=new AddChkInfoRequest(authorizedId,
                mChooseDeviceItemData.getCO(),
                mChooseDeviceItemData.getMNTFCT(),
                mChooseDeviceItemData.getWAYID(),
                mChooseDeviceItemData.getWAYNM(),
                mChooseDeviceItemData.getCO(),
                mChooseDeviceItemData.getCONM(),
                mChooseDeviceItemData.getPMFCT(),
                mChooseDeviceItemData.getPMFCTNM(),
                "end",
                mChooseDeviceItemData.getUploadEMP(),
                mChooseDeviceItemData.getUploadNM(),
                mChooseDeviceItemData.getRecordDate());
        String url = getView().getResourceString(R.string.api_on_AddChkInfo);
        getCompositeDisposable().add(getApiService().onAddChkInfo(url, mAddChkInfoRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<AddChkInfoResponse>() {

                    @Override
                    public void onNext(AddChkInfoResponse addChkInfoResponse) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        onAddEndChkInfo(mChooseDeviceItemData);
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    @Override
    public void onGetEQKDData(String account, String CO, String PMFCT, final String EQKD, final int ispickImage) {
        getView().showProgressDialog("讀取中");
        EQKDRequest mEQKDRequest = new EQKDRequest(KEY_SEARCH_EQKD, account, CO, PMFCT);
        String url = getView().getResourceString(R.string.api_on_getEQKD);
        getCompositeDisposable().add(getApiService().getEQKD(url, mEQKDRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<EQKDResultList>() {

                    @Override
                    public void onNext(EQKDResultList mEQKDResultList) {
                        getView().dismissProgressDialog();
                        String EQKDNM="";
                        if (mEQKDResultList.getmEQKDResponseList().size() < 1) {
                            // getView().dismissProgressDialog();
                            getView().showDialogCaveatMessage(getView().getResourceString(R.string.get_eqkd_error_no_data));

                        }else{
                            for(EQKDResponse mEQKDResponse:mEQKDResultList.getmEQKDResponseList()){
                                if(EQKD.equals(mEQKDResponse.getmEQKD())){
                                    EQKDNM=mEQKDResponse.getmEQKDNM();
                                }
                            }
                            if("".equals(EQKDNM)){
                                getView().onSetEQKDNMData(EQKDNM,3);
                            }else{
                                getView().onSetEQKDNMData(EQKDNM,ispickImage);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().dismissProgressDialog();
                        getView().showDialogCaveatMessage(getView().getResourceString(R.string.add_device_error));

                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    @Override
    public void onGetEQKDDataNoImg(String account, String CO, String PMFCT, final String EQKD, final Intent data, final Integer nowInList, final Integer urlNow,final Integer pickWhat) {
        //  getView().showProgressDialog("讀取中");
        EQKDRequest mEQKDRequest = new EQKDRequest(KEY_SEARCH_EQKD, account, CO, PMFCT);
        String url = getView().getResourceString(R.string.api_on_getEQKD);
        getCompositeDisposable().add(getApiService().getEQKD(url, mEQKDRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<EQKDResultList>() {

                    @Override
                    public void onNext(EQKDResultList mEQKDResultList) {
                        //   getView().dismissProgressDialog();
                        generateLogTxt("GetEQKDData onNext"+"\n");
                        String EQKDNM="";
                        if (mEQKDResultList.getmEQKDResponseList().size() < 1) {
                            //getView().showDialogCaveatMessage(getView().getResourceString(R.string.get_eqkd_error_no_data));
                            generateLogTxt("GetEQKDData onNext 但回傳無資料 "+"\n");
                            getView().onSetEQKDdataNoTalk("",data,nowInList,urlNow,pickWhat);
                        }else{
                            for(EQKDResponse mEQKDResponse:mEQKDResultList.getmEQKDResponseList()){
                                if(EQKD.equals(mEQKDResponse.getmEQKD())){
                                    EQKDNM=mEQKDResponse.getmEQKDNM();
                                }
                            }
                            if("".equals(EQKDNM)){
                                generateLogTxt("GetEQKDData onNext資料 :"+EQKDNM+"\n");
                                getView().onSetEQKDdataNoTalk(EQKDNM,data,nowInList,urlNow,pickWhat);
                            }else{
                                generateLogTxt("GetEQKDData onNext資料 :"+EQKDNM+"\n");
                                getView().onSetEQKDdataNoTalk(EQKDNM,data,nowInList,urlNow,pickWhat);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // getView().dismissProgressDialog();
                        generateLogTxt("GetEQKDData Internnet失敗"+e+"\n");
                        getView().onSetEQKDdataNoTalk("Internnet",data,nowInList,urlNow,pickWhat);
                        //  getView().showDialogCaveatMessage(getView().getResourceString(R.string.add_device_error));

                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    public void onInsInfo(String DeviceId) {
        String authorizedId ="fec40e7e-48c2-4226-81ca-5044b72a8e1f";
        String url = getView().getResourceString(R.string.api_on_InsInfo);
        DisposableTokenRequest mDisposableTokenRequest=new DisposableTokenRequest(authorizedId,mLoginPreferencesProvider.getToken(),DeviceId);
        getCompositeDisposable().add(getApiService().onDisposableToken(url, mDisposableTokenRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<DisposableTokenResponse>() {
                    @Override
                    public void onNext(DisposableTokenResponse disposableTokenResponse) {
                        disposableToken = disposableTokenResponse.getmDisposableToken();
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

    public String getDisposableToken(){
        return disposableToken;
    }
}
