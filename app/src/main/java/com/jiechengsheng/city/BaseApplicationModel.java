package com.jiechengsheng.city;

import com.jiechengsheng.city.api.BaseModel;
import com.jiechengsheng.city.api.BaseObserver;
import com.jiechengsheng.city.api.response.message.RongCloudUserResponse;

/**
 * Created by Wangsw  2021/2/24 15:52.
 */
public class BaseApplicationModel extends BaseModel {

    public void getRongCloudUserInfo(BaseObserver<RongCloudUserResponse> observer,String userId){
        dealResponse(mRetrofit.getRongCloudUserInfo(userId),observer);
    }

}
