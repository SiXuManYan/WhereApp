package com.jcs.where.features.address;

import com.jcs.where.api.network.BaseMvpView;
import com.jcs.where.api.response.address.AddressResponse;

import java.util.List;

/**
 * Created by Wangsw  2021/3/22 10:31.
 */
public interface AddressView extends BaseMvpView {


    void bindList(List<AddressResponse> response);
}
