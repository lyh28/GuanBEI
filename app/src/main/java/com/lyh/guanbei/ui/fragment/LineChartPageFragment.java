package com.lyh.guanbei.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.lyh.guanbei.R;
import com.lyh.guanbei.adapter.LineChartAdapter;
import com.lyh.guanbei.base.BaseFragment;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.db.RecordDao;
import com.lyh.guanbei.ui.activity.RecordListActivity1;
import com.lyh.guanbei.ui.widget.TopListDialog;
import com.lyh.guanbei.util.DateUtil;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.adapter.LineChartAdapter.LineChartData;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LineChartPageFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, BaseQuickAdapter.OnItemClickListener {
    private long bookId;
    private RadioGroup mRadioGroup;
    private TextView mDate;
    private LineChart mChart;
    private LineDataSet inDataSet;
    private LineDataSet outDataSet;
    private LineDataSet SumDataSet;
    private TextView mIntv;
    private TextView mOuttv;
    private TextView mSumtv;
    private RecyclerView mRecyclerView;
    private LineChartAdapter mAdapter;


    private String mStartDate;
    private String mEndDate;
    private String type;
    private List<LineChartData> lineChartDatas;
    private Invoke mInvoke;

    //当前选择日期
    private int year, month, week;
    private boolean showIn, showOut, showSum;

    private final int[] colors = new int[]{
            Color.rgb(137, 230, 81),
            Color.rgb(89, 199, 250),
            Color.rgb(250, 104, 104)
    };

    public LineChartPageFragment(long bookId) {
        this.bookId = bookId;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chart_page_line;
    }

    @Override
    protected void initUi() {
        mRadioGroup = findViewById(R.id.fragment_chart_page_line_radiogroup);
        mRadioGroup.setOnCheckedChangeListener(this);
        mChart = findViewById(R.id.fragment_chart_page_line_linechart);
        mRecyclerView = findViewById(R.id.fragment_chart_page_line_recycerview);
        mDate = findViewById(R.id.fragment_chart_page_line_date);
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInvoke.chooseDate();
            }
        });
        mIntv = findViewById(R.id.fragment_chart_page_line_in);
        mOuttv = findViewById(R.id.fragment_chart_page_line_out);
        mSumtv = findViewById(R.id.fragment_chart_page_line_sum);
        mIntv.setOnClickListener(this);
        mOuttv.setOnClickListener(this);
        mSumtv.setOnClickListener(this);

        FrameLayout layout = findViewById(R.id.fragment_chart_page_line_framelayout);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.height = (int) (0.2 * QMUIDisplayHelper.getScreenHeight(getmActivity()));
        layout.requestLayout();
    }

    @Override
    protected void init() {
        type = "周";
        year = DateUtil.getYear();
        month = Integer.parseInt(DateUtil.getMonth());
        week = DateUtil.getWeeksByNowDate();
        mInvoke = new Invoke();
        showIn = true;
        showOut = true;
        showSum = true;
        mEndDate = DateUtil.getNowDateTimeWithoutSecond().split(" ")[0] + " " + DateUtil.FILL_TIME;
        initChart();
        initList();
        mRadioGroup.check(R.id.fragment_chart_page_line_week);
    }

    private void initList() {
        mAdapter = new LineChartAdapter(this);
        mAdapter.setOnItemClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getmActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void refreshData() {
        lineChartDatas = mInvoke.getData();
        mAdapter.setNewData(lineChartDatas);
        List<Entry> inEntries = new ArrayList<>();
        List<Entry> outEntries = new ArrayList<>();
        List<Entry> numEntries = new ArrayList<>();
        double max = 50;
        //收入
        for (int i = 0; i < lineChartDatas.size(); i++) {
            LineChartData data = lineChartDatas.get(i);
            inEntries.add(new Entry(data.getIndex(), (float) data.getInSum()));
            outEntries.add(new Entry(data.getIndex(), (float) data.getOutSum()));
            numEntries.add(new Entry(data.getIndex(), (float) (data.getInSum() - data.getOutSum())));
            max = Math.max(max, Math.max(data.getInSum(), data.getOutSum()));
        }
        max += 100;
        mChart.getAxisLeft().setAxisMaximum((float) max);
        mChart.getAxisLeft().setAxisMinimum((float) (-max));
        //一个LineDataSet就是一条线
        inDataSet = new LineDataSet(inEntries, "");
        outDataSet = new LineDataSet(outEntries, "");
        SumDataSet = new LineDataSet(numEntries, "");
        //线颜色
        LineData data = new LineData(setupChartData(SumDataSet, colors[2]), setupChartData(inDataSet, colors[0]), setupChartData(outDataSet, colors[1]));
        mChart.setData(data);
        mChart.animateX(1000);
        mChart.invalidate();
    }

    private LineDataSet setupChartData(LineDataSet dataSet, int color) {
        dataSet.setColor(color);
        dataSet.setDrawCircleHole(false);
        dataSet.setCircleColor(color);
        dataSet.setCircleRadius(3f);
        dataSet.setLineWidth(2f);
        dataSet.setDrawValues(false);
        return dataSet;
    }

    private void initChart() {
        mChart.setScaleEnabled(false);
        //得到X轴
        XAxis xAxis = mChart.getXAxis();
        //设置X轴的位置（默认在上方)
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //不显示网格线
        xAxis.setDrawGridLines(false);
        //隐藏轴线
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1);
        //设置X轴值为字符串
        xAxis.setValueFormatter(mInvoke);
        //得到Y轴
        YAxis yAxis = mChart.getAxisLeft();
        YAxis rightYAxis = mChart.getAxisRight();
        //设置Y轴是否显示
        rightYAxis.setEnabled(false); //右侧Y轴不显示
        //不显示网格线
        yAxis.setDrawGridLines(true);
        yAxis.setGridColor(getResources().getColor(R.color.colorbg));
        yAxis.setGridLineWidth(1f);
        yAxis.setDrawAxisLine(false);
        //图例：得到Lengend
        mChart.getLegend().setEnabled(false);
        //隐藏描述
        mChart.getDescription().setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_chart_page_line_in:
                showIn = !showIn;
                if (showIn) {
                    inDataSet.setVisible(true);
                    mIntv.setTextColor(colors[0]);
                } else {
                    inDataSet.setVisible(false);
                    mIntv.setTextColor(Color.BLACK);
                }
                mChart.invalidate();
                break;
            case R.id.fragment_chart_page_line_out:
                showOut = !showOut;
                if (showOut) {
                    outDataSet.setVisible(true);
                    mOuttv.setTextColor(colors[1]);
                } else {
                    outDataSet.setVisible(false);
                    mOuttv.setTextColor(Color.BLACK);
                }
                mChart.invalidate();
                break;
            case R.id.fragment_chart_page_line_sum:
                showSum = !showSum;
                if (showSum) {
                    SumDataSet.setVisible(true);
                    mSumtv.setTextColor(colors[2]);
                } else {
                    SumDataSet.setVisible(false);
                    mSumtv.setTextColor(Color.BLACK);
                }
                mChart.invalidate();
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Bundle bundle=new Bundle();
        String res[]=new String[2];
        getDataByItem(mAdapter.getItem(position),res);
        bundle.putString("startDate",res[0]);
        bundle.putString("endDate",res[1]);
        bundle.putLong("bookId",bookId);
        bundle.putString("title",mAdapter.getItem(position).getDate());
        startActivity(RecordListActivity1.class,bundle);
    }
    private void getDataByItem(LineChartData data,String res[]){
        switch (type){
            case "周":
                String[] arr=data.getDate().split("/");
                res[0]=year+"-"+arr[0]+"-"+arr[1]+" "+DateUtil.ZERO_TIME;
                res[1]=year+"-"+arr[0]+"-"+arr[1]+" "+DateUtil.FILL_TIME;
                break;
            case "月":
                String str=data.getDate();
                str=str.substring(1,str.length()-1);
                int week=Integer.parseInt(str);
                DateUtil.getDatesRangeByWeek(year,week,res);
                break;
            case "年":
                str=data.getDate();
                str=str.substring(1,str.length()-1);
                int month=Integer.parseInt(str);
                DateUtil.getDateRangeOfMonth(year,month,res);
                break;
        }
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        year = DateUtil.getYear();
        month = Integer.parseInt(DateUtil.getMonth());
        week = DateUtil.getWeeksByNowDate();
        switch (checkedId) {
            case R.id.fragment_chart_page_line_week:
                type = "周";
                mStartDate = DateUtil.getDateBeforeDays(6) + " " + DateUtil.ZERO_TIME;
                mDate.setText(week + "周" + "(" + DateUtil.getDayByDate(mStartDate) + "," + DateUtil.getDayByDate(mEndDate) + ")");
                break;
            case R.id.fragment_chart_page_line_month:
                type = "月";
                mStartDate = DateUtil.getMonthFirstDay();
                mDate.setText(month + "月" + "(" + DateUtil.getDayByDate(mStartDate) + "," + DateUtil.getDayByDate(mEndDate) + ")");
                break;
            case R.id.fragment_chart_page_line_year:
                mStartDate = DateUtil.getYearFirstDay();
                type = "年";
                mDate.setText(year + "年" + "(" + DateUtil.getDayByDate(mStartDate) + "," + DateUtil.getDayByDate(mEndDate) + ")");
                break;
        }
        refreshData();
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
        refreshData();
    }

    @Override
    public void createPresenters() {

    }

    public String getType() {
        return type;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getWeek() {
        return week;
    }

    class Invoke extends ValueFormatter implements TopListDialog.onItemSelectListener {
        int chooseYear;

        @Override
        public String getFormattedValue(float value) {
            int IValue = (int) value;
            switch (type) {
                case "周":
                    String date = DateUtil.getDateAfterDays(mStartDate.split(" ")[0], IValue);
                    String[] str = date.split("-");
                    return str[1] + "/" + str[2];
                case "年":
                    return "第" + IValue + "月";
                case "月":
                    return "第" + IValue + "周";
                default:
                    return "";
            }
        }

        public void chooseDate() {
            TopListDialog dialog = new TopListDialog(getmActivity());
            List<String> list = new ArrayList<>();
            switch (type) {
                case "周":
                    int nowWeek = DateUtil.getWeeksByNowDate();
                    list.add("本周");
                    list.add("上周");
                    list.add("上上周");
                    list.add("上上上周");
                    dialog.setList2(list).setShowPart1(false).setSelectItem2(Math.abs(nowWeek - week)).setTitle2Str("选择周").setOnItemSelectListener(this);
                    dialog.show();
                    break;
                case "月":
                    int selectItem1 = 0, selectItem2 = 0;
                    int nowYear = DateUtil.getYear();
                    List<String> part1 = new ArrayList<>();
                    String firstDate = Record.getFirstRecordDate(bookId);
                    int firstYear;
                    if (TextUtils.isEmpty(firstDate))
                        firstYear = nowYear;
                    else firstYear = Integer.parseInt(firstDate.split(" ")[0].split("-")[0]);
                    for (int i = 0; i <= nowYear - firstYear; i++) {
                        part1.add((i + firstYear) + "");
                        if ((i + firstYear) == year) selectItem1 = i;
                    }
                    for (int i = 1; i <= 12; i++) {
                        list.add(i + "月");
                        if (i == month) selectItem2 = i - 1;
                    }
                    dialog.setList1(part1).setList2(list).setSelectItem1(selectItem1).setSelectItem2(selectItem2).setTitle1Str("选择年份").setTitle2Str("选择月份").setShowPart1(true).setOnItemSelectListener(this);
                    dialog.show();
                    break;
                case "年":
                    int selectItem = 0;
                    nowYear = DateUtil.getYear();
                    firstDate = Record.getFirstRecordDate(bookId);
                    if (TextUtils.isEmpty(firstDate))
                        firstYear = year;
                    else firstYear = Integer.parseInt(firstDate.split(" ")[0].split("-")[0]);
                    for (int i = 0; i <= nowYear - firstYear; i++) {
                        list.add((i + firstYear) + "");
                        if ((i + firstYear) == year) selectItem = i;
                    }
                    dialog.setList2(list).setShowPart1(false).setSelectItem2(selectItem).setTitle2Str("选择年份").setOnItemSelectListener(this);
                    dialog.show();
                    break;
            }
        }

        public List<LineChartData> getData() {
            List<LineChartData> res = new ArrayList<>();
            LogUtil.logD("getdata  " + mStartDate + "  " + mEndDate);
            List<Record> records = Record.query(RecordDao.Properties.Book_local_id.eq(bookId)
                    , RecordDao.Properties.Date.between(mStartDate, mEndDate));
            Map<Integer, LineChartData> map = new HashMap<>();
            for (Record r : records)
                LogUtil.logD(r.toString());
            switch (type) {
                case "周":
                    for (int i = 0; i < 7; i++) {
                        String date = DateUtil.getDateAfterDays(mStartDate.split(" ")[0], i);
                        String[] str = date.split("-");
                        map.put(i, new LineChartData(i, str[1] + "/" + str[2]));
                    }
                    for (Record r : records) {
                        int key = DateUtil.differentDaysWithoutSecond(mStartDate, r.getDate());
                        if (map.containsKey(key))
                            map.get(key).addData(r);
                    }
                    mChart.getXAxis().setAxisMaximum(6);
                    mChart.getXAxis().setAxisMinimum(0);
                    break;
                case "月":
                    int[] weeksRange = new int[2];
                    DateUtil.getWeeksRangeOfMonth(2020, 2, weeksRange);
                    for (int i = weeksRange[0]; i <= weeksRange[1]; i++) {
                        map.put(i, new LineChartData(i, "第" + i + "周"));
                    }
                    for (Record r : records) {
                        //一年中的第几周
                        int key = DateUtil.getWeeksByDate(r.getDate().split(" ")[0]);
                        if (map.containsKey(key))
                            map.get(key).addData(r);
                    }
                    mChart.getXAxis().setAxisMinimum(weeksRange[0]);
                    mChart.getXAxis().setAxisMaximum(weeksRange[1]);
                    break;
                case "年":
                    for (int i = 1; i <= 12; i++) {
                        map.put(i, new LineChartData(i, "第" + i + "月"));
                    }
                    for (Record r : records) {
                        int key = Integer.parseInt(r.getDate().split(" ")[0].split("-")[1]);
                        if (map.containsKey(key))
                            map.get(key).addData(r);
                    }
                    mChart.getXAxis().setAxisMaximum(12);
                    mChart.getXAxis().setAxisMinimum(1);
                    break;
            }
            LogUtil.logD("结果map  " + map);
            res.addAll(map.values());
            return res;
        }

        @Override
        public void onSelectPart1(String item, int position) {
            if (type == "月") {
                chooseYear = Integer.parseInt(item);
                String[] monthRes = new String[2];
                DateUtil.getDateRangeOfMonth(chooseYear, month, monthRes);
                mStartDate = monthRes[0] + " " + DateUtil.ZERO_TIME;
                mEndDate = monthRes[1] + " " + DateUtil.FILL_TIME;
                mDate.setText(month + "月" + "(" + monthRes[0] + "," + monthRes[1] + ")");
                refreshData();
            }
        }

        @Override
        public void onSelectPart2(String item, int position) {
            chooseYear = year;
            switch (type) {
                case "周":
                    week = DateUtil.getWeeksByNowDate() - position;
                    String[] weekRes = new String[2];
                    DateUtil.getDatesRangeByWeek(year, week, weekRes);
                    mStartDate = weekRes[0] + " " + DateUtil.ZERO_TIME;
                    mEndDate = weekRes[1] + " " + DateUtil.FILL_TIME;
                    mDate.setText(week + "周" + "(" + weekRes[0] + "," + weekRes[1] + ")");
                    refreshData();
                    break;
                case "月":
                    month = position + 1;
                    break;
                case "年":
                    year = Integer.parseInt(item);
                    String[] yearRes = new String[2];
                    DateUtil.getDatesRangeByYear(year, yearRes);
                    mStartDate = yearRes[0] + " " + DateUtil.ZERO_TIME;
                    mEndDate = yearRes[1] + " " + DateUtil.FILL_TIME;
                    mDate.setText(year + "年" + "(" + yearRes[0] + "," + yearRes[1] + ")");
                    refreshData();

                    break;
            }
        }
    }

}
