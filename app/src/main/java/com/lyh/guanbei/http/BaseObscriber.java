package com.lyh.guanbei.http;


import android.util.Log;

import com.lyh.guanbei.util.LogUtil;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObscriber<T> implements Observer<BaseResponse<T>> {
    @Override
    public void onSubscribe(Disposable d) {}

    @Override
    public void onComplete() {}

    @Override
    public void onError(Throwable e) {
        LogUtil.logD("出错"+e.getMessage());
        onFailed(e.getMessage());
    }
    @Override
    public void onNext(BaseResponse<T> baseResponse) {
        LogUtil.logD("---返回数据---");
        LogUtil.logD(baseResponse.toString());
        LogUtil.logD("---------------");
        if(baseResponse.isSuccess()){
            onSuccess(baseResponse.getData());
        }else{
            onFailed(baseResponse.getMsg());
        }
    }
    protected abstract void onSuccess(T data);

    protected abstract void onFailed(String msg);
}
