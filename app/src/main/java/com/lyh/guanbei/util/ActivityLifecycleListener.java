package com.lyh.guanbei.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.lyh.guanbei.manager.ActivityManager;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.ui.activity.UnLockActivity;

public class ActivityLifecycleListener implements Application.ActivityLifecycleCallbacks {
    private int refCount = 0;
    private boolean outBackGround=false;
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        refCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if(outBackGround)
            setAppGoBackGround();
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        refCount--;
        if(refCount == 0){
            outBackGround=true;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
    private void setAppGoBackGround(){
//        LogUtil.logD("进入后台");
//        if(ActivityManager.getInstance().getTopActivity().isLocked()&& CustomSharedPreferencesManager.getInstance().getUser().getSetting().isLocked()){
//            //手势密码解锁页
//            ActivityManager.getInstance().getTopActivity().startActivity(UnLockActivity.class);
//        }
    }
}
