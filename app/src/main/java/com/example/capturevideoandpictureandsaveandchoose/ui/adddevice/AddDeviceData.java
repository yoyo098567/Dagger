package com.example.capturevideoandpictureandsaveandchoose.ui.adddevice;

import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchco.COResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqkd.EQKDResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqno.EQNOResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchmntfct.MNTFCTResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchpmfct.PMFCTResponse;

import java.util.List;

public class AddDeviceData {
    private List<COResponse> mCODataList;
    private List<EQKDResponse> mEQKDDataList;
    private List<EQNOResponse> mEQNODataList;
    private List<MNTFCTResponse> mMNTFCTDataList;
    private List<PMFCTResponse> mPMFCTDataList;

    public List<COResponse> getmCODataList() {
        return mCODataList;
    }

    public void setmCODataList(List<COResponse> mCODataList) {
        this.mCODataList = mCODataList;
    }


    public List<MNTFCTResponse> getmMNTFCTDataList() {
        return mMNTFCTDataList;
    }

    public void setmMNTFCTDataList(List<MNTFCTResponse> mMNTFCTDataList) {
        this.mMNTFCTDataList = mMNTFCTDataList;
    }

    public List<EQKDResponse> getmEQKDDataList() {
        return mEQKDDataList;
    }

    public void setmEQKDDataList(List<EQKDResponse> mEQKDDataList) {
        this.mEQKDDataList = mEQKDDataList;
    }

    public List<EQNOResponse> getmEQNODataList() {
        return mEQNODataList;
    }

    public void setmEQNODataList(List<EQNOResponse> mEQNODataList) {
        this.mEQNODataList = mEQNODataList;
    }

    public List<PMFCTResponse> getmPMFCTDataList() {
        return mPMFCTDataList;
    }

    public void setmPMFCTDataList(List<PMFCTResponse> mPMFCTDataList) {
        this.mPMFCTDataList = mPMFCTDataList;
    }
}
