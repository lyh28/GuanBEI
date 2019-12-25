package com.lyh.guanbei.common;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.lyh.guanbei.R;
import com.lyh.guanbei.bean.CategoryBean;
import com.lyh.guanbei.bean.UserBean;
import com.lyh.guanbei.db.DaoMaster;
import com.lyh.guanbei.db.DaoSession;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.Util;

import org.greenrobot.greendao.database.Database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.MultiActionsNotificationBuilder;
import io.reactivex.Observable;
import okhttp3.OkHttpClient;

public class GuanBeiApplication extends Application {
    private static Context context;
    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        //推送 可异步
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //设置通知栏
//        LogUtil.logD("该设备号： " + JPushInterface.getRegistrationID(this));
        //数据库
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "guanbei_db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        //需要额外的标志或放在注册功能下
        CategoryBean.InsertPresetInList();
        CategoryBean.InsertPresetOutList();
    }

    public static Context getContext() {
        return context;
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static void showShortToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }
}
