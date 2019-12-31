package com.lyh.guanbei.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyh.guanbei.R;
import com.lyh.guanbei.bean.SMS;

import androidx.annotation.NonNull;

public class SMSAdapter extends BaseQuickAdapter<SMS, BaseViewHolder> {
    public SMSAdapter() {
        super(R.layout.listitem_sms);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SMS item) {
        if (item.isChoose())
            helper.setImageResource(R.id.listitem_sms_choose, R.drawable.circle_choose);
        else
            helper.setImageResource(R.id.listitem_sms_choose, R.drawable.circle_unchoose);
        helper.setText(R.id.listitem_sms_name,item.getName());
        helper.setText(R.id.listitem_sms_date,item.getDate());
        helper.setText(R.id.listitem_sms_content,item.getContent());
    }
}
