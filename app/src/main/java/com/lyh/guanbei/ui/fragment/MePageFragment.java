package com.lyh.guanbei.ui.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseFragment;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.jpush.PushMessageReceiver;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.ui.activity.IndexActivity;
import com.lyh.guanbei.ui.activity.LoginActivity;
import com.lyh.guanbei.ui.activity.UserActivity;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.service.JPushMessageReceiver;

public class MePageFragment extends BaseFragment implements View.OnClickListener {
    private ImageView mIcon;
    private TextView mName;
    private TextView mId;
    private View mNameView;
    private View mLoginView;
    private TextView mDays;
    private TextView mBooks;
    private TextView mRecords;
    private Button mQuit;
    private User mUser;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me_page;
    }

    @Override
    protected void initUi() {
        mIcon=findViewById(R.id.fragment_me_page_icon);
        mName=findViewById(R.id.fragment_me_page_name);
        mId=findViewById(R.id.fragment_me_page_id);
        mNameView=findViewById(R.id.fragment_me_page_nameview);
        mLoginView=findViewById(R.id.fragment_me_page_loginview);
        mDays=findViewById(R.id.fragment_me_page_days);
        mBooks=findViewById(R.id.fragment_me_page_books);
        mRecords=findViewById(R.id.fragment_me_page_records);
        mQuit=findViewById(R.id.fragment_me_page_quit);
        findViewById(R.id.fragment_me_page_me).setOnClickListener(this);
        findViewById(R.id.fragment_me_page_login_register).setOnClickListener(this);
        findViewById(R.id.fragment_me_page_help).setOnClickListener(this);
        findViewById(R.id.fragment_me_page_about).setOnClickListener(this);
        findViewById(R.id.fragment_me_page_lockerview).setOnClickListener(this);
        mQuit.setOnClickListener(this);
        initShadowLayout();
    }

    private void initShadowLayout() {
        QMUILinearLayout layout = getmView().findViewById(R.id.fragment_me_page_layout);
        int radius=QMUIDisplayHelper.dp2px(getContext(), 15);
        layout.setRadiusAndShadow(radius,QMUIDisplayHelper.dp2px(getmActivity(),14),0.25f);
        int margintop=QMUIDisplayHelper.getStatusBarHeight(getmActivity());
        int margin=QMUIDisplayHelper.dpToPx(20);
        setMargins(layout,margin,margintop+margin*2,margin,margin);
    }

    @Override
    protected void init() {
        refreshData();
    }
    private void refreshData(){
        mUser = CustomSharedPreferencesManager.getInstance().getUser();
        if(mUser==null){

        }else{
            mLoginView.setVisibility(View.GONE);
            mNameView.setVisibility(View.VISIBLE);
            mQuit.setVisibility(View.VISIBLE);
            //设置各种数据
            Glide.with(getmActivity()).load(mUser.getUser_icon()).error(R.drawable.defaulticon).into(mIcon);
            mName.setText(mUser.getUser_name());
            mId.setText(mUser.getUser_id()+"");
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_me_page_login_register:
                startActivity(LoginActivity.class);
                break;
            case R.id.fragment_me_page_quit:
                CustomSharedPreferencesManager.getInstance().saveUser(null);
                JPushInterface.deleteAlias(getmActivity(), PushMessageReceiver.USERALIAS);
                startActivity(IndexActivity.class);
                getmActivity().finish();
                break;
            case R.id.fragment_me_page_me:
                startActivity(UserActivity.class);
                break;
            case R.id.fragment_me_page_lockerview:
                break;
        }
    }

    @Override
    public void createPresenters() {

    }

}
