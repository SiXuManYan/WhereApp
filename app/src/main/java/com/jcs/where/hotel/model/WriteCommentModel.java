package com.jcs.where.hotel.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.JcsResponse;
import com.jcs.where.api.request.WriteHotelCommentRequest;
import com.jcs.where.api.response.SuccessResponse;
import com.jcs.where.api.response.UploadFileResponse;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * create by zyf on 2021/2/22 12:11 上午
 */
public class WriteCommentModel extends BaseModel {

    public void postUploadFile(String fileDirPath, BaseObserver<UploadFileResponse> observer) {

        // 参照 https://blog.csdn.net/zhijiandedaima/article/details/84587291
        File file = new File(fileDirPath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        String type = "1";
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), type);

        dealResponse(mRetrofit.uploadFile(description, body), observer);
    }

    public void postHotelComment(WriteHotelCommentRequest request, BaseObserver<SuccessResponse> observer) {
        dealResponse(mRetrofit.postHotelComment(request), observer);
    }
}
