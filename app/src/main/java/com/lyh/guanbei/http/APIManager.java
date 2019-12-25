package com.lyh.guanbei.http;

import com.lyh.guanbei.bean.BookBean;
import com.lyh.guanbei.bean.RecordBean;
import com.lyh.guanbei.bean.UserBean;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.db.DBManager;
import com.lyh.guanbei.http.api.BookServiceApi;
import com.lyh.guanbei.http.api.RecordServiceApi;
import com.lyh.guanbei.http.api.UserServiceApi;
import com.lyh.guanbei.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class APIManager {
    //登录注册
    public static void logIn(String pwd,String phone,BaseObscriber<UserBean> baseObscriber){
        getRetrofit().create(UserServiceApi.class).login(pwd,phone).compose(getMainThreadTransformer()).subscribe(baseObscriber);
    }
    public static void register(UserBean user,BaseObscriber<UserBean> baseObscriber){
        getRetrofit().create(UserServiceApi.class).register(user).compose(getMainThreadTransformer()).subscribe(baseObscriber);
    }
    public static void queryUserById(List<Long> id,BaseObscriber<List<UserBean>> baseObscriber){
        getRetrofit().create(UserServiceApi.class).queryById(id).compose(getMainThreadTransformer()).subscribe(baseObscriber);
    }
    public static void queryUserByPhone(String phone,BaseObscriber<UserBean> baseObscriber){
        getRetrofit().create(UserServiceApi.class).queryByPhone(phone).compose(getMainThreadTransformer()).subscribe(baseObscriber);
    }
    //账单记录操作
    public static void commitRecord(RecordBean recordBean, BaseObscriber<List<RecordBean>> baseObscriber){
        List<RecordBean> list=new ArrayList<>();
        list.add(recordBean);
        commitRecord(list,baseObscriber);
    }
    public static void commitRecord(List<RecordBean> recordBean, BaseObscriber<List<RecordBean>> baseObscriber){
        getRetrofit().create(RecordServiceApi.class).insert(recordBean).compose(getIOThreadTransformer()).map(new Function() {
            @Override
            public Object apply(Object o) throws Exception {
                BaseResponse<List<RecordBean>> baseResponse=(BaseResponse<List<RecordBean>>)o;
                List<RecordBean> list=baseResponse.getData();
                for(RecordBean r:list)
                    r.setStatus(DBManager.CLIENT_SERVER_STATUS);
                return baseResponse;
            }
        }).subscribe(baseObscriber);
    }
    public static void deleteRecord(List<Long> recordId,BaseObscriber<String> baseObscriber){
        getRetrofit().create(RecordServiceApi.class).delete(recordId).compose(getIOThreadTransformer()).subscribe(baseObscriber);
    }
    public static void updateRecord(RecordBean recordBean,BaseObscriber<String> baseObscriber){
        List<RecordBean> list=new ArrayList<>();
        list.add(recordBean);
        updateRecord(list,baseObscriber);
    }
    public static void updateRecord(List<RecordBean> recordBean,BaseObscriber<String> baseObscriber){
        getRetrofit().create(RecordServiceApi.class).update(recordBean).compose(getIOThreadTransformer()).subscribe(baseObscriber);
    }
    public static void queryRecordByUserId(List<Long> userId,BaseObscriber<List<RecordBean>> baseObscriber){
        getRetrofit().create(RecordServiceApi.class).queryByUserId(userId).compose(getMainThreadTransformer()).map(new Function() {
            @Override
            public Object apply(Object o) throws Exception {
                BaseResponse<List<RecordBean>> baseResponse=(BaseResponse<List<RecordBean>>)o;
                List<RecordBean> list=baseResponse.getData();
                for(RecordBean r:list)
                    r.setStatus(DBManager.CLIENT_SERVER_STATUS);
                return baseResponse;
            }
        }).subscribe(baseObscriber);
    }
    public static void queryRecordByBookId(List<Long> bookId,BaseObscriber<List<RecordBean>> baseObscriber){
        getRetrofit().create(RecordServiceApi.class).queryByBookId(bookId).compose(getMainThreadTransformer()).map(new Function() {
            @Override
            public Object apply(Object o) throws Exception {
                BaseResponse<List<RecordBean>> baseResponse=(BaseResponse<List<RecordBean>>)o;
                List<RecordBean> list=baseResponse.getData();
                for(RecordBean r:list)
                    r.setStatus(DBManager.CLIENT_SERVER_STATUS);
                return baseResponse;
            }
        }).subscribe(baseObscriber);
    }
    //账单操作
    public static void insertBook(List<BookBean> bookBean,BaseObscriber<List<BookBean>> baseObscriber){
        getRetrofit().create(BookServiceApi.class).insert(bookBean).compose(getIOThreadTransformer()).map(new Function() {
            @Override
            public Object apply(Object o) throws Exception {
                BaseResponse<List<BookBean>> baseResponse=(BaseResponse<List<BookBean>>)o;
                List<BookBean> data=baseResponse.getData();
                for(BookBean book:data)
                    book.setStatus(DBManager.CLIENT_SERVER_STATUS);
                return baseResponse;
            }
        }).subscribe(baseObscriber);
    }
    public static void queryBook(List<Long> idList,BaseObscriber<List<BookBean>> baseObscriber){
        getRetrofit().create(BookServiceApi.class).query(idList).compose(getMainThreadTransformer()).map(new Function() {
            @Override
            public Object apply(Object o) throws Exception {
                BaseResponse<List<BookBean>> baseResponse=(BaseResponse<List<BookBean>>)o;
                for(BookBean book:baseResponse.getData())
                    book.setStatus(DBManager.CLIENT_SERVER_STATUS);
                return baseResponse;
            }
        }).subscribe(baseObscriber);
    }
    public static void updateBook(List<BookBean> book,BaseObscriber<String> baseObscriber){
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
    public static void addBookUser(long userId,long bookId,BaseObscriber<BookBean> baseObscriber){
        getRetrofit().create(BookServiceApi.class).addUser(userId,bookId).compose(getMainThreadTransformer()).map(new Function() {
            @Override
            public Object apply(Object o) throws Exception {
                BaseResponse<BookBean> baseResponse=(BaseResponse<BookBean>)o;
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
    public static void changeManager(long oldId,long newId,long bookId,BaseObscriber<BookBean> baseObscriber){
        getRetrofit().create(BookServiceApi.class).changeManager(oldId, newId, bookId).compose(getMainThreadTransformer()).map(new Function() {
            @Override
            public Object apply(Object o) throws Exception {
                BaseResponse<BookBean> baseResponse=(BaseResponse<BookBean>)o;
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
