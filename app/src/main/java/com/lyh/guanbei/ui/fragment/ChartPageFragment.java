package com.lyh.guanbei.ui.fragment;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseFragment;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.ui.widget.BottomBookDialog;
import com.lyh.guanbei.util.LogUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ChartPageFragment extends BaseFragment implements View.OnClickListener {
    private FrameLayout mFragmentView;
    private BottomBookDialog mBookDialog;
    private PieChartPageFragment mPieFragment;
    private LineChartPageFragment mLineFragment;
    private RadioGroup radioGroup;
    private ViewGroup rootView;
    private View noDataView;
    private long bookId;
    private TextView mBookView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chart_page;
    }

    @Override
    protected void initUi() {
        mFragmentView = findViewById(R.id.fragment_chart_page_framelayout);
        mBookView = findViewById(R.id.fragment_chart_page_book);
        mBookView.setOnClickListener(this);
        mBookDialog = new BottomBookDialog(getmActivity(), mBookView);
        mBookDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mPieFragment.setBookId(mBookDialog.getCurrBookId());
                mLineFragment.setBookId(mBookDialog.getCurrBookId());

                if(radioGroup.getCheckedRadioButtonId()==R.id.fragment_chart_page_line_btn)
                    mLineFragment.refreshData();
                else
                    mPieFragment.refreshData();
            }
        });
        rootView = findViewById(R.id.fragment_chart_rootview);
        radioGroup = findViewById(R.id.fragment_chart_page_radiogroup);
        radioGroup.check(R.id.fragment_chart_page_pie_btn);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.fragment_chart_page_pie_btn) {
                    setFragment(mPieFragment);
                } else if (checkedId == R.id.fragment_chart_page_line_btn) {
                    setFragment(mLineFragment);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_chart_page_book:
                mBookDialog.show();
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (Book.queryByLocalId(bookId) == null) {
                noDataView = LayoutInflater.from(getmActivity()).inflate(R.layout.fragment_book_page_nodata, null);
                rootView.addView(noDataView);
            } else {
                if (noDataView != null) {
                    initView();
                    rootView.removeView(noDataView);
                    noDataView = null;
                }
                if (radioGroup.getCheckedRadioButtonId() == R.id.fragment_chart_page_line_btn) {
                    if (mLineFragment != null)
                        mLineFragment.refreshData();
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.fragment_chart_page_pie_btn) {
                    if (mPieFragment != null)
                        mPieFragment.refreshData();
                }
            }

        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_chart_page_framelayout, fragment, fragment.getClass().toString());
        fragmentTransaction.commit();
    }

    @Override
    protected void init() {
        initWindow();
        bookId = CustomSharedPreferencesManager.getInstance().getCurrBookId();
        if (Book.queryByLocalId(bookId) != null) {
            initView();
        }
    }

    private void initView() {
        mBookView.setText(Book.queryByLocalId(bookId).getBook_name());
        mPieFragment = new PieChartPageFragment(bookId);
        mPieFragment.setmActivity(getmActivity());
        mLineFragment = new LineChartPageFragment(bookId);
        mLineFragment.setmActivity(getmActivity());
        setFragment(mPieFragment);
    }

    private void initWindow() {
        View layout = findViewById(R.id.fragment_chart_page_toolbar);
        int margintop = QMUIDisplayHelper.getStatusBarHeight(getmActivity());
        int margin = QMUIDisplayHelper.dpToPx(20);
        setMargins(layout, margin, margintop, margin, margin);
    }

    @Override
    public void createPresenters() {

    }
}
