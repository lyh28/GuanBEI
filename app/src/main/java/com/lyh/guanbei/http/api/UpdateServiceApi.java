package com.lyh.guanbei.http.api;

import com.lyh.guanbei.bean.Apk;
import com.lyh.guanbei.http.BaseResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface UpdateServiceApi {
    @GET("update/getNewVersion")
    Observable<BaseResponse<Apk>> getNewVersion();
    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Header("RANGE") String start,@Url String url);
}
