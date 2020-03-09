package com.lyh.guanbei.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyh.guanbei.R;
import com.lyh.guanbei.adapter.NotificationAdapter;
import com.lyh.guanbei.adapter.NotificationItemEntity;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.Notification;
import com.lyh.guanbei.db.NotificationDao;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.manager.JPushCallbackManager;
import com.lyh.guanbei.util.LogUtil;

public class NotificationListActivity extends BaseActivity implements View.OnClickListener,BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private NotificationAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notification_list;
    }

    @Override
    protected void initUi() {
        mRecyclerView=findViewById(R.id.activity_notification_list_recyclerview);
        findViewById(R.id.activity_notification_list_back).setOnClickListener(this);
        findViewById(R.id.activity_notification_list_clear).setOnClickListener(this);
    }

    @Override
    protected void init() {
        mAdapter=new NotificationAdapter(this);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
    }
    private void refreshData(){
        mAdapter.setNewDatas(Notification.query(NotificationDao.Properties.UserId.eq(CustomSharedPreferencesManager.getInstance().getUser().getUser_id())));
    }
    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        NotificationItemEntity itemEntity=mAdapter.getItem(position);
        switch (view.getId()){
            case R.id.listitem_notification_check:
                JPushCallbackManager.onClick(this,itemEntity.getNotification().getData());
                break;
            case R.id.listitem_notification_reject:
                Notification notification=itemEntity.getNotification();
                notification.setStatus(-1);
                itemEntity.setItemType(NotificationItemEntity.DETAIL);
                itemEntity.setDetail(NotificationItemEntity.REJECT_DETAIL);
                mAdapter.notifyItemChanged(position);
                notification.save();
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        NotificationItemEntity itemEntity=mAdapter.getItem(position);
        if(itemEntity.getItemType()==NotificationItemEntity.ACTION){
            JPushCallbackManager.onClick(this,itemEntity.getNotification().getData());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_notification_list_back:
                finish();
                break;
            case R.id.activity_notification_list_clear:
                DBManager.getInstance().getDaoSession().getNotificationDao()
                        .deleteInTx(Notification.query(NotificationDao.Properties.UserId.eq(CustomSharedPreferencesManager.getInstance().getUser().getUser_id())));
                mAdapter.setNewDatas(null);
                break;
        }
    }

    @Override
    public void createPresenters() {

    }
}
