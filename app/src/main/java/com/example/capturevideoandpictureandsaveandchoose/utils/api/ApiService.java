package com.example.capturevideoandpictureandsaveandchoose.utils.api;


import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.addchkInfo.AddChkInfoRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.addchkInfo.AddChkInfoResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.disposabletoken.DisposableTokenRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.disposabletoken.DisposableTokenResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.login.LoginRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.login.LoginResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqkd.EQKDRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqkd.EQKDResultList;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqno.EQNORequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqno.EQNOResultList;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchpmfct.PMFCTRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchco.CORequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchco.COResultList;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchmntfct.MNTFCTRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchmntfct.MNTFCTResultList;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchpmfct.PMFCTResultList;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiService {
    @POST
    Observable<COResultList> getCO(@Url String url, @Body CORequest mCORequest);
    @POST
    Observable<MNTFCTResultList> getMNTFCT(@Url String url, @Body MNTFCTRequest mMNTFCTRequest);
    @POST
    Observable<PMFCTResultList> getPMFCT(@Url String url, @Body PMFCTRequest mPMFCTRequest);
    @POST
    Observable<EQKDResultList> getEQKD(@Url String url, @Body EQKDRequest mEQKDRequest);
    @POST
    Observable<EQNOResultList> getEQNO(@Url String url, @Body EQNORequest mEQNORequest);
    @POST
    Observable<LoginResponse> onLogin(@Url String url, @Body LoginRequest mLoginRequest);
    @POST
    Observable<DisposableTokenResponse> onDisposableToken(@Url String url, @Body DisposableTokenRequest mDisposableTokenRequest);
    @POST
    Observable<AddChkInfoResponse> onAddChkInfo(@Url String url, @Body AddChkInfoRequest mAddChkInfoRequest);

}
