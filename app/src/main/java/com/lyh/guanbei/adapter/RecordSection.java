package com.lyh.guanbei.adapter;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.lyh.guanbei.bean.Record;

public class RecordSection extends SectionEntity<Record> {
    public RecordSection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public RecordSection(Record record) {
        super(record);
    }
}
