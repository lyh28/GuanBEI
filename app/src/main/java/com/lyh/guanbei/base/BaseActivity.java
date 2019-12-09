package com.lyh.guanbei.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.MessageQueue;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lyh.guanbei.common.NetRestartService;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.NetUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;

public abstract class BaseActivity extends AppCompatActivity implements IView{
    private List<IPresenter> mPresenterList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mPresenterList = new ArrayList<>();
        createPresenters();
        for (IPresenter iPresenter : mPresenterList)
            iPresenter.onAttach(this, this);
        initUi();
        init();

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
        startActivity(activity, null);
    }

    protected void startActivity(Class activity, Bundle bundle) {
        Intent intent = new Intent(this, activity);
        intent.putExtra("data", bundle);
        startActivity(intent);
    }

}
