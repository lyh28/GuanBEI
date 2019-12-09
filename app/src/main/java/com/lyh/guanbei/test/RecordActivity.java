package com.lyh.guanbei.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.DeleteRecordBean;
import com.lyh.guanbei.bean.RecordBean;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.db.RecordBeanDao;
import com.lyh.guanbei.mvp.contract.DeleteRecordContract;
import com.lyh.guanbei.mvp.contract.QueryRecordContract;
import com.lyh.guanbei.mvp.model.QueryRecordModel;
import com.lyh.guanbei.mvp.presenter.DeleteRecordPresenter;
import com.lyh.guanbei.mvp.presenter.QueryRecordPresenter;
import com.lyh.guanbei.util.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecordActivity extends BaseActivity implements QueryRecordContract.IQueryRecordView, DeleteRecordContract.IDeleteRecordView {
    private QueryRecordPresenter mQueryRecordPresenter;
    private DeleteRecordPresenter mDeleteRecordPresenter;
    private EditText updateId;
    private EditText deleteId;
    private EditText queryUserId;
    private EditText queryBookId;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_record;
    }

    @Override
    protected void initUi() {
        findViewById(R.id.activity_record_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RecordActivity.this,RecordIUActivity.class);
                intent.putExtra("type","add");
                startActivity(intent);
            }
        });
        updateId=findViewById(R.id.activity_record_updateid);
        findViewById(R.id.activity_record_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RecordActivity.this,RecordIUActivity.class);
                intent.putExtra("type","update");
                intent.putExtra("id",updateId.getText().toString());
                startActivity(intent);
            }
        });
        deleteId=findViewById(R.id.activity_record_deleteid);
        findViewById(R.id.activity_record_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] idstr=deleteId.getText().toString().split("-");
                mDeleteRecordPresenter.delete(RecordBean.query(RecordBeanDao.Properties.Record_id.in(Arrays.asList(idstr))));
            }
        });
        queryUserId=findViewById(R.id.activity_record_query_userid);
        findViewById(R.id.activity_record_query_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] idstr=queryUserId.getText().toString().split("-");
                List<Long> idList=new ArrayList<>();
                for(String s:idstr)
                    idList.add(Long.parseLong(s));
                mQueryRecordPresenter.queryRecordById(QueryRecordModel.USERID,idList);
            }
        });
        queryBookId=findViewById(R.id.activity_record_query_bookid);
        findViewById(R.id.activity_record_query_book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] idstr=queryBookId.getText().toString().split("-");
                List<Long> idList=new ArrayList<>();
                for(String s:idstr)
                    idList.add(Long.parseLong(s));
                mQueryRecordPresenter.queryRecordById(QueryRecordModel.BOOKID,idList);
            }
        });
        findViewById(R.id.activity_record_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatabase();
            }
        });
    }

    @Override
    protected void init() {

    }

    @Override
    public void createPresenters() {
        mQueryRecordPresenter=new QueryRecordPresenter();
        mDeleteRecordPresenter=new DeleteRecordPresenter();
        addPresenter(mQueryRecordPresenter);
        addPresenter(mDeleteRecordPresenter);
    }

    @Override
    public void onQueryRecordSuccess(List<RecordBean> recordBeans) {
        LogUtil.logD("查询成功");
        for(RecordBean r:recordBeans)
            LogUtil.logD(r.toString());
    }

    @Override
    public void onQueryRecordFailed(String msg) {
        LogUtil.logD("查询失败"+msg);
    }

    @Override
    public void startLoading() {
        LogUtil.logD("开始动画");
    }

    @Override
    public void endLoading() {
        LogUtil.logD("结束动画");
    }
    public void showDatabase(){
        LogUtil.logD("---------------查看数据表--------------");
        List<RecordBean> list= GuanBeiApplication.getDaoSession().getRecordBeanDao().loadAll();
        for(RecordBean recordBean:list){
            LogUtil.logD(recordBean.toString());
        }
        LogUtil.logD("----------------------------------------");

        LogUtil.logD("---------------查看删除数据表--------------");
        List<DeleteRecordBean> list1=GuanBeiApplication.getDaoSession().getDeleteRecordBeanDao().loadAll();
        for(DeleteRecordBean recordBean:list1){
            LogUtil.logD(recordBean.toString());
        }
        LogUtil.logD("----------------------------------------");
    }
}
