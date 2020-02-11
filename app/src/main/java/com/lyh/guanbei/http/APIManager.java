package com.lyh.guanbei.http;

import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.http.api.BookServiceApi;
import com.lyh.guanbei.http.api.RecordServiceApi;
import com.lyh.guanbei.http.api.UserServiceApi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.Retrofit;

public class APIManager {
    //登录注册
    public static void logIn(String pwd,String phone,BaseObscriber<User> baseObscriber){
        getRetrofit().create(UserServiceApi.class).login(pwd,phone).compose(getMainThreadTransformer()).subscribe(baseObscriber);
    }
    public static void register(User user, BaseObscriber<User> baseObscriber){
        getRetrofit().create(UserServiceApi.class).register(user).compose(getMainThreadTransformer()).subscribe(baseObscriber);
    }
    public static void updateIcon(long id, MultipartBody.Part icon,BaseObscriber<User> baseObscriber){
        getRetrofit().create(UserServiceApi.class).updateIcon(id,icon).compose(getMainThreadTransformer()).subscribe(baseObscriber);
    }
    public static void update(User user,BaseObscriber<User> baseObscriber){
        getRetrofit().create(UserServiceApi.class).update(user).compose(getMainThreadTransformer()).subscribe(baseObscriber);
    }
    public static void queryUserById(List<Long> id,BaseObscriber<List<User>> baseObscriber){
        getRetrofit().create(UserServiceApi.class).queryById(id).compose(getMainThreadTransformer()).subscribe(baseObscriber);
    }
    public static void queryUserByPhone(String phone,BaseObscriber<User> baseObscriber){
        getRetrofit().create(UserServiceApi.class).queryByPhone(phone).compose(getMainThreadTransformer()).subscribe(baseObscriber);
    }
    //账单记录操作
    public static void commitRecord(Record record, BaseObscriber<List<Record>> baseObscriber){
        List<Record> list=new ArrayList<>();
        list.add(record);
        commitRecord(list,baseObscriber);
    }
    public static void commitRecord(List<Record> record, BaseObscriber<List<Record>> baseObscriber){
        getRetrofit().create(RecordServiceApi.class).insert(record).compose(getIOThreadTransformer()).map(new Function() {
            @Override
            public Object apply(Object o) throws Exception {
                BaseResponse<List<Record>> baseResponse=(BaseResponse<List<Record>>)o;
                List<Record> list=baseResponse.getData();
                for(Record r:list)
                    r.setStatus(DBManager.CLIENT_SERVER_STATUS);
                return baseResponse;
            }
        }).subscribe(baseObscriber);
    }
    public static void deleteRecord(List<Long> recordId,BaseObscriber<String> baseObscriber){
        getRetrofit().create(RecordServiceApi.class).delete(recordId).compose(getIOThreadTransformer()).subscribe(baseObscriber);
    }
    public static void updateRecord(Record record, BaseObscriber<String> baseObscriber){
        List<Record> list=new ArrayList<>();
        list.add(record);
        updateRecord(list,baseObscriber);
    }
    public static void updateRecord(List<Record> record, BaseObscriber<String> baseObscriber){
        getRetrofit().create(RecordServiceApi.class).update(record).compose(getIOThreadTransformer()).subscribe(baseObscriber);
    }
    public static void queryRecordByUserId(List<Long> userId,BaseObscriber<List<Record>> baseObscriber){
        getRetrofit().create(RecordServiceApi.class).queryByUserId(userId).compose(getMainThreadTransformer()).map(new Function() {
            @Override
            public Object apply(Object o) throws Exception {
                BaseResponse<List<Record>> baseResponse=(BaseResponse<List<Record>>)o;
                List<Record> list=baseResponse.getData();
                for(Record r:list)
                    r.setStatus(DBManager.CLIENT_SERVER_STATUS);
                return baseResponse;
            }
        }).subscribe(baseObscriber);
    }
    public static void queryRecordByBookId(List<Long> bookId,BaseObscriber<List<Record>> baseObscriber){
        getRetrofit().create(RecordServiceApi.class).queryByBookId(bookId).compose(getMainThreadTransformer()).map(new Function() {
            @Override
            public Object apply(Object o) throws Exception {
                BaseResponse<List<Record>> baseResponse=(BaseResponse<List<Record>>)o;
                List<Record> list=baseResponse.getData();
                for(Record r:list)
                    r.setStatus(DBManager.CLIENT_SERVER_STATUS);
                return baseResponse;
            }
        }).subscribe(baseObscriber);
    }
    //账单操作
    public static void insertBook(List<Book> bookBean, BaseObscriber<List<Book>> baseObscriber){
        getRetrofit().create(BookServiceApi.class).insert(bookBean).compose(getIOThreadTransformer()).map(new Function() {
            @Override
            public Object apply(Object o) throws Exception {
                BaseResponse<List<Book>> baseResponse=(BaseResponse<List<Book>>)o;
                List<Book> data=baseResponse.getData();
                for(Book book:data)
                    book.setStatus(DBManager.CLIENT_SERVER_STATUS);
                return baseResponse;
            }
        }).subscribe(baseObscriber);
    }
    public static void queryBook(List<Long> idList,BaseObscriber<List<Book>> baseObscriber){
        getRetrofit().create(BookServiceApi.class).query(idList).compose(getMainThreadTransformer()).map(new Function() {
            @Override
            public Object apply(Object o) throws Exception {
                BaseResponse<List<Book>> baseResponse=(BaseResponse<List<Book>>)o;
                for(Book book:baseResponse.getData())
                    book.setStatus(DBManager.CLIENT_SERVER_STATUS);
                return baseResponse;
            }
        }).subscribe(baseObscriber);
    }
    public static void updateBook(List<Book> book, BaseObscriber<String> baseObscriber){
        getRetrofit().create(BookServiceApi.class).update(book).compose(getIOThreadTransformer()).subscribe(baseObscriber);
    }
    public static void deleteBookFromList(List<Long> bookIds,BaseObscriber<String> baseObscriber){
        getRetrofit().create(BookServiceApi.class).deleteFromList(bookIds).compose(getIOThreadTransformer()).subscribe(baseObscriber);
    }
    public static void deleteBook(long bookId,BaseObscriber<String> baseObscriber){
        getRetrofit().create(BookServiceApi.class).delete(bookId).compose(getIOThreadTransformer()).subscribe(baseObscriber);
    }
    public static void deleteBookUser(long userId,long bookId,BaseObscriber<String> baseObscriber){
        getRetrofit().create(BookServiceApi.class).deleteUser(userId,bookId).compose(getIOThreadTransformer()).subscribe(baseObscriber);
    }
    //直接添加
    public static void addBookUser(long userId,long bookId,BaseObscriber<Book> baseObscriber){
        getRetrofit().create(BookServiceApi.class).addUser(userId,bookId).compose(getMainThreadTransformer()).map(new Function() {
            @Override
            public Object apply(Object o) throws Exception {
                BaseResponse<Book> baseResponse=(BaseResponse<Book>)o;
                baseResponse.getData().setStatus(DBManager.CLIENT_SERVER_STATUS);
                return baseResponse;
            }
        }).subscribe(baseObscriber);
    }
    //发起请求，需要等待对方确认
    public static void addUserRequest(long userId,long requestId,long bookId,BaseObscriber<String> baseObscriber){
        getRetrofit().create(BookServiceApi.class).addUserRequest(userId,requestId,bookId).compose(getMainThreadTransformer()).subscribe(baseObscriber);
    }
    //更改管理员
    public static void changeManager(long oldId,long newId,long bookId,BaseObscriber<Book> baseObscriber){
        getRetrofit().create(BookServiceApi.class).changeManager(oldId, newId, bookId).compose(getMainThreadTransformer()).map(new Function() {
            @Override
            public Object apply(Object o) throws Exception {
                BaseResponse<Book> baseResponse=(BaseResponse<Book>)o;
                baseResponse.getData().setStatus(DBManager.CLIENT_SERVER_STATUS);
                return baseResponse;
            }
        }).subscribe(baseObscriber);
    }


    private static Retrofit getRetrofit(){
        return RetrofitManager.getRetrofit();
    }

    private static ObservableTransformer getMainThreadTransformer(){
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
    private static ObservableTransformer getIOThreadTransformer(){
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io());
            }
        };
    }
}
