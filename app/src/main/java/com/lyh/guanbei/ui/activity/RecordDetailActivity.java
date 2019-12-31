package com.lyh.guanbei.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.bean.Tag;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.manager.TagManager;
import com.lyh.guanbei.mvp.contract.DeleteRecordContract;
import com.lyh.guanbei.mvp.contract.QueryUserContract;
import com.lyh.guanbei.mvp.presenter.DeleteRecordPresenter;
import com.lyh.guanbei.mvp.presenter.QueryUserPresenter;
import com.lyh.guanbei.ui.widget.AskDialog;
import com.lyh.guanbei.util.LogUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.List;

public class RecordDetailActivity extends BaseActivity  implements DeleteRecordContract.IDeleteRecordView, View.OnClickListener, QueryUserContract.IQueryUserView {
    private TextView mTitle;
    private ImageView mIcon;
    private TextView mCategory;
    private TextView mAmount;
    private TextView mUser;
    private TextView mDate;
    private TextView mRemark;

    private AskDialog mDialog;
    private Record record;

    private DeleteRecordPresenter mDeleteRecordPresenter;
    private QueryUserPresenter mQueryUserPresenter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_detail;
    }

    @Override
    protected void initUi() {
        mTitle=findViewById(R.id.activity_record_detail_type);
        mIcon=findViewById(R.id.activity_record_detail_icon);
        mCategory=findViewById(R.id.activity_record_detail_category);
        mAmount=findViewById(R.id.activity_record_detail_amount);
        mUser=findViewById(R.id.activity_record_detail_user);
        mDate=findViewById(R.id.activity_record_detail_date);
        mRemark=findViewById(R.id.activity_record_detail_remark);
        findViewById(R.id.activity_record_detail_back).setOnClickListener(this);
        findViewById(R.id.activity_record_detail_delete).setOnClickListener(this);
        findViewById(R.id.activity_record_detail_edit).setOnClickListener(this);
    }

    @Override
    protected void init() {
        mDialog=new AskDialog(this).setContent("确认要继续删除该账单吗？");
        mDialog.setListener(new AskDialog.onClickListener() {
            @Override
            public void onEnsure() {
            }

            @Override
            public void dismiss() {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData(){
        long recordId=getIntentData().getLong("recordId");
        record= Record.queryByLocalId(recordId);
        int iconId= TagManager.getIconByCategory(record.getCategory(),record.getAmount_type());
        if(record.getAmount_type()==Tag.IN)
            mTitle.setText("收入");
        else
            mTitle.setText("支出");
        Glide.with(this).load(iconId).into(mIcon);
        mCategory.setText(record.getCategory());
        mAmount.setText(record.getAmount());
        mDate.setText(record.getDate());
        mRemark.setText(record.getRemark());
        //查询用户信息
        mQueryUserPresenter.query(record.getUser_id());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_record_detail_back:
                finish();
                break;
            case R.id.activity_record_detail_delete:
                if(checkIsOwn()){
                    //删除操作  弹出确认删除窗
                    mDialog.show();
                }else{
                    showErrorDialog("你没有权限删除该账单");
                }
                break;
            case R.id.activity_record_detail_edit:
                if(checkIsOwn()){
                    //跳转Activity
                    Bundle bundle=new Bundle();
                    bundle.putLong("recordId",record.getRecord_id());
                    startActivity(AddByMyselfActivity.class,bundle);
                }else{
                    showErrorDialog("你没有权限修改该账单");
                }
                break;
        }
    }
    private boolean checkIsOwn(){
        return record.getUser_id()== CustomSharedPreferencesManager.getInstance(this).getUser().getUser_id();
    }
    private void showErrorDialog(String msg){
        final QMUITipDialog dialog=new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                .setTipWord(msg)
                .create();
        dialog.show();
        mTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        },1000);
    }

    @Override
    public void onQueryUserSuccess(User user) {
    }

    @Override
    public void onQueryUserSuccess(List<User> userList) {
        if(userList.size()!=0)
            mUser.setText(userList.get(0).getUser_name());
    }

    @Override
    public void onQueryUserError(String msg) {
    }

    @Override
    public void createPresenters() {
        mDeleteRecordPresenter=new DeleteRecordPresenter();
        mQueryUserPresenter=new QueryUserPresenter();
        addPresenter(mDeleteRecordPresenter);
        addPresenter(mQueryUserPresenter);
    }

}
