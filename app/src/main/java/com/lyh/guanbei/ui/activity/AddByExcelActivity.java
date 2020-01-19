package com.lyh.guanbei.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyh.guanbei.R;
import com.lyh.guanbei.adapter.RecordAdapter;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.Model;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.common.Contact;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.ui.widget.BottomBookDialog;
import com.lyh.guanbei.util.FileUtil;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.excel.RecordExcel;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class AddByExcelActivity extends BaseActivity implements View.OnClickListener {
    private TextView mModel;
    private TextView mDate;
    private TextView mToWho;
    private TextView mType;
    private TextView mAmount;
    private TextView mRemark;
    private TextView mBook;
    private TextView mFile;

    private BottomBookDialog mDialog;
    private QMUIPopup mPopup;
    private String path;
    private int currentIndex;   //当前模板的索引
    private List<Model> modelList;

    private static final int CHOOSE_CODE = 1;
    private static final int PARSE_CODE = 2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_by_excel;
    }

    @Override
    protected void initUi() {
        mModel = findViewById(R.id.activity_add_by_excel_model);
        mDate = findViewById(R.id.activity_add_by_excel_date);
        mToWho = findViewById(R.id.activity_add_by_excel_towho);
        mType = findViewById(R.id.activity_add_by_excel_amount_type);
        mAmount = findViewById(R.id.activity_add_by_excel_amount);
        mRemark = findViewById(R.id.activity_add_by_excel_remark);
        mBook = findViewById(R.id.activity_add_by_excel_book);
        mFile = findViewById(R.id.activity_add_by_excel_file);

        findViewById(R.id.activity_add_by_excel_book_view).setOnClickListener(this);
        findViewById(R.id.activity_add_by_excel_back).setOnClickListener(this);
        findViewById(R.id.activity_add_by_excel_change).setOnClickListener(this);
        findViewById(R.id.activity_add_by_excel_modelmanager).setOnClickListener(this);
        findViewById(R.id.activity_add_by_excel_file).setOnClickListener(this);
        findViewById(R.id.activity_add_by_excel_parsing).setOnClickListener(this);
    }

    @Override
    protected void init() {
        currentIndex=0;
        mDialog = new BottomBookDialog(this, mBook);
        //初始化当前book名称
        mBook.setText(Book.queryByLocalId(CustomSharedPreferencesManager.getInstance().getCurrBookId()).getBook_name());
    }

    @Override
    protected void onResume() {
        super.onResume();
        initModel();
    }

    private void initModel() {
        modelList = DBManager.getInstance().getDaoSession().getModelDao().loadAll();
        if (modelList.size() != 0) {
            Model model = modelList.get(currentIndex);
            mModel.setText(model.getName());
            changeModel(model);
        }else{
            //处理没有的情况
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_add_by_excel_book_view:
                mDialog.show();
                break;
            case R.id.activity_add_by_excel_back:
                finish();
                break;
            case R.id.activity_add_by_excel_change:
                showPopup(v);
                break;
            case R.id.activity_add_by_excel_modelmanager:
                startActivity(ModelActivity.class);
                break;
            case R.id.activity_add_by_excel_file:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, CHOOSE_CODE);
                break;
            case R.id.activity_add_by_excel_parsing:
                if ("".equals(path)||path==null) {
                    showErrorDialog("未选择文件");
                } else if (path.endsWith(".xls") || path.endsWith(".xlsx") || path.endsWith(".csv")) {
                    //开始处理
                    Intent intent1 = new Intent(this, RecordListActivity.class);
                    intent1.putExtra("path", path);
                    intent1.putExtra("model",modelList.get(currentIndex));
                    intent1.putExtra("bookid", mDialog.getCurrBookId());
                    startActivityForResult(intent1, PARSE_CODE);
                } else {
                    showErrorDialog("该文件不是xls、xlsx、csv格式");
                }
                break;

        }
    }
    private void showPopup(View v){
        List<String> data = new ArrayList<>();
        for(Model m:modelList)
            data.add(m.getName());
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, data);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentIndex=i;
                Model model=modelList.get(currentIndex);
                mModel.setText(model.getName());
                changeModel(model);
                if (mPopup != null) {
                    mPopup.dismiss();
                }
            }
        };
        mPopup = QMUIPopups.listPopup(this,
                QMUIDisplayHelper.dp2px(this, 150),
                QMUIDisplayHelper.dp2px(this, 100),
                adapter,
                onItemClickListener)
                .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                .preferredDirection(QMUIPopup.DIRECTION_BOTTOM)
                .onDismiss(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mPopup=null;
                    }
                })
                .shadowInset(QMUIDisplayHelper.dp2px(this,5))
                .shadow(false)
                .show(v);
    }
    @Override
    public void createPresenters() {

    }

    //models以-分割
    private void changeModel(Model model) {
        if (model != null) {
            mDate.setText(model.getDate());
            mToWho.setText(model.getToWho());
            mType.setText(model.getAmount_Type());
            mAmount.setText(model.getAmount());
            mRemark.setText(model.getRemark());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_CODE) {
            if (data == null) {
                // 用户未选择任何文件，直接返回
                return;
            }
            Uri uri = data.getData(); // 获取用户选择文件的URI
            path = FileUtil.getFilePathByUri(this, uri);
            mFile.setText(path.substring(path.lastIndexOf("/") + 1));
        } else if (requestCode == PARSE_CODE) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }


    private String getText(TextView textView) {
        return textView.getText().toString();
    }

    private void showErrorDialog(String content) {
        final QMUITipDialog mDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                .setTipWord(content)
                .create();
        mDialog.show();
        mDate.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDialog.dismiss();
            }
        }, 1000);
    }
}
