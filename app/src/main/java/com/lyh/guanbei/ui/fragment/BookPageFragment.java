package com.lyh.guanbei.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.lyh.guanbei.R;
import com.lyh.guanbei.adapter.RecordSectionAdapter;
import com.lyh.guanbei.base.BaseFragment;
import com.lyh.guanbei.bean.BookBean;
import com.lyh.guanbei.bean.RecordBean;
import com.lyh.guanbei.db.RecordBeanDao;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.mvp.contract.QueryRecordContract;
import com.lyh.guanbei.mvp.presenter.QueryRecordPresenter;
import com.lyh.guanbei.ui.activity.ChangeBookActivity;
import com.lyh.guanbei.ui.activity.RecordDetailActivity;
import com.lyh.guanbei.util.CustomOffsetChangeListener;
import com.lyh.guanbei.util.LogUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.nio.channels.OverlappingFileLockException;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BookPageFragment extends BaseFragment implements QueryRecordContract.IQueryRecordView , View.OnClickListener {
    private RecordSectionAdapter mRecordSectionAdapter;
    private RecyclerView recyclerView;
    private AppBarLayout mAppbar;
    private View mOutView;
    private View mInView;
    private ImageView mFilter;
    private ImageView mMore;
    private TextView mBookName;
    private View mBookView;
    private TextView mBudget;
    private TextView mIn;
    private TextView mOut;
    private View mNoDataView;   //没有账本时的界面
    private ViewGroup mRootView;    //根界面
    private List<RecordBean> recordBeanList;
    private QueryRecordPresenter mQueryRecordPresenter;
    private long bookId;

    public BookPageFragment() {
        super();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_book_page;
    }

    @Override
    protected void initUi() {
        mAppbar=mView.findViewById(R.id.fragment_book_appbar);
        recyclerView=mView.findViewById(R.id.fragment_book_recyclerview);
        mOutView=mView.findViewById(R.id.fragment_book_outView);
        mInView=mView.findViewById(R.id.fragment_book_inView);
        mBookView=mView.findViewById(R.id.fragment_book_nameView);
        mFilter=mView.findViewById(R.id.fragment_book_filter);
        mMore=mView.findViewById(R.id.fragment_book_more);
        mBookName=mView.findViewById(R.id.fragment_book_name);
        mBudget=mView.findViewById(R.id.fragment_book_budget);
        mIn=mView.findViewById(R.id.fragment_book_inNum);
        mOut=mView.findViewById(R.id.fragment_book_outNum);

        mFilter.setOnClickListener(this);
        mBookView.setOnClickListener(this);
        mMore.setOnClickListener(this);
        mBudget.setOnClickListener(this);
        //设置间隔
        int marginTop= QMUIStatusBarHelper.getStatusbarHeight(getmActivity());
        setMargins(mOutView,0,marginTop,0,0);
        setMargins(mInView,0,marginTop,0,0);
    }

    @Override
    protected void init() {
        //列表
        mRecordSectionAdapter =new RecordSectionAdapter();
        mRecordSectionAdapter.openLoadAnimation();
        mRecordSectionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(!mRecordSectionAdapter.getItem(position).isHeader){
                    Bundle bundle=new Bundle();
                    bundle.putLong("recordId",mRecordSectionAdapter.getItem(position).t.getRecord_id());
                    startActivity(RecordDetailActivity.class,bundle);
                }
            }
        });
        recyclerView.setAdapter(mRecordSectionAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getmActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        //appbar滑动监听
        mAppbar.addOnOffsetChangedListener(new CustomOffsetChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if(state==State.IDLE&&mOutView.getVisibility()==View.GONE){
                    mOutView.setVisibility(View.VISIBLE);
                }else if(state==State.COLLAPSED){
                    mOutView.setVisibility(View.GONE);
                }
            }
            @Override
            public void onOffsetChangedForRate(AppBarLayout appBarLayout, float rate) {
                mOutView.setAlpha(1-rate);
                mBudget.setAlpha(1-rate);
            }
        });
    }
    @Override
    public void onResume() {
        CustomSharedPreferencesManager customSharedPreferencesManager=CustomSharedPreferencesManager.getInstance(getmActivity());
        long id=customSharedPreferencesManager.getCurrBookId();
        LogUtil.logD("bookFragment  resume");
        if(bookId!=id){
            bookId=id;
            if(bookId==-1L){
                //处理掉没有book的情况
                mRootView=getmView().findViewById(R.id.fragment_book_rootview);
                mNoDataView= LayoutInflater.from(getmActivity()).inflate(R.layout.fragment_book_page_nodata,null);
                mRootView.addView(mNoDataView);
            }else {
                if(mNoDataView!=null){
                    mRootView.removeView(mNoDataView);
                    mNoDataView=null;
                }
                BookBean book=BookBean.queryByBookId(bookId);
                if(book!=null){
                    mBookName.setText(book.getBook_name());
                    mQueryRecordPresenter.queryRecordById(QueryRecordPresenter.BOOKID, bookId);
                }
            }
        }else{
            List<RecordBean> list=RecordBean.query(RecordBeanDao.Properties.Book_id.eq(bookId));
            mRecordSectionAdapter.setNewDatas(list);
            recordBeanList=list;
        }
        super.onResume();
    }

    @Override
    public void createPresenters() {
        mQueryRecordPresenter=new QueryRecordPresenter();

        addPresenter(mQueryRecordPresenter);
    }

    @Override
    public void onQueryRecordSuccess(List<RecordBean> recordBeans) {
        mRecordSectionAdapter.setNewDatas(recordBeans);
        recordBeanList=recordBeans;
    }

    @Override
    public void onQueryRecordFailed(String msg) {
        LogUtil.logD("失败"+msg);
    }

    @Override
    public void startLoading() {
        LogUtil.logD("开始加载");
    }

    @Override
    public void endLoading() {

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_book_filter:
                break;
            case R.id.fragment_book_more:
                break;
            case R.id.fragment_book_budget:
                break;
            case R.id.fragment_book_nameView:
                //开启下拉
                startActivity(ChangeBookActivity.class);
                break;
        }
    }
}
