package com.lyh.guanbei.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyh.guanbei.R;
import com.lyh.guanbei.bean.UserBean;

import java.util.Collection;

import androidx.annotation.NonNull;

public class UserLinearAdapter extends BaseQuickAdapter<UserBean, BaseViewHolder> {
    private boolean[] isChoose;
    private Context mContext;
    public UserLinearAdapter(Context context){
        super(R.layout.listitem_user_linear);
        this.mContext=context;
    }
    @Override
    protected void convert(@NonNull BaseViewHolder helper, UserBean item) {
        int position=helper.getPosition();
        ImageView icon=helper.getView(R.id.listitem_user_linear_icon);
        Glide.with(mContext).load(item.getUser_icon()).error(R.drawable.defaulticon).into(icon);
        helper.setText(R.id.listitem_user_linear_name,item.getUser_name());
        ImageView choose=helper.getView(R.id.listitem_user_linear_choose);
        if(isChoose[position])
            Glide.with(mContext).load(R.drawable.circle_choose).into(choose);
        else
            Glide.with(mContext).load(R.drawable.circle_unchoose).into(choose);
    }

    @Override
    public void addData(@NonNull UserBean data) {
        initNewChoose(1);
        super.addData(data);
    }

    @Override
    public void addData(@NonNull Collection<? extends UserBean> newData) {
        initNewChoose(newData.size());
        super.addData(newData);
    }
    private void initNewChoose(int n){
        if(isChoose==null){
            isChoose=new boolean[n];
        }
        boolean[] tmp=new boolean[isChoose.length+n];
        System.arraycopy(isChoose,0,tmp,0,isChoose.length);
        isChoose=tmp;
    }
    public void setChoose(int index){
        isChoose[index]=!isChoose[index];
        notifyItemChanged(index);
    }
    public boolean isChoose(int index){
        return isChoose[index];
    }
}
