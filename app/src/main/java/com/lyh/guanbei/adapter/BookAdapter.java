package com.lyh.guanbei.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyh.guanbei.R;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.util.Util;

import androidx.annotation.NonNull;

public class BookAdapter extends BaseQuickAdapter<Book, BaseViewHolder> {
    private long currentBookId;
    private boolean isEditStatus;

    public BookAdapter(long currentBookId) {
        super(R.layout.listitem_book);
        this.currentBookId = currentBookId;
        isEditStatus=false;
    }
    @Override
    protected void convert(@NonNull BaseViewHolder helper,final Book item) {
        if (!isEditStatus) {
            helper.setGone(R.id.listitem_book_delete, false);
            helper.setVisible(R.id.listitem_book_edit, false);
        } else{
            helper.setVisible(R.id.listitem_book_delete, true);
            helper.setVisible(R.id.listitem_book_edit, true);
        }
        if (!isEditStatus)
            if (item.getLocal_id() == currentBookId) {
                helper.setVisible(R.id.listitem_book_label, true);
            } else {
                helper.setVisible(R.id.listitem_book_label, false);
            }
        else{
            helper.setGone(R.id.listitem_book_label, false);
        }
        helper.setText(R.id.listitem_book_person,wrapPersonCount(Util.getCountFormData(item.getPerson_id())+1));
        helper.setText(R.id.listitem_book_name,item.getBook_name());
        helper.setText(R.id.listitem_book_in,"收入");
        helper.setText(R.id.listitem_book_out,"支出");
        helper.addOnClickListener(R.id.listitem_book_delete,R.id.listitem_book_edit);
    }
    private String wrapPersonCount(int num){
        return "共"+num+"人";
    }
    public void setStatus(boolean isEditStatus) {
        this.isEditStatus = isEditStatus;
        notifyDataSetChanged();
    }
    public boolean getIsEditStatus(){
        return isEditStatus;
    }
    public void setCurrentBookId(long id) {
        currentBookId = id;
        notifyDataSetChanged();
    }
    public long getCurrentBookId(){
        return currentBookId;
    }
}
