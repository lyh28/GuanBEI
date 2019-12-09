package com.lyh.guanbei.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.BookBean;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.mvp.contract.InsertBookContract;
import com.lyh.guanbei.mvp.contract.UpdateBookContract;
import com.lyh.guanbei.mvp.presenter.AddBookUserPresenter;
import com.lyh.guanbei.mvp.presenter.InsertBookPresenter;
import com.lyh.guanbei.mvp.presenter.UpdateBookPresenter;
import com.lyh.guanbei.util.LogUtil;

public class BookIUActivity extends BaseActivity implements InsertBookContract.IInsertBookView, UpdateBookContract.IUpdateBookView {
    int type;   //0为添加 1为更新
    long id;

    private InsertBookPresenter insertBookPresenter;
    private UpdateBookPresenter updateBookPresenter;

    private TextView bookId;
    private EditText bookName;
    private EditText managerId;
    private EditText personId;
    private EditText maxSum;
    private EditText nowSum;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_iu;
    }

    @Override
    protected void initUi() {
        bookId=findViewById(R.id.activity_book_iu_bookId);
        bookName=findViewById(R.id.activity_book_iu_bookName);
        managerId=findViewById(R.id.activity_book_iu_managerId);
        personId=findViewById(R.id.activity_book_iu_personId);
        maxSum=findViewById(R.id.activity_book_iu_maxsum);
        nowSum=findViewById(R.id.activity_book_iu_nowsum);
        findViewById(R.id.activity_book_iu_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=bookName.getText().toString();
                String managerIdStr=managerId.getText().toString();
                String personIdStr=personId.getText().toString();
                String max=maxSum.getText().toString();
                String now=nowSum.getText().toString();
                BookBean bookBean;
                if(type==0)
                    bookBean=new BookBean();
                else
                    bookBean=GuanBeiApplication.getDaoSession().getBookBeanDao().load(id);
                bookBean.setBook_name(name);
                bookBean.setPerson_id(personIdStr);
                bookBean.setManager_id(Long.parseLong(managerIdStr));
                bookBean.setMax_sum(max);
                bookBean.setNow_sum(now);
                if(type==1){
                    updateBookPresenter.updateBook(bookBean);
                }else if(type==0){
                    insertBookPresenter.insert(bookBean);
                }
            }
        });
    }

    @Override
    protected void init() {
        Intent intent=getIntent();
        type=intent.getIntExtra("type",-1);
        id=intent.getLongExtra("id",0);
        if(type==1){
            BookBean bookBean= GuanBeiApplication.getDaoSession().getBookBeanDao().load(id);
            bookId.setText(id+"");
            bookName.setText(bookBean.getBook_name());
            managerId.setText(bookBean.getManager_id()+"");
            personId.setText(bookBean.getPerson_id());
            maxSum.setText(bookBean.getMax_sum());
            nowSum.setText(bookBean.getNow_sum());
        }
    }

    @Override
    public void createPresenters() {
        insertBookPresenter=new InsertBookPresenter();
        updateBookPresenter=new UpdateBookPresenter();

        addPresenter(insertBookPresenter);
        addPresenter(updateBookPresenter);
    }

    @Override
    public void onMessageError(String msg) {
        LogUtil.logD("出错 "+msg);
    }

    @Override
    public void onUpdateBookFailed(String msg) {
        LogUtil.logD(msg);
    }
}
