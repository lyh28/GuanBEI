package com.lyh.guanbei.base;

import android.content.Context;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<V extends IView,M extends IModel> implements IPresenter<V,M> {
    private WeakReference<V> mView;
    private WeakReference<Context> mContext;
    private M mModel;
    public BasePresenter(){
        mModel=createModel();
    }
    @Override
    public void onAttach(V view, Context context) {
        mView=new WeakReference<>(view);
        mContext=new WeakReference<>(context);
    }

    @Override
    public void onDettach() {
        if(mView!=null){
            mView.clear();
            mView=null;
        }
        if(mContext!=null){
            mContext.clear();
            mContext=null;
        }
    }

    @Override
    public boolean checkAttach() {
        return mView!=null&&mContext!=null;
    }

    public V getmView() {
        if(checkAttach())
            return mView.get();
        else
            return null;
    }

    public Context getmContext() {
        if(checkAttach())
            return mContext.get();
        else
            return null;
    }

    protected M getmModel() {
        return mModel;
    }
}
