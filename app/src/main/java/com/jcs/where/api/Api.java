package com.jcs.where.api;

import com.jcs.where.Const;
import com.jcs.where.bean.HomeBannerBean;

import java.util.List;

import co.tton.android.base.api.ApiResult;
import co.tton.android.base.api.RxApiClient;
import retrofit2.http.GET;
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
        @GET("commonapi/v1/banners")
        Observable<ApiResult<List<HomeBannerBean>>> getHomeBannerList();

    }
}
