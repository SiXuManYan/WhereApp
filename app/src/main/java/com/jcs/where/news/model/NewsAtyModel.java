package com.jcs.where.news.model;

import android.util.Log;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.JcsResponse;
import com.jcs.where.api.response.NewsChannelResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function4;
import kotlin.jvm.functions.Function2;
import okhttp3.ResponseBody;

/**
 * create by zyf on 2021/1/18 9:26 下午
 */
public class NewsAtyModel extends BaseModel {

    public void getNewsTabs(BaseObserver<List<NewsChannelResponse>> observer) {
        dealResponse(mRetrofit.getNewsTabs(), observer);
    }

    public void postFollowChannel(String postChannelIds, BaseObserver<Object> observer) {
        dealResponse(mRetrofit.postFollowChannels(postChannelIds), observer);
    }

    public void delFollowChannel(String delChannelIds, BaseObserver<Object> observer) {
        dealResponse(mRetrofit.delFollowChannels(delChannelIds), observer);
    }

    public void updateFollow(String postChannelIds, String delChannelIds, BaseObserver<UpdateFollowZipResponse> observer) {
        Log.e("NewsAtyModel", "updateFollow: " + "");
        Observable<JcsResponse<Object>> post = mRetrofit.postFollowChannels(postChannelIds);
        Observable<JcsResponse<Object>> del = mRetrofit.delFollowChannels(delChannelIds);
        Observable<JcsResponse<UpdateFollowZipResponse>> zip = Observable.zip(post, del, (objectJcsResponse, objectJcsResponse2) -> {
            JcsResponse<UpdateFollowZipResponse> jcsResponse = new JcsResponse<>();
            int code = objectJcsResponse.getCode();
            int code2 = objectJcsResponse2.getCode();
            if (code != 200) {
                jcsResponse.setCode(code);
                jcsResponse.setMessage(objectJcsResponse.getMessage());
                return jcsResponse;
            }

            if (code2 != 200) {
                jcsResponse.setCode(code2);
                jcsResponse.setMessage(objectJcsResponse2.getMessage());
                return jcsResponse;
            }
            jcsResponse.setCode(200);
            UpdateFollowZipResponse zipResponse = new UpdateFollowZipResponse();
            jcsResponse.setData(zipResponse);
            return jcsResponse;
        });

        dealResponse(zip, observer);
    }

    public static class UpdateFollowZipResponse {
    }
}
