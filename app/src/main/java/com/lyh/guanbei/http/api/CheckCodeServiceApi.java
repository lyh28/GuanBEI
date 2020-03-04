package com.lyh.guanbei.http.api;

import com.lyh.guanbei.bean.CheckCode;
import com.lyh.guanbei.http.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CheckCodeServiceApi {
    @POST("/checkCode/sendCheckCode")
    @FormUrlEncoded
    Observable<BaseResponse<String>> sendCheckCode(@Field("phone")String phone);
    @POST("/checkCode/checkCheckCode")
    Observable<BaseResponse<String>> checkdCheckCode(@Body CheckCode checkCode);
}
