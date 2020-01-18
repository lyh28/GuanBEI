package com.lyh.guanbei.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseFragment;
import com.lyh.guanbei.util.LogUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ChartPageFragment extends BaseFragment {
    private FrameLayout mFragmentView;
    private CategoryChartPageFragment fragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chart_page;
    }

    @Override
    protected void initUi() {
        mFragmentView = findViewById(R.id.fragment_chart_page_framelayout);
        RadioGroup radioGroup = findViewById(R.id.fragment_chart_page_radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.fragment_chart_page_category_btn) {
                    setFragment(fragment);
                } else if (checkedId == R.id.fragment_chart_page_acount_btn) {
                    setFragment(fragment);
                }
            }
        });
        fragment = new CategoryChartPageFragment();
        fragment.setmActivity(getmActivity());
        setFragment(fragment);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (fragment != null) {
                fragment.setAdapterData();
                fragment.setData();
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
