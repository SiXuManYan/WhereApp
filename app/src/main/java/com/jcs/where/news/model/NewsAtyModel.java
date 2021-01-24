package com.jcs.where.news.model;

import android.util.Log;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.NewsChannelResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * create by zyf on 2021/1/18 9:26 下午
 */
public class NewsAtyModel extends BaseModel {

    public void getNewsTabs(BaseObserver<List<NewsChannelResponse>> observer) {
        dealResponse(mRetrofit.getNewsTabs(), observer);
    }

    public void postFollowChannel(String postChannelIds, BaseObserver<ResponseBody> observer) {
        dealResponse(mRetrofit.postFollowChannels(postChannelIds), observer);
    }

    public void delFollowChannel(String delChannelIds, BaseObserver<ResponseBody> observer) {
        dealResponse(mRetrofit.delFollowChannels(delChannelIds), observer);
    }

    public void updateFollow(String postChannelIds, String delChannelIds, BaseObserver<UpdateFollowZipResponse> observer) {
        Log.e("NewsAtyModel", "updateFollow: "+"");
        Observable<ResponseBody> post = mRetrofit.postFollowChannels(postChannelIds);
        Observable<ResponseBody> del = mRetrofit.delFollowChannels(delChannelIds);
        Observable<UpdateFollowZipResponse> zip = Observable.zip(post, del, UpdateFollowZipResponse::new);

        dealResponse(zip, observer);
    }

    public static class UpdateFollowZipResponse {
        ResponseBody follow;
        ResponseBody del;

        public UpdateFollowZipResponse(ResponseBody follow, ResponseBody del) {
            this.follow = follow;
            this.del = del;
        }
    }
}
