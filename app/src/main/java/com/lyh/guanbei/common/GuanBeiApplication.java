package com.lyh.guanbei.common;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.lyh.guanbei.db.DaoMaster;
import com.lyh.guanbei.db.DaoSession;
import com.lyh.guanbei.util.LogUtil;

import org.greenrobot.greendao.database.Database;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
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
        LogUtil.logD("该设备号： "+JPushInterface.getRegistrationID(this));
        //数据库
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "guanbei_db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        daoSession.getRecordBeanDao().deleteAll();
        daoSession.getDeleteRecordBeanDao().deleteAll();
        daoSession.getBookBeanDao().deleteAll();
        daoSession.getDeleteBookBeanDao().deleteAll();
    }

    public static Context getContext() {
        return context;
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static void showShortToast(Context context,String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context,String content) {
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }
}
