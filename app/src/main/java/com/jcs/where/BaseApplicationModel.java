package com.jcs.where;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.message.RongCloudUserResponse;

/**
 * Created by Wangsw  2021/2/24 15:52.
 */
public class BaseApplicationModel extends BaseModel {

    public void getRongCloudUserInfo(BaseObserver<RongCloudUserResponse> observer,String userId){
        dealResponse(mRetrofit.getRongCloudUserInfo(userId),observer);
    }

}
