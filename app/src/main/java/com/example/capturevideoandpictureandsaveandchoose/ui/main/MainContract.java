package com.example.capturevideoandpictureandsaveandchoose.ui.main;

import com.example.capturevideoandpictureandsaveandchoose.base.BaseAttacher;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseView;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchco.CORequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqkd.EQKDRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchpmfct.PMFCTRequest;

public interface MainContract {
    interface View extends BaseView {
    }

    interface Presenter<V extends MainContract.View> extends BaseAttacher<V> {
        void onGetCOData(String authorizedId,String idNo);
        void onGetMNTFCTData(String authorizedId,String idNo);
        void onGetPMFCTData(PMFCTRequest mPMFCTRequest);
        void onGetEQKDData(EQKDRequest mEQKDRequest);

    }
}
