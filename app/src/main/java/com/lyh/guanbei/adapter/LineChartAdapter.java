package com.lyh.guanbei.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyh.guanbei.R;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.bean.Tag;
import com.lyh.guanbei.ui.fragment.LineChartPageFragment;

import androidx.annotation.NonNull;

public class LineChartAdapter extends BaseQuickAdapter <LineChartAdapter.LineChartData,BaseViewHolder>{
    public LineChartPageFragment mFragment;
    public LineChartAdapter(LineChartPageFragment fragment){
        super(R.layout.listitem_line_chart);
        this.mFragment=fragment;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LineChartData item) {
        helper.setText(R.id.listitem_line_chart_date,item.getDate());
        helper.setText(R.id.listitem_line_chart_in,item.getInSum()+"");
        helper.setText(R.id.listitem_line_chart_out,item.getOutSum()+"");
        helper.setText(R.id.listitem_line_chart_sum,(item.getInSum()-item.getOutSum())+"");
    }
    public static class LineChartData {
        private double inSum;
        private double outSum;
        private String date;    //所属时间段
        private int index;
        public LineChartData(int index,String date) {
            this.inSum = 0;
            this.outSum = 0;
            this.index=index;
            this.date = date;
        }

        public void addData(Record data) {
            if (data.getAmount_type() == Tag.IN) this.inSum += data.getAmount();
            else this.outSum += data.getAmount();
        }

        public double getInSum() {
            return inSum;
        }

        public double getOutSum() {
            return outSum;
        }

        public int getIndex() {
            return index;
        }

        public String getDate() {
            return date;
        }

        @Override
        public String toString() {
            return "LineChartData{" +
                    "inSum=" + inSum +
                    ", outSum=" + outSum +
                    ", date='" + date + '\'' +
                    '}';
        }
    }
}
