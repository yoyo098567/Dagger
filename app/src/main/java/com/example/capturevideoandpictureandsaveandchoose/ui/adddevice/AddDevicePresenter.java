package com.example.capturevideoandpictureandsaveandchoose.ui.adddevice;

import android.util.Log;

import com.example.capturevideoandpictureandsaveandchoose.R;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseActivity;
import com.example.capturevideoandpictureandsaveandchoose.base.BasePresenter;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.ApiService;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.ErpAPI;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchco.CORequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchco.COResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchco.COResultList;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqkd.EQKDRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqkd.EQKDResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqkd.EQKDResultList;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqno.EQNORequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqno.EQNOResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqno.EQNOResultList;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchmntfct.MNTFCTRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchmntfct.MNTFCTResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchmntfct.MNTFCTResultList;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchpmfct.PMFCTRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchpmfct.PMFCTResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchpmfct.PMFCTResultList;
import com.example.capturevideoandpictureandsaveandchoose.utils.rxjava.SchedulerProvider;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public class AddDevicePresenter<V extends AddDeviceContract.View> extends BasePresenter<V> implements AddDeviceContract.Presenter<V> {

    //台灣廠區
    private String KEY_SEARCH_CO = "6c66fcbd-6dfe-45a2-ad6b-cbcda09b25bd";
    private String KEY_SEARCH_MNTFCT = "345972b6-d20f-43d8-8688-d253477a6b26";
    private String KEY_SEARCH_PMFCT = "25d5cf12-a1aa-428b-8297-3dc042580e24";
    private String KEY_SEARCH_EQKD = "378540a4-6d39-448d-ad34-1db12e61550a";
    private String KEY_SEARCH_EQNO = "568c47b1-a332-49ee-929a-6f3cc7c7303c";

    private String account = "";

    //寧波廠區
    private String KEY_CN_SEARCH_CO = "324cf132-33e6-4112-af67-8353715fda9d";
    private String KEY_CN_SEARCH_MNTFCT = "79b45dba-f09d-46ca-894c-beeedac61bc5";
    private String KEY_CN_SEARCH_PMFCT = "2d4f67bd-4a7c-4812-ba7f-b804989f253b";
    private String KEY_CN_SEARCH_EQKD = "d53dc261-d0e8-4835-90f0-14916ad00ccf";
    private String KEY_CN_SEARCH_EQNO = "c7c10faa-7418-46e2-87a1-6cd644ca3f18";

    @Inject
    public AddDevicePresenter(ApiService api, ErpAPI erpAPI, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(api, erpAPI, schedulerProvider, compositeDisposable);
    }

    @Override
    public void setUserId(String account) {
        this.account=account;
    }

    @Override
    public void onGetCOData() {
        getView().showProgressDialog(R.string.loading);
        String url = getView().getResourceString(R.string.api_on_getCO);
        final CORequest mCORequest = new CORequest(KEY_SEARCH_CO, account);
        getCompositeDisposable().add(getApiService().getCO(url, mCORequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<COResultList>() {

                    @Override
                    public void onNext(COResultList mCOResultList) {
                        getView().setCOData(mCOResultList.getcOResponseList());
                        getView().dismissProgressDialog();
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
    public void onCNGetCOData() {
        getView().showProgressDialog(R.string.loading);
        String url = getView().getResourceString(R.string.api_on_getCO);
        final CORequest mCORequest = new CORequest(KEY_CN_SEARCH_CO, account);
        getCompositeDisposable().add(getErpAPI().getCNCO(url, mCORequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<COResultList>() {

                    @Override
                    public void onNext(COResultList mCOResultList) {
                        getView().setCOData(mCOResultList.getcOResponseList());
                        getView().dismissProgressDialog();
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
    public void onGetMNTFCTData() {
        getView().showProgressDialog(R.string.loading);
        String url = getView().getResourceString(R.string.api_on_getMNTFCT);
        MNTFCTRequest mMNTFCTRequest = new MNTFCTRequest(KEY_SEARCH_MNTFCT, account);
        getCompositeDisposable().add(getApiService().getMNTFCT(url, mMNTFCTRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<MNTFCTResultList>() {

                    @Override
                    public void onNext(MNTFCTResultList mMNTFCTResultList) {
                        getView().setMNTFCTData(mMNTFCTResultList.getmNTFCTResponse());
                        getView().dismissProgressDialog();
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
    public void onCNGetMNTFCTData() {
        getView().showProgressDialog(R.string.loading);
        String url = getView().getResourceString(R.string.api_on_getMNTFCT);
        MNTFCTRequest mMNTFCTRequest = new MNTFCTRequest(KEY_CN_SEARCH_MNTFCT, account);
        getCompositeDisposable().add(getErpAPI().getCNMNTFCT(url, mMNTFCTRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<MNTFCTResultList>() {

                    @Override
                    public void onNext(MNTFCTResultList mMNTFCTResultList) {
                        getView().setMNTFCTData(mMNTFCTResultList.getmNTFCTResponse());
                        getView().dismissProgressDialog();
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
    public void onGetPMFCTData(String MNTCO, String MNTFCT) {
        getView().showProgressDialog(R.string.loading);
        PMFCTRequest mPMFCTRequest = new PMFCTRequest(KEY_SEARCH_PMFCT, account, MNTCO, MNTFCT);
        String url = getView().getResourceString(R.string.api_on_getPMFCT);
        getCompositeDisposable().add(getApiService().getPMFCT(url, mPMFCTRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<PMFCTResultList>() {

                    @Override
                    public void onNext(PMFCTResultList mPMFCTResultList) {
                        getView().dismissProgressDialog();
                        if (mPMFCTResultList.getmPMFCTResponseList().size() < 1) {
                            getView().showDialogCaveatMessage(getView().getResourceString(R.string.get_pmfct_error_no_data));
                        }else{
                            getView().setPMFCTData(mPMFCTResultList.getmPMFCTResponseList());
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
    public void onCNGetPMFCTData(String mMNTCO, String mMNTFCT) {
        getView().showProgressDialog(R.string.loading);
        PMFCTRequest mPMFCTRequest = new PMFCTRequest(KEY_CN_SEARCH_PMFCT, account, mMNTCO, mMNTFCT);
        String url = getView().getResourceString(R.string.api_on_getPMFCT);
        getCompositeDisposable().add(getErpAPI().getCNPMFCT(url, mPMFCTRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<PMFCTResultList>() {

                    @Override
                    public void onNext(PMFCTResultList mPMFCTResultList) {
                        getView().dismissProgressDialog();
                        if (mPMFCTResultList.getmPMFCTResponseList().size() < 1) {
                            getView().showDialogCaveatMessage(getView().getResourceString(R.string.get_pmfct_error_no_data));
                        }else{
                            getView().setPMFCTData(mPMFCTResultList.getmPMFCTResponseList());
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
    public void onGetEQKDData(String CO,String PMFCT) {
        getView().showProgressDialog(R.string.loading);
        EQKDRequest mEQKDRequest = new EQKDRequest(KEY_SEARCH_EQKD, account, CO, PMFCT);
        String url = getView().getResourceString(R.string.api_on_getEQKD);
        getCompositeDisposable().add(getApiService().getEQKD(url, mEQKDRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<EQKDResultList>() {

                    @Override
                    public void onNext(EQKDResultList mEQKDResultList) {
                        getView().dismissProgressDialog();
                        if (mEQKDResultList.getmEQKDResponseList().size() < 1) {
                            getView().showDialogCaveatMessage(getView().getResourceString(R.string.get_eqkd_error_no_data));
                        }else{
                            getView().setEQKDData(mEQKDResultList.getmEQKDResponseList());
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
    public void onCNGetEQKDData(String CO, String PMFCT) {
        getView().showProgressDialog(R.string.loading);
        EQKDRequest mEQKDRequest = new EQKDRequest(KEY_CN_SEARCH_EQKD, account, CO, PMFCT);
        String url = getView().getResourceString(R.string.api_on_getEQKD);
        getCompositeDisposable().add(getErpAPI().getCNEQKD(url, mEQKDRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<EQKDResultList>() {

                    @Override
                    public void onNext(EQKDResultList mEQKDResultList) {
                        getView().dismissProgressDialog();
                        if (mEQKDResultList.getmEQKDResponseList().size() < 1) {
                            getView().showDialogCaveatMessage(getView().getResourceString(R.string.get_eqkd_error_no_data));
                        }else{
                            getView().setEQKDData(mEQKDResultList.getmEQKDResponseList());
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
    public void onGetEQNOData(String CO,String PMFCT,String EQKD) {
        getView().showProgressDialog(R.string.loading);
        EQNORequest mEQNORequest=new EQNORequest(KEY_SEARCH_EQNO, account, CO, PMFCT, EQKD);
        String url = getView().getResourceString(R.string.api_on_getEQNO);
        getCompositeDisposable().add(getApiService().getEQNO(url, mEQNORequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<EQNOResultList>() {

                    @Override
                    public void onNext(EQNOResultList mEQNOResultList) {
                        getView().dismissProgressDialog();
                        if (mEQNOResultList.getmEQNOResponseList().size() < 1) {
                            getView().showDialogCaveatMessage(getView().getResourceString(R.string.get_eqno_error_no_data));
                        }else{
                            getView().setEQNOData(mEQNOResultList.getmEQNOResponseList());
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
    public void onCNGetEQNOData(String CO, String PMFCT, String EQKD) {
        getView().showProgressDialog(R.string.loading);
        EQNORequest mEQNORequest=new EQNORequest(KEY_CN_SEARCH_EQNO, account, CO, PMFCT, EQKD);
        String url = getView().getResourceString(R.string.api_on_getEQNO);
        getCompositeDisposable().add(getErpAPI().getCNEQNO(url, mEQNORequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<EQNOResultList>() {

                    @Override
                    public void onNext(EQNOResultList mEQNOResultList) {
                        getView().dismissProgressDialog();
                        if (mEQNOResultList.getmEQNOResponseList().size() < 1) {
                            getView().showDialogCaveatMessage(getView().getResourceString(R.string.get_eqno_error_no_data));
                        }else{
                            getView().setEQNOData(mEQNOResultList.getmEQNOResponseList());
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
}
