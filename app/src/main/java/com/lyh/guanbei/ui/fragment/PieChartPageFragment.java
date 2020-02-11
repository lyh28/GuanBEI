package com.lyh.guanbei.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.lyh.guanbei.R;
import com.lyh.guanbei.adapter.PieChartAdapter;
import com.lyh.guanbei.base.BaseFragment;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.bean.Tag;
import com.lyh.guanbei.db.RecordDao;
import com.lyh.guanbei.ui.activity.DateChooseActivity;
import com.lyh.guanbei.util.DateUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_OK;

public class PieChartPageFragment extends BaseFragment implements View.OnClickListener {
    private PieChart mChart;
    private TextView mType;
    private TextView mSum;
    private TextView mDate;
    private ArrayList<PieEntry> mEntries;
    private RecyclerView mRecyclerview;
    private PieChartAdapter mAdapter;

    private long bookId;
    private String startDate;
    private String endDate;
    private String type;
    private static final int CHOOSE_DATE_CODE = 1;

    public PieChartPageFragment(long bookId) {
        this.startDate = DateUtil.getMonthFirstDay().split(" ")[0];
        this.endDate = DateUtil.getNowDateTimeWithoutSecond().split(" ")[0];
        this.bookId = bookId;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chart_page_category;
    }

    @Override
    protected void initUi() {
        mChart = findViewById(R.id.fragment_chart_page_category_piechart);
        mDate = findViewById(R.id.fragment_chart_page_category_date);
        mSum = findViewById(R.id.fragment_chart_page_category_sum);
        mType = findViewById(R.id.fragment_chart_page_category_type);
        mRecyclerview = findViewById(R.id.fragment_chart_page_category_recyclerview);
        //设置大小
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mChart.getLayoutParams();
        layoutParams.height = QMUIDisplayHelper.getScreenHeight(getmActivity()) / 3;
        mChart.requestLayout();

        findViewById(R.id.fragment_chart_page_category_date).setOnClickListener(this);
        findViewById(R.id.fragment_chart_page_category_typeview).setOnClickListener(this);
    }

    @Override
    protected void init() {
        type = "月";
        mEntries = new ArrayList<>();
        initChart();
        mAdapter = new PieChartAdapter(getmActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getmActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setLayoutManager(layoutManager);
        refreshData();
    }

    public void refreshData() {
        mDate.setText(wrapDate());
        refreshAdapterData();
        refreshChartData();
        if (mAdapter.getType() == Tag.OUT) {
            mSum.setText(mAdapter.getmOutSum() + "");
            mType.setText("支出");
        } else {
            mSum.setText(mAdapter.getmInSum() + "");
            mType.setText("收入");
        }
    }

    private void refreshAdapterData() {
        mAdapter.setDatas(getData());
    }

    private List<Record> getData() {
        List<Record> list = Record.query(RecordDao.Properties.Book_local_id.eq(bookId)
                , RecordDao.Properties.Date.between(startDate + " " + DateUtil.ZERO_TIME, endDate + " " + DateUtil.FILL_TIME));
        return list;
    }

    private void initChart() {
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
//        mChart.setExtraOffsets(5, 10, 5, 5);
//
//        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(55f);
        mChart.setTransparentCircleRadius(55f);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);
        //关闭图例
        mChart.getLegend().setEnabled(false);
        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);
    }

    private void refreshPieData() {
        mEntries.clear();
        List<PieChartAdapter.PieChartData> list = mAdapter.getData();
        for (PieChartAdapter.PieChartData categoryChart : list) {
            mEntries.add(new PieEntry((float) categoryChart.getmSum(), categoryChart.getmCategory()));
        }
    }

    private void refreshChartData() {
        refreshPieData();
        //判断是否为空
        PieDataSet dataSet = new PieDataSet(mEntries, "Election Results");
        if (mEntries.isEmpty()) {
            mEntries.add(new PieEntry(1, ""));
            dataSet.setColor(getmActivity().getResources().getColor(R.color.colorbg));
            dataSet.setDrawValues(false);
            PieData data = new PieData(dataSet);
            mChart.setDrawSliceText(false);
            mChart.setData(data);
            mChart.invalidate();
        } else {
            // add a lot of colors
            ArrayList<Integer> colors = new ArrayList<>();
            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);
            colors.add(ColorTemplate.getHoloBlue());
            dataSet.setColors(colors);
            dataSet.setValueLinePart1Length(0.2f);
            dataSet.setValueLinePart2Length(0.4f);
            dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.BLACK);
            //动画
            mChart.animateY(1000, Easing.EaseInOutQuad);
            mChart.setDrawSliceText(true);
            mChart.setData(data);
            // undo all highlights
//        mChart.highlightValues(null);
            mChart.invalidate();
        }
    }


    public void setBookId(long bookId) {
        this.bookId = bookId;
        refreshData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_chart_page_category_typeview:
                mAdapter.changeType();
                refreshData();
                break;
            case R.id.fragment_chart_page_category_date:
                Intent intent = new Intent(getContext(), DateChooseActivity.class);
                intent.putExtra("startDate", startDate);
                intent.putExtra("endDate", endDate);
                intent.putExtra("type", type);
                intent.putExtra("bookId", bookId);
                startActivityForResult(intent, CHOOSE_DATE_CODE);
                break;
        }
    }

    @Override
    public void createPresenters() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_DATE_CODE:
                if (resultCode == RESULT_OK) {
                    startDate = data.getStringExtra("startDate");
                    endDate = data.getStringExtra("endDate");
                    type = data.getStringExtra("type");
                    refreshData();
                    mDate.setText(wrapDate());
                }
                break;
        }
    }

    private String wrapDate() {
        String middle = "";
        switch (type) {
            case "周":
                middle = " - 周 - ";
                break;
            case "月":
                middle = " - 月 - ";
                break;
            case "年":
                middle = " - 年 - ";
                break;
            default:
                middle = " ～ ";
                break;
        }
        return startDate + middle + endDate;
    }
}
