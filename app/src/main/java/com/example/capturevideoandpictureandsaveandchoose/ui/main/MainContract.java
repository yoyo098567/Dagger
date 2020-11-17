package com.example.capturevideoandpictureandsaveandchoose.ui.main;

import android.content.Intent;

import com.example.capturevideoandpictureandsaveandchoose.base.BaseAttacher;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseView;
import com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice.ChooseDeviceItemData;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchco.CORequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqkd.EQKDRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqno.EQNORequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchpmfct.PMFCTRequest;

public interface MainContract {
    interface View extends BaseView {
        //click Button
        void OnFetchDeviceClick();
        void OnChooseDeviceClick();
        void OnDeviceEditClick();
        void OnCentralCloudClick();
        void OnBasicInformationClick();
        void OnCaptureImageClick();
        void OnRecordVideoClick();
        void OnGetImageFromGalleryClick();
        void OnGetVideoFromGalleryClick();

        //set Data
        void onNonService();
        void onSetEQKDNMData(String EQKDNM,int ispickImage);
        void onSetEQKDdataNoTalk(String EQKDM, Intent data,Integer nowInList,Integer urlNow,Integer pickWhat);
    }

    interface Presenter<V extends MainContract.View> extends BaseAttacher<V> {

        //taiwan
        void onGetDisposableToken(String DeviceId);
        //void onAddChkInfo(ChooseDeviceItemData mChooseDeviceItemData,boolean loginStatus);
        void onAddChkInfo(ChooseDeviceItemData mChooseDeviceItemData);
        void onAddEndChkInfo(ChooseDeviceItemData mChooseDeviceItemData);
        void onGetEQKDDataNoImg(String account,String CO,String PMFCT,String EQKD,Intent data,Integer nowInList,Integer urlNow,Integer pickWhat);

        //ningboa
        void onCNGetDisposableToken(String DeviceId);
        void onCNAddChkInfo(ChooseDeviceItemData chooseDeviceItemData);
        void onCNAddEndChkInfo(ChooseDeviceItemData chooseDeviceItemData);
        void onCNGetEQKDDataNoImg(String account,String CO,String PMFCT,String EQKD,Intent data,Integer nowInList,Integer urlNow,Integer pickWhat);


        String getDisposableToken();
    }
}
