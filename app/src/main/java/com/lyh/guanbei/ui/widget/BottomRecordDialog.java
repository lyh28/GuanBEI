package com.lyh.guanbei.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyh.guanbei.R;
import com.lyh.guanbei.adapter.RecordAdapter;
import com.lyh.guanbei.adapter.RecordSectionAdapter;
import com.lyh.guanbei.bean.RecordBean;
import com.lyh.guanbei.util.LogUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BottomRecordDialog extends Dialog {
    private RecyclerView recyclerView;
    private RecordAdapter recordAdapter;
    private onItemOnClickListener mListener;
    private Context context;

    public BottomRecordDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
        this.context = context;
        init();
    }

    private void init(){
        recordAdapter = new RecordAdapter();
        recordAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.listitem_record_delete:
                        if(mListener!=null) {
                            recordAdapter.remove(position);
                            mListener.onDelete(position);
                        }
                        break;
                    case R.id.listitem_record_edit:
                        if(mListener!=null)
                            mListener.onEdit(recordAdapter.getItem(position));
                        break;
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setContentView(R.layout.dialog_bottom_record);
        initWindow();
        initView();
    }

    private void initWindow() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        int height = (int) (0.5f * QMUIDisplayHelper.getScreenHeight(context));
        int width =  QMUIDisplayHelper.getScreenWidth(context);
        layoutParams.width = width;
        layoutParams.height = height;
        layoutParams.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(layoutParams);
        setCanceledOnTouchOutside(true);
    }

    private void initView() {
        recyclerView = findViewById(R.id.dialog_bottom_record_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recordAdapter);
    }
    /**
     * 点击监听
     */
    public interface onItemOnClickListener {
        void onDelete(int record);
        void onEdit(RecordBean record);
    }
    public RecordAdapter getAdapter(){
        return recordAdapter;
    }

    public void setListener(onItemOnClickListener mListener) {
        this.mListener = mListener;
    }
}
