package com.example.capturevideoandpictureandsaveandchoose.ui.main;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.example.capturevideoandpictureandsaveandchoose.GenerateLog;
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
    private String KEY_CN_SEARCH_EQKD = "d53dc261-d0e8-4835-90f0-14916ad00ccf";
    private GenerateLog generateLogger=new GenerateLog();
    private DateFormat dateFormat;

    @Inject
    public MainPresenter(ApiService api, ErpAPI erpAPI, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(api, erpAPI, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onGetDisposableToken(String DeviceId) {
        generateLogger.generateLogTxt("onGetDisposableToken start"+"\n");
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
                        generateLogger.generateLogTxt("onGetDisposableToken next"+disposableToken+"\n");
                        Log.d("DisposableToken", "onNext: "+disposableToken);
                    }

                    @Override
                    public void onError(Throwable e) {
                        generateLogger.generateLogTxt("onGetDisposableToken error"+e+"\n");
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    @Override
    public void onCNGetDisposableToken(String DeviceId) {
        generateLogger.generateLogTxt("onCNGetDisposableToken start"+"\n");
        String authorizedId ="a5019f21-3c03-468c-8195-6ce6260b45da";
        String url = getView().getResourceString(R.string.api_on_DisposableToken);
        DisposableTokenRequest mDisposableTokenRequest=new DisposableTokenRequest(authorizedId,mLoginPreferencesProvider.getToken(),DeviceId);
        getCompositeDisposable().add(getErpAPI().onCNDisposableToken(url, mDisposableTokenRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<DisposableTokenResponse>() {
                    @Override
                    public void onNext(DisposableTokenResponse disposableTokenResponse) {
                        disposableToken = disposableTokenResponse.getmDisposableToken();
                        generateLogger.generateLogTxt("onCNGetDisposableToken next  "+disposableToken+"\n");
                        Log.d("DisposableToken", "onNext: "+disposableToken);
                    }

                    @Override
                    public void onError(Throwable e) {
                        generateLogger.generateLogTxt("onCNGetDisposableToken error "+e+"\n");
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    @Override
    public void onCNAddChkInfo(ChooseDeviceItemData chooseDeviceItemData) {
        generateLogger.generateLogTxt("onCNAddChkInfo start "+"\n");
        String authorizedId ="b654a8cb-d874-4ad1-beef-174fc0013f99";
        AddChkInfoRequest mAddChkInfoRequest=new AddChkInfoRequest(authorizedId,
                chooseDeviceItemData.getCO(),
                chooseDeviceItemData.getMNTFCT(),
                chooseDeviceItemData.getWAYID(),
                chooseDeviceItemData.getWAYNM(),
                chooseDeviceItemData.getCO(),
                chooseDeviceItemData.getCONM(),
                chooseDeviceItemData.getPMFCT(),
                chooseDeviceItemData.getPMFCTNM(),
                chooseDeviceItemData.getEQNO(),
                chooseDeviceItemData.getUploadEMP(),
                chooseDeviceItemData.getUploadNM(),
                chooseDeviceItemData.getRecordDate());
        String url = getView().getResourceString(R.string.api_on_AddChkInfo);
        getCompositeDisposable().add(getErpAPI().onCNAddChkInfo(url, mAddChkInfoRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<AddChkInfoResponse>() {

                    @Override
                    public void onNext(AddChkInfoResponse addChkInfoResponse) {
                        generateLogger.generateLogTxt("onCNAddChkInfo Presenter 打API成功"+"\n");
                        // generateLogTxt("Presenter 打API成功:"+mChooseDeviceItemData.getEQNO()+mChooseDeviceItemData.getEQNM()+"API，時間為"+dateFormat.format(Calendar.getInstance().getTime())+"\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        generateLogger.generateLogTxt("onCNAddChkInfo Presenter 打API失敗:"+"\n");
                        //generateLogTxt("Presenter 打API失敗:"+mChooseDeviceItemData.getEQNO()+mChooseDeviceItemData.getEQNM()+"API，時間為"+dateFormat.format(Calendar.getInstance().getTime())+"\n");
                        onAddChkInfo(chooseDeviceItemData);
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    @Override
    public void onCNAddEndChkInfo(ChooseDeviceItemData chooseDeviceItemData) {
        generateLogger.generateLogTxt("onCNAddEndChkInfo start "+"\n");
        String authorizedId ="b654a8cb-d874-4ad1-beef-174fc0013f99";
        AddChkInfoRequest mAddChkInfoRequest=new AddChkInfoRequest(authorizedId,
                chooseDeviceItemData.getCO(),
                chooseDeviceItemData.getMNTFCT(),
                chooseDeviceItemData.getWAYID(),
                chooseDeviceItemData.getWAYNM(),
                chooseDeviceItemData.getCO(),
                chooseDeviceItemData.getCONM(),
                chooseDeviceItemData.getPMFCT(),
                chooseDeviceItemData.getPMFCTNM(),
                "end",
                chooseDeviceItemData.getUploadEMP(),
                chooseDeviceItemData.getUploadNM(),
                chooseDeviceItemData.getRecordDate());
        String url = getView().getResourceString(R.string.api_on_AddChkInfo);
        getCompositeDisposable().add(getErpAPI().onCNAddChkInfo(url, mAddChkInfoRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<AddChkInfoResponse>() {

                    @Override
                    public void onNext(AddChkInfoResponse addChkInfoResponse) {
                        generateLogger.generateLogTxt("onCNAddEndChkInfo next "+"\n");
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        generateLogger.generateLogTxt("onCNAddEndChkInfo error "+e+"\n");
                        onAddEndChkInfo(chooseDeviceItemData);
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    @Override
    public void onCNGetEQKDDataNoImg(String account, String CO, String PMFCT, String EQKD, Intent data, Integer nowInList, Integer urlNow, Integer pickWhat) {
        generateLogger.generateLogTxt("onCNGetEQKDDataNoImg start "+"\n");
        getView().showProgressDialog(R.string.loading);
        EQKDRequest mEQKDRequest = new EQKDRequest(KEY_CN_SEARCH_EQKD, account, CO, PMFCT);
        String url = getView().getResourceString(R.string.api_on_getEQKD);
        getCompositeDisposable().add(getErpAPI().getCNEQKD(url, mEQKDRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<EQKDResultList>() {

                    @Override
                    public void onNext(EQKDResultList mEQKDResultList) {
                        generateLogger.generateLogTxt("CNGetEQKDData onNext"+"\n");
                        String EQKDNM="";
                        if (mEQKDResultList.getmEQKDResponseList().size() < 1) {
                            //getView().showDialogCaveatMessage(getView().getResourceString(R.string.get_eqkd_error_no_data));
                            generateLogger.generateLogTxt("CNGetEQKDData onNext 但回傳無資料 "+"\n");
                            getView().onSetEQKDdataNoTalk("",data,nowInList,urlNow,pickWhat);
                        }else{
                            for(EQKDResponse mEQKDResponse:mEQKDResultList.getmEQKDResponseList()){
                                if(EQKD.equals(mEQKDResponse.getmEQKD())){
                                    EQKDNM=mEQKDResponse.getmEQKDNM();
                                }
                            }
                            if("".equals(EQKDNM)){
                                generateLogger.generateLogTxt("CNGetEQKDData onNext資料 :EQKDNM=' ' "+"\n");
                                getView().onSetEQKDdataNoTalk(EQKDNM,data,nowInList,urlNow,pickWhat);
                            }else{
                                generateLogger.generateLogTxt("CNGetEQKDData onNext資料 :"+EQKDNM+"\n");
                                getView().onSetEQKDdataNoTalk(EQKDNM,data,nowInList,urlNow,pickWhat);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        generateLogger.generateLogTxt("GetCNEQKDData Internnet失敗"+e+"\n");
                        getView().onSetEQKDdataNoTalk("Internnet",data,nowInList,urlNow,pickWhat);
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    @Override
    public void onAddChkInfo(final ChooseDeviceItemData mChooseDeviceItemData) {
        generateLogger.generateLogTxt("onAddChkInfo start "+"\n");
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
                        generateLogger.generateLogTxt("onAddChkInfo Presenter 打API成功"+"\n");
                       // generateLogTxt("Presenter 打API成功:"+mChooseDeviceItemData.getEQNO()+mChooseDeviceItemData.getEQNM()+"API，時間為"+dateFormat.format(Calendar.getInstance().getTime())+"\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        generateLogger.generateLogTxt("onAddChkInfo Presenter 打API失敗:"+e+"\n");
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
        generateLogger.generateLogTxt("onAddEndChkInfo start "+"\n");
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
                        generateLogger.generateLogTxt("onAddEndChkInfo next "+"\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        generateLogger.generateLogTxt("onAddEndChkInfo error "+e+"\n");
                        onAddEndChkInfo(mChooseDeviceItemData);
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
        generateLogger.generateLogTxt("onGetEQKDDataNoImg start "+"\n");
        EQKDRequest mEQKDRequest = new EQKDRequest(KEY_SEARCH_EQKD, account, CO, PMFCT);
        String url = getView().getResourceString(R.string.api_on_getEQKD);
        getCompositeDisposable().add(getApiService().getEQKD(url, mEQKDRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<EQKDResultList>() {

                    @Override
                    public void onNext(EQKDResultList mEQKDResultList) {
                        //   getView().dismissProgressDialog();
                        generateLogger.generateLogTxt("GetEQKDData onNext"+"\n");
                        String EQKDNM="";
                        if (mEQKDResultList.getmEQKDResponseList().size() < 1) {
                            //getView().showDialogCaveatMessage(getView().getResourceString(R.string.get_eqkd_error_no_data));
                            generateLogger.generateLogTxt("GetEQKDData onNext 但回傳無資料 "+"\n");
                            getView().onSetEQKDdataNoTalk("",data,nowInList,urlNow,pickWhat);
                        }else{
                            for(EQKDResponse mEQKDResponse:mEQKDResultList.getmEQKDResponseList()){
                                if(EQKD.equals(mEQKDResponse.getmEQKD())){
                                    EQKDNM=mEQKDResponse.getmEQKDNM();
                                }
                            }
                            if("".equals(EQKDNM)){
                                generateLogger.generateLogTxt("GetEQKDData onNext資料 :EQKDNM= ' ' "+"\n");
                                getView().onSetEQKDdataNoTalk(EQKDNM,data,nowInList,urlNow,pickWhat);
                            }else{
                                generateLogger.generateLogTxt("GetEQKDData onNext資料 :"+EQKDNM+"\n");
                                getView().onSetEQKDdataNoTalk(EQKDNM,data,nowInList,urlNow,pickWhat);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // getView().dismissProgressDialog();
                        generateLogger.generateLogTxt("GetEQKDData Internnet失敗"+e+"\n");
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
