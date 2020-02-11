package com.lyh.guanbei.ui.activity;

import android.app.TimePickerDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TimePicker;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.Setting;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.ui.widget.BottomDateDialog;
import com.lyh.guanbei.util.DateUtil;
import com.lyh.guanbei.util.LogUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.util.Calendar;

import cn.jpush.android.api.JPushInterface;

public class NoDisturbActivity extends BaseActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    private QMUIGroupListView mGroupListView;

    private QMUICommonListItemView mDisturbStartTime;
    private QMUICommonListItemView mDisturbEndTime;
    private Setting mSetting;
    private int currentType;    //最近类型  是开始时间还是结束时间

    @Override
    protected int getLayoutId() {
        return R.layout.activity_nodisturb;
    }

    @Override
    protected void initUi() {
        mGroupListView = findViewById(R.id.activity_notify_set_grouplistview);
    }

    @Override
    protected void init() {
        initData();
        initListView();
    }

    private void initData() {
        mSetting = CustomSharedPreferencesManager.getInstance().getUser().getSetting();
    }

    private void initListView() {
        //title:通知开关
        int paddingVer = QMUIDisplayHelper.dp2px(this, 15);
        //title:勿扰模式
        QMUICommonListItemView mNoDisturb = mGroupListView.createItemView(null
                , "勿扰模式"
                , "开启后，在设定时间段内收到新消息时不会响铃或振动"
                , QMUICommonListItemView.VERTICAL
                , QMUICommonListItemView.ACCESSORY_TYPE_SWITCH
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        mNoDisturb.setPadding(mNoDisturb.getPaddingLeft(), paddingVer, mNoDisturb.getPaddingRight(), paddingVer);
        mDisturbStartTime = mGroupListView.createItemView(null
                , "开始时间"
                , DateUtil.getDayTime(mSetting.getNodisturb_start_date())
                , QMUICommonListItemView.HORIZONTAL
                , QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        mDisturbStartTime.setPadding(mDisturbStartTime.getPaddingLeft(), paddingVer, mDisturbStartTime.getPaddingRight(), paddingVer);
        mDisturbEndTime = mGroupListView.createItemView(null
                , "结束时间"
                , DateUtil.getDayTime(mSetting.getNodisturb_end_date())
                , QMUICommonListItemView.HORIZONTAL
                , QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        mDisturbEndTime.setPadding(mDisturbEndTime.getPaddingLeft(), paddingVer, mDisturbEndTime.getPaddingRight(), paddingVer);
        mNoDisturb.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LogUtil.logD("check "+isChecked);
                if (isChecked) {
                    mDisturbStartTime.setVisibility(View.VISIBLE);
                    mDisturbEndTime.setVisibility(View.VISIBLE);
                    mSetting.setNodisturb(true);
                } else {
                    mDisturbStartTime.setVisibility(View.GONE);
                    mDisturbEndTime.setVisibility(View.GONE);
                    mSetting.setNodisturb(false);
                }
            }
        });
        mNoDisturb.getSwitch().setChecked(mSetting.getNodisturb());
        if(!mSetting.getNodisturb()){
            mDisturbStartTime.setVisibility(View.GONE);
            mDisturbEndTime.setVisibility(View.GONE);
        }
        mGroupListView.newSection(this)
                .setTitle("勿扰模式")
                .addItemView(mNoDisturb, this)
                .addItemView(mDisturbStartTime, this)
                .addItemView(mDisturbEndTime, this)
                .setMiddleSeparatorInset(QMUIDisplayHelper.dp2px(this, 20), 0)
                .addTo(mGroupListView);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String date = DateUtil.singleToDouble(hourOfDay) + ":" + DateUtil.singleToDouble(minute);
        if (currentType == 1) {
            mSetting.setNodisturb_start_date(date);
            mDisturbStartTime.setDetailText(DateUtil.getDayTime(date));
        } else {
            mSetting.setNodisturb_end_date(date);
            mDisturbEndTime.setDetailText(DateUtil.getDayTime(date));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSetting.save();
        //设置静默时间
        JPushInterface.setSilenceTime(this, getHFromDate(mSetting.getNodisturb_start_date()), getMFromDate(mSetting.getNodisturb_start_date())
                , getHFromDate(mSetting.getNodisturb_end_date()), getMFromDate(mSetting.getNodisturb_end_date()));
    }

    @Override
    public void onClick(View v) {
        if (v instanceof QMUICommonListItemView) {
            QMUICommonListItemView mView = (QMUICommonListItemView) v;
            switch (mView.getText().toString()) {
                case "勿扰模式":
                    mView.getSwitch().toggle();
                    break;
                case "开始时间":
                    currentType = 1;
                    new TimePickerDialog(this, TimePickerDialog.THEME_HOLO_LIGHT, this
                            , getHFromDate(mSetting.getNodisturb_start_date()), getMFromDate(mSetting.getNodisturb_start_date()), true).show();
                    break;
                case "结束时间":
                    currentType = 2;
                    new TimePickerDialog(this, TimePickerDialog.THEME_HOLO_LIGHT, this
                            , getHFromDate(mSetting.getNodisturb_end_date()), getMFromDate(mSetting.getNodisturb_end_date()), true).show();
                    break;
            }
            return;
        }
        switch (v.getId()) {

        }
    }

    private int getMFromDate(String date) {
        return Integer.parseInt(date.split(":")[1]);
    }

    private int getHFromDate(String date) {
        return Integer.parseInt(date.split(":")[0]);
    }

    @Override
    public void createPresenters() {

    }
}
