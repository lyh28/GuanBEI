package com.lyh.guanbei.mvp.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;

import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.mvp.contract.NetListenerContract;
import com.lyh.guanbei.util.NetUtil;

public class NetListenerPresenter extends BasePresenter<NetListenerContract.INetListenerView, IModel> implements NetListenerContract.INetListenerPresenter {
    private ConnectivityManager.NetworkCallback networkCallback;
    private NetBroadCastReceiver receiver;
    private static final String OLD_NETWORK_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    @Override
    @SuppressWarnings("all")
    public void startNetListener() {
        if (Build.VERSION.SDK_INT >= 21) {
            createNetworkCallback();
            ConnectivityManager connectivityManager = (ConnectivityManager) getmContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.requestNetwork(new NetworkRequest.Builder().build(), networkCallback);
        } else {
            if (receiver == null) {
                receiver = new NetBroadCastReceiver();
            }
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(OLD_NETWORK_ACTION);
            getmContext().registerReceiver(receiver, intentFilter);
        }
    }

    @Override
    @SuppressWarnings("all")
    public void closeNetListener() {
        if (Build.VERSION.SDK_INT >= 21) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getmContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.unregisterNetworkCallback(networkCallback);
            networkCallback = null;
        } else {
            getmContext().unregisterReceiver(receiver);
            receiver = null;
        }
    }

    /**
     * 创建监听Callback
     */
    @SuppressWarnings("all")
    private void createNetworkCallback() {
        if (networkCallback == null)
            networkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    if (checkAttach())
                        getmView().onNetAvailable();
                }

                @Override
                public void onLost(Network network) {
                    super.onLost(network);
                    if (checkAttach())
                        getmView().onNetUnavailable();
                }
            };
    }

    @Override
    public IModel createModel() {
        return null;
    }

    public class NetBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (NetUtil.isNetWorkAvailable()) {
                if (checkAttach())
                    getmView().onNetAvailable();
            } else {
                if (checkAttach())
                    getmView().onNetUnavailable();
            }
        }
    }
}
