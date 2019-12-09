package com.lyh.guanbei.base;

public interface ICallbackListener<T> {
    void onSuccess(T data);
    void onFailed(String msg);
}
