package com.lyh.guanbei.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyh.guanbei.R;
import com.lyh.guanbei.adapter.BookAdapter;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.BookBean;
import com.lyh.guanbei.bean.UserBean;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.mvp.contract.DeleteBookContract;
import com.lyh.guanbei.mvp.presenter.DeleteBookPresenter;
import com.lyh.guanbei.ui.widget.AskDialog;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.Util;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChangeBookActivity extends BaseActivity implements View.OnClickListener, DeleteBookContract.IDeleteBookView {
    private ImageView mAdd;
    private ImageView mEdit;
    private TextView mTitle;
    private RecyclerView mRecyclerview;
    private BookAdapter mBookAdapter;
    private AskDialog mDialog;

    private DeleteBookPresenter mDeleteBookPresenter;

    private int currDeleteBookIndex;
    private long currDeleteBookId;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_change;
    }

    @Override
    protected void initUi() {
        setWindowConfig();
        mAdd = findViewById(R.id.activity_book_change_add);
        mEdit = findViewById(R.id.activity_book_change_edit);
        mTitle = findViewById(R.id.activity_book_change_title);
        mRecyclerview = findViewById(R.id.activity_book_change_recyclerview);
        mDialog = new AskDialog(this);
        mDialog.setListener(new AskDialog.onClickListener() {
            @Override
            public void onEnsure() {
                currDeleteBookId=mBookAdapter.getItem(currDeleteBookIndex).getBook_id();
                mDeleteBookPresenter.deleteBook(mBookAdapter.getItem(currDeleteBookIndex));
            }
            @Override
            public void dismiss() {
                initBookData();
            }
        });

        mAdd.setOnClickListener(this);
        mEdit.setOnClickListener(this);
    }

    @Override
    protected void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mBookAdapter = new BookAdapter(CustomSharedPreferencesManager.getInstance(this).getCurrBookId());
        mBookAdapter.openLoadAnimation();
        mBookAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.listitem_book_delete:
                        currDeleteBookIndex = position;
                        mDialog.show();
                        break;
                    case R.id.listitem_book_edit:
                        Bundle bundle=new Bundle();
                        bundle.putLong("bookId",mBookAdapter.getItem(position).getBook_id());
                        startActivity(BookDetailActivity.class,bundle);
                        /*
                            编辑  进入账本详细页面
                         */
                        break;
                }
            }
        });
        mBookAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mBookAdapter.setCurrentBookId(mBookAdapter.getItem(position).getBook_id());
            }
        });
        mRecyclerview.setLayoutManager(layoutManager);
        mRecyclerview.setAdapter(mBookAdapter);
    }

    private void setWindowConfig() {
        //设置窗体大小
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        int height = (int) (0.85f * QMUIDisplayHelper.getScreenHeight(this));
        int width = (int) (0.8f * QMUIDisplayHelper.getScreenWidth(this));
        layoutParams.width = width;
        layoutParams.height = height;
        layoutParams.gravity = Gravity.TOP;
        getWindow().setAttributes(layoutParams);
        setFinishOnTouchOutside(true);
    }

    private void initBookData() {
        List<BookBean> list = BookBean.queryByUserId(this);
        mBookAdapter.setNewData(list);
        CustomSharedPreferencesManager sharedPreferencesManager=CustomSharedPreferencesManager.getInstance(this);
        if(sharedPreferencesManager.getCurrBookId()==-1&&list.size()!=0){
            BookBean book=list.get(0);
            sharedPreferencesManager.saveCurrBookId(book.getBook_id());
            mBookAdapter.setCurrentBookId(book.getBook_id());
        }

        mTitle.setText(wrapTitle(list.size()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initBookData();
    }

    @Override
    public void createPresenters() {
        mDeleteBookPresenter = new DeleteBookPresenter();

        addPresenter(mDeleteBookPresenter);
    }

    private String wrapTitle(int num) {
        return "我的账本（" + num + "）";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_book_change_add:
                startActivity(AddBookActivity.class);
                break;
            case R.id.activity_book_change_edit:
                mBookAdapter.setStatus(!mBookAdapter.getIsEditStatus());
                onStatusShow();
                break;
        }
    }

    private void onStatusShow() {
        if (mBookAdapter.getIsEditStatus()) {
            mAdd.setVisibility(View.INVISIBLE);
        } else {
            mAdd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDeleteError(String msg) {
        final QMUITipDialog dialog=new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                .setTipWord(msg)
                .create();
        dialog.show();
        mAdd.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        },1000);
    }

    @Override
    public void onDeleteSuccess() {
        //从用户数据中删除bookid
        CustomSharedPreferencesManager customSharedPreferencesManager=CustomSharedPreferencesManager.getInstance(this);
        String bookId= customSharedPreferencesManager.getUser().getBook_id();
        bookId=Util.deleteFormData(currDeleteBookId,bookId);
        //如果是当前的最近记录 则更换
        if(currDeleteBookId==customSharedPreferencesManager.getCurrBookId()){
            if(bookId.equals(""))
                customSharedPreferencesManager.saveCurrBookId(-1);
            else
                customSharedPreferencesManager.saveCurrBookId(Util.getLongFromData(bookId).get(0));
        }
        mBookAdapter.notifyItemRemoved(currDeleteBookIndex);
    }

    @Override
    public void finish() {
        LogUtil.logD("ChangeBookActivity  finish");
        CustomSharedPreferencesManager customSharedPreferencesManager = CustomSharedPreferencesManager.getInstance(this);
        customSharedPreferencesManager.saveCurrBookId(mBookAdapter.getCurrentBookId());
        super.finish();
    }

}
