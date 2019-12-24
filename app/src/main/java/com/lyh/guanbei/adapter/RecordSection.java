package com.lyh.guanbei.adapter;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.lyh.guanbei.bean.RecordBean;

public class RecordSection extends SectionEntity<RecordBean> {
    public RecordSection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public RecordSection(RecordBean recordBean) {
        super(recordBean);
    }
}
