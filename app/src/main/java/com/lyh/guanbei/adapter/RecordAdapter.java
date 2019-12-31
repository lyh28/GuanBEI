package com.lyh.guanbei.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyh.guanbei.R;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.bean.Tag;
import com.lyh.guanbei.manager.TagManager;

import androidx.annotation.NonNull;

public class RecordAdapter extends BaseQuickAdapter<Record, BaseViewHolder> {
    public RecordAdapter(){
        super(R.layout.listitem_record);
    }
    @Override
    protected void convert(@NonNull BaseViewHolder helper, Record item) {
        helper.setVisible(R.id.listitem_record_delete,true);
        helper.setVisible(R.id.listitem_record_edit,true);
        //设置图标
        helper.setImageResource(R.id.listitem_record_icon, TagManager.getIconByCategory(item.getCategory(), Tag.IN));
        helper.setText(R.id.listitem_record_content,item.getCategory());
        helper.setText(R.id.listitem_record_user,item.getUser_id()+"");
        helper.setText(R.id.listitem_record_amount,item.getAmount());
        helper.setText(R.id.listitem_record_date,item.getDate());
        helper.addOnClickListener(R.id.listitem_record_delete,R.id.listitem_record_edit);
    }
}
