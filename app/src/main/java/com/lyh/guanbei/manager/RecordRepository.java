package com.lyh.guanbei.manager;

import com.lyh.guanbei.bean.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecordRepository extends ViewModel {
    private static RecordRepository mSinleton;
    private Map<Long, MutableLiveData<List<Record>>>  recordMap;        //key:bookid
    private RecordRepository(){}
    public static RecordRepository getSingleton(){
        if(mSinleton==null){
            synchronized (RecordRepository.class){
                if(mSinleton==null) {
                    mSinleton = new RecordRepository();
                    mSinleton.init();
                }
            }
        }
        return mSinleton;
    }
    private void init(){
        //填充数据
        List<Record> list=DBManager.getInstance().getDaoSession().getRecordDao().loadAll();
        for(Record r:list){
            long bookId=r.getBook_local_id();
            if(recordMap.containsKey(bookId)){
                recordMap.get(bookId).getValue().add(r);
            }else{
                List<Record> value=new ArrayList<>();
                value.add(r);
                MutableLiveData<List<Record>> mutableLiveData=new MutableLiveData<>();
                mutableLiveData.setValue(value);
                recordMap.put(bookId,mutableLiveData);
            }
        }
    }
    private void addRecord(Record record){
        MutableLiveData<List<Record>> data=recordMap.get(record.getBook_local_id());
        List<Record> records=data.getValue();
        records.add(record);
        data.setValue(records);
    }
    private void removeRecord(Record record){
        MutableLiveData<List<Record>> data=recordMap.get(record.getBook_local_id());
        List<Record> records=data.getValue();
        records.remove(record);
        data.setValue(records);
    }
}
