package com.lyh.guanbei.manager;

import android.util.Log;

import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.bean.Tag;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.db.RecordDao;
import com.lyh.guanbei.util.DateUtil;
import com.lyh.guanbei.util.LogUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookManager {
    private static BookManager singleton;
    //总支出   Key:bookLocalId
    private Map<Long, Double> mAllOutMap;
    //总收入
    private Map<Long, Double> mAllInMap;
    //当月支出
    private Map<Long, Double> mOutMap;
    //当月收入
    private Map<Long, Double> mInMap;

    private BookManager() {
        mAllOutMap = new HashMap<>();
        mAllInMap = new HashMap<>();
        mOutMap = new HashMap<>();
        mInMap = new HashMap<>();
        //如果第一次登陆的话就查询服务端   需要设置强制退出
    }

    public void init() {
//        List<Book> list = Book.queryByUserId();
//        for (Book book : list) {
//            LogUtil.logD(book.toString());
//            List<Record> recordList=Record.query(RecordDao.Properties.Book_local_id.eq(book.getLocal_id()));
//            for(Record r:recordList)
//                addRecord(r);
//        }
//        LogUtil.logD("总支出 "+mAllOutMap);
//        LogUtil.logD("总收入 "+mAllOutMap);
//        LogUtil.logD("当月支出 "+mOutMap);
//        LogUtil.logD("当月收入 "+mInMap);
    }

    public void addRecord(Record record) {
        if (record.getAmount_type() == Tag.IN) {
            addToMap(mAllInMap, record);
            if(checkThisMonth(record))
                addToMap(mInMap, record);
        } else {
            addToMap(mAllOutMap, record);
            if(checkThisMonth(record))
                addToMap(mOutMap, record);
        }
    }

    public void deleteRecord(Record record) {
        if (record.getAmount_type() == Tag.IN)
            deleteFromMap(mAllInMap, record);
        else
            deleteFromMap(mAllOutMap, record);
    }

    private void addToMap(Map<Long, Double> map, Record record) {
        LogUtil.logD("添加  "+record);
        long localId = record.getBook_local_id();
        double value = map.get(localId) == null ? 0 : map.get(localId);
        map.put(localId, value + Math.abs(record.getAmount()));
    }

    private void deleteFromMap(Map<Long, Double> map, Record record) {
        long localId = record.getBook_local_id();
        double value = map.get(localId) == null ? 0 : map.get(localId);
        map.put(localId, value - Math.abs(record.getAmount()));
    }

    private boolean checkThisMonth(Record record) {
        if (record.getDate().compareTo(DateUtil.getNowDateTimeWithoutSecond()) < 0
                && record.getDate().compareTo(DateUtil.getMonthFirstDay()) > 0) {
            return true;
        }
        return false;
    }

    public static BookManager getInstance() {
        if (singleton == null) {
            synchronized (BookManager.class) {
                if (singleton == null)
                    singleton = new BookManager();
            }
        }
        return singleton;
    }
}
