package com.lyh.guanbei.ui.activity;

import android.inputmethodservice.KeyboardView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyh.guanbei.R;
import com.lyh.guanbei.adapter.BookLinearAdapter;
import com.lyh.guanbei.adapter.CategoryLinearAdapter;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.bean.Tag;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.manager.TagManager;
import com.lyh.guanbei.mvp.presenter.InsertRecordPresenter;
import com.lyh.guanbei.util.DateUtil;
import com.lyh.guanbei.util.KeyBoardUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class InputActivity extends BaseActivity implements View.OnClickListener {
    private EditText mAmount;
    private TextView mType;
    private EditText mRemark;
    private CategoryLinearAdapter mTagAdapter;
    private RecyclerView mTagRecyclerView;
    private BookLinearAdapter mBookAdapter;
    private RecyclerView mBookRecyclerView;

    private KeyboardView mKeyboardView;
    private KeyBoardUtil keyBoardUtil;


    private InsertRecordPresenter mInsertRecordPresenter;
    private int type;   //当前状态  收入还是支出
    private List<Tag> categoryOutList;
    private List<Tag> categoryInList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_input;
    }

    @Override
    protected void initUi() {
        initWindow();
        QMUIStatusBarHelper.setStatusBarDarkMode(this);
        mKeyboardView = findViewById(R.id.activity_input_keyboard);
        mAmount = findViewById(R.id.activity_input_amount);
        mType=findViewById(R.id.activity_input_type);
        mRemark=findViewById(R.id.activity_input_remark);
        mTagRecyclerView=findViewById(R.id.activity_input_tag_recyclerview);
        mBookRecyclerView=findViewById(R.id.activity_input_book_recyclerview);
        mAmount.setOnClickListener(this);
//        findViewById(R.id.activity_input_other).setOnClickListener(this);
        findViewById(R.id.activity_input_back).setOnClickListener(this);
        findViewById(R.id.activity_input_done).setOnClickListener(this);
        findViewById(R.id.activity_input_type_view).setOnClickListener(this);
    }

    private void initWindow() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        int width = (int)(0.8*QMUIDisplayHelper.getScreenWidth(this));
        layoutParams.width = width;
        layoutParams.gravity = Gravity.CENTER;
        getWindow().setAttributes(layoutParams);
    }

    @Override
    protected void init() {
        categoryOutList = Tag.getTagByType(Tag.OUT);
        categoryInList = Tag.getTagByType(Tag.IN);
        type=Tag.OUT;
        initList();
        //键盘
        keyBoardUtil = new KeyBoardUtil(this, mKeyboardView, mAmount, new KeyBoardUtil.Listener() {
            @Override
            public void addMore() {
                mInsertRecordPresenter.add(createRecord());
                //清空
                mAmount.getText().clear();
            }
        });
    }
    private void initList(){
        //初始化列表
        mTagAdapter=new CategoryLinearAdapter(categoryOutList);
        mTagAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mTagAdapter.setCurrentId(mTagAdapter.getItem(position).getLocal_id());
            }
        });
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mTagRecyclerView.setLayoutManager(layoutManager);
        mTagRecyclerView.setAdapter(mTagAdapter);
        //账本名列表
        mBookAdapter=new BookLinearAdapter(CustomSharedPreferencesManager.getInstance().getCurrBookId(),Book.queryByUserId());
        mBookAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mBookAdapter.setCurrentId(mBookAdapter.getItem(position).getLocal_id());
            }
        });
        layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mBookRecyclerView.setLayoutManager(layoutManager);
        mBookRecyclerView.setAdapter(mBookAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.activity_input_other:
//                if(keyBoardUtil.isKeyBoardShow())
//                    keyBoardUtil.hideKeyBoard();
//                keyBoardUtil.hideSystemKeyboard(this);
//                break;
            case R.id.activity_input_back:
                mInsertRecordPresenter.insert();
                finish();
                break;
            case R.id.activity_input_done:
                mInsertRecordPresenter.insert(createRecord());
                finish();
                break;
            case R.id.activity_input_type_view:
                mTagRecyclerView.scrollToPosition(0);
                type=type==Tag.IN?Tag.OUT:Tag.IN;
                if(type==Tag.IN) {
                    mType.setText("收入");
                    mTagAdapter.setNewData(categoryInList);
                }else {
                    mType.setText("支出");
                    mTagAdapter.setNewData(categoryOutList);
                }
                break;
        }
    }
    @Override
    public void onBackPressed() {
        if (keyBoardUtil.isKeyBoardShow()) {
            keyBoardUtil.hideKeyBoard();
            return;
        }
        mInsertRecordPresenter.insert();
        super.onBackPressed();
    }
    private Record createRecord() {
        long user_id = CustomSharedPreferencesManager.getInstance().getUser().getUser_id();
        long book_id = mBookAdapter.getCurrentId();
        //此处需要修改
        String date = DateUtil.getNowDateTimeWithoutSecond();
        String amount = mAmount.getText().toString();
        String payTo = "";
        String remark = mRemark.getText().toString();
        Tag tag;
        if(mTagAdapter.getCurrentId()==-1)
            tag= TagManager.getDefaultCategory(type);
        else
            tag=Tag.queryById(mTagAdapter.getCurrentId());
        Book book = Book.queryByLocalId(book_id);
        return new Record(user_id, book.getBook_id(), book_id, date, amount, type, payTo, remark, tag.getName());
    }

    @Override
    public void createPresenters() {
        mInsertRecordPresenter =new InsertRecordPresenter();
        addPresenter(mInsertRecordPresenter);
    }
}
