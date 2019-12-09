package com.lyh.guanbei.http.api;

import com.lyh.guanbei.bean.RecordBean;
import com.lyh.guanbei.http.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RecordServiceApi {
    //添加记录
    @POST("record/insert")
    Observable<BaseResponse<List<RecordBean>>> insert(@Body List<RecordBean> recordBeanList);
    //删除记录
    @POST("record/delete")
    Observable<BaseResponse<String>> delete(@Body List<Long> idList);
    //更新记录
    @POST("record/update")
    Observable<BaseResponse<String>> update(@Body List<RecordBean> recordBeanList);
    //拉取记录
    @POST("record/queryByUserId")
    Observable<BaseResponse<List<RecordBean>>> queryByUserId(@Body List<Long> userId);
    @POST("record/queryByBookId")
    Observable<BaseResponse<List<RecordBean>>> queryByBookId(@Body List<Long> bookId);
}
