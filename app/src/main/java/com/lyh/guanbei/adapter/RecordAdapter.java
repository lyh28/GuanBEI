package com.lyh.guanbei.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyh.guanbei.R;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.bean.Tag;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.manager.TagManager;
import com.lyh.guanbei.util.LogUtil;

import androidx.annotation.NonNull;

public class RecordAdapter extends BaseQuickAdapter<Record, BaseViewHolder> {
    private Context context;
    public RecordAdapter(Context context) {
        super(R.layout.listitem_record);
        this.context=context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Record item) {
        helper.setVisible(R.id.listitem_record_delete, true);
        helper.setVisible(R.id.listitem_record_edit, true);
        //设置图标
        ImageView icon=helper.getView(R.id.listitem_record_icon);
        Glide.with(context).load(TagManager.getIconByCategory(item.getCategory(), item.getAmount_type())).into(icon);
        helper.setText(R.id.listitem_record_category, item.getCategory());
        User user = User.queryById(item.getUser_id());
        if (user == null)
            helper.setText(R.id.listitem_record_user, item.getUser_id() + "");
        else
            helper.setText(R.id.listitem_record_user, user.getUser_name());
        String amount=item.getAmount();
        if(item.getAmount_type()==Tag.OUT&&!amount.startsWith("-"))
            amount="-"+amount;
        helper.setText(R.id.listitem_record_amount, amount);
        helper.setText(R.id.listitem_record_date, item.getDate());
        helper.addOnClickListener(R.id.listitem_record_delete, R.id.listitem_record_edit);
    }
}
