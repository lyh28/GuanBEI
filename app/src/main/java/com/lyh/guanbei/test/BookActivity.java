package com.lyh.guanbei.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.BookBean;
import com.lyh.guanbei.bean.DeleteBookBean;
import com.lyh.guanbei.common.Contact;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.mvp.contract.AddBookUserContract;
import com.lyh.guanbei.mvp.contract.DeleteBookContract;
import com.lyh.guanbei.mvp.contract.DeleteBookUserContract;
import com.lyh.guanbei.mvp.contract.QueryBookContract;
import com.lyh.guanbei.mvp.contract.UpdateBookContract;
import com.lyh.guanbei.mvp.presenter.AddBookUserPresenter;
import com.lyh.guanbei.mvp.presenter.DeleteBookPresenter;
import com.lyh.guanbei.mvp.presenter.DeleteBookUserPresenter;
import com.lyh.guanbei.mvp.presenter.QueryBookPresenter;
import com.lyh.guanbei.mvp.presenter.UpdateBookPresenter;
import com.lyh.guanbei.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends BaseActivity implements View.OnClickListener
        , QueryBookContract.IQueryBookView, UpdateBookContract.IUpdateBookView, DeleteBookContract.IDeleteBookView
        , AddBookUserContract.IAddBookUserView, DeleteBookUserContract.IDeleteBookUserView {
    private EditText query;
    private EditText delete;
    private EditText deleteUser;
    private EditText deleteUserBook;
    private EditText addUser;
    private EditText addUserBook;
    private EditText addUser1;
    private EditText addUserBook1;
    private EditText user;
    private EditText bookId;

    private QueryBookPresenter queryBookPresenter;
    private UpdateBookPresenter updateBookPresenter;
    private DeleteBookPresenter deleteBookPresenter;
    private DeleteBookUserPresenter deleteBookUserPresenter;
    private AddBookUserPresenter addBookUserPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_book;
    }

    @Override
    protected void initUi() {
        query = findViewById(R.id.activity_book_queryId);
        delete = findViewById(R.id.activity_book_deleteId);
        deleteUser = findViewById(R.id.activity_book_deleteUserId);
        deleteUserBook = findViewById(R.id.activity_book_deleteUserBookId);
        addUser = findViewById(R.id.activity_book_addUserId);
        addUserBook = findViewById(R.id.activity_book_addUserBookId);
        addUser1 = findViewById(R.id.activity_book_addUserId1);
        addUserBook1 = findViewById(R.id.activity_book_addUserBookId1);
        user = findViewById(R.id.activity_book_userId);
        bookId = findViewById(R.id.activity_book_bookId);



        findViewById(R.id.activity_book_query).setOnClickListener(this);
        findViewById(R.id.activity_book_delete).setOnClickListener(this);
        findViewById(R.id.activity_book_deleteUser).setOnClickListener(this);
        findViewById(R.id.activity_book_addUser).setOnClickListener(this);
        findViewById(R.id.activity_book_addUser1).setOnClickListener(this);

        findViewById(R.id.activity_book_changeManager).setOnClickListener(this);
        findViewById(R.id.activity_book_checkDB).setOnClickListener(this);
    }

    @Override
    protected void init() {

    }

    @Override
    public void createPresenters() {
        queryBookPresenter = new QueryBookPresenter();
        updateBookPresenter = new UpdateBookPresenter();
        deleteBookUserPresenter = new DeleteBookUserPresenter();
        deleteBookPresenter = new DeleteBookPresenter();
        addBookUserPresenter = new AddBookUserPresenter();

        addPresenter(queryBookPresenter);
        addPresenter(updateBookPresenter);
        addPresenter(deleteBookUserPresenter);
        addPresenter(deleteBookPresenter);
        addPresenter(addBookUserPresenter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_book_checkDB:
                LogUtil.logD("---------查询数据库  Book表------");
                List<BookBean> list = GuanBeiApplication.getDaoSession().getBookBeanDao().loadAll();
                for (BookBean b : list) {
                    LogUtil.logD(b.toString());
                }
                LogUtil.logD("---------------------------------");
                LogUtil.logD("---------查询数据库  Book删除表------");
                List<DeleteBookBean> list1 = GuanBeiApplication.getDaoSession().getDeleteBookBeanDao().loadAll();
                for (DeleteBookBean b : list1) {
                    LogUtil.logD(b.getBook_id() + "");
                }
                LogUtil.logD("---------------------------------");
                break;

            case R.id.activity_book_query:
                String queryStr = query.getText().toString();
                String[] arr = queryStr.split(Contact.SEPARATOR);
                List<Long> idList = new ArrayList<>(arr.length);
                for (String s : arr)
                    idList.add(Long.parseLong(s));
                queryBookPresenter.queryBook(idList);
                break;

            case R.id.activity_book_delete:
                BookBean bookBean =
                        GuanBeiApplication.getDaoSession().getBookBeanDao().load(Long.parseLong(delete.getText().toString()));
                deleteBookPresenter.deleteBook(bookBean);
                break;
            case R.id.activity_book_deleteUser:
                deleteBookUserPresenter.deleteBookUser(Long.parseLong(deleteUser.getText().toString()), Long.parseLong(deleteUserBook.getText().toString()));
                break;
            case R.id.activity_book_addUser:
                addBookUserPresenter.addUserRequest(Long.parseLong(addUser.getText().toString()), Long.parseLong(addUserBook.getText().toString()));
                break;
            case R.id.activity_book_addUser1:
                long uId=Long.parseLong(addUser1.getText().toString());
                long bId=Long.parseLong(addUserBook1.getText().toString());
                APIManager.addBookUser(uId, bId, new BaseObscriber<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        LogUtil.logD("添加成员成功");
                    }

                    @Override
                    protected void onFailed(String msg) {
                        LogUtil.logD("添加成员失败 "+msg);

                    }
                });
                break;
            case R.id.activity_book_changeManager:
                addBookUserPresenter.changeManager(Long.parseLong(user.getText().toString()), Long.parseLong(bookId.getText().toString()));
                break;
        }
    }

    @Override
    public void onChangeManagerSuccess() {
        LogUtil.logD("修改管理员成功");
    }

    @Override
    public void onAddUserRequestSuccess() {
        LogUtil.logD("添加用户成功");
    }

    @Override
    public void onNoAccount() {
        LogUtil.logD("没有此用户");

    }

    @Override
    public void onAddBookUserFailed(String msg) {
        LogUtil.logD("添加用户失败 " + msg);
    }

    @Override
    public void onDeleteBookUserSuccess(long userId) {
        LogUtil.logD("删除用户成功 " + userId);

    }

    @Override
    public void onDeleteBookUserFailed(String msg) {
        LogUtil.logD("删除用户失败 " + msg);

    }

    @Override
    public void showBook(List<BookBean> list) {
        LogUtil.logD("查询Book成功");
        for (BookBean bookBean : list)
            LogUtil.logD(bookBean.toString());
    }
    @Override
    public void onUpdateBookFailed(String msg) {
        LogUtil.logD("更新账本失败");
    }
}
