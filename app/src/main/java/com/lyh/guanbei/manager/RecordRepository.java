package com.lyh.guanbei.manager;

import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.util.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;

public class RecordRepository extends LiveData<List<Record>> {
    private static RecordRepository mSinleton;
    private Map<Long, List<Record>>  recordMap;        //key:bookid
    private RecordRepository(){
        recordMap=new HashMap<>();
        LogUtil.logD("初始化");
    }
    public static RecordRepository getSingleton(){
        if(mSinleton==null){
            synchronized (RecordRepository.class){
                if(mSinleton==null) {
                    mSinleton = new RecordRepository();
                }
            }
        }
        return mSinleton;
    }
    public void init(){
        recordMap.clear();
        //填充数据
        List<Record> list=DBManager.getInstance().getDaoSession().getRecordDao().loadAll();
        for(Record r:list){
            long bookId=r.getBook_local_id();
            if(recordMap.containsKey(bookId)){
                recordMap.get(bookId).add(r);
            }else{
                List<Record> value=new ArrayList<>();
                value.add(r);
                recordMap.put(bookId,value);
            }
        }
        LogUtil.logD("init   "+recordMap.keySet());
    }
    public List<Record> getRecordLiveData(long bookId){
        return recordMap.get(bookId);
    }
    public void addRecord(Record record){
        List<Record> records=recordMap.get(record.getBook_local_id());
        records.add(record);
        postValue(records);
    }
    public void addRecord(List<Record> recordList){
        if(recordList.size()==0) return;
        List<Record> records=recordMap.get(recordList.get(0).getBook_local_id());
        if(records==null)
            records=new ArrayList<>();
        records.addAll(recordList);
        Collections.sort(records, new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                int dateCom=o2.getDate().compareTo(o1.getDate());
                if(dateCom!=0)
                    return dateCom;
                else return (int)(o2.getLocal_id()-o1.getLocal_id());
            }
        });
        postValue(records);
    }
    public void removeRecord(Record record){
        List<Record> records=recordMap.get(record.getBook_local_id());
        records.remove(record);
    }
}
