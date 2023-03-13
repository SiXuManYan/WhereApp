package com.jiechengsheng.city.features.setting.information;

import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;
import com.jiechengsheng.city.BaseApplication;
import com.jiechengsheng.city.api.network.BaseMvpObserver;
import com.jiechengsheng.city.api.network.BaseMvpPresenter;
import com.jiechengsheng.city.api.request.UpdateUserInfoRequest;
import com.jiechengsheng.city.api.response.UploadFileResponse;
import com.jiechengsheng.city.api.response.UserInfoResponse;
import com.jiechengsheng.city.storage.dao.UserDao;
import com.jiechengsheng.city.storage.entity.User;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Wangsw  2021/2/4 12:00.
 */
public class ModifyInfoPresenter extends BaseMvpPresenter {

    private ModifyInfoView mView;

    public ModifyInfoPresenter(ModifyInfoView baseMvpView) {
        super(baseMvpView);
        mView = baseMvpView;
    }

    public void uploadFile(String fileDirPath, String nickName) {

        // 参照 https://blog.csdn.net/zhijiandedaima/article/details/84587291
        File file = new File(fileDirPath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        String type = "1";
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), type);


        requestApi(mRetrofit.uploadFile(description, body), new BaseMvpObserver<UploadFileResponse>(mView) {
            @Override
            protected void onSuccess(UploadFileResponse response) {
                String link = response.link;

                mView.uploadFileSuccess(link);
                modifyInfo(link, nickName);

            }

        });


    }

    public void modifyInfo(String link, String nickName) {
        UpdateUserInfoRequest infoRequest = new UpdateUserInfoRequest();
        infoRequest.setNickname(nickName);
        infoRequest.setAvatar(link);
        requestApi(mRetrofit.patchUpdateUserInfo(infoRequest), new BaseMvpObserver<UserInfoResponse>(mView) {
            @Override
            protected void onSuccess(UserInfoResponse response) {
                User user = User.getInstance();
                if (!TextUtils.isEmpty(link)) {
                    user.avatar = link;
                }
                if (!TextUtils.isEmpty(nickName)) {
                    user.nickName = nickName;
                }
                BaseApplication app = (BaseApplication) Utils.getApp();
                UserDao userDao = app.getDatabase().userDao();
                userDao.addUser(user);
                User.update();
                mView.modifyInfoSuccess();
            }
        });
    }


}
