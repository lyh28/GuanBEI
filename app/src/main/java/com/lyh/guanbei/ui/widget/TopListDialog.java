package com.lyh.guanbei.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.lyh.guanbei.R;
import com.lyh.guanbei.adapter.TopListAdapter;
import com.lyh.guanbei.util.LogUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class TopListDialog extends Dialog implements TagFlowLayout.OnTagClickListener {
    private Context mContext;

    private TextView title1;
    private TextView title2;
    private TagFlowLayout mFlowLayout1;
    private TopListAdapter mAdapter1;
    private TagFlowLayout mFlowLayout2;
    private TopListAdapter mAdapter2;
    private View part1;

    private onItemSelectListener mListener;
    private List<String> list1;
    private List<String> list2;
    private String title1Str;
    private String title2Str;
    private int selectItem1;
    private int selectItem2;
    private boolean isShowPart1;
    public TopListDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
        this.mContext=context;
        isShowPart1=false;
        list1=new ArrayList<>();
        list2=new ArrayList<>();
        title1Str="";
        title2Str="";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_top_list);
        initWindowConfig();
        initView();
        initData();
    }
    private void initView(){
        title1=findViewById(R.id.dialog_top_list_title1);
        title2=findViewById(R.id.dialog_top_list_title2);
        part1=findViewById(R.id.dialog_top_list_part1);
        mFlowLayout1=findViewById(R.id.dialog_top_list_TagFlowLayout1);
        mFlowLayout2=findViewById(R.id.dialog_top_list_TagFlowLayout2);
        title1.setText(title1Str);
        title2.setText(title2Str);
    }
    private void initData(){
        if(isShowPart1){
            mAdapter1=new TopListAdapter(list1,mContext);
            mAdapter1.setSelectedList(selectItem1);
            mFlowLayout1.setOnTagClickListener(this);
            mFlowLayout1.setAdapter(mAdapter1);
            part1.setVisibility(View.VISIBLE);
        }else{
            part1.setVisibility(View.GONE);
        }
        mAdapter2=new TopListAdapter(list2,mContext);
        mAdapter2.setSelectedList(selectItem2);
        mFlowLayout2.setAdapter(mAdapter2);
        mFlowLayout2.setOnTagClickListener(this);
//
//        FlowLayoutManager flowLayoutManager2=new FlowLayoutManager();
//        LinearLayoutManager layoutManager=new LinearLayoutManager(mContext);
//        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
//        mAdapter2.setSelectItem(selectItem2);
//        mAdapter2.setOnItemClickListener(this);
//        recyclerView2.setAdapter(mAdapter2);
//        recyclerView2.setLayoutManager(flowLayoutManager2);

    }

    public TopListDialog setOnItemSelectListener(onItemSelectListener mListener) {
        this.mListener = mListener;
        return this;
    }

    public TopListDialog setList1(List<String> list1) {
        this.list1 = list1;
        return this;
    }

    public TopListDialog setList2(List<String> list2) {
        this.list2 = list2;
        return this;
    }

    public TopListDialog setShowPart1(boolean showPart1) {
        isShowPart1 = showPart1;
        return this;
    }

    public TopListDialog setSelectItem1(int selectItem1) {
        this.selectItem1 = selectItem1;
        return this;
    }

    public TopListDialog setSelectItem2(int selectItem2) {
        this.selectItem2 = selectItem2;
        return this;
    }

    public TopListDialog setTitle1Str(String title1Str) {
        this.title1Str = title1Str;
        return this;
    }

    public TopListDialog setTitle2Str(String title2Str) {
        this.title2Str = title2Str;
        return this;
    }
    @Override
    public void cancel() {
        super.cancel();
        LogUtil.logD("cancel");
        if(mListener!=null){
            mListener.onSelectPart2(list2.get(selectItem2),selectItem2);
            if(isShowPart1)
                mListener.onSelectPart1(list1.get(selectItem1),selectItem1);
        }
    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        if(parent==mFlowLayout1){
            selectItem1=position;
        }else{
            selectItem2=position;
            cancel();
        }
        return true;
    }

    private void initWindowConfig() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = (int)(0.75*QMUIDisplayHelper.getScreenWidth(mContext));
        layoutParams.gravity = Gravity.CENTER;
        getWindow().setAttributes(layoutParams);
        setCanceledOnTouchOutside(true);
    }
    public interface onItemSelectListener{
        void onSelectPart1(String item,int position);
        void onSelectPart2(String item,int position);
    }
}
