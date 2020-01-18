package com.lyh.guanbei.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity implements IView{
    private List<IPresenter> mPresenterList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(getLayoutId());
        mPresenterList = new ArrayList<>();
        createPresenters();
        for (IPresenter iPresenter : mPresenterList)
            iPresenter.onAttach(this, this);

        initUi();
        init();
    }
    private void setStatusBar(){
        //状态栏
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    protected void onDestroy() {
        for (IPresenter iPresenter : mPresenterList)
            iPresenter.onDettach();
        super.onDestroy();
    }

    protected void addPresenter(IPresenter iPresenter) {
        mPresenterList.add(iPresenter);
    }

    protected abstract int getLayoutId();

    protected abstract void initUi();

    protected abstract void init();

    protected void startActivity(Class activity) {
        startActivity(activity,new Bundle());
    }

    protected void startActivity(Class activity, Bundle bundle) {
        Intent intent = new Intent(this, activity);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    protected Bundle getIntentData(){
        return getIntent().getExtras();
    }
    protected void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
}
