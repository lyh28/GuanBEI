package com.lyh.guanbei.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyh.guanbei.R;
import com.lyh.guanbei.bean.Model;

import androidx.annotation.NonNull;

public class ModelAdapter extends BaseQuickAdapter<Model,BaseViewHolder> {
    public ModelAdapter(){
        super(R.layout.listitem_model);
    }
    @Override
    protected void convert(@NonNull BaseViewHolder helper, Model item) {
        helper.setText(R.id.listitem_model_name,item.getName());
        helper.addOnClickListener(R.id.listitem_model_edit);
    }
}
