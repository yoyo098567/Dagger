package com.example.capturevideoandpictureandsaveandchoose.utils.api;

import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.addchkInfo.AddChkInfoRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.addchkInfo.AddChkInfoResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.disposabletoken.DisposableTokenRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.disposabletoken.DisposableTokenResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.login.LoginRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.login.LoginResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchco.CORequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchco.COResultList;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqkd.EQKDRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqkd.EQKDResultList;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqno.EQNORequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqno.EQNOResultList;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchmntfct.MNTFCTRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchmntfct.MNTFCTResultList;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchpmfct.PMFCTRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchpmfct.PMFCTResultList;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
public interface ErpAPI {
    @POST
    Observable<COResultList> getCNCO(@Url String url, @Body CORequest mCORequest);
    @POST
    Observable<MNTFCTResultList> getCNMNTFCT(@Url String url, @Body MNTFCTRequest mMNTFCTRequest);
    @POST
    Observable<PMFCTResultList> getCNPMFCT(@Url String url, @Body PMFCTRequest mPMFCTRequest);
    @POST
    Observable<EQKDResultList> getCNEQKD(@Url String url, @Body EQKDRequest mEQKDRequest);
    @POST
    Observable<EQNOResultList> getCNEQNO(@Url String url, @Body EQNORequest mEQNORequest);
    @POST
    Observable<LoginResponse> onCNLogin(@Url String url, @Body LoginRequest mLoginRequest);
    @POST
    Observable<DisposableTokenResponse> onCNDisposableToken(@Url String url, @Body DisposableTokenRequest mDisposableTokenRequest);
    @POST
    Observable<AddChkInfoResponse> onCNAddChkInfo(@Url String url, @Body AddChkInfoRequest mAddChkInfoRequest);
}
