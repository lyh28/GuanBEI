package com.lyh.guanbei.ui.activity;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyh.guanbei.R;
import com.lyh.guanbei.adapter.CategoryAdapter;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.bean.Tag;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.mvp.contract.CommitRecordContract;
import com.lyh.guanbei.mvp.contract.UpdateRecordContract;
import com.lyh.guanbei.mvp.presenter.CommitRecordPresenter;
import com.lyh.guanbei.mvp.presenter.UpdateRecordPresenter;
import com.lyh.guanbei.ui.widget.BottomBookDialog;
import com.lyh.guanbei.util.DateUtil;
import com.lyh.guanbei.util.KeyBoardUtil;
import com.lyh.guanbei.manager.TagManager;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddByMyselfActivity extends BaseActivity implements UpdateRecordContract.IUpdateRecordView, CommitRecordContract.ICommitRecordView, View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private RadioGroup mRadioGroup;
    private ImageView mBack;
    private ImageView mDone;
    private ImageView mIcon;
    private TextView mCategory;
    private KeyboardView mKeyboardView;
    private EditText mAmount;
    private RecyclerView mRecyclerview;
    private TextView mBookName;
    private EditText mDate;
    private EditText mToWho;
    private EditText mContent;
    private EditText mRemark;
    private View mRootView;
    private BottomBookDialog mDialog;

    private UpdateRecordPresenter updateRecordPresenter;
    private CommitRecordPresenter commitRecordPresenter;
    private CategoryAdapter mCategoryAdapter;
    private KeyBoardUtil keyBoardUtil;

    private long currBookId;
    private int type;   //当前状态  收入还是支出
    private boolean isUpdate;       //状态    添加还是更新
    private Record mRecord;     //需更新的record

    private List<Tag> categoryOutList;
    private List<Tag> categoryInList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_myself;
    }

    @Override
    protected void initUi() {
        mBack = findViewById(R.id.activity_add_myself_close);
        mDone = findViewById(R.id.activity_add_myself_done);
        mRadioGroup = findViewById(R.id.activity_add_myself_radio);
        mIcon = findViewById(R.id.activity_add_myself_icon);
        mCategory = findViewById(R.id.activity_add_myself_name);
        mKeyboardView = findViewById(R.id.activity_add_myself_keyboard);
        mAmount = findViewById(R.id.activity_add_myself_amount);
        mRecyclerview = findViewById(R.id.activity_add_myself_recyclerview);
        mBookName = findViewById(R.id.activity_add_myself_book);
        mDate = findViewById(R.id.activity_add_myself_date);
        mToWho = findViewById(R.id.activity_add_myself_towho);
        mContent = findViewById(R.id.activity_add_myself_content);
        mRemark = findViewById(R.id.activity_add_myself_remark);
        mDialog = new BottomBookDialog(this, mBookName);

        mBack.setOnClickListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
        mDone.setOnClickListener(this);
        findViewById(R.id.activity_add_myself_bookview).setOnClickListener(this);
        mRecyclerview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setFocusable(true);
                    v.setFocusableInTouchMode(true);
                    v.requestFocus();
                    keyBoardUtil.hideKeyBoard();
                    keyBoardUtil.hideSystemKeyboard(AddByMyselfActivity.this);
                }
                return true;
            }
        });
        //设置点击区域
        findViewById(R.id.activity_add_myself_date_view).setOnClickListener(this);
        //点击其他位置时隐藏键盘
        mRootView = findViewById(R.id.activity_add_myself_rootview);
        mRootView.setOnClickListener(this);
    }

    @Override
    protected void init() {
        initData();
        //列表
        mCategoryAdapter = new CategoryAdapter(categoryOutList);
        mCategoryAdapter.openLoadAnimation();
        mCategoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Tag tag;
                if (type == Tag.IN) {
                    tag = categoryInList.get(position);
                } else {
                    tag = categoryOutList.get(position);
                }
                setCategoryData(tag);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        mRecyclerview.setAdapter(mCategoryAdapter);
        mRecyclerview.setLayoutManager(gridLayoutManager);
        //键盘
        keyBoardUtil = new KeyBoardUtil(this, mKeyboardView, mAmount, new KeyBoardUtil.Listener() {
            @Override
            public void addMore() {
                commitRecordPresenter.add(createRecord());
                Toast.makeText(AddByMyselfActivity.this, "再来一笔", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        Bundle bundle = getIntentData();
        categoryOutList = Tag.getTagByType(Tag.OUT);
        categoryInList = Tag.getTagByType(Tag.IN);
        if (bundle != null) {
            long recordId = bundle.getLong("recordId", -1);
            isUpdate = true;
            mRecord = Record.queryByLocalId(recordId);
            currBookId = mRecord.getBook_local_id();
            mDate.setText(mRecord.getDate().split(" ")[1]);
            mAmount.setText(mRecord.getAmount());
            int iconId = TagManager.getIconByCategory(mRecord.getCategory(), type);
            Glide.with(this).load(iconId).into(mIcon);
            mCategory.setText(mRecord.getCategory());
            mRemark.setText(mRecord.getRemark());
            mToWho.setText(mRecord.getTowho());
        } else {
            CustomSharedPreferencesManager customSharedPreferencesManager = CustomSharedPreferencesManager.getInstance(this);
            type = Tag.OUT;
            currBookId = customSharedPreferencesManager.getCurrBookId();
            mDate.setText(DateUtil.getNowDateTime().split(" ")[1]);
            //设置大图标
            setDefaultCategoryData();
        }
        mBookName.setText(Book.queryByLocalId(currBookId).getBook_name());
    }

    @Override
    public void createPresenters() {
        commitRecordPresenter = new CommitRecordPresenter();
        updateRecordPresenter = new UpdateRecordPresenter();
        addPresenter(updateRecordPresenter);
        addPresenter(commitRecordPresenter);
    }

    @Override
    public void onMessageError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_add_myself_close:
                finish();
                break;
            case R.id.activity_add_myself_done:
                if (isUpdate)
                    updateRecordPresenter.update(createRecord());
                else
                    commitRecordPresenter.commit(createRecord());
                finish();
                break;
            case R.id.activity_add_myself_date_view:
                //选择日期
                Toast.makeText(this, "选择日期", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_add_myself_rootview:
                mRootView.setFocusable(true);
                mRootView.setFocusableInTouchMode(true);
                mRootView.requestFocus();
                keyBoardUtil.hideKeyBoard();
                keyBoardUtil.hideSystemKeyboard(AddByMyselfActivity.this);
                break;
            case R.id.activity_add_myself_bookview:
                if (!isUpdate)
                    mDialog.show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (keyBoardUtil.isKeyBoardShow()) {
            keyBoardUtil.hideKeyBoard();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.activity_add_myself_in_btn:
                type = Tag.IN;
                mCategoryAdapter.setNewData(categoryInList);
                break;
            case R.id.activity_add_myself_out_btn:
                type = Tag.OUT;
                mCategoryAdapter.setNewData(categoryOutList);
                break;
        }
        //变换大图标文字
        setDefaultCategoryData();
    }

    //设置所属的分类（大图标文字）
    private void setDefaultCategoryData() {
        Tag tag;
        if (type == Tag.IN) {
            if (categoryInList == null)
                return;
            if (categoryInList.size() == 0) {
                tag = TagManager.getDefaultCategory(type);
            } else {
                tag = categoryInList.get(0);
            }
        } else {
            if (categoryOutList == null)
                return;
            if (categoryOutList.size() == 0) {
                tag = TagManager.getDefaultCategory(type);
            } else {
                tag = categoryOutList.get(0);
            }
        }
        setCategoryData(tag);
    }

    private void setCategoryData(Tag tag) {
        mIcon.setImageResource(tag.getIconId());
        mCategory.setText(tag.getName());
    }

    private Record createRecord() {
        long user_id = CustomSharedPreferencesManager.getInstance(this).getUser().getUser_id();
        long book_id = mDialog.getCurrBookId();
        //此处需要修改
        String date = DateUtil.getNowDateTime();
        String amount = mAmount.getText().toString();
        String payTo = mToWho.getText().toString();
        String remark = mRemark.getText().toString();
        String category = mCategory.getText().toString();
        if (isUpdate) {
            mRecord.setAmount(amount);
            mRecord.setCategory(category);
            mRecord.setRemark(remark);
            mRecord.setDate(date);
            mRecord.setTowho(payTo);
            mRecord.setRemark(remark);
            return mRecord;
        }
        Book book = Book.queryByLocalId(book_id);
        return new Record(user_id, book.getBook_id(), book_id, date, amount, type, payTo, remark, category);
    }

}
