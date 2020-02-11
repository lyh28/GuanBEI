package com.lyh.guanbei.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.ui.widget.WaveBollView;
import com.lyh.guanbei.util.DateUtil;

import java.util.Date;

public class BudgetActivity extends BaseActivity  {
    private WaveBollView mWaveBollView;
    private TextView mDate;
    private TextView mOutSum;
    private TextView mBudget;
    private TextView mSum;      //剩余预算
    private TextView mAverage;  //每日可用
    private Book mBook;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_budget;
    }

    @Override
    protected void initUi() {
        mWaveBollView=findViewById(R.id.activity_budget_wavebollview);
        mDate=findViewById(R.id.activity_budget_date);
        mOutSum=findViewById(R.id.activity_budget_outSum);
        mBudget=findViewById(R.id.activity_budget_budget);
        mSum=findViewById(R.id.activity_budget_sum);
        mAverage=findViewById(R.id.activity_budget_averagesum);
    }

    @Override
    protected void init() {
        mBook=Book.queryByLocalId(CustomSharedPreferencesManager.getInstance().getCurrBookId());
        double outSum=mBook.getNow_out_sum();
        double budget=mBook.getMax_sum();
        mBudget.setText(budget+"");
        mOutSum.setText(outSum+"");
        double sum=budget-outSum;
        mSum.setText(sum+"");
        String[] str=new String[2];
        int year=DateUtil.getYear();
        int month=Integer.parseInt(DateUtil.getMonth());
        DateUtil.getDateRangeOfMonth(year,month,str);
        int days=DateUtil.getDaysOfMonth(year,month);
        double average=sum/(days-Integer.parseInt(DateUtil.getDay()));
        mAverage.setText(average+"");
        mWaveBollView.setPercent((int)((outSum/budget)*100));
    }

    @Override
    public void createPresenters() {

    }
}
