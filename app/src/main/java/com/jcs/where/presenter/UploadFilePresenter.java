package com.jcs.where.presenter;

import android.content.Context;

import java.util.HashMap;

public class UploadFilePresenter {

    private final HashMap<String, String> mUploadRecordMap = new HashMap<>(); //存放上传记录
    private final Context mContext;

    public UploadFilePresenter(Context context) {
        mContext = context;
    }

}
