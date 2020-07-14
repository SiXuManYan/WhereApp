package com.jcs.where.api;

import com.jcs.where.Const;
import com.jcs.where.bean.ImageBean;

import co.tton.android.base.api.RxApiClient;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

public class Api {

    private static volatile Api sInstance;

    private InternalApi mApi;

    private Api() {
        RxApiClient.setBaseUrl(Const.API_ENDPOINT);
        mApi = RxApiClient.get().createApi(InternalApi.class);
    }

    public static InternalApi get() {
        if (sInstance == null) {
            synchronized (Api.class) {
                if (sInstance == null) {
                    sInstance = new Api();
                }
            }
        }
        return sInstance.mApi;
    }

    public interface InternalApi {

        //上传图片
        @Multipart
        @POST("commonapi/v1/file")
        Observable<ImageBean> uploadFile(
                @Query("type") String type,
                @Part MultipartBody.Part params);
    }
}
