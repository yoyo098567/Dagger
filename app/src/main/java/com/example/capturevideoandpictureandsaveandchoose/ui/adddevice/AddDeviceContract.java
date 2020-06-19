package com.example.capturevideoandpictureandsaveandchoose.ui.adddevice;

import com.example.capturevideoandpictureandsaveandchoose.base.BaseAttacher;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseView;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchco.COResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqkd.EQKDResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqno.EQNOResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchmntfct.MNTFCTResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchpmfct.PMFCTResponse;

import java.util.ArrayList;
import java.util.List;

public interface AddDeviceContract {
    interface View extends BaseView {
        void setCOData(List<COResponse> adapterData);
        void setEQKDData(List<EQKDResponse> adapterData);
        void setEQNOData(List<EQNOResponse> adapterData);
        void setMNTFCTData(List<MNTFCTResponse> adapterData);
        void setPMFCTData(List<PMFCTResponse> adapterData);
    }

    interface Presenter<V extends AddDeviceContract.View> extends BaseAttacher<V> {
        void setUserId(String account);
        void onGetCOData();
        void onGetMNTFCTData();
        void onGetPMFCTData(String mMNTCO,String mMNTFCT);
        void onGetEQKDData(String CO,String PMFCT);
        void onGetEQNOData(String CO,String PMFCT,String EQKD);
    }
}
