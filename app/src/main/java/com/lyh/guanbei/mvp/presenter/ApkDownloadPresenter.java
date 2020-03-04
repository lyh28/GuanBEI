package com.lyh.guanbei.mvp.presenter;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.Apk;
import com.lyh.guanbei.manager.CustomNotificationManager;
import com.lyh.guanbei.mvp.contract.ApkDownloadContract;
import com.lyh.guanbei.mvp.model.ApkDownloadModel;
import com.lyh.guanbei.util.ApkUtil;

import java.io.File;
import java.io.InputStream;

import androidx.annotation.Nullable;
import okhttp3.ResponseBody;

public class ApkDownloadPresenter extends BasePresenter<ApkDownloadContract.IApkDownloadView, ApkDownloadContract.IApkDownloadModel> implements ApkDownloadContract.IApkDownloadPresenter {
    private static InputStream inputStream;
    private static File apk;
    @Override
    public void getVersion() {
        getmModel().getVersion(new ICallbackListener<Apk>() {
            @Override
            public void onSuccess(Apk data) {
                if(checkAttach())
                    getmView().getVersionSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                if(checkAttach())
                    getmView().getVersionFailed(msg);
            }
        });
    }

    @Override
    public void download(File file, String url) {
        getmModel().download("bytes=" + file.length() + "-", url, new ICallbackListener<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody data) {
                if(checkAttach())
                    getmView().downloadCallback(data);
            }

            @Override
            public void onFailed(String msg) {
                if(checkAttach())
                    getmView().downloadFailed(msg);
            }
        });
    }
    public void downloadFromStream(InputStream inputStream, File apkFile){
        this.inputStream=inputStream;
        this.apk=apkFile;
        Intent intent=new Intent(getmContext(),DownloadService.class);
        getmContext().startService(intent);
    }
    @Override
    public ApkDownloadContract.IApkDownloadModel createModel() {
        return new ApkDownloadModel();
    }
    public static class DownloadService extends IntentService{
        public DownloadService(){
            super("download");
        }
        @Override
        protected void onHandleIntent(@Nullable Intent intent) {
            boolean done=ApkUtil.downloadApkFromStream(inputStream,apk);
            if(done){
                CustomNotificationManager.startDownloadApkDoneNotification(this,apk);
            }
        }
    }
}
