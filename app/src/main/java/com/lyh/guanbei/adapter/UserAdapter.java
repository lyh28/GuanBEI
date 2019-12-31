package com.lyh.guanbei.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyh.guanbei.R;
import com.lyh.guanbei.bean.User;

import androidx.annotation.NonNull;

public class UserAdapter extends BaseMultiItemQuickAdapter<UserItemEntity, BaseViewHolder> {
    private Context mContext;
    public UserAdapter(Context context) {
        super(null);
        this.mContext=context;
        addItemType(UserItemEntity.NORMAL, R.layout.listitem_user);
        addItemType(UserItemEntity.ADD, R.layout.listitem_add_delete);
        addItemType(UserItemEntity.DELETE, R.layout.listitem_add_delete);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, UserItemEntity item) {
        switch (helper.getItemViewType()) {
            case UserItemEntity.NORMAL:
                User user=item.getUser();
                helper.setText(R.id.listitem_user_name,user.getUser_name());
                ImageView icon=helper.getView(R.id.listitem_user_icon);
                Glide.with(mContext).load(user.getUser_icon()).placeholder(R.drawable.defaulticon).error(R.drawable.defaulticon).into(icon);
                break;
            case UserItemEntity.ADD:
                ImageView imgAdd=helper.getView(R.id.listitem_add_delete_img);
                Glide.with(mContext).load(R.drawable.add_circle_gray).into(imgAdd);
                break;
            case UserItemEntity.DELETE:
                ImageView imgDel=helper.getView(R.id.listitem_add_delete_img);
                Glide.with(mContext).load(R.drawable.delete_circle_gray).into(imgDel);
                break;
        }
    }
}
