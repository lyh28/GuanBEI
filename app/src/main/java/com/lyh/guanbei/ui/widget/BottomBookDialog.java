package com.lyh.guanbei.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyh.guanbei.R;
import com.lyh.guanbei.adapter.BookAdapter;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BottomBookDialog extends Dialog {
    private Context context;
    private BookAdapter bookAdapter;
    private RecyclerView recyclerView;

    private List<Book> bookList;
    private long currBookId;
    private TextView mName;
    public BottomBookDialog(@NonNull Context context,TextView name) {
        super(context, R.style.CustomDialog);
        this.context=context;
        CustomSharedPreferencesManager customSharedPreferencesManager=CustomSharedPreferencesManager.getInstance(context);
        currBookId=customSharedPreferencesManager.getCurrBookId();
        bookList= Book.queryByUserId(context);
        mName=name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bottom_book);
        initWindowConfig();
        initView();
    }
    private void initWindowConfig(){
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        int height = (int) (0.7f * QMUIDisplayHelper.getScreenHeight(context));
        int width =  (int)(0.8f*QMUIDisplayHelper.getScreenWidth(context));
        layoutParams.width = width;
        layoutParams.height = height;
        layoutParams.gravity = Gravity.CENTER;
        getWindow().setAttributes(layoutParams);
        setCanceledOnTouchOutside(true);
    }
    private void initView(){
        recyclerView=findViewById(R.id.dialog_bottom_book_recyclerview);
        bookAdapter=new BookAdapter(currBookId);
        bookAdapter.setNewData(bookList);
        bookAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                bookAdapter.setCurrentBookId(bookList.get(position).getLocal_id());
                currBookId=bookAdapter.getCurrentBookId();
            }
        });
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setAdapter(bookAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void cancel() {
        mName.setText(Book.queryByLocalId(currBookId).getBook_name());
        super.cancel();
    }
    public long getCurrBookId(){
        return currBookId;
    }
}
