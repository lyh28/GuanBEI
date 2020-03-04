package com.lyh.guanbei.mvp.model;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.Apk;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.mvp.contract.ApkDownloadContract;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApkDownloadModel implements ApkDownloadContract.IApkDownloadModel {
    @Override
    public void getVersion(final ICallbackListener<Apk> iCallbackListener) {
        APIManager.getNewVersion(new BaseObscriber<Apk>() {
            @Override
            protected void onSuccess(Apk data) {
                iCallbackListener.onSuccess(data);
            }

            @Override
            protected void onFailed(String msg) {
                iCallbackListener.onFailed(msg);
            }
        });
    }

    @Override
    public void download(String start, String url, final ICallbackListener<ResponseBody> iCallbackListener) {
        APIManager.download(start, url, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iCallbackListener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                iCallbackListener.onFailed(t.getMessage());
            }
        });
    }
}
