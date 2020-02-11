package com.lyh.guanbei.adapter;

import android.content.Context;
import android.text.TextUtils;
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
    private boolean showEdit;
    private boolean showUser;       //true时用户名  false时towho
    public RecordAdapter(Context context) {
        super(R.layout.listitem_record);
        this.context = context;
        this.showEdit = true;
        showUser=true;
    }

    public RecordAdapter(Context context, boolean showEdit,boolean showUser) {
        super(R.layout.listitem_record);
        this.context = context;
        this.showEdit = showEdit;
        this.showUser=showUser;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Record item) {
        if (showEdit) {
            helper.setVisible(R.id.listitem_record_delete, true);
            helper.setVisible(R.id.listitem_record_edit, true);
        }
        //设置图标
        ImageView icon = helper.getView(R.id.listitem_record_icon);
        Glide.with(context).load(TagManager.getIconByCategory(item.getCategory(), item.getAmount_type())).into(icon);
        User user = User.queryById(item.getUser_id());
        if (user == null)
            helper.setText(R.id.listitem_record_user, item.getUser_id() + "");
        else
            helper.setText(R.id.listitem_record_user, showUser?user.getUser_name():item.getTowho());
        String amount = item.getAmount() + "";
        if (item.getAmount_type() == Tag.OUT && !amount.startsWith("-"))
            amount = "-" + amount;
        if (!TextUtils.isEmpty(item.getRemark()) && !"/".equals(item.getRemark()))
            helper.setText(R.id.listitem_record_category, item.getRemark());
        else
            helper.setText(R.id.listitem_record_category, item.getCategory());

        helper.setText(R.id.listitem_record_amount, amount);
        helper.setText(R.id.listitem_record_date, item.getDate());
        helper.addOnClickListener(R.id.listitem_record_delete, R.id.listitem_record_edit);
    }
}
