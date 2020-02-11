package com.lyh.guanbei.jpush;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.lyh.guanbei.bean.Notification;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.manager.JPushCallbackManager;
import com.lyh.guanbei.ui.activity.LoginActivity;
import com.lyh.guanbei.util.DateUtil;
import com.lyh.guanbei.util.LogUtil;

import org.json.JSONObject;

import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class PushMessageReceiver extends JPushMessageReceiver {
    public static final int USERALIAS=1;
    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        super.onMessage(context, customMessage);
        LogUtil.logD("信息");

    }

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageArrived(context, notificationMessage);
        //通知回调
        LogUtil.logD("通知 "+notificationMessage.notificationExtras);
        Notification.save(notificationMessage.notificationTitle,notificationMessage.notificationContent,notificationMessage.notificationExtras
        , DateUtil.getNowDateTimeWithoutSecond());
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        //点击通知的回调
        LogUtil.logD("onNotifyMessageOpened   "+notificationMessage.notificationExtras);
        JPushCallbackManager.onClick(context,notificationMessage.notificationExtras);
    }
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        LogUtil.logD("jpush  "+jPushMessage.getAlias());
        if(jPushMessage.getErrorCode()==6002)
            JPushInterface.setAlias(context,USERALIAS,jPushMessage.getAlias());

        super.onAliasOperatorResult(context, jPushMessage);
    }
}
