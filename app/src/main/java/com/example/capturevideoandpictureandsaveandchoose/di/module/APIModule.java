package com.example.capturevideoandpictureandsaveandchoose.di.module;

import android.content.Context;

import com.example.capturevideoandpictureandsaveandchoose.R;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.ApiService;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.ErpAPI;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class APIModule {
    @Provides
    @Singleton
    ErpAPI provideErpRemoteSource(@Named("ErpApi") Retrofit retrofit) {
        return retrofit.create(ErpAPI.class);
    }
    @Provides
    @Singleton
    ApiService provideApiRemoteSource(@Named("Api") Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    @Provides
    @Singleton
    @Named("ErpApi")
    Retrofit provideErpApiRetrofit(@Named("ErpApiURL") String url,
                                   @Named("ErpApiClient") OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
    @Provides
    @Singleton
    @Named("Api")
    Retrofit provideApiRetrofit(@Named("ApiURL") String url,
                                @Named("ApiClient") OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
    @Singleton
    @Provides
    @Named("ErpApiURL")
    String provideErpApiURL(Context context) {
        return context.getResources().getString(R.string.api_erp_url);
    }
    @Singleton
    @Provides
    @Named("ApiURL")
    String provideApiURL(Context context) {
        return context.getResources().getString(R.string.api_base_url);
    }
    @Provides
    @Singleton
    @Named("ErpApiClient")
    OkHttpClient provideErpApiOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor,
                                           @Named("ErpApiHeader") Interceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }
    @Provides
    @Singleton
    @Named("ApiClient")
    OkHttpClient provideMachanApiOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor,
                                              @Named("ApiHeader") Interceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    @Named("ErpApiHeader")
    Interceptor provideErpApiHeader() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return chain.proceed(chain.request().newBuilder()
                        .header("Content-Type", "application/json")
                        .build());
            }
        };
    }
    @Provides
    @Singleton
    @Named("ApiHeader")
    Interceptor provideMachanApiHeader() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return chain.proceed(chain.request().newBuilder()
                        .header("Content-Type", "application/json")
                        .build());
            }
        };
    }

    @Provides
    @Singleton
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

}
