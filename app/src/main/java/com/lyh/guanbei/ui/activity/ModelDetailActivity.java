package com.lyh.guanbei.ui.activity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.Model;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.ui.widget.AskDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

public class ModelDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTitle;
    private TextView mName;
    private TextView mDate;
    private TextView mToWho;
    private TextView mType;
    private TextView mAmount;
    private TextView mRemark;
    private Button mDelete;
    private AskDialog mDialog;

    private Model mModel;
    private int status;
    public static final int INSERT_STATUS = 1;
    public static final int EDIT_STATUS = 2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_model_detail;
    }

    @Override
    protected void initUi() {
        mName = findViewById(R.id.activity_model_detail_name);
        mTitle = findViewById(R.id.activity_model_detail_title);
        mDate = findViewById(R.id.activity_model_detail_date);
        mToWho = findViewById(R.id.activity_model_detail_towho);
        mType = findViewById(R.id.activity_model_detail_type);
        mAmount = findViewById(R.id.activity_model_detail_amount);
        mRemark = findViewById(R.id.activity_model_detail_remark);
        mDelete = findViewById(R.id.activity_model_detail_delete);
        mName.setOnClickListener(this);
        mDelete.setOnClickListener(this);
        findViewById(R.id.activity_model_detail_back).setOnClickListener(this);
        findViewById(R.id.activity_model_detail_save).setOnClickListener(this);
        mDate.setOnClickListener(this);
        mRemark.setOnClickListener(this);
        mToWho.setOnClickListener(this);
        mType.setOnClickListener(this);
        mAmount.setOnClickListener(this);
        mDialog = new AskDialog(this).setContent("确定要继续删除该模板吗？");
        mDialog.setListener(new AskDialog.onClickListener() {
            @Override
            public void onEnsure() {
                //删除操作
                deleteModel();
                finish();
            }

            @Override
            public void dismiss() {

            }
        });
    }

    @Override
    protected void init() {
        initData();
    }

    private void deleteModel() {
        DBManager.getInstance().getDaoSession().getModelDao().deleteByKey(mModel.getId());
    }

    private void initData() {
        Bundle bundle = getIntentData();
        status = bundle.getInt("status", INSERT_STATUS);
        if (status == EDIT_STATUS) {
            long modelId = bundle.getLong("modelid");
            mModel = Model.queryById(modelId);
            mTitle.setText("修改模板");
            if (mModel != null) {
                mName.setText(mModel.getName());
                mDate.setText(mModel.getDate());
                mToWho.setText(mModel.getToWho());
                mType.setText(mModel.getAmount_Type());
                mAmount.setText(mModel.getAmount());
                mRemark.setText(mModel.getRemark());
            }
        } else {
            mDelete.setVisibility(View.GONE);
            mModel = new Model();
            mTitle.setText("新建模板");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_model_detail_back:
                finish();
                break;
            case R.id.activity_model_detail_name:
                if (!checkDefault())
                    showEditTextDialog("模板名称", mName, getText(mName));
                break;
            case R.id.activity_model_detail_date:
                if (!checkDefault())
                    showEditTextDialog("账单日期", mDate, getText(mDate));
                break;
            case R.id.activity_model_detail_towho:
                if (!checkDefault())
                    showEditTextDialog("交易对象", mToWho, getText(mToWho));
                break;
            case R.id.activity_model_detail_type:
                if (!checkDefault())
                    showEditTextDialog("收支", mType, getText(mType));
                break;
            case R.id.activity_model_detail_amount:
                if (!checkDefault())
                    showEditTextDialog("金额", mAmount, getText(mAmount));
                break;
            case R.id.activity_model_detail_remark:
                if (!checkDefault())
                    showEditTextDialog("备注", mRemark, getText(mRemark));
                break;
            case R.id.activity_model_detail_delete:
                if (!checkDefault())
                    mDialog.show();
                break;
            case R.id.activity_model_detail_save:
                if (!checkDefault()) {
                    saveData();
                    if (status == EDIT_STATUS) {
                        DBManager.getInstance().getDaoSession().getModelDao().update(mModel);
                    } else {
                        Model.save(mModel);
                    }
                    finish();
                }
                break;
        }
    }

    private boolean checkDefault() {
        boolean is = Model.isDefaultModel(mModel);
        if (is)
            Toast.makeText(this, "该模板为系统模板，不能进行操作", Toast.LENGTH_SHORT).show();
        return is;
    }

    private void saveData() {
        mModel.setName(mName.getText().toString());
        mModel.setUserId(CustomSharedPreferencesManager.getInstance().getUser().getUser_id());
        mModel.setDate(mDate.getText().toString());
        mModel.setToWho(mToWho.getText().toString());
        mModel.setAmount_Type(mType.getText().toString());
        mModel.setAmount(mAmount.getText().toString());
        mModel.setRemark(mRemark.getText().toString());
    }

    private String getText(TextView textView) {
        return textView.getText().toString();
    }

    private void showEditTextDialog(String title, final TextView textview, String content) {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(this);
        builder.setTitle(title)
                .setDefaultText(content)
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        String str = "";
                        if (text != null && text.length() > 0) {
                            str = text.toString();
                        }
                        textview.setText(str);
                        dialog.dismiss();
                    }
                })
                .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
    }

    @Override
    public void createPresenters() {

    }

}
