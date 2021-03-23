package com.jcs.where.features.address.edit;

import com.jcs.where.api.network.BaseMvpView;

/**
 * Created by Wangsw  2021/3/22 14:16.
 */
public interface AddressEditView extends BaseMvpView {

    /**
     * 修改成功
     */
    void editSuccess();

    /**
     * 地址添加成功
     */
    void addAddressSuccess();

    /**
     * 地址删除成功
     */
    void deleteAddressSuccess();
}
