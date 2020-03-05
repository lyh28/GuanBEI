package com.lyh.guanbei.Repository;

import androidx.lifecycle.ViewModel;

public class DataViewModel extends ViewModel {
    private RecordRepository mRrecordRepository= RecordRepository.getSingleton();
    private BookRepository mBookRepository=BookRepository.getSingleton();
    public RecordRepository getRecordRespository(){
        return mRrecordRepository;
    }
    public BookRepository getBookRepository(){
        return mBookRepository;
    }

}
