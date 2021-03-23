package com.jcs.where.features.address;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.address.AddressResponse;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Wangsw  2021/3/22 13:47.
 */
public class AddressAdapter extends BaseQuickAdapter<AddressResponse, BaseViewHolder> {

    TextView addressTv;
    TextView nameTv;
    TextView phoneTv;
    ImageView editIv;


    public AddressAdapter() {
        super(R.layout.item_address);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, AddressResponse data) {

        addressTv = holder.getView(R.id.address_tv);
        nameTv = holder.getView(R.id.name_tv);
        phoneTv = holder.getView(R.id.phone_tv);
        editIv = holder.getView(R.id.edit_iv);

        addressTv.setText(data.address);
        nameTv.setText(data.contact_name);
        phoneTv.setText(data.contact_number);



    }



}
