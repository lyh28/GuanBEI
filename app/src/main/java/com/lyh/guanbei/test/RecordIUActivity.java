package com.lyh.guanbei.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.RecordBean;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.mvp.contract.CommitRecordContract;
import com.lyh.guanbei.mvp.contract.UpdateRecordContract;
import com.lyh.guanbei.mvp.presenter.CommitRecordPresenter;
import com.lyh.guanbei.mvp.presenter.UpdateRecordPresenter;
import com.lyh.guanbei.util.LogUtil;

public class RecordIUActivity extends BaseActivity implements UpdateRecordContract.IUpdateRecordView, CommitRecordContract.ICommitRecordView {
    private EditText userId;
    private EditText bookId;
    private EditText content;
    private EditText time;
    private EditText amount;
    private EditText payto;
    private EditText remark;
    private EditText category;

    private UpdateRecordPresenter updateRecordPresenter;
    private CommitRecordPresenter commitRecordPresenter;
    private RecordBean recordBean;
    private long id;
    private int type;   //0增加  1更新
    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_iu;
    }

    @Override
    protected void initUi() {
        userId=findViewById(R.id.activity_record_iu_userid);
        bookId=findViewById(R.id.activity_record_iu_bookid);
        content=findViewById(R.id.activity_record_iu_content);
        time=findViewById(R.id.activity_record_iu_time);
        amount=findViewById(R.id.activity_record_iu_amount);
        payto=findViewById(R.id.activity_record_iu_payto);
        remark=findViewById(R.id.activity_record_iu_remark);
        category=findViewById(R.id.activity_record_iu_category);
        findViewById(R.id.activity_record_iu_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userid=Integer.parseInt(userId.getText().toString());
                int bookid=Integer.parseInt(bookId.getText().toString());
                String con=content.getText().toString();
                String date=time.getText().toString();
                String amounts=amount.getText().toString();
                String paytos=payto.getText().toString();
                String remarks=remark.getText().toString();
                String categorys=category.getText().toString();
                if(type==0){
                    RecordBean recordBeans=new RecordBean(userid,bookid,date,amounts,paytos,con,remarks,categorys);
                    LogUtil.logD("提交"+recordBeans);
                    commitRecordPresenter.commit(recordBeans);
                    finish();
                }else{
                    LogUtil.logD("更新"+recordBean);
                    recordBean.setContent(con);
                    recordBean.setTime(date);
                    recordBean.setAmount(amounts);
                    recordBean.setPayto(paytos);
                    recordBean.setCategory(categorys);
                    recordBean.setRemark(remarks);
                    updateRecordPresenter.update(recordBean);
                    finish();
                }
            }
        });
    }

    @Override
    protected void init() {
        Intent intent=getIntent();
        type=intent.getStringExtra("type").equals("add")?0:1;
        if(type==1) {
            id = Long.parseLong(intent.getStringExtra("id"));
            recordBean= GuanBeiApplication.getDaoSession().getRecordBeanDao().load(id);
            userId.setText(recordBean.getUser_id()+"");
            bookId.setText(recordBean.getBook_id()+"");
            content.setText(recordBean.getContent());
            time.setText(recordBean.getTime());
            amount.setText(recordBean.getAmount());
            payto.setText(recordBean.getPayto());
            remark.setText(recordBean.getRemark());
            category.setText(recordBean.getCategory());

        }
    }

    @Override
    public void createPresenters() {
        commitRecordPresenter=new CommitRecordPresenter();
        updateRecordPresenter=new UpdateRecordPresenter();
        addPresenter(commitRecordPresenter);
        addPresenter(updateRecordPresenter);
    }

    @Override
    public void onMessageError(String msg) {
        LogUtil.logD("失败"+msg);
    }
}
