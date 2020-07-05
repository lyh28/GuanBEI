package com.lyh.guanbei.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyh.guanbei.R;
import com.lyh.guanbei.adapter.RecordAdapter;
import com.lyh.guanbei.adapter.SMSAdapter;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.bean.SMS;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.ui.widget.BottomRecordDialog;
import com.lyh.guanbei.ui.widget.CustomFloatingBtn;
import com.lyh.guanbei.util.DateUtil;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.SMSUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AddBySMSActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mClose;
    private ImageView mDone;
    private RecyclerView mRecyclerView;
    private SMSAdapter mSMSAdapter;
    private RecordAdapter mRecordAdapter;
    private CustomFloatingBtn mFloatingBtn;
    private BottomRecordDialog mDialog;

    private Map<Integer, Record> chooseMap;     //列表的position

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sms;
    }

    @Override
    protected void initUi() {
        mClose = findViewById(R.id.activity_sms_close);
        mDone = findViewById(R.id.activity_sms_done);
        mRecyclerView = findViewById(R.id.activity_sms_recyclerview);
        mFloatingBtn = findViewById(R.id.activity_sms_floatingbtn);
        mDialog = new BottomRecordDialog(this);
        mDialog.setListener(new BottomRecordDialog.onItemOnClickListener() {
            @Override
            public void onDelete(int record) {
                LogUtil.logD("删除账单");
            }
            @Override
            public void onEdit(Record record) {
                LogUtil.logD("编辑账单");
            }
        });
        mFloatingBtn.setListener(new CustomFloatingBtn.Listener() {
            @Override
            public void onClick() {
                //点击事件
                mRecordAdapter.setNewData(new ArrayList<>(chooseMap.values()));
                mDialog.show();
            }
        });
    }

    @Override
    protected void init() {
        chooseMap = new HashMap<>();
        mRecordAdapter = mDialog.getAdapter();
        //设置列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mSMSAdapter = new SMSAdapter();
        mSMSAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SMS sms = mSMSAdapter.getItem(position);
                sms.setChoose(!sms.isChoose());
                boolean isChoose=sms.isChoose();
                if(isChoose){
                    chooseMap.put(position,createRecordBean(sms));
                }else
                    chooseMap.remove(position);
                mSMSAdapter.notifyItemChanged(position);
                mFloatingBtn.setAmount(chooseMap.size());
            }
        });
        mRecyclerView.setAdapter(mSMSAdapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mSMSAdapter.setNewData(SMSUtil.getSmsFromPhone(this));
    }

    private Record createRecordBean() {
        Random random = new Random();
        long userId= CustomSharedPreferencesManager.getInstance().getUser().getUser_id();
        long bookId=CustomSharedPreferencesManager.getInstance().getCurrBookId();
        Book book=Book.queryByLocalId(bookId);
        return new Record(userId,book.getBook_id(),bookId, DateUtil.getNowDateTimeWithoutSecond(),""+random.nextInt(100)*50,1,"","","");
    }

    private Record createRecordBean(SMS sms) {
        LogUtil.logD(sms.toString());
        long userId= CustomSharedPreferencesManager.getInstance().getUser().getUser_id();
        long bookId=CustomSharedPreferencesManager.getInstance().getCurrBookId();
        Book book=Book.queryByLocalId(bookId);
        return new Record(userId,book.getBook_id(),bookId, sms.getDate(),"0",1,"",sms.getContent(),"短信导入");
    }
    @Override
    public void createPresenters() {

    }

    @Override
    public void onClick(View v) {

    }
}
