package com.lyh.guanbei.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lyh.guanbei.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

public class TopListAdapter extends TagAdapter<String> {
    private final String BLACK_COLOR="#000000";
    private final String WHITE_COLOR="#FFFFFF";
    private Context context;
    private LayoutInflater layoutInflater;
    public TopListAdapter(List<String> datas,Context context) {
        super(datas);
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
    }
    @Override
    public View getView(FlowLayout parent, int position, String s) {
        TextView tv=(TextView) layoutInflater.inflate(R.layout.listitem_text,parent,false);
        tv.setText(s);
        return tv;
    }

    @Override
    public void onSelected(int position, View view) {
        TextView tv=(TextView)view;
        tv.setTextColor(Color.parseColor(WHITE_COLOR));
        tv.setBackgroundResource(R.drawable.corners_shape_normal_bg);
    }
    @Override
    public void unSelected(int position, View view) {
        TextView tv=(TextView)view;
        tv.setTextColor(Color.parseColor(BLACK_COLOR));
        tv.setBackgroundResource(0);
    }
}
