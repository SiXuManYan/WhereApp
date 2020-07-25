package com.jcs.where.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.jcs.where.Const;
import com.jcs.where.api.Api;
import com.jcs.where.bean.ImageBean;
import com.jcs.where.utils.ConvertUtils;
import com.jcs.where.utils.ImageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import co.tton.android.base.app.activity.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.FuncN;
import rx.schedulers.Schedulers;

public class UploadFilePresenter {

    private HashMap<String, String> mUploadRecordMap = new HashMap<>(); //存放上传记录
    private Context mContext;

    public UploadFilePresenter(Context context) {
        mContext = context;
    }

    public Observable<List<String>> uploadFiles(final List<String> pathList) {
        List<Observable<String>> obList = new ArrayList<>();
        for (final String path : pathList) {
            Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(final Subscriber<? super String> subscriber) {
                    if (TextUtils.isEmpty(path)) {
                        subscriber.onNext("");
                        subscriber.onCompleted();
                    } else if (!ImageUtils.isLocalPath(path)) {
                        String newPath = path.substring(Const.IMAGE_ROOT_PATH.length(), path.length());
                        subscriber.onNext(newPath);
                        subscriber.onCompleted();
                    } else {
                        if (mUploadRecordMap.get(path) == null) {
                            Subscription subscription = Api.get().uploadFile("1", ConvertUtils.convertToPartBody("file", new File(path)))
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new Subscriber<ImageBean>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            subscriber.onError(e);
                                        }

                                        @Override
                                        public void onNext(ImageBean imageBean) {
                                            mUploadRecordMap.put(path, imageBean.image);
                                            subscriber.onNext(imageBean.image);
                                            subscriber.onCompleted();
                                        }
                                    });
                            ((BaseActivity) mContext).addSubscription(subscription);

                        } else {
                            subscriber.onNext(mUploadRecordMap.get(path));
                            subscriber.onCompleted();
                        }
                    }
                }
            });
            obList.add(observable);
        }
        if (!obList.isEmpty()) {
            return Observable.zip(obList, new FuncN<List<String>>() {
                @Override
                public List<String> call(Object... args) {
                    return (List<String>) (List) Arrays.asList(args);
                }
            });
        } else {
            List<String> newPathList = new ArrayList<>();
            return Observable.just(newPathList);
        }
    }
}
