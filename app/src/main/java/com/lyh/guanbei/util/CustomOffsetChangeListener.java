package com.lyh.guanbei.util;

import com.google.android.material.appbar.AppBarLayout;

public abstract class CustomOffsetChangeListener implements AppBarLayout.OnOffsetChangedListener {
    private int totalScrollRange;
    public enum State {
        EXPANDED,       //展开状态
        COLLAPSED,      //折叠状态
        IDLE            //中间状态
    }

    private State mCurrentState = State.IDLE;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if(totalScrollRange<=0){
            totalScrollRange=appBarLayout.getTotalScrollRange();
        }
        onOffsetChangedForRate(appBarLayout,Math.abs(i)*1.0f/totalScrollRange);
        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(i) >= totalScrollRange) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE);
            }
            mCurrentState = State.IDLE;
        }
    }

    //状态发生了改变
    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);

    //发生了偏移  rate为偏移比率
    public abstract void onOffsetChangedForRate(AppBarLayout appBarLayout,float rate);
}
