package com.lyh.guanbei.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.mvp.contract.AddBookUserContract;
import com.lyh.guanbei.mvp.contract.QueryBookContract;
import com.lyh.guanbei.mvp.contract.QueryUserContract;
import com.lyh.guanbei.mvp.presenter.AddBookUserPresenter;
import com.lyh.guanbei.mvp.presenter.QueryBookPresenter;
import com.lyh.guanbei.mvp.presenter.QueryUserPresenter;
import com.lyh.guanbei.util.Util;

import java.util.List;

public class BookDetailAddUserActivity extends BaseActivity implements View.OnClickListener, QueryUserContract.IQueryUserView, QueryBookContract.IQueryBookView , AddBookUserContract.IAddBookUserView {
    private TextView mBookName;
    private ImageView mManagerIcon;
    private TextView mManagerName;
    private TextView mPerson;
    private ImageView mRequestIcon;
    private TextView mRequestName;

    private QueryBookPresenter mQueryBookPresenter;
    private QueryUserPresenter mQueryUserPresenter;
    private AddBookUserPresenter mAddBookUserPresenter;

    private long mBookId;
    private long mRequestId;
    private Book mBook;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_detail_add_user;
    }

    @Override
    protected void initUi() {
        mBookName=findViewById(R.id.activity_book_detail_add_user_bookname);
        mManagerIcon=findViewById(R.id.activity_book_detail_add_user_manager_icon);
        mManagerName=findViewById(R.id.activity_book_detail_add_user_manager);
        mPerson=findViewById(R.id.activity_book_detail_add_user_preson);
        mRequestIcon=findViewById(R.id.activity_book_detail_add_user_request_icon);
        mRequestName=findViewById(R.id.activity_book_detail_add_user_request_name);
        findViewById(R.id.activity_book_detail_add_user_back).setOnClickListener(this);
        findViewById(R.id.activity_book_detail_add_user_preson_view).setOnClickListener(this);
        findViewById(R.id.activity_book_detail_add_user_btn).setOnClickListener(this);
    }

    @Override
    protected void init() {
        Bundle bundle=getIntentData();
        mBookId = bundle.getLong("bookId");
        mRequestId=bundle.getLong("requestId");
        initData();
    }
    private void initData(){
        mQueryBookPresenter.queryBookServer(mBookId);
        mQueryUserPresenter.queryServer(mRequestId);
    }
    @Override
    public void showBook(List<Book> list) {
        if(list.size()!=0)
            mBook=list.get(0);
        mPerson.setText(wrapPersonNum(Util.getLongFromData(mBook.getPerson_id()).size()));
        mBookName.setText(mBook.getBook_name());
        mQueryUserPresenter.queryServer(mBook.getManager_id());
    }
    private String wrapPersonNum(int num) {
        if (num == 0)
            return "暂无成员";
        else
            return "共" + num + "个成员";
    }

    @Override
    public void onQueryUserSuccess(User user) {

    }

    @Override
    public void onQueryUserSuccess(List<User> userList) {
        User user;
        if((user= User.queryById(mRequestId))!=null){
            mRequestName.setText(user.getUser_name());
            Glide.with(this).load(user.getUser_icon()).error(R.drawable.defaulticon).into(mRequestIcon);
        }
        if((user= User.queryById(mBook.getManager_id()))!=null){
            mManagerName.setText(user.getUser_name());
            Glide.with(this).load(user.getUser_icon()).error(R.drawable.defaulticon).into(mManagerIcon);
        }
    }
    @Override
    public void onQueryUserError(String msg) {
    }
    @Override
    public void onAddBookUserRequestSuccess() {
    }
    @Override
    public void onNoAccount() {
    }
    @Override
    public void onAddBookUserRequestFailed(String msg) {
    }
    @Override
    public void onAddBookUserSuccess() {
        Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onAddBookUserFailed(String msg) {
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_book_detail_add_user_back:
                finish();
                break;
            case R.id.activity_book_detail_add_user_preson_view:
                //封装数据
                Bundle bundle = new Bundle();
                bundle.putString("memberId", mBook.getPerson_id());
                bundle.putBoolean("isManager", mBook.getManager_id() == CustomSharedPreferencesManager.getInstance(this).getUser().getUser_id());
                bundle.putLong("bookId", mBook.getLocal_id());
                startActivity(MemberActivity.class, bundle);
                break;
            case R.id.activity_book_detail_add_user_btn:
                mAddBookUserPresenter.addUser(mBookId);
                finish();
                break;
        }
    }

    @Override
    public void createPresenters() {
        mQueryUserPresenter=new QueryUserPresenter();
        mQueryBookPresenter=new QueryBookPresenter();
        mAddBookUserPresenter=new AddBookUserPresenter();
        addPresenter(mAddBookUserPresenter);
        addPresenter(mQueryBookPresenter);
        addPresenter(mQueryUserPresenter);
    }
}
