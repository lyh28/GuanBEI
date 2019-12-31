package com.lyh.guanbei.adapter;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyh.guanbei.R;
import com.lyh.guanbei.bean.Tag;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.manager.TagManager;
import com.lyh.guanbei.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class RecordSectionAdapter extends BaseSectionQuickAdapter<RecordSection, BaseViewHolder> {
    public RecordSectionAdapter() {
        super(R.layout.listitem_record,R.layout.listitem_head_record,null);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, RecordSection item) {
        helper.setText(R.id.listitem_head_date,item.header);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RecordSection item) {
        Record record =item.t;
        helper.setImageResource(R.id.listitem_record_icon, TagManager.getIconByCategory(record.getCategory(), record.getAmount_type()));
        String amount=record.getAmount();
        if(record.getAmount_type()==Tag.OUT)
            amount="-"+amount;
        helper.setText(R.id.listitem_record_amount, amount);
        helper.setText(R.id.listitem_record_user, record.getUser_id()+"");
        helper.setText(R.id.listitem_record_content, record.getCategory());
        helper.setText(R.id.listitem_record_date, record.getDate());
    }
    public void setNewDatas(List<Record> list){
        List<RecordSection> recordSectionList=new ArrayList<>();
        String date=null;
        for(Record record :list){
            String currDate= record.getDate();
            if(!currDate.equals(date)){
                date= record.getDate();
                recordSectionList.add(new RecordSection(true,date));
            }
            recordSectionList.add(new RecordSection(record));
        }
        setNewData(recordSectionList);
    }
    private String getDayDate(String date){
        int index=date.charAt(' ');
        return date.substring(0,index);
    }

}
