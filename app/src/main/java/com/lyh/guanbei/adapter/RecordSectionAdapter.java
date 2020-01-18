package com.lyh.guanbei.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyh.guanbei.R;
import com.lyh.guanbei.bean.Tag;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.manager.TagManager;
import com.lyh.guanbei.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class RecordSectionAdapter extends BaseSectionQuickAdapter<RecordSection, BaseViewHolder> {
    private Context context;
    public RecordSectionAdapter(Context context) {
        super(R.layout.listitem_record,R.layout.listitem_head_record,null);
        this.context=context;
    }

    @Override
    protected void convertHead(BaseViewHolder helper, RecordSection item) {
        helper.setText(R.id.listitem_head_date,item.header);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RecordSection item) {
        Record record =item.t;
        ImageView icon=helper.getView(R.id.listitem_record_icon);
        Glide.with(context).load(TagManager.getIconByCategory(record.getCategory(), record.getAmount_type())).into(icon);
        String amount=record.getAmount();
        if(record.getAmount_type()==Tag.OUT&&!amount.startsWith("-"))
            amount="-"+amount;
        helper.setText(R.id.listitem_record_amount, amount);
        User user = User.queryById(record.getUser_id());
        if (user == null)
            helper.setText(R.id.listitem_record_user, record.getUser_id()+"");
        else
            helper.setText(R.id.listitem_record_user, user.getUser_name());
        helper.setText(R.id.listitem_record_category, record.getCategory());
        helper.setText(R.id.listitem_record_date, record.getDate());
    }
    public void setNewDatas(List<Record> list){
        List<RecordSection> recordSectionList=new ArrayList<>();
        String date=null;
        for(Record record :list){
            String dateDate=getDayDate(record.getDate());
            if(!dateDate.equals(date)){
                date= dateDate;
                recordSectionList.add(new RecordSection(true,date));
            }
            recordSectionList.add(new RecordSection(record));
        }
        setNewData(recordSectionList);
    }
    private String getDayDate(String date){
        return date.split(" ")[0];
    }

}
