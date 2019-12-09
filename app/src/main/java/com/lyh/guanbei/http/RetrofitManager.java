package com.lyh.guanbei.http;

import com.lyh.guanbei.common.Contact;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private volatile static Retrofit sRetrofit;
    //Okhttp常量
    private static final int TIMEOUT=5*1000;

    public static Retrofit getRetrofit(){
        if(sRetrofit==null){
            sRetrofit=createRetrofit();
        }
        return sRetrofit;
    }
    private static Retrofit createRetrofit(){
        synchronized (RetrofitManager.class){
            if(sRetrofit==null){
                OkHttpClient okHttpClient=new OkHttpClient.Builder()
                        .connectTimeout(TIMEOUT,TimeUnit.MILLISECONDS)
                        .readTimeout(TIMEOUT,TimeUnit.MILLISECONDS)
                        .writeTimeout(TIMEOUT,TimeUnit.MILLISECONDS)
                        .build();
                sRetrofit=new Retrofit.Builder()
                        .client(okHttpClient)
                        .baseUrl(Contact.BASEURL)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
        }
        return sRetrofit;
    }
}
