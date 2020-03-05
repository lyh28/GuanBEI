package com.lyh.guanbei.Repository;

import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.db.RecordDao;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;

public class RecordRepository extends LiveData<List<Record>> {
    private static RecordRepository mSingleton;
    private Map<Long, List<Record>>  recordMap;        //key:bookid
    private RecordRepository(){
        recordMap=new HashMap<>();
    }
    public static RecordRepository getSingleton(){
        if(mSingleton==null){
            synchronized (RecordRepository.class){
                if(mSingleton==null) {
                    mSingleton = new RecordRepository();
                }
            }
        }
        return mSingleton;
    }
    public void init(){
        recordMap.clear();
        //填充数据
        User user= CustomSharedPreferencesManager.getInstance().getUser();
        List<Long> bookIdList=Util.getLongFromData(user.getLocal_book_id());
        List<Record> list= Record.query(RecordDao.Properties.Book_local_id.in(bookIdList));
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
    public void updateRecord(Record record){
        List<Record> records=recordMap.get(record.getBook_local_id());
        for(int i=0;i<records.size();i++){
            if(records.get(i).getLocal_id()==record.getLocal_id()) {
                records.set(i, record);
                break;
            }
        }
        postValue(records);
    }
    public void removeRecord(Record record){
        List<Record> records=recordMap.get(record.getBook_local_id());
        records.remove(record);
    }
}
