package com.lyh.guanbei.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.EditText;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.mvp.contract.NetListenerContract;
import com.lyh.guanbei.mvp.presenter.NetListenerPresenter;
import com.lyh.guanbei.test.BookActivity;
import com.lyh.guanbei.test.BookIUActivity;
import com.lyh.guanbei.test.FilterActivity;
import com.lyh.guanbei.test.LoginActivity;
import com.lyh.guanbei.test.RecordActivity;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.NetUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

public class MainActivity extends BaseActivity implements View.OnClickListener, NetListenerContract.INetListenerView {
    private NetListenerPresenter mNetListenerPresenter;

    private EditText update;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initUi() {
        findViewById(R.id.activity_main_login).setOnClickListener(this);
        findViewById(R.id.activity_main_record).setOnClickListener(this);
        findViewById(R.id.activity_main_book).setOnClickListener(this);

        findViewById(R.id.activity_book_add).setOnClickListener(this);
        findViewById(R.id.activity_book_update).setOnClickListener(this);
        findViewById(R.id.activity_main_filter).setOnClickListener(this);
        update = findViewById(R.id.activity_book_updateId);

        List<String> list=new ArrayList<>(10);
        LogUtil.logD(list.size()+"");
    }

    @Override
    protected void init() {
        mNetListenerPresenter.startNetListener();
    }

    @Override
    protected void onDestroy() {
        mNetListenerPresenter.closeNetListener();
        super.onDestroy();
    }

    @Override
    public void createPresenters() {
        mNetListenerPresenter=new NetListenerPresenter();

        addPresenter(mNetListenerPresenter);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.activity_main_login:
                intent = new Intent(this, LoginActivity.class);
                break;
            case R.id.activity_main_record:
                intent = new Intent(this, RecordActivity.class);
                break;
            case R.id.activity_main_book:
                intent = new Intent(this, BookActivity.class);
                break;
            case R.id.activity_book_update:
                intent = new Intent(this, BookIUActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("id", Long.parseLong(update.getText().toString()));
                break;
            case R.id.activity_book_add:
                intent = new Intent(this, BookIUActivity.class);
                intent.putExtra("type", 0);
                break;
            case R.id.activity_main_filter:
                intent=new Intent(this, FilterActivity.class);
                break;

        }
        startActivity(intent);
    }

    @Override
    public void onNetAvailable() {
        LogUtil.logD("有网了");
        startService(new Intent(this, NetRestartService.class));
    }

    @Override
    public void onNetUnavailable() {
        LogUtil.logD("断网了");
    }
}
