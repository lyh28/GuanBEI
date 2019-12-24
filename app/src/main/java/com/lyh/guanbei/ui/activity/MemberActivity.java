package com.lyh.guanbei.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyh.guanbei.R;
import com.lyh.guanbei.adapter.UserAdapter;
import com.lyh.guanbei.adapter.UserItemEntity;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.UserBean;
import com.lyh.guanbei.mvp.contract.QueryUserContract;
import com.lyh.guanbei.mvp.presenter.QueryUserPresenter;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MemberActivity extends BaseActivity implements QueryUserContract.IQueryUserView {
    private QueryUserPresenter mQueryUserPresenter;
    private UserAdapter mUserAdapter;
    private RecyclerView mRecyclerView;
    private ImageView mBack;
    private TextView mTitle;

    private long bookId;
    private boolean isManager;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_member;
    }

    @Override
    protected void initUi() {
        mRecyclerView=findViewById(R.id.activity_member_recyclerview);
        mBack=findViewById(R.id.activity_member_close);
        mTitle=findViewById(R.id.activity_member_title);
    }

    @Override
    protected void init() {
        //设置列表
        mUserAdapter=new UserAdapter(this);
        mUserAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int type=adapter.getItemViewType(position);
                if(type==UserItemEntity.ADD){
                    Bundle bundle=new Bundle();
                    bundle.putLong("bookId",bookId);
                    startActivity(AddUserActivity.class,bundle);
                }else if(type==UserItemEntity.DELETE){
                    Toast.makeText(MemberActivity.this,"删除成员",Toast.LENGTH_SHORT).show();
                }
            }
        });
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,5);
        mRecyclerView.setAdapter(mUserAdapter);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        ///得到Activity跳转的数据
        Bundle bundle=getIntentData();
        String memberId=bundle.getString("memberId","");
        isManager=bundle.getBoolean("isManager",false);
        bookId=bundle.getLong("bookId");
        if(TextUtils.isEmpty(memberId)){
            List<UserItemEntity> list=new ArrayList<>();
            list.add(new UserItemEntity(null,UserItemEntity.ADD));
            mUserAdapter.setNewData(list);
        }else {
            //获取数据
            mQueryUserPresenter.query(Util.getLongFromData(memberId));
        }
    }

    @Override
    public void onQueryUserSuccess(UserBean user) {
    }

    @Override
    public void onQueryUserSuccess(List<UserBean> userList) {
        int n=isManager?2:0;
        List<UserItemEntity> list=new ArrayList<>(userList.size()+n);
        for(UserBean user:userList){
            list.add(new UserItemEntity(user,UserItemEntity.NORMAL));
        }
        if(isManager){
            list.add(new UserItemEntity(null,UserItemEntity.ADD));
            list.add(new UserItemEntity(null,UserItemEntity.DELETE));
        }
        mUserAdapter.setNewData(list);
    }

    @Override
    public void onQueryUserError(String msg) {
        LogUtil.logD("查询用户出错 "+msg);
    }

    @Override
    public void createPresenters() {
        mQueryUserPresenter=new QueryUserPresenter();

        addPresenter(mQueryUserPresenter);
    }
}
