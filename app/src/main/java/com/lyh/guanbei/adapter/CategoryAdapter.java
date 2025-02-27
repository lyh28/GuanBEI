package com.lyh.guanbei.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyh.guanbei.R;
import com.lyh.guanbei.bean.Tag;

import java.util.List;

import androidx.annotation.NonNull;

public class CategoryAdapter extends BaseQuickAdapter<Tag, BaseViewHolder> {
    public CategoryAdapter(List<Tag> list) {
        super(R.layout.listitem_record_category,list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Tag item) {
        helper.setText(R.id.listitem_record_category_name,item.getName());
        helper.setImageResource(R.id.listitem_record_category_icon,item.getIconId());
    }
}
