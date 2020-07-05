package com.lyh.guanbei.ui.activity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.mvp.contract.InsertBookContract;
import com.lyh.guanbei.mvp.presenter.InsertBookPresenter;
import com.lyh.guanbei.util.LogUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

public class AddBookActivity extends BaseActivity implements InsertBookContract.IInsertBookView, View.OnClickListener {
    private EditText mName;
    private ImageView mClose;
    private ImageView mDone;
    private InsertBookPresenter mInsertBookPresenter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_book;
    }

    @Override
    protected void initUi() {
        mName=findViewById(R.id.activity_add_book_name);
        mDone=findViewById(R.id.activity_add_book_done);
        mClose=findViewById(R.id.activity_add_book_close);

        mClose.setOnClickListener(this);
        mDone.setOnClickListener(this);
    }

    @Override
    protected void init() {

    }

    @Override
    public void createPresenters() {
        mInsertBookPresenter=new InsertBookPresenter();

        addPresenter(mInsertBookPresenter);
    }

    @Override
    public void onMessageError(String msg) {
        new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                .setTipWord(msg)
                .create();
    }

    @Override
    public void onInsertSuccess(List<Book> books) {
        long id=CustomSharedPreferencesManager.getInstance().getCurrBookId();
        if((id==-1||Book.queryByLocalId(id)==null)&&books.size()!=0)
            CustomSharedPreferencesManager.getInstance().saveCurrBookId(books.get(0).getLocal_id());
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_add_book_close:
                finish();
                break;
            case R.id.activity_add_book_done:
                mInsertBookPresenter.insert(createBook());
                break;
        }
    }
    private Book createBook(){
        return new Book(mName.getText().toString(), CustomSharedPreferencesManager.getInstance().getUser().getUser_id());
    }
}
