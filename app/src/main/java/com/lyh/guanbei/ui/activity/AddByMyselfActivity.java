package com.lyh.guanbei.ui.activity;

import android.content.Intent;
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
import com.lyh.guanbei.common.Contact;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.manager.TagManager;
import com.lyh.guanbei.mvp.contract.InsertRecordContract;
import com.lyh.guanbei.mvp.contract.UpdateRecordContract;
import com.lyh.guanbei.mvp.presenter.ApkDownloadPresenter;
import com.lyh.guanbei.mvp.presenter.InsertRecordPresenter;
import com.lyh.guanbei.mvp.presenter.UpdateRecordPresenter;
import com.lyh.guanbei.ui.widget.BottomBookDialog;
import com.lyh.guanbei.ui.widget.BottomDateDialog;
import com.lyh.guanbei.util.DateUtil;
import com.lyh.guanbei.util.KeyBoardUtil;
import com.lyh.guanbei.util.LogUtil;

import java.util.Calendar;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddByMyselfActivity extends BaseActivity implements UpdateRecordContract.IUpdateRecordView, InsertRecordContract.IInsertRecordView, View.OnClickListener, RadioGroup.OnCheckedChangeListener, BottomDateDialog.onDoneListener {
    private RadioGroup mRadioGroup;
    private ImageView mBack;
    private ImageView mDone;
    private ImageView mIcon;
    private TextView mCategory;
    private KeyboardView mKeyboardView;
    private EditText mAmount;
    private RecyclerView mRecyclerview;
    private TextView mBookName;
    private TextView mDate;
    private EditText mToWho;
    private EditText mRemark;
    private View mRootView;
    private BottomBookDialog mDialog;

    private UpdateRecordPresenter updateRecordPresenter;
    private InsertRecordPresenter insertRecordPresenter;
    private CategoryAdapter mCategoryAdapter;
    private KeyBoardUtil keyBoardUtil;

    private long currBookId;
    private int type;   //当前状态  收入还是支出
    private int status;       //状态    添加还是更新还是编辑   0  1  2
    private String date;
    public static final int INSERT_STATUS = 0;
    public static final int UPDATE_STATUS = 1;
    public static final int EDIT_STATUS = 2;

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
        //列表
        categoryOutList = Tag.getTagByType(Tag.OUT);
        categoryInList = Tag.getTagByType(Tag.IN);
        mCategoryAdapter = new CategoryAdapter(categoryOutList);
        initData();
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
                if (status == INSERT_STATUS) {
                    insertRecordPresenter.add(createRecord());
                    //清空
                    mAmount.setText("0");
                    Toast.makeText(AddByMyselfActivity.this, "再来一笔", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddByMyselfActivity.this, "当前不允许该操作", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initData() {
        Bundle bundle = getIntentData();
        if (bundle != null) {
            //更新编辑状态
            status = bundle.getInt("status");
            mRecord =  bundle.getParcelable("record");
            currBookId = mRecord.getBook_local_id();
            date=mRecord.getDate();
            if (!"".equals(mRecord.getDate()))
                mDate.setText(mRecord.getDate().split(" ")[1]);
            mAmount.setText(mRecord.getAmount() + "");
            mCategory.setText(mRecord.getCategory());
            mRemark.setText(mRecord.getRemark());
            mToWho.setText(mRecord.getTowho());
            setCategoryData(Tag.getTagByName(mRecord.getCategory(), mRecord.getAmount_type()));
            if (mRecord.getAmount_type() == Tag.IN) {
                mRadioGroup.check(R.id.activity_add_myself_in_btn);
            } else {
                mRadioGroup.check(R.id.activity_add_myself_out_btn);
            }
        } else {
            //插入状态
            status = INSERT_STATUS;
            date=DateUtil.getNowDateTimeWithoutSecond();
            CustomSharedPreferencesManager customSharedPreferencesManager = CustomSharedPreferencesManager.getInstance();
            type = Tag.OUT;
            currBookId = customSharedPreferencesManager.getCurrBookId();
            mDate.setText(DateUtil.getNowDateTimeWithoutSecond().split(" ")[1]);
            //设置大图标
            setDefaultCategoryData();
        }
        if (currBookId != -1)
            mBookName.setText(Book.queryByLocalId(currBookId).getBook_name());
        else
            mBookName.setText("暂无账本");
    }

    @Override
    public void createPresenters() {
        insertRecordPresenter = new InsertRecordPresenter();
        updateRecordPresenter = new UpdateRecordPresenter();
        addPresenter(updateRecordPresenter);
        addPresenter(insertRecordPresenter);
    }

    @Override
    public void onMessageError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_add_myself_close:
                insertRecordPresenter.insert();
                finish();
                break;
            case R.id.activity_add_myself_done:
                if(!checkAmount()){
                    Toast.makeText(this,"金额不能为0",Toast.LENGTH_SHORT).show();
                    return ;
                }
                if (status == UPDATE_STATUS)
                    updateRecordPresenter.update(createRecord());
                else if (status == EDIT_STATUS) {
                    Record record = createRecord();
                    Intent intent = new Intent();
                    intent.putExtra("record", record);
                    setResult(RESULT_OK, intent);
                } else
                    insertRecordPresenter.insert(createRecord());
                finish();
                break;
            case R.id.activity_add_myself_date_view:
                //选择日期
                Calendar calendar = Calendar.getInstance();
                if (status != INSERT_STATUS) {
                    int[] res = new int[5];
                    getFromDate(mRecord.getDate(), res);
                    calendar.set(res[0], res[1]-1, res[2], res[3], res[4]);
                }
                new BottomDateDialog(this, calendar).setDoneListener(this).show();
                break;
            case R.id.activity_add_myself_rootview:
                setRootViewFocus();
                keyBoardUtil.hideSystemKeyboard(AddByMyselfActivity.this);
                break;
            case R.id.activity_add_myself_bookview:
                mDialog.show();
                break;
        }
    }

    @Override
    public void onDone(String dateAndTime) {
        date=dateAndTime;
        mDate.setText(dateAndTime.split(" ")[1]);
    }

    private void getFromDate(String date, int[] res) {
        String dates = date.split(" ")[0];
        String times = date.split(" ")[1];
        String[] dateArr = dates.split(Contact.SEPARATOR);
        res[0] = Integer.parseInt(dateArr[0]);
        res[1] = Integer.parseInt(dateArr[1]);
        res[2] = Integer.parseInt(dateArr[2]);
        String[] timeArr = times.split(":");
        res[3] = Integer.parseInt(timeArr[0]);
        res[4] = Integer.parseInt(timeArr[1]);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setRootViewFocus();
    }
    private boolean checkAmount(){
        return mAmount.getText().length()!=0&&Double.parseDouble(mAmount.getText().toString())!=0;
    }
    private void setRootViewFocus() {
        mRootView.setFocusable(true);
        mRootView.setFocusableInTouchMode(true);
        mRootView.requestFocus();
    }

    @Override
    public void onBackPressed() {
        if (keyBoardUtil.isKeyBoardShow()) {
            keyBoardUtil.hideKeyBoard();
            return;
        }
        insertRecordPresenter.insert();
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
        if (status == INSERT_STATUS)
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
        Glide.with(this).load(tag.getIconId()).into(mIcon);
        mCategory.setText(tag.getName());
    }

    private Record createRecord() {
        long user_id = CustomSharedPreferencesManager.getInstance().getUser().getUser_id();
        long book_id = mDialog.getCurrBookId();
        //此处需要修改
        String amount = mAmount.getText().toString();
        String payTo = mToWho.getText().toString();
        String remark = mRemark.getText().toString();
        String category = mCategory.getText().toString();
        if (status != INSERT_STATUS) {
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
