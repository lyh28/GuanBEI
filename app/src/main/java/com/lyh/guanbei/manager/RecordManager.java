package com.lyh.guanbei.manager;

import android.util.SparseIntArray;

import com.lyh.guanbei.bean.Record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordManager {
    private static RecordManager singleton;
    private Map<Integer, List<Record>> mRecordMap;       //不同账本的本地ID对应不同的List
    private SparseIntArray mSum;

    private RecordManager(){
        mSum=new SparseIntArray();
        mRecordMap=new HashMap<>();
        //如果第一次登陆的话就查询服务端   需要设置强制退出

    }
    public static RecordManager getInstance(){
        if(singleton==null){
            synchronized (RecordManager.class){
                if(singleton==null)
                    singleton=new RecordManager();
            }
        }
        return singleton;
    }
}
