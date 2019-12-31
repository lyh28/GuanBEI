package com.lyh.guanbei.http.api;


import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.http.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserServiceApi {
    //登录
    @POST("user/login")
    @FormUrlEncoded
    Observable<BaseResponse<User>> login(@Field("user_pwd") String pwd, @Field("user_phone") String phone);
    //注册
    @POST("user/register")
    Observable<BaseResponse<User>> register(@Body User user);
    //查询
    @POST("user/queryById")
    Observable<BaseResponse<List<User>>> queryById(@Body List<Long> idList);
    @GET("user/queryByPhone/{phone}")
    Observable<BaseResponse<User>> queryByPhone(@Path("phone")String phone);
}
