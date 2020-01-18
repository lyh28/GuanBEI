package com.lyh.guanbei.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyh.guanbei.R;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.Tag;

import java.util.List;

import androidx.annotation.NonNull;

public class BookLinearAdapter extends BaseQuickAdapter<Book, BaseViewHolder> {
    private long currentId;     //local
    private final String NORMAL_COLOR="#e98f36";
    private final String WHITE_COLOR="#FFFFFF";
    public BookLinearAdapter(long currentId,List<Book> data){
        super(R.layout.listitem_category_linear,data);
        this.currentId=currentId;
    }
    public long getCurrentId() {
        return currentId;
    }

    public void setCurrentId(long currentId) {
        this.currentId = currentId;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Book item) {
        if(currentId!=item.getLocal_id()){
            helper.setBackgroundRes(R.id.listitem_category_linear_view,R.drawable.corners_shape_normal_border);
            helper.setTextColor(R.id.listitem_category_linear_name, Color.parseColor(NORMAL_COLOR));
        }else{
            helper.setBackgroundRes(R.id.listitem_category_linear_view,R.drawable.corners_shape_normal_bg);
            helper.setTextColor(R.id.listitem_category_linear_name, Color.parseColor(WHITE_COLOR));
        }
        helper.setText(R.id.listitem_category_linear_name,item.getBook_name());
    }
}
