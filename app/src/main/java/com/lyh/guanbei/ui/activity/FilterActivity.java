package com.lyh.guanbei.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyh.guanbei.R;
import com.lyh.guanbei.adapter.RecordAdapter;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;

import java.util.List;

public class FilterActivity extends BaseActivity implements View.OnClickListener {
    private EditText mKey;
    private RecyclerView mRecyclerview;
    private RecordAdapter mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_filter;
    }

    @Override
    protected void initUi() {
        mKey=findViewById(R.id.activity_filter_key);
        mRecyclerview=findViewById(R.id.activity_filter_recyclerview);
        findViewById(R.id.page_back).setOnClickListener(this);
        findViewById(R.id.activity_filter_search).setOnClickListener(this);

    }

    @Override
    protected void init() {
        initList();
    }
    private void initList(){
        mAdapter=new RecordAdapter(this,false,true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setLayoutManager(layoutManager);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_filter_search:
                long id= CustomSharedPreferencesManager.getInstance().getUser().getUser_id();
                mAdapter.setNewData(Record.queryByKey(mKey.getText().toString(),id));
                break;
            case R.id.page_back:
                finish();
                break;
        }
    }
    @Override
    public void createPresenters() {

    }
}
