package com.lyh.guanbei.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyh.guanbei.R;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.util.Util;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BookAdapter extends BaseQuickAdapter<Book, BaseViewHolder> {
    private long currentBookId;
    private boolean isEditStatus;

    public BookAdapter(long currentBookId) {
        super(R.layout.listitem_book);
        this.currentBookId = currentBookId;
        isEditStatus = false;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, final Book item) {
        if (!isEditStatus) {
            helper.setGone(R.id.listitem_book_delete, false);
            helper.setVisible(R.id.listitem_book_edit, false);
            helper.setGone(R.id.listitem_book_out,true);
            helper.setGone(R.id.listitem_book_in,true);
            helper.setGone(R.id.listitem_book_person,true);
        } else {
            helper.setVisible(R.id.listitem_book_delete, true);
            helper.setVisible(R.id.listitem_book_edit, true);
            helper.setGone(R.id.listitem_book_out,false);
            helper.setGone(R.id.listitem_book_in,false);
            helper.setGone(R.id.listitem_book_person,false);
        }
        if (!isEditStatus)
            if (item.getLocal_id() == currentBookId) {
                helper.setVisible(R.id.listitem_book_label, true);
            } else {
                helper.setVisible(R.id.listitem_book_label, false);
            }
        else {
            helper.setGone(R.id.listitem_book_label, false);
        }
        helper.setText(R.id.listitem_book_person, wrapPersonCount(Util.getCountFormData(item.getPerson_id()) + 1));
        helper.setText(R.id.listitem_book_name, item.getBook_name());
        helper.setText(R.id.listitem_book_in, wrapInSum(item));
        helper.setText(R.id.listitem_book_out, wrapOutSum(item));
        helper.addOnClickListener(R.id.listitem_book_delete, R.id.listitem_book_edit);
    }

    private String wrapInSum(Book book) {
        return "收入 " + book.getIn_sum() + "元";
    }

    private String wrapOutSum(Book book) {
        return "支出 " + book.getOut_sum() + "元";
    }

    private String wrapPersonCount(int num) {
        return "共" + num + "人";
    }

    public void setStatus(boolean isEditStatus) {
        this.isEditStatus = isEditStatus;
        notifyDataSetChanged();
    }

    public boolean getIsEditStatus() {
        return isEditStatus;
    }

    public void setCurrentBookId(long id) {
        currentBookId = id;
        notifyDataSetChanged();
    }

    public long getCurrentBookId() {
        return currentBookId;
    }

    @Override
    public void setNewData(@Nullable List<Book> data) {
        if (data == null || data.size() == 0) {
            //插入空白页
        } else
            super.setNewData(data);
    }
}
