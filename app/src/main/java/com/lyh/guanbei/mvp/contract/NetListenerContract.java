package com.lyh.guanbei.mvp.contract;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;

import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.base.IPresenter;
import com.lyh.guanbei.base.IView;
import com.lyh.guanbei.common.NetRestartService;
import com.lyh.guanbei.util.LogUtil;

public interface NetListenerContract {
    interface INetListenerView extends IView {
        void onNetAvailable();
        void onNetUnavailable();
    }

    interface INetListenerPresenter extends IPresenter<INetListenerView, IModel> {
        /**
         * 开启网络连接监听
         */
        void startNetListener();


        /**
         * 关闭
         */
        void closeNetListener();

    }
}
