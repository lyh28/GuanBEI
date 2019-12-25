package com.lyh.guanbei.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lyh.guanbei.ui.activity.BookDetailAddUserActivity;
import com.lyh.guanbei.ui.activity.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.NotificationMessage;

public class JPushCallbackManager {
    private static final int BOOK_INVITE=1; //账本邀请请求
    private static final int BUDGET_WARNING=2;      //超出预算警告
    private static final String TYPE="type";
    public static void onClick(Context context, NotificationMessage notificationMessage){
        try {
            JSONObject jsonObject=new JSONObject(notificationMessage.notificationExtras);
            switch (jsonObject.getInt(TYPE)){
                case BOOK_INVITE:
                    Intent intent=new Intent(context, BookDetailAddUserActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putLong("bookId",jsonObject.getLong("bookId"));
                    bundle.putLong("requestId",jsonObject.getLong("requestId"));
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
                case BUDGET_WARNING:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
