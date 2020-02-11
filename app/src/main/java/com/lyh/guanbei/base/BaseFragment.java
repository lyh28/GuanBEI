package com.lyh.guanbei.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment implements IView {
    private List<IPresenter> mPresenterList;
    private Activity mActivity;
    protected View mView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(getLayoutId(),container,false);
        mPresenterList=new ArrayList<>();
        mActivity=getActivity();
        createPresenters();
        for(IPresenter iPresenter:mPresenterList)
            iPresenter.onAttach(this,mActivity);
        initUi();
        init();
        return mView;
    }

    @Override
    public void onDestroyView() {
        for(IPresenter iPresenter:mPresenterList)
            iPresenter.onDettach();
        super.onDestroyView();
    }
    protected void addPresenter(IPresenter iPresenter){
        mPresenterList.add(iPresenter);
    }

    public List<IPresenter> getmPresenterList() {
        return mPresenterList;
    }

    public Activity getmActivity() {
        return mActivity;
    }

    public View getmView() {
        return mView;
    }

    public void setmActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    protected abstract int getLayoutId();
    protected abstract void initUi();
    protected abstract void init();
    protected void startActivity(Class activity){
        Intent intent=new Intent(mActivity,activity);
        startActivity(intent);
    }
    protected void startActivity(Class activity,Bundle bundle){
        Intent intent=new Intent(mActivity,activity);
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }
    protected Bundle getIntentData(){
        return mActivity.getIntent().getExtras();
    }
    protected void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
    protected final <T extends View> T findViewById(@IdRes int id) {
        return getmView().findViewById(id);
    }
}
