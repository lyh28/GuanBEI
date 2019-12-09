package com.lyh.guanbei.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseFragment extends Fragment implements IView {
    private List<IPresenter> mPresenterList;
    private Activity mactivity;
    private View mview;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview=inflater.inflate(getLayoutId(),container);
        mPresenterList=new ArrayList<>();
        mactivity=getActivity();
        createPresenters();
        initUi();
        init();
        for(IPresenter iPresenter:mPresenterList)
            iPresenter.onAttach(this,mactivity);
        return mview;
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
    protected abstract int getLayoutId();
    protected abstract void initUi();
    protected abstract void init();

    protected void startActivity(Class activity){
        startActivity(activity,null);
    }
    protected void startActivity(Class activity,Bundle bundle){
        Intent intent=new Intent(mactivity,activity);
        intent.putExtra("data",bundle);
        startActivity(intent);
    }
}
