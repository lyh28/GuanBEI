package com.lyh.guanbei.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyh.guanbei.R;
import com.lyh.guanbei.adapter.ModelAdapter;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.Model;
import com.lyh.guanbei.db.ModelDao;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.manager.DBManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ModelActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView mRecyclerview;
    private ModelAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_model;
    }

    @Override
    protected void initUi() {
        mRecyclerview = findViewById(R.id.activity_model_recyclerview);
        findViewById(R.id.activity_model_back).setOnClickListener(this);
        findViewById(R.id.activity_model_new).setOnClickListener(this);
    }

    @Override
    protected void init() {
        //初始化列表
        mAdapter = new ModelAdapter();
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.listitem_model_edit:
                        Bundle bundle=new Bundle();
                        bundle.putInt("status",ModelDetailActivity.EDIT_STATUS);
                        bundle.putLong("modelid",mAdapter.getItem(position).getId());
                        startActivity(ModelDetailActivity.class,bundle);
                        break;
                }
            }
        });
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerview.setLayoutManager(layoutManager);
        mRecyclerview.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initList();
    }

    private void initList(){
        mAdapter.setNewData(Model.queryByUserId(CustomSharedPreferencesManager.getInstance().getUser().getUser_id()));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_model_back:
                finish();
                break;
            case R.id.activity_model_new:
                startActivity(ModelDetailActivity.class);
                break;
        }
    }



    @Override
    public void createPresenters() {

    }

}
