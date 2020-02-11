package com.lyh.guanbei.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyh.guanbei.R;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.bean.Tag;
import com.lyh.guanbei.manager.TagManager;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PieChartAdapter extends BaseQuickAdapter<PieChartAdapter.PieChartData, BaseViewHolder> {
    private Map<String, PieChartData> mInMap;      //收入
    private Map<String, PieChartData> mOutMap;      //支出

    private double mInSum;
    private double mOutSum;
    private int type;
    private Context context;

    public PieChartAdapter(Context context) {
        super(R.layout.listitem_chart_category);
        mInMap = new HashMap<>();
        mOutMap = new HashMap<>();
        mInSum=0;
        mOutSum=0;
        type = Tag.IN;
        this.context = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PieChartData item) {
        helper.setText(R.id.listitem_chart_category_name, item.getmCategory());
        helper.setText(R.id.listitem_chart_category_num, wrapNumAndRate(item));
        ImageView icon = helper.getView(R.id.listitem_chart_category_icon);
        Glide.with(context).load(item.getmIconId()).into(icon);
        helper.setText(R.id.listitem_chart_category_sum, item.getmSum()+"");
    }

    public Map<String, PieChartData> getmInMap() {
        return mInMap;
    }

    public Map<String, PieChartData> getmOutMap() {
        return mOutMap;
    }

    public int getType() {
        return type;
    }

    public void changeType() {
        type=type==Tag.IN?Tag.OUT:Tag.IN;
        changeData();
    }

    private String wrapNumAndRate(PieChartData item) {
        return item.getmRate() + "% " + item.getmNum() + "笔";
    }

    public void setDatas(@Nullable List<Record> datas) {
        mInSum=0;
        mOutSum=0;
        mInMap.clear();
        mOutMap.clear();
        for (Record record : datas) {
            if (record.getAmount_type() == Tag.IN) {
                addToMap(mInMap, record);
                mInSum+=record.getAmount();
            } else {
                addToMap(mOutMap, record);
                mOutSum+=record.getAmount();
            }
        }
        for(PieChartData pieChartData:mInMap.values())
            pieChartData.setmRate((pieChartData.getmSum()/mInSum*100));
        for(PieChartData pieChartData:mOutMap.values())
            pieChartData.setmRate((pieChartData.getmSum()/mOutSum*100));
        changeData();
    }
    public void changeData(){
        if (type == Tag.IN) {
            setNewData(new ArrayList<>(mInMap.values()));
        } else {
            setNewData(new ArrayList<>(mOutMap.values()));
        }
    }
    private void addToMap(Map<String, PieChartData> map, Record record) {
        String category = record.getCategory();
        if (map.containsKey(record.getCategory())) {
            map.get(category).addRecord(record);
        } else {
            map.put(category, new PieChartData(record));
        }
    }

    public double getmInSum() {
        return mInSum;
    }

    public double getmOutSum() {
        return mOutSum;
    }

    public static class PieChartData {
        private int mIconId;
        private String mCategory;
        private double mSum;
        private int mNum;
        private String mRate;

        public PieChartData(Record record) {
            this.mIconId = TagManager.getIconByCategory(record.getCategory(), record.getAmount_type());
            this.mCategory = record.getCategory();
            this.mSum =record.getAmount();
            this.mNum = 1;
        }

        public void addRecord(Record record) {
            mSum += record.getAmount();
            mNum++;
        }

        public int getmIconId() {
            return mIconId;
        }


        public String getmCategory() {
            return mCategory;
        }

        public double getmSum() {
            return mSum;
        }


        public int getmNum() {
            return mNum;
        }

        public String getmRate() {
            return mRate;
        }

        public void setmRate(double mRate) {
            DecimalFormat df = new DecimalFormat("0.0");
            df.setMaximumFractionDigits(1);
            df.setRoundingMode(RoundingMode.HALF_UP);
            String rate=df.format(mRate);
            this.mRate = rate;
        }

        @Override
        public String toString() {
            return "CategoryChart{" +
                    "mIconId=" + mIconId +
                    ", mCategory='" + mCategory + '\'' +
                    ", mSum=" + mSum +
                    ", mNum=" + mNum +
                    ", mRate='" + mRate + '\'' +
                    '}';
        }
    }

}
