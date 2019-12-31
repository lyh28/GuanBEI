package com.lyh.guanbei.db;

import org.greenrobot.greendao.async.AsyncSession;

public class DBManager {
    /*
        状态描述：
        0：  仅客户端拥有，服务端未拥有
        1：  客户端和服务端共同拥有     正常状态
        2：  客户端进行了修改，未同步至服务端
     */
    private DaoSession daoSession;
    private static DBManager singleton;
    public static final int CLIENT_ONLY_STATUS = 0;
    public static final int CLIENT_SERVER_STATUS = 1;
    public static final int CLIENT_UPDATE_STATUS = 2;

    private DBManager() {

    }

    public static DBManager getInstance() {
        if (singleton == null) {
            synchronized (DBManager.class) {
                if (singleton == null)
                    singleton = new DBManager();
            }
        }
        return singleton;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    public static boolean isClientOnly(int status) {
        return status == CLIENT_ONLY_STATUS;
    }

    public static boolean isClientServer(int status) {
        return status == CLIENT_SERVER_STATUS;
    }

    public static boolean isClientUpdate(int status) {
        return status == CLIENT_UPDATE_STATUS;
    }

    //使用线程池操作
    public AsyncSession getAsyncSession() {
        if (daoSession != null) {
            AsyncSession asyncSession = daoSession.startAsyncSession();
            return asyncSession;
        }
        return null;
    }
}
