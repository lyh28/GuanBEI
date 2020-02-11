package com.lyh.guanbei.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lyh.guanbei.R;
import com.lyh.guanbei.adapter.RecordSectionAdapter;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.db.RecordDao;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecordListActivity1 extends BaseActivity {
    private TextView mTitle;
    private RecyclerView mRecyclerview;
    private RecordSectionAdapter mAdapter;
    private String mStartDate;
    private String mEndDate;
    private long mBookId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_list1;
    }

    @Override
    protected void initUi() {
        mTitle = findViewById(R.id.activity_record_list1_title);
        mRecyclerview = findViewById(R.id.activity_record_list1_recyclerview);
        findViewById(R.id.page_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void init() {
        Bundle bundle = getIntentData();
        mStartDate = bundle.getString("startDate");
        mEndDate = bundle.getString("endDate");
        mBookId = bundle.getLong("bookId");
        mTitle.setText(bundle.getString("title"));
        initList();
    }

    private void initList() {
        List<Record> records = Record.query(RecordDao.Properties.Book_local_id.eq(mBookId)
                , RecordDao.Properties.Date.between(mStartDate, mEndDate));
        mAdapter=new RecordSectionAdapter(this);
        mAdapter.setNewDatas(records);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setLayoutManager(layoutManager);
    }

    @Override
    public void createPresenters() {

    }
}
