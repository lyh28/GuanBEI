package com.lyh.guanbei.mvp.contract;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.base.IPresenter;
import com.lyh.guanbei.base.IView;
import com.lyh.guanbei.bean.Apk;

import java.io.File;
import java.io.InputStream;

import okhttp3.ResponseBody;

public interface ApkDownloadContract {
    interface IApkDownloadView extends IView {
        void showLoading(String msg);
        void dissmisLoading();
        void getVersionSuccess(Apk apk);
        void getVersionFailed(String msg);
        void downloadCallback(ResponseBody responseBody);
        void downloadFailed(String msg);
    }
    interface IApkDownloadPresenter extends IPresenter<IApkDownloadView,IApkDownloadModel>{
        void getVersion();
        void download(File file,String url);
        void downloadFromStream(InputStream inputStream, File apkFile);
    }
    interface IApkDownloadModel extends IModel{
        void getVersion(ICallbackListener<Apk> iCallbackListener);
        void download(String start, String url, ICallbackListener<ResponseBody> iCallbackListener);
    }
}
