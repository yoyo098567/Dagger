package com.example.capturevideoandpictureandsaveandchoose.ui.main;

import android.util.Log;

import com.example.capturevideoandpictureandsaveandchoose.R;
import com.example.capturevideoandpictureandsaveandchoose.base.BasePresenter;
import com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice.ChooseDeviceContract;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.ApiService;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.ErpAPI;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchco.CORequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchco.COResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchco.COResultList;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchmntfct.MNTFCTRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchmntfct.MNTFCTResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchmntfct.MNTFCTResultList;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchpmfct.PMFCTRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchpmfct.PMFCTResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchpmfct.PMFCTResultList;
import com.example.capturevideoandpictureandsaveandchoose.utils.rxjava.SchedulerProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public class MainPresenter<V extends MainContract.View> extends BasePresenter<V> implements MainContract.Presenter<V> {
    @Inject
    public MainPresenter(ApiService api, ErpAPI erpAPI, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(api, erpAPI, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onGetCOData(String authorizedId, String idNo) {
        String url = getView().getResourceString(R.string.api_on_getCO);
        CORequest mCORequest = new CORequest(authorizedId, idNo);
        getCompositeDisposable().add(getApiService().getCO(url, mCORequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<COResultList>() {

                    @Override
                    public void onNext(COResultList mCOResultList) {
                        for(int i=0;i<mCOResultList.getcOResponseList().size();i++){
                        }
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

    @Override
    public void onGetMNTFCTData(String authorizedId, String idNo) {
        String url = getView().getResourceString(R.string.api_on_getMNTFCT);
        MNTFCTRequest mMNTFCTRequest = new MNTFCTRequest(authorizedId, idNo);
        getCompositeDisposable().add(getApiService().getMNTFCT(url, mMNTFCTRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<MNTFCTResultList>() {

                    @Override
                    public void onNext(MNTFCTResultList mMNTFCTResultList) {
                        for(MNTFCTResponse mMNTFCTResponse :mMNTFCTResultList.getmNTFCTResponse()){
//                            Log.e("wwwww","CTNM:"+mMNTFCTResponse.getmNTFCTNM());
//                            Log.e("wwwww","TCO:"+mMNTFCTResponse.getmNTCO());
                        }
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

    @Override
    public void onGetPMFCTData(PMFCTRequest mPMFCTRequest) {

        String url = getView().getResourceString(R.string.api_on_getPMFCT);
        getCompositeDisposable().add(getApiService().getPMFCT(url, mPMFCTRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribeWith(new DisposableObserver<PMFCTResultList>() {

                    @Override
                    public void onNext(PMFCTResultList mPMFCTResultList) {
                        for(PMFCTResponse mPMFCTResponse :mPMFCTResultList.getmNTFCTResponse()){
                            Log.e("wwwww","getmPMFCT:"+mPMFCTResponse.getmPMFCT());
                            Log.e("wwwww","getmPMFCTNM:"+mPMFCTResponse.getmPMFCTNM());
                        }
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
}
