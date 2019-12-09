package com.lyh.guanbei.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.ArrayList;
import java.util.List;

@Entity
public class DeleteRecordBean {
    @Id
    private Long record_id;

    @Generated(hash = 1579055913)
    public DeleteRecordBean(Long record_id) {
        this.record_id = record_id;
    }
    @Generated(hash = 222112861)
    public DeleteRecordBean() {
    }

    public static List<DeleteRecordBean> createDeleteRecordBean(List<Long> idList){
        List<DeleteRecordBean> list=new ArrayList<>();
        for(Long id:idList)
            list.add(new DeleteRecordBean(id));
        return list;
    }
    public static DeleteRecordBean createDeleteRecordBean(long id){
        return new DeleteRecordBean(id);
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
