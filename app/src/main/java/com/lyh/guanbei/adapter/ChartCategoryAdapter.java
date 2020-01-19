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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ChartCategoryAdapter extends BaseQuickAdapter<ChartCategoryAdapter.CategoryChart, BaseViewHolder> {
    private Map<String, CategoryChart> mInMap;      //收入
    private Map<String, CategoryChart> mOutMap;      //支出

    private double mInSum;
    private double mOutSum;
    private int type;
    private Context context;

    public ChartCategoryAdapter(Context context) {
        super(R.layout.listitem_chart_category);
        mInMap = new HashMap<>();
        mOutMap = new HashMap<>();
        mInSum=0;
        mOutSum=0;
        type = Tag.IN;
        this.context = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CategoryChart item) {
        helper.setText(R.id.listitem_chart_category_name, item.getmCategory());
        helper.setText(R.id.listitem_chart_category_num, wrapNumAndRate(item));
        ImageView icon = helper.getView(R.id.listitem_chart_category_icon);
        Glide.with(context).load(item.getmIconId()).into(icon);
        helper.setText(R.id.listitem_chart_category_sum, item.getmSum()+"");
    }

    public Map<String, CategoryChart> getmInMap() {
        return mInMap;
    }

    public Map<String, CategoryChart> getmOutMap() {
        return mOutMap;
    }

    public int getType() {
        return type;
    }

    public void changeType() {
        type=type==Tag.IN?Tag.OUT:Tag.IN;
        changeData();
    }

    private String wrapNumAndRate(CategoryChart item) {
        return item.getmRate() + "% " + item.getmNum() + "笔";
    }

    public void setDatas(@Nullable List<Record> datas) {
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

        for(CategoryChart categoryChart:mInMap.values())
            categoryChart.setmRate((categoryChart.getmSum()/mInSum*100));
        for(CategoryChart categoryChart:mOutMap.values())
            categoryChart.setmRate((categoryChart.getmSum()/mOutSum*100));
        changeData();
    }
    public void changeData(){
        if (type == Tag.IN) {
            setNewData(new ArrayList<>(mInMap.values()));
        } else {
            setNewData(new ArrayList<>(mOutMap.values()));
        }
    }
    private void addToMap(Map<String, CategoryChart> map, Record record) {
        String category = record.getCategory();
        if (map.containsKey(record.getCategory())) {
            map.get(category).addRecord(record);
        } else {
            map.put(category, new CategoryChart(record));
        }
    }

    public static class CategoryChart {
        private int mIconId;
        private String mCategory;
        private double mSum;
        private int mNum;
        private double mRate;

        public CategoryChart(Record record) {
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

        public double getmRate() {
            return mRate;
        }

        public void setmRate(double mRate) {
            this.mRate = mRate;
        }
    }
}
