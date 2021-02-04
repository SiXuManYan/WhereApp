package com.jcs.where.features.setting.information;

import com.jcs.where.api.network.BaseMvpView;

/**
 * Created by Wangsw  2021/2/4 13:33.
 */
public interface ModifyInfoView extends BaseMvpView {
    void modifyInfoSuccess();

    void uploadFileSuccess(String link);
}
