package com.lyh.guanbei.adapter;

import android.content.Context;
import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyh.guanbei.R;
import com.lyh.guanbei.bean.Tag;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CategoryLinearAdapter extends BaseQuickAdapter<Tag,BaseViewHolder> {
    private long currentId;     //local
    private final String NORMAL_COLOR="#e98f36";
    private final String WHITE_COLOR="#FFFFFF";
    public CategoryLinearAdapter(List<Tag> data){
        super(R.layout.listitem_category_linear,data);
        if(data==null||data.size()==0)
            currentId=-1;
        else
            currentId=data.get(0).getLocal_id();
    }

    public long getCurrentId() {
        return currentId;
    }

    public void setCurrentId(long currentId) {
        this.currentId = currentId;
        notifyDataSetChanged();
    }

    @Override
    public void setNewData(@Nullable List<Tag> data) {
        if(data==null||data.size()==0)  currentId=-1;
        else
            currentId=data.get(0).getLocal_id();
        super.setNewData(data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Tag item) {
        if(currentId!=item.getLocal_id()){
            helper.setBackgroundRes(R.id.listitem_category_linear_view,R.drawable.corners_shape_normal_border);
            helper.setTextColor(R.id.listitem_category_linear_name, Color.parseColor(NORMAL_COLOR));
        }else{
            helper.setBackgroundRes(R.id.listitem_category_linear_view,R.drawable.corners_shape_normal_bg);
            helper.setTextColor(R.id.listitem_category_linear_name, Color.parseColor(WHITE_COLOR));
        }
        helper.setText(R.id.listitem_category_linear_name,item.getName());
    }
}
