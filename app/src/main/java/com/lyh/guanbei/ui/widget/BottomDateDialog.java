package com.lyh.guanbei.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.lyh.guanbei.R;
import com.lyh.guanbei.common.Contact;
import com.lyh.guanbei.util.DateUtil;
import com.lyh.guanbei.util.LogUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;

public class BottomDateDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private onDoneListener mListener;

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinutes;
    private Calendar mCalendar;
    public BottomDateDialog(@NonNull Context context,Calendar calendar) {
        super(context, R.style.CustomDialog);
        this.context = context;
        if(calendar==null)
            mCalendar=Calendar.getInstance();
        else
            mCalendar=calendar;
        mYear=mCalendar.get(Calendar.YEAR);
        mMonth=mCalendar.get(Calendar.MONTH);
        mDay=mCalendar.get(Calendar.DAY_OF_MONTH);
        mHour=mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinutes=mCalendar.get(Calendar.MINUTE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bottom_date);
        initWindowConfig();
        initView();
        initData();
    }

    private void initWindowConfig() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        int width = QMUIDisplayHelper.getScreenWidth(context);
        layoutParams.width = width;
        layoutParams.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(layoutParams);
        setCanceledOnTouchOutside(true);
    }

    private void initView() {
        mDatePicker = findViewById(R.id.datepicker);
        mTimePicker = findViewById(R.id.timepicker);
        mTimePicker.setIs24HourView(true);
        findViewById(R.id.dialog_bottom_date_cancel).setOnClickListener(this);
        findViewById(R.id.dialog_bottom_date_done).setOnClickListener(this);
        resizePikcer(mDatePicker);
        resizePikcer(mTimePicker);
    }
    private void initData(){
        mDatePicker.init(mYear,mMonth,mDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear=year;
                mMonth=monthOfYear;
                mDay=dayOfMonth;
            }
        });
        mTimePicker.setCurrentHour(mHour);
        mTimePicker.setCurrentMinute(mMinutes);
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mHour=hourOfDay;
                mMinutes=minute;
            }
        });
    }
    private void resizePikcer(FrameLayout tp) {
        List<NumberPicker> npList = findNumberPicker(tp);
        for (NumberPicker np : npList) {
            resizeNumberPicker(np);
        }
    }

    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList<>();
        View child = null;
        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout) {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }
        return npList;
    }
    private void resizeNumberPicker(NumberPicker np) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 0, 10, 0);
        np.setLayoutParams(params);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_bottom_date_cancel:
                dismiss();
                break;
            case R.id.dialog_bottom_date_done:
                if (mListener != null) {
                    mListener.onDone(wrap());
                }
                dismiss();
                break;
        }
    }
    private String wrap(){
        return mYear+Contact.SEPARATOR+DateUtil.singleToDouble(mMonth+1)+Contact.SEPARATOR+DateUtil.singleToDouble(mDay)
                +" "+DateUtil.singleToDouble(mHour)+":"+DateUtil.singleToDouble(mMinutes);
    }
    public BottomDateDialog setDoneListener(onDoneListener mListener) {
        this.mListener = mListener;
        return this;
    }

    public interface onDoneListener {
        void onDone(String dateAndTime);
    }
}
