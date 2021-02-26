package com.jcs.where.mine.model.merchant_settled;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.JcsResponse;
import com.jcs.where.api.request.MerchantSettledRequest;
import com.jcs.where.api.response.SuccessResponse;
import com.jcs.where.api.response.UploadFileResponse;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * create by zyf on 2021/2/26 8:49 下午
 */
public class MerchantSettledModel extends BaseModel {

    public void postMerchant(MerchantSettledRequest request, BaseObserver<SuccessResponse> observer) {
        dealResponse(mRetrofit.postMerchant(request), observer);
    }

    public void postUploadFile(String fileDirPath, BaseObserver<UploadFileResponse> observer) {

        // 参照 https://blog.csdn.net/zhijiandedaima/article/details/84587291
        File file = new File(fileDirPath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        String type = "1";
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), type);

        dealResponse(mRetrofit.uploadFile(description, body), observer);
    }
}
