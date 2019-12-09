package com.lyh.guanbei.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.RecordBean;
import com.lyh.guanbei.mvp.contract.FilterRecordContract;
import com.lyh.guanbei.mvp.presenter.FilterRecordPresenter;
import com.lyh.guanbei.util.DateUtil;
import com.lyh.guanbei.util.LogUtil;

import java.util.List;

public class FilterActivity extends BaseActivity implements FilterRecordContract.IFilterRecordView {
    private FilterRecordPresenter mFilterRecordPresenter;
    private EditText start_date;
    private EditText end_date;
    private EditText category;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_filter;
    }

    @Override
    protected void initUi() {
        start_date = findViewById(R.id.activity_filter_start);
        end_date = findViewById(R.id.activity_filter_end);
        category = findViewById(R.id.activity_filter_category);

        start_date.setText(DateUtil.getMonthFirstDay());
        end_date.setText(DateUtil.getNowDateTime());
        findViewById(R.id.activity_filter_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start = start_date.getText().toString();
                String end = end_date.getText().toString();
                String categoryS = category.getText().toString();
                mFilterRecordPresenter.setmDateCondition(start, end);
                if (!categoryS.equals(""))
                    mFilterRecordPresenter.addContidion(FilterRecordPresenter.FilterRecord.CATEGORY_FILTER, categoryS);
                else
                    mFilterRecordPresenter.clearContidion(FilterRecordPresenter.FilterRecord.CATEGORY_FILTER);
                mFilterRecordPresenter.filterRecord();
            }
        });
    }

    @Override
    protected void init() {

    }

    @Override
    public void filterRecordShow(List<RecordBean> records) {
        LogUtil.logD("筛选出Record");
        for (RecordBean record : records)
            LogUtil.logD(record.toString());
    }

    @Override
    public void createPresenters() {
        mFilterRecordPresenter = new FilterRecordPresenter();

        addPresenter(mFilterRecordPresenter);
    }
}
