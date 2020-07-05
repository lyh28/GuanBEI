package com.lyh.guanbei.manager;

import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.ui.activity.MainActivity;
import com.lyh.guanbei.util.LogUtil;

import java.lang.ref.WeakReference;
import java.util.Stack;

public class ActivityManager {
    private static ActivityManager mInstance;
    private Stack<BaseActivity> stack;
    private WeakReference<BaseActivity> mMainActivity;
    private ActivityManager() {
        stack = new Stack<>();
    }

    public static ActivityManager getInstance() {
        if (mInstance == null)
            synchronized (ActivityManager.class) {
                if (mInstance == null)
                    mInstance = new ActivityManager();
            }
        return mInstance;
    }

    public void finishAll(){
        while(!stack.empty()){
            BaseActivity activity=stack.pop();
            activity.finish();
            activity=null;
        }
    }
    public void addActivity(BaseActivity activity){
        stack.push(activity);
        if(activity instanceof MainActivity)
            mMainActivity=new WeakReference<>(activity);
    }
    public BaseActivity getMainActivity(){
        return mMainActivity.get();
    }
    public void finishActivity(BaseActivity activity){
        if(!stack.empty()) {
            stack.remove(activity);
            activity.finish();
            activity = null;
        }
    }
    public BaseActivity getTopActivity(){
        return stack.peek();
    }
}
