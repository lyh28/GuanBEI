package com.lyh.guanbei.ui.widget.pattenerLocker;

import com.bumptech.glide.load.engine.bitmap_recycle.IntegerArrayAdapter;

import java.util.List;

public interface PatternLockerStatusListener {
    void onStart(List<Integer> points);

    void onChange(List<Integer> points);

    void onEnd(List<Integer> points);

    void onClear();
}
