package com.example.capturevideoandpictureandsaveandchoose.ui.login;

import android.util.Log;

import com.example.capturevideoandpictureandsaveandchoose.GenerateLog;
import com.example.capturevideoandpictureandsaveandchoose.R;
import com.example.capturevideoandpictureandsaveandchoose.base.BasePresenter;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.ApiService;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.ErpAPI;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.login.LoginRequest;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.login.LoginResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.rxjava.SchedulerProvider;
import com.example.capturevideoandpictureandsaveandchoose.utils.sharepreferences.LoginPreferencesProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public class LoginPresenter<V extends LoginContract.View> extends BasePresenter<V> implements LoginContract.Presenter<V> {
    @Inject
    LoginPreferencesProvider mLoginPreferencesProvider;
    private String LOGIN_AUTHORIZED_ID = "acd9be92-46bf-4185-8721-5b60c67f0742";
    private String LOGIN_CN_AUTHORIZED_ID="79bde949-d20f-4e6a-8f27-c18ac8c50d8e";
    private String AccessToken = "";
    private GenerateLog generateLogger=new GenerateLog();
    @Inject
    public LoginPresenter(ApiService api, ErpAPI erpAPI, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(api, erpAPI, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onLogin(String account, String password) {
        generateLogger.generateLogTxt("onLogin start"+"\n");
        getView().showProgressDialog(getView().getResourceString(R.string.login));
        String url = getView().getResourceString(R.string.api_on_Login);
//        LoginRequest mLoginRequest=new LoginRequest(LOGIN_AUTHORIZED_ID,"N000054949","1203-Z");
//        LoginRequest mLoginRequest=new LoginRequest(LOGIN_AUTHORIZED_ID,"N000135056","1203-Z");
        LoginRequest mLoginRequest = new LoginRequest(LOGIN_AUTHORIZED_ID, account, password);
        getCompositeDisposable().add(getApiService().onLogin(url, mLoginRequest)
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeWith(new DisposableObserver<LoginResponse>() {

                            @Override
                            public void onNext(LoginResponse loginResponse) {
                                Log.v("LoginResponse", "" + loginResponse.getmResult());

                                if ("true".equals(loginResponse.getmResult())) {

                                    AccessToken = loginResponse.getmToken();
                                    generateLogger.generateLogTxt("onLogin next true    "+AccessToken+"\n");
                                    mLoginPreferencesProvider.setToken(loginResponse.getmToken());
                                    getView().dismissProgressDialog();
                                    getView().onCompleteLogin();
                                } else {
                                    generateLogger.generateLogTxt("onLogin next error 登入失敗"+"\n");
                                    getView().showDialogCaveatMessage(getView().getResourceString(R.string.login_false));
                                    getView().dismissProgressDialog();
//                            getView().onCompleteLogin();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.v("LoginResponse", "" + e.getMessage());
                                if ("HTTP 400 Bad Request".equals(e.getMessage())) {
                                    generateLogger.generateLogTxt("onLogin error 400 登入失敗帳密布存在"+e+"\n");
                                    getView().showDialogCaveatMessage(getView().getResourceString(R.string.login_input_false));
                                } else if ("java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 2 path $".equals(e.getMessage())) {
                                    generateLogger.generateLogTxt("onLogin error java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 2 path"+"\n");
                                    getView().showDialogCaveatMessage(getView().getResourceString(R.string.login_false));
                                } else {
                                    generateLogger.generateLogTxt("onLogin error 檢查網路狀況"+e+"\n");
                                    getView().showDialogCaveatMessage(getView().getResourceString(R.string.login_false_internet));
                                }
                                getView().dismissProgressDialog();
//                        getView().onCompleteLogin();        //API壞掉，暫時修改
                            }

                            @Override
                            public void onComplete() {

                            }
                        })
        );
    }

    @Override
    public void onAutoLogin(final String account, String password) {
        generateLogger.generateLogTxt("onAutoLogin start"+"\n");
        getView().showProgressDialog(getView().getResourceString(R.string.auto_login));
        String url = getView().getResourceString(R.string.api_on_Login);
        LoginRequest mLoginRequest = new LoginRequest(LOGIN_AUTHORIZED_ID, account, password);
        getCompositeDisposable().add(getApiService().onLogin(url, mLoginRequest)
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeWith(new DisposableObserver<LoginResponse>() {

                            @Override
                            public void onNext(LoginResponse loginResponse) {
                                if ("true".equals(loginResponse.getmResult())) {

                                    AccessToken = loginResponse.getmToken();
                                    generateLogger.generateLogTxt("onAutologin next true"+AccessToken+"\n");
                                    mLoginPreferencesProvider.setToken(loginResponse.getmToken());
                                    getView().dismissProgressDialog();
                                    getView().onCoompleteAutoLogin(account);
                                } else {
                                    generateLogger.generateLogTxt("onAutologin next error "+"\n");
                                    getView().showDialogCaveatMessage(getView().getResourceString(R.string.auto_login_false));
                                    getView().dismissProgressDialog();
//                            getView().onCompleteLogin();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().dismissProgressDialog();
                                generateLogger.generateLogTxt("onAutologin  error "+e+"\n");
                                getView().showDialogCaveatMessage(getView().getResourceString(R.string.auto_login_false));
                            }

                            @Override
                            public void onComplete() {

                            }
                        })
        );
    }

    public String getAccessToken() {
        return AccessToken;
    }

    @Override
    public void onCNLogin(String account, String pw) {
        generateLogger.generateLogTxt("onCNlogin start "+"\n");
        getView().showProgressDialog(getView().getResourceString(R.string.login));
        String url = getView().getResourceString(R.string.api_on_Login);
//        LoginRequest mLoginRequest=new LoginRequest(LOGIN_AUTHORIZED_ID,"N000054949","1203-Z");
//        LoginRequest mLoginRequest=new LoginRequest(LOGIN_AUTHORIZED_ID,"N000135056","1203-Z");
        LoginRequest mLoginRequest = new LoginRequest(LOGIN_CN_AUTHORIZED_ID, account, pw);
        getCompositeDisposable().add(getErpAPI().onCNLogin(url, mLoginRequest)
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeWith(new DisposableObserver<LoginResponse>() {

                            @Override
                            public void onNext(LoginResponse loginResponse) {
                                Log.v("LoginResponse", "" + loginResponse.getmResult());

                                if ("true".equals(loginResponse.getmResult())) {

                                    AccessToken = loginResponse.getmToken();
                                    generateLogger.generateLogTxt("onCNlogin next true "+AccessToken+"\n");
                                    mLoginPreferencesProvider.setToken(loginResponse.getmToken());
                                    getView().dismissProgressDialog();
                                    getView().onCompleteLogin();
                                } else {
                                    generateLogger.generateLogTxt("onCNlogin next error "+"\n");
                                    getView().showDialogCaveatMessage(getView().getResourceString(R.string.login_false));
                                    getView().dismissProgressDialog();
//                            getView().onCompleteLogin();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.v("LoginResponse", "" + e.getMessage());

                                if ("HTTP 400 Bad Request".equals(e.getMessage())) {
                                    generateLogger.generateLogTxt("onCNlogin error 400 "+e+"\n");
                                    getView().showDialogCaveatMessage(getView().getResourceString(R.string.login_input_false));
                                } else if ("java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 2 path $".equals(e.getMessage())) {
                                    generateLogger.generateLogTxt("onCNlogin error java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 2 path "+e+"\n");
                                    getView().showDialogCaveatMessage(getView().getResourceString(R.string.login_false));
                                } else {
                                    generateLogger.generateLogTxt("onCNlogin error  "+e+"\n");
                                    getView().showDialogCaveatMessage(getView().getResourceString(R.string.login_false_internet));
                                }
                                getView().dismissProgressDialog();
//                        getView().onCompleteLogin();        //API壞掉，暫時修改
                            }

                            @Override
                            public void onComplete() {

                            }
                        })
        );
    }

    @Override
    public void onCNAutoLogin(String account, String pw) {
        generateLogger.generateLogTxt("onCNlogin AutoLogin start"+"\n");
        getView().showProgressDialog(getView().getResourceString(R.string.auto_login));
        String url = getView().getResourceString(R.string.api_on_Login);
        LoginRequest mLoginRequest = new LoginRequest(LOGIN_CN_AUTHORIZED_ID, account, pw);
        getCompositeDisposable().add(getErpAPI().onCNLogin(url, mLoginRequest)
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeWith(new DisposableObserver<LoginResponse>() {

                            @Override
                            public void onNext(LoginResponse loginResponse) {
                                if ("true".equals(loginResponse.getmResult())) {
                                    AccessToken = loginResponse.getmToken();
                                    generateLogger.generateLogTxt("onCNlogin AutoLogin true"+AccessToken+"\n");
                                    mLoginPreferencesProvider.setToken(loginResponse.getmToken());
                                    getView().dismissProgressDialog();
                                    getView().onCoompleteAutoLogin(account);
                                } else {
                                    generateLogger.generateLogTxt("onCNlogin AutoLogin next error"+"\n");
                                    getView().showDialogCaveatMessage(getView().getResourceString(R.string.auto_login_false));
                                    getView().dismissProgressDialog();
//                            getView().onCompleteLogin();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                generateLogger.generateLogTxt("onCNlogin AutoLogin error"+e+"\n");
                                getView().dismissProgressDialog();
                                getView().showDialogCaveatMessage(getView().getResourceString(R.string.auto_login_false));
                            }

                            @Override
                            public void onComplete() {

                            }
                        })
        );
    }

    @Override
    public String getCNAccessToken() {
        return AccessToken;
    }
}
