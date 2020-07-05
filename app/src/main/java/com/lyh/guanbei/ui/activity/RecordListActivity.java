package com.lyh.guanbei.ui.activity;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyh.guanbei.R;
import com.lyh.guanbei.adapter.RecordAdapter;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.Model;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.mvp.contract.InsertRecordContract;
import com.lyh.guanbei.mvp.presenter.InsertRecordPresenter;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.excel.RecordExcel;

import java.util.ArrayList;
import java.util.List;

public class RecordListActivity extends BaseActivity implements View.OnClickListener, BaseQuickAdapter.OnItemChildClickListener, InsertRecordContract.IInsertRecordView {
    private RecyclerView recyclerView;
    private RecordAdapter mAdapter;

    private String path;
    private List<Record> recordList;
    private int editPosition;
    private Model mModel;
    private long mBookLocalId;
    private InsertRecordPresenter mInsertRecordPresenter;
    private static final int EDIT_CODE = 2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_list;
    }

    @Override
    protected void initUi() {
        recyclerView = findViewById(R.id.activity_record_list_recyclerview);
        findViewById(R.id.activity_record_list_back).setOnClickListener(this);
        findViewById(R.id.activity_record_list_done).setOnClickListener(this);
    }

    @Override
    protected void init() {
        getData();
        recordList = new ArrayList<>();
        //初始化列表
        mAdapter = new RecordAdapter(this,true,false);
        mAdapter.setOnItemChildClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);

        recordList = parseExcel(path);
        mAdapter.setNewData(recordList);
    }

    private void getData() {
        Bundle bundle = getIntentData();
        path = bundle.getString("path", "");
        mModel = (Model) bundle.getSerializable("model");
        mBookLocalId = bundle.getLong("bookid");
    }

    private List<Record> parseExcel(String path) {
        LogUtil.logD("文件  "+path);
        CustomSharedPreferencesManager customSharedPreferencesManager = CustomSharedPreferencesManager.getInstance();
        RecordExcel recordExcel = new RecordExcel(mModel)
                .setBookLocalId(mBookLocalId).setUserId(customSharedPreferencesManager.getUser().getUser_id());
        return recordExcel.getRecordBean(path);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_record_list_back:
                finish();
                break;
            case R.id.activity_record_list_done:
                //保存
                mInsertRecordPresenter.insert(recordList);
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.listitem_record_delete:
                recordList.remove(position);
                mAdapter.remove(position);
                break;
            case R.id.listitem_record_edit:
                editPosition = position;
                Bundle bundle = new Bundle();
                bundle.putInt("status", AddByMyselfActivity.EDIT_STATUS);
                bundle.putParcelable("record", recordList.get(position));
                Intent intent = new Intent(this, AddByMyselfActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, EDIT_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_CODE) {
            if (resultCode == RESULT_OK) {
                Record record = (Record) data.getSerializableExtra("record");
                recordList.set(editPosition, record);
                Record r = mAdapter.getItem(editPosition);
                r = record;
                mAdapter.notifyItemChanged(editPosition);
            }
        }
    }

    @Override
    public void createPresenters() {
        mInsertRecordPresenter =new InsertRecordPresenter();
        addPresenter(mInsertRecordPresenter);
    }

    @Override
    public void onMessageError(String msg) {

    }
}
