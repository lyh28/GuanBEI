package com.lyh.guanbei.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyh.guanbei.R;
import com.lyh.guanbei.adapter.UserAdapter;
import com.lyh.guanbei.adapter.UserLinearAdapter;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.BookBean;
import com.lyh.guanbei.bean.UserBean;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.mvp.contract.AddBookUserContract;
import com.lyh.guanbei.mvp.contract.QueryUserContract;
import com.lyh.guanbei.mvp.presenter.AddBookUserPresenter;
import com.lyh.guanbei.mvp.presenter.QueryUserPresenter;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.Util;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddUserActivity extends BaseActivity implements View.OnClickListener, AddBookUserContract.IAddBookUserView, QueryUserContract.IQueryUserView {
    private EditText mName;
    private UserLinearAdapter mUserAdapter;
    private RecyclerView mRecyclerView;

    private AddBookUserPresenter mAddBookUserPresenter;
    private QueryUserPresenter mQueryUserPresenter;
    private long mBookId;
    private BookBean mBook;
    private Map<Integer,UserBean> userMap;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_user;
    }

    @Override
    protected void initUi() {
        mName = findViewById(R.id.activity_add_user_phone);
        mRecyclerView = findViewById(R.id.activity_add_user_recyclerview);
        findViewById(R.id.activity_add_user_close).setOnClickListener(this);
        findViewById(R.id.activity_add_user_done).setOnClickListener(this);
        findViewById(R.id.activity_add_user_search).setOnClickListener(this);
    }

    @Override
    protected void init() {
        initData();
        mUserAdapter=new UserLinearAdapter(this);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mUserAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mUserAdapter.setChoose(position);
                if(mUserAdapter.isChoose(position)){
                    userMap.put(position,mUserAdapter.getItem(position));
                }else{
                    userMap.remove(position);
                }
            }
        });
        mRecyclerView.setAdapter(mUserAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void initData() {
        Bundle bundle=getIntentData();
        mBookId=bundle.getLong("bookId");
        mBook=BookBean.queryByBookId(mBookId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_add_user_close:
                finish();
                break;
            case R.id.activity_add_user_done:
                //得到剩余可以添加的用户
                if(userMap.size()==0){
                    showInfoDialog("未选择任何用户");
                    return ;
                }
                List<UserBean> resList=checkIsIn();
                if(resList.size()==0) {
                    showInfoDialog("该用户已被添加进账本中");
                    return ;
                }
                for(UserBean user:resList)
                mAddBookUserPresenter.addUserRequest(user.getUser_id(),mBookId);
                break;
            case R.id.activity_add_user_search:
                clearList();
                String str=mName.getText().toString();
                if(TextUtils.isEmpty(str))
                    return;
                //手机号
                if(str.length()==11)
                    mQueryUserPresenter.queryServer(str);
                mQueryUserPresenter.queryServer(Long.parseLong(str));
                break;
        }
    }
    private List<UserBean> checkIsIn(){
        List<UserBean> resList=new ArrayList<>();
        List<UserBean> list=new ArrayList<>(userMap.values());
        List<Long> userId= Util.getLongFromData(mBook.getPerson_id());
        for(UserBean user:list){
            if(user.getUser_id()==mBook.getManager_id()||userId.contains(user.getUser_id()))
                continue;
            resList.add(user);
        }
        return resList;
    }
    private void clearList(){
        userMap=new HashMap<>();
        mUserAdapter.setNewData(null);
    }

    @Override
    public void onChangeManagerSuccess() {

    }

    @Override
    public void onAddUserRequestSuccess() {

    }

    @Override
    public void onNoAccount() {
        showErrorDialog("您需要进行登录才能进行此操作");
    }

    @Override
    public void onAddBookUserFailed(String msg) {

    }
    private void showErrorDialog(String word) {
        final QMUITipDialog dialog=new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                .setTipWord(word)
                .create();
        dialog.show();
        mName.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        },1000);
    }
    private void showInfoDialog(String word){
        final QMUITipDialog dialog=new QMUITipDialog.Builder(this)
                .setTipWord(word)
                .create();
        dialog.show();
        mName.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        },1000);
    }
    @Override
    public void onQueryUserSuccess(UserBean user) {
        mUserAdapter.addData(user);
    }

    @Override
    public void onQueryUserSuccess(List<UserBean> userList) {
        mUserAdapter.addData(userList);
    }

    @Override
    public void onQueryUserError(String msg) {
        //显示错误界面
//        mUserAdapter.setEmptyView(R.layout.fragment_me_page,mRecyclerView);
    }

    @Override
    public void createPresenters() {
        mAddBookUserPresenter=new AddBookUserPresenter();
        mQueryUserPresenter=new QueryUserPresenter();
        addPresenter(mQueryUserPresenter);
        addPresenter(mAddBookUserPresenter);
    }
}
