package com.lyh.guanbei;

import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.manager.RecordRepository;

import androidx.lifecycle.ViewModel;

public class DataViewModel extends ViewModel {
    private RecordRepository mRrecordRepository= RecordRepository.getSingleton();
    public RecordRepository getRecordRespository(){
        return mRrecordRepository;
    }
}
