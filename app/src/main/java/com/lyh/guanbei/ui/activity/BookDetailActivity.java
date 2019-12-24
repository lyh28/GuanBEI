package com.lyh.guanbei.ui.activity;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.BookBean;
import com.lyh.guanbei.bean.UserBean;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.mvp.contract.DeleteBookContract;
import com.lyh.guanbei.mvp.contract.QueryUserContract;
import com.lyh.guanbei.mvp.contract.UpdateBookContract;
import com.lyh.guanbei.mvp.presenter.DeleteBookPresenter;
import com.lyh.guanbei.mvp.presenter.QueryUserPresenter;
import com.lyh.guanbei.mvp.presenter.UpdateBookPresenter;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.Util;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.List;

public class BookDetailActivity extends BaseActivity implements View.OnClickListener, DeleteBookContract.IDeleteBookView, UpdateBookContract.IUpdateBookView, QueryUserContract.IQueryUserView {
    private TextView mName;
    private TextView mPerson;
    private ImageView mIcon;
    private TextView mManager;
    private TextView mBudget;

    private long bookId;
    private long userId;
    private BookBean bookBean;

    private DeleteBookPresenter mDeleteBookPresenter;
    private UpdateBookPresenter mUpdateBookPresenter;
    private QueryUserPresenter mQueryUserPresenter;

    private static final int NAME_CODE = 1;
    private static final int BUDGET_CODE = 2;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_detail;
    }

    @Override
    protected void initUi() {
        mName = findViewById(R.id.activity_book_detail_name);
        mIcon = findViewById(R.id.activity_book_detail_manager_icon);
        mManager = findViewById(R.id.activity_book_detail_manager);
        mBudget = findViewById(R.id.activity_book_detail_budget);
        mPerson = findViewById(R.id.activity_book_detail_person);
        //点击事件
        findViewById(R.id.activity_book_detail_back).setOnClickListener(this);
        findViewById(R.id.activity_book_detail_done).setOnClickListener(this);
        findViewById(R.id.activity_book_detail_name_view).setOnClickListener(this);
        findViewById(R.id.activity_book_detail_person_view).setOnClickListener(this);
        findViewById(R.id.activity_book_detail_budget_view).setOnClickListener(this);
        findViewById(R.id.activity_book_detail_delete).setOnClickListener(this);
    }

    @Override
    protected void init() {
        bookId = getIntentData().getLong("bookId");
        bookBean = BookBean.queryByBookId(bookId);
        userId = CustomSharedPreferencesManager.getInstance(this).getUser().getUser_id();
        initData();
    }

    private void initData() {
        mQueryUserPresenter.query(bookBean.getManager_id());
        mName.setText(bookBean.getBook_name());
        if (TextUtils.isEmpty(bookBean.getMax_sum()))
            mBudget.setText("0");
        int personNum = Util.getLongFromData(bookBean.getPerson_id()).size();
        mPerson.setText(wrapPersonNum(personNum));
    }

    private String wrapPersonNum(int num) {
        if (num == 0)
            return "暂无成员";
        else
            return "共" + num + "个成员";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_book_detail_back:
                finish();
                break;
            case R.id.activity_book_detail_done:
                if (checkIsManager()) {
                    updateBookData();
                    mUpdateBookPresenter.updateBook(bookBean);
                }
                break;
            case R.id.activity_book_detail_name_view:
                if (checkIsManager()) {
                    showEditTextDialog(bookBean.getBook_name(), "账本名称", NAME_CODE);
                } else {
                    showErrorDialog();
                }
                break;
            case R.id.activity_book_detail_person_view:
                //封装数据
                Bundle bundle = new Bundle();
                bundle.putString("memberId", bookBean.getPerson_id());
                bundle.putBoolean("isManager", bookBean.getManager_id() == userId);
                bundle.putLong("bookId", bookId);
                startActivity(MemberActivity.class, bundle);
                break;
            case R.id.activity_book_detail_budget_view:
                if (checkIsManager()) {
                    showEditTextDialog(bookBean.getMax_sum(), "预算", BUDGET_CODE);
                } else {
                    showErrorDialog();
                }
                break;
            case R.id.activity_book_detail_delete:
                if (checkIsManager()) {
                    mDeleteBookPresenter.deleteBook(bookBean);
                } else {
                    showErrorDialog();
                }
                break;
        }
    }

    private void updateBookData() {
        bookBean.setMax_sum(mBudget.getText().toString());
        bookBean.setBook_name(mName.getText().toString());
    }

    private boolean checkIsManager() {
        if (bookBean.getManager_id() == userId)
            return true;
        return false;
    }

    private void showEditTextDialog(String defaultText, String title, final int code) {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(this);
        builder.setTitle(title)
                .setDefaultText(defaultText)
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
                        if (text != null && text.length() > 0) {
                            //检测是否符合规定   限额
                            if (code == BUDGET_CODE) {

                            }
                            setText(code, text.toString());
                            dialog.dismiss();
                        } else {
                            if (code == NAME_CODE)
                                Toast.makeText(BookDetailActivity.this, "账本名不能为空", Toast.LENGTH_SHORT).show();
                            else
                                dialog.dismiss();
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void setText(int code, String text) {
        switch (code) {
            case NAME_CODE:
                mName.setText(text);
                break;
            case BUDGET_CODE:
                mBudget.setText(text);
                break;
        }
    }

    private void showErrorDialog() {
        final QMUITipDialog mDialog =new QMUITipDialog.Builder(this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                        .setTipWord("您没有权限修改账本信息")
                        .create();
        mDialog.show();
        mIcon.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDialog.dismiss();
            }
        },1000);
    }

    @Override
    public void onDeleteError(String msg) {
    }

    @Override
    public void onDeleteSuccess() {
    }

    @Override
    public void onUpdateBookFailed(String msg) {

    }

    @Override
    public void createPresenters() {
        mDeleteBookPresenter = new DeleteBookPresenter();
        mUpdateBookPresenter = new UpdateBookPresenter();
        mQueryUserPresenter = new QueryUserPresenter();

        addPresenter(mDeleteBookPresenter);
        addPresenter(mUpdateBookPresenter);
        addPresenter(mQueryUserPresenter);
    }

    @Override
    public void onQueryUserSuccess(UserBean user) {
    }

    @Override
    public void onQueryUserSuccess(List<UserBean> userList) {
        if (userList.size() != 0) {
            UserBean user=userList.get(0);
            if (TextUtils.isEmpty(user.getUser_icon())) {
                //设置用户头像
                Glide.with(this).load(R.drawable.defaulticon).into(mIcon);
            }
            Glide.with(this).load(user.getUser_icon()).placeholder(R.drawable.defaulticon).error(R.drawable.defaulticon).into(mIcon);
            mManager.setText(user.getUser_name());
        }
    }

    @Override
    public void onQueryUserError(String msg) {
    }
}
