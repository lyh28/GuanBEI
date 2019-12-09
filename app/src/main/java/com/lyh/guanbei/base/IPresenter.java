package com.lyh.guanbei.base;

import android.content.Context;

public interface IPresenter<V extends IView,M extends IModel> {
    //绑定
    void onAttach(V view,Context context);
    //解绑
    void onDettach();
    //检测是否已绑定
    boolean checkAttach();
    //返回model
    M createModel();
}
