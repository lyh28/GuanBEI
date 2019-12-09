package com.lyh.guanbei.util;

import android.util.Log;

public class LogUtil {
    private static final String LOGT="lyh_guanbei_log";
    private static final int DEBUG=1;
    private static int level=1;
    public static void logD(String msg){
        if(level<=DEBUG)
            Log.d(LOGT, msg);
    }
}
