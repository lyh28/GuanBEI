package com.lyh.guanbei.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.manager.ActivityManager;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.mvp.contract.InsertBookContract;
import com.lyh.guanbei.mvp.contract.RegisterContract;
import com.lyh.guanbei.mvp.contract.UpdateUserContract;
import com.lyh.guanbei.mvp.presenter.InsertBookPresenter;
import com.lyh.guanbei.mvp.presenter.RegisterPresenter;
import com.lyh.guanbei.mvp.presenter.UpdateUserPresenter;
import com.lyh.guanbei.util.LogUtil;

import java.util.List;

public class SetPwdActivity extends BaseActivity implements View.OnClickListener , RegisterContract.IRegisterView, UpdateUserContract.IUpdateUserView, InsertBookContract.IInsertBookView {
    private String mPhone;
    private TextView mHead;
    private EditText mPwd;
    private Button mSave;

    private int type;   //类型  0  注册用户   1  忘记密码或修改密码
    private InsertBookPresenter mInsertBookPresenter;
    private RegisterPresenter mRegisterPresenter;
    private UpdateUserPresenter mUpdateUserPresenter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_pwd;
    }

    @Override
    protected void initUi() {
        mHead=findViewById(R.id.activity_set_pwd_head);
        mPwd=findViewById(R.id.activity_set_pwd_pwd);
        mSave=findViewById(R.id.activity_set_pwd_save);
        findViewById(R.id.page_back).setOnClickListener(this);
        mSave.setOnClickListener(this);
        mPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int c=mPwd.getText().length();
                if(c>=8&&c<=20)
                    mSave.setEnabled(true);
                else
                    mSave.setEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.page_back:
                finish();
                break;
            case R.id.activity_set_pwd_save:
                if(type==1){
                    mUpdateUserPresenter.resetPwd(mPhone,mPwd.getText().toString());
                }else if(type==0){
                    mRegisterPresenter.register("",mPhone,mPwd.getText().toString());
                }
                break;
        }
    }
    @Override
    protected void init() {
        Bundle bundle=getIntentData();
        mPhone=bundle.getString("phone");
        type=bundle.getInt("type");
        mHead.setText(wrapHead(mPhone));
    }
    private String wrapHead(String phone){
        return "请为您的账号"+phone+"设置新密码";
    }
    @Override
    public void createPresenters() {
        mRegisterPresenter=new RegisterPresenter();
        mUpdateUserPresenter=new UpdateUserPresenter();
        mInsertBookPresenter=new InsertBookPresenter();
        addPresenter(mInsertBookPresenter);
        addPresenter(mRegisterPresenter);
        addPresenter(mUpdateUserPresenter);
    }

    @Override
    public void onRegisterSuccess(User user) {
        CustomSharedPreferencesManager.getInstance().clearAll();
        CustomSharedPreferencesManager.getInstance().saveUser(user);
        //新建一个book
        mInsertBookPresenter.insert(Book.createNewBook(user.getUser_id()));
    }

    @Override
    public void onRegisterFailed(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMessageError(String msg) {
    }
    @Override
    public void onUpdateFailed(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateSuccess(User user) {
        Toast.makeText(this,"修改密码成功",Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onInsertSuccess(List<Book> books) {
        ActivityManager.getInstance().finishAll();
        CustomSharedPreferencesManager.getInstance().saveCurrBookId(books.get(0).getLocal_id());
        startActivity(MainActivity.class);
    }
}
