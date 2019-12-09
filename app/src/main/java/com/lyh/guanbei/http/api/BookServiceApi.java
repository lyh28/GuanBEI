package com.lyh.guanbei.http.api;

import com.lyh.guanbei.bean.BookBean;
import com.lyh.guanbei.bean.UserBean;
import com.lyh.guanbei.http.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface BookServiceApi {
    @POST("/book/insert")
    Observable<BaseResponse<List<BookBean>>> insert(@Body List<BookBean> bookList);
    @POST("/book/queryForList")
    Observable<BaseResponse<List<BookBean>>> query(@Body List<Long> idList);
    @POST("/book/update")
    Observable<BaseResponse<String>> update(@Body List<BookBean> bookList);
    @POST("/book/deleteFromList")
    Observable<BaseResponse<String>> deleteFromList(@Body List<Long> ids);
    @GET("/book/delete/{id}")
    Observable<BaseResponse<String>> delete(@Path("id") long id);
    @POST("/book/deleteUser")
    @FormUrlEncoded
    Observable<BaseResponse<String>> deleteUser(@Field("userId") long userId,@Field("bookId") long bookId);
    @POST("/book/addUser")
    @FormUrlEncoded
    Observable<BaseResponse<String>> addUser(@Field("userId") long userId, @Field("bookId") long bookId);
    @POST("/book/addUserRequest")
    @FormUrlEncoded
    Observable<BaseResponse<String>> addUserRequest(@Field("userId") long userId,@Field("requestId")long requestId,@Field("bookId") long bookId);
    @POST("/book/changeManager")
    @FormUrlEncoded
    Observable<BaseResponse<BookBean>> changeManager(@Field("oldId") long oldId,@Field("newId") long newId,@Field("bookId") long bookId);
}
