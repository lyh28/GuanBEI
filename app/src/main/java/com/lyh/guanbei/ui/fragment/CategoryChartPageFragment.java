package com.lyh.guanbei.ui.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.lyh.guanbei.R;
import com.lyh.guanbei.adapter.ChartCategoryAdapter;
import com.lyh.guanbei.base.BaseFragment;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.bean.Tag;
import com.lyh.guanbei.db.DBManager;
import com.lyh.guanbei.util.DateUtil;
import com.lyh.guanbei.util.LogUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryChartPageFragment extends BaseFragment {
    private PieChart mChart;
    private Activity mActivity;
    private ArrayList<PieEntry> mEntries;
    private RecyclerView mRecyclerview;
    private ChartCategoryAdapter mAdapter;
    public CategoryChartPageFragment(Activity activity){
        mActivity=activity;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chart_page_category;
    }

    @Override
    protected void initUi() {
        mChart=findViewById(R.id.fragment_chart_page_category_piechart);
        mRecyclerview=findViewById(R.id.fragment_chart_page_category_recyclerview);
        //设置大小
        FrameLayout.LayoutParams layoutParams=(FrameLayout.LayoutParams)mChart.getLayoutParams();
        layoutParams.height= QMUIDisplayHelper.getScreenHeight(getmActivity())/3;
        mChart.requestLayout();
        mRecyclerview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){
                    mAdapter.changeType();
                    setData();
                }
                return true;
            }
        });
    }

    @Override
    protected void init() {
        mEntries=new ArrayList<>();
        initChart();
        mAdapter=new ChartCategoryAdapter(mActivity);
        LinearLayoutManager layoutManager=new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setLayoutManager(layoutManager);
        setAdapterData();
        setData();
    }
    public void setAdapterData(){
        mAdapter.setDatas(getData());
    }
    private List<Record> getData(){
        List<Record> list= DBManager.getInstance().getDaoSession().getRecordDao().loadAll();
        return list;
    }
    private void initChart(){
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        mChart.animateY(1400, Easing.EaseInOutQuad);
    }
    private void initData(){
        mEntries.clear();
        List<ChartCategoryAdapter.CategoryChart> list=mAdapter.getData();
        for(ChartCategoryAdapter.CategoryChart categoryChart:list){
            mEntries.add(new PieEntry(categoryChart.getmSum(),categoryChart.getmCategory()));
        }
    }
    public void setData(){
        initData();
        PieDataSet dataSet = new PieDataSet(mEntries, "Election Results");
        if(mAdapter.getType()== Tag.IN){
            mChart.setCenterText("收入");
        }else{
            mChart.setCenterText("支出");
        }
        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);



        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        //dataSet.setUsingSliceColorAsValueLineColor(true);

        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mChart.setData(data);
        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }
    @Override
    public void createPresenters() {

    }
}
