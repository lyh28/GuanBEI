package com.lyh.guanbei.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.lyh.guanbei.R;
import com.lyh.guanbei.util.LogUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import androidx.annotation.NonNull;

public class AskDialog extends Dialog {
    private TextView mTitle;
    private TextView mContent;
    private TextView mCancel;
    private TextView mEnsure;
    private onClickListener mListener;
    private Context mContext;

    private String mTitleStr;
    private String mContentStr;
    public AskDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
        mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ask);
        initWindow();
        initView();
    }
    private void initWindow(){
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
//        int height = (int) (0.4f * QMUIDisplayHelper.getScreenHeight(mContext));
//        int width =  (int)(0.5f*QMUIDisplayHelper.getScreenWidth(mContext));
//        layoutParams.width = width;
//        layoutParams.height = height;
        layoutParams.gravity = Gravity.CENTER;
        getWindow().setAttributes(layoutParams);
        setCanceledOnTouchOutside(true);
    }
    private void initView(){
        mCancel=findViewById(R.id.dialog_ask_cancel);
        mEnsure=findViewById(R.id.dialog_ask_ensure);
        mTitle=findViewById(R.id.dialog_ask_title);
        mContent=findViewById(R.id.dialog_ask_content);
        if(mTitleStr!=null)
            mTitle.setText(mTitleStr);
        if(mContentStr!=null)
            mContent.setText(mContentStr);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dismiss();
            }
        });
        mEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null) {
                    mListener.onEnsure();
                    dismiss();
                }
            }
        });
    }
    public AskDialog setTitle(String title){
        mTitleStr=title;
        return this;
    }
    public AskDialog setContent(String content){
        mContentStr=content;
        return this;
    }

    @Override
    public void dismiss() {
        if(mListener!=null)
            mListener.dismiss();
        super.dismiss();
    }

    public void setListener(onClickListener mListener) {
        this.mListener = mListener;
    }

    public interface onClickListener{
        void onEnsure();
        void dismiss();
    }
}
