package com.lyh.guanbei.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.ArrayList;
import java.util.List;

@Entity
public class DeleteRecord {
    @Id
    private Long record_id;


    @Generated(hash = 1237187819)
    public DeleteRecord(Long record_id) {
        this.record_id = record_id;
    }
    @Generated(hash = 1885440116)
    public DeleteRecord() {
    }


    public static List<DeleteRecord> createDeleteRecordBean(List<Long> idList){
        List<DeleteRecord> list=new ArrayList<>();
        for(Long id:idList)
            list.add(new DeleteRecord(id));
        return list;
    }
    public static DeleteRecord createDeleteRecordBean(long id){
        return new DeleteRecord(id);
    }
    public Long getRecord_id() {
        return this.record_id;
    }

    public void setRecord_id(long record_id) {
        this.record_id = record_id;
    }
    public void setRecord_id(Long record_id) {
        this.record_id = record_id;
    }
}
