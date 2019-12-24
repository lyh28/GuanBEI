package com.lyh.guanbei.adapter;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyh.guanbei.R;
import com.lyh.guanbei.bean.CategoryBean;
import com.lyh.guanbei.bean.RecordBean;
import com.lyh.guanbei.manager.RecordCategoryManager;
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
        RecordBean recordBean=item.t;
        helper.setImageResource(R.id.listitem_record_icon, RecordCategoryManager.getIconByCategory(recordBean.getCategory(), CategoryBean.IN));
        helper.setText(R.id.listitem_record_amount,recordBean.getAmount());
        helper.setText(R.id.listitem_record_user,recordBean.getUser_id()+"");
        helper.setText(R.id.listitem_record_content,recordBean.getContent());
        helper.setText(R.id.listitem_record_date,recordBean.getTime());
    }
    public void setNewDatas(List<RecordBean> list){
        List<RecordSection> recordSectionList=new ArrayList<>();
        String date=null;
        for(RecordBean recordBean:list){
            String currDate=recordBean.getTime();
            if(!currDate.equals(date)){
                date=recordBean.getTime();
                recordSectionList.add(new RecordSection(true,date));
            }
            recordSectionList.add(new RecordSection(recordBean));
        }
        setNewData(recordSectionList);
    }
    private String getDayDate(String date){
        int index=date.charAt(' ');
        return date.substring(0,index);
    }

}
