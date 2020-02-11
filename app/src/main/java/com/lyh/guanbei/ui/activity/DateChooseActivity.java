package com.lyh.guanbei.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.util.DateUtil;
import com.lyh.guanbei.util.LogUtil;

public class DateChooseActivity extends BaseActivity implements View.OnClickListener,DatePicker.OnDateChangedListener, RadioGroup.OnCheckedChangeListener {
    private DatePicker mStartDate;
    private DatePicker mEndDate;
    private String mStartStr;
    private String mEndStr;
    private String type;
    private long bookId;
    private RadioGroup mRadioGroup;
    private boolean isOther;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_date_choose;
    }

    @Override
    protected void initUi() {
        mStartDate=findViewById(R.id.activity_date_choose_startdate);
        mEndDate=findViewById(R.id.activity_date_choose_enddate);
        mRadioGroup=findViewById(R.id.activity_date_choose_gradiogroup);
        findViewById(R.id.activity_date_choose_done).setOnClickListener(this);
        findViewById(R.id.page_back).setOnClickListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void init() {
        Intent intent=getIntent();
        mStartStr=intent.getStringExtra("startDate");
        mEndStr=intent.getStringExtra("endDate");
        type=intent.getStringExtra("type");
        bookId=intent.getLongExtra("bookId",-1);
        updateRadio();
        String[] date=splitDate(mStartStr);
        mStartDate.init(Integer.parseInt(date[0]),Integer.parseInt(date[1])-1,Integer.parseInt(date[2]),this);
        date=splitDate(mEndStr);
        mEndDate.init(Integer.parseInt(date[0]),Integer.parseInt(date[1])-1,Integer.parseInt(date[2]),this);
    }
    private void updateRadio(){
        switch (type){
            case "周":
                mRadioGroup.check(R.id.activity_date_choose_week);
                break;
            case "月":
                mRadioGroup.check(R.id.activity_date_choose_month);
                break;
            case "年":
                mRadioGroup.check(R.id.activity_date_choose_year);
                break;
            case "全部":
                mRadioGroup.check(R.id.activity_date_choose_all);
                break;
            case "其他":
                mRadioGroup.clearCheck();
                isOther=true;
                return;
        }
        isOther=false;
    }
    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String date=wrapDate(year,monthOfYear,dayOfMonth);
        if(view==mStartDate){
            mStartStr=date;
        }else if(view==mEndDate){
            mEndStr=date;
        }
        if(isOther)
            mRadioGroup.clearCheck();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.activity_date_choose_week:
                mStartStr=DateUtil.getDateBeforeDays(6);
                type="周";
                isOther=false;
                break;
            case R.id.activity_date_choose_month:
                mStartStr=DateUtil.getMonthFirstDay().split(" ")[0];
                type="月";
                isOther=false;
                break;
            case R.id.activity_date_choose_year:
                mStartStr=DateUtil.getYearFirstDay().split(" ")[0];
                type="年";
                isOther=false;
                break;
            case R.id.activity_date_choose_all:
                type="全部";
                isOther=false;
                mStartStr= Record.getFirstRecordDate(bookId).split(" ")[0];
                break;
                default:
                    type="其他";
                    isOther=true;
                    return ;
        }
        mEndStr=DateUtil.getNowDateTimeWithoutSecond().split(" ")[0];
        updateDate(mEndDate,mEndStr);
        updateDate(mStartDate,mStartStr);
        isOther=true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_date_choose_done:
                Intent intent=new Intent();
                intent.putExtra("startDate",mStartStr);
                intent.putExtra("endDate",mEndStr);
                intent.putExtra("type",type);
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.page_back:
                finish();
                break;
        }
    }

    private void updateDate(DatePicker datePicker, String dates){
        String[] date=splitDate(dates);
        datePicker.updateDate(Integer.parseInt(date[0]),Integer.parseInt(date[1])-1,Integer.parseInt(date[2]));
    }
    private String wrapDate(int year, int month, int day){
        return year+"-"+DateUtil.singleToDouble(month+1)+"-"+ DateUtil.singleToDouble(day);
    }
    private String[] splitDate(String date){
        return date.split("-");
    }

    @Override
    public void createPresenters() {

    }
}
