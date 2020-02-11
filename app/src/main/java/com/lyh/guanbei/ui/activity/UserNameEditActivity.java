package com.lyh.guanbei.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.mvp.contract.UpdateUserContract;
import com.lyh.guanbei.mvp.presenter.UpdateUserPresenter;

public class UserNameEditActivity extends BaseActivity implements View.OnClickListener,UpdateUserContract.IUpdateUserView {
    private TextView mSave;
    private EditText mName;
    private User mUser;

    private UpdateUserPresenter mUpdateUserPresenter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_name_edit;
    }

    @Override
    protected void initUi() {
        mSave=findViewById(R.id.activity_user_name_save);
        mName=findViewById(R.id.activity_user_name_name);
        mName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSave.setEnabled(true);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        findViewById(R.id.activity_user_name_back).setOnClickListener(this);
        findViewById(R.id.activity_user_name_save).setOnClickListener(this);
    }

    @Override
    protected void init() {
        mUser= CustomSharedPreferencesManager.getInstance().getUser();
        mName.setText(mUser.getUser_name());
    }

    @Override
    public void onUpdateFailed(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onUpdateSuccess(User user) {
        CustomSharedPreferencesManager.getInstance().saveUser(user);
        DBManager.getInstance().getDaoSession().getUserDao().update(user);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_user_name_back:
                finish();
                break;
            case R.id.activity_user_name_save:
                mUser.setUser_name(mName.getText().toString());
                mUpdateUserPresenter.updateOther(mUser);
                break;
        }
    }

    @Override
    public void createPresenters() {
        mUpdateUserPresenter=new UpdateUserPresenter();
        addPresenter(mUpdateUserPresenter);
    }
}
