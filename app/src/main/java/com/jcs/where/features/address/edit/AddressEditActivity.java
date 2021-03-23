package com.jcs.where.features.address.edit;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.jcs.where.R;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.home.watcher.AfterTextChangeWatcher;
import com.jcs.where.utils.Constant;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Wangsw  2021/3/22 14:19.
 * 收货地址编辑
 */
public class AddressEditActivity extends BaseMvpActivity<AddressEditPresenter> implements AddressEditView {

    private ImageView backIv;
    private TextView titleTv;
    private TextView deleteTv;
    private AppCompatEditText addressEt;
    private AppCompatEditText recipientEt;
    private MaterialRadioButton manRb;
    private MaterialRadioButton womanRb;
    private AppCompatEditText phoneEt;
    private TextView saveTv;

    private String mAddress;
    private String mRecipient;
    private int mSex;
    private String mPhone;
    private String mAddressId;

    /**
     * 是否是修改密码
     */
    private boolean isChange;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_address_edit;
    }

    @Override
    protected void initView() {

        backIv = findViewById(R.id.back_iv);
        titleTv = findViewById(R.id.title_tv);
        deleteTv = findViewById(R.id.delete_tv);
        addressEt = findViewById(R.id.address_et);
        recipientEt = findViewById(R.id.recipient_et);
        manRb = findViewById(R.id.man_rb);
        womanRb = findViewById(R.id.woman_rb);
        phoneEt = findViewById(R.id.phone_et);
        saveTv = findViewById(R.id.save_tv);
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected void initData() {
        presenter = new AddressEditPresenter(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isChange = true;
            deleteTv.setVisibility(View.VISIBLE);
            mAddressId = bundle.getString(Constant.PARAM_ADDRESS_ID, "");
            mAddress = bundle.getString(Constant.PARAM_ADDRESS, "");
            mRecipient = bundle.getString(Constant.PARAM_RECIPIENT, "");
            mSex = bundle.getInt(Constant.PARAM_SEX, 0);
            mPhone = bundle.getString(Constant.PARAM_PHONE, "");

            addressEt.setText(mAddress);
            recipientEt.setText(mRecipient);
            if (mSex == 1) {
                manRb.setChecked(true);
            } else {
                womanRb.setChecked(true);
            }
            phoneEt.setText(mPhone);
            titleTv.setText(R.string.edit_address);
            return;
        }
        deleteTv.setVisibility(View.GONE);
        titleTv.setText(R.string.add_address);
    }

    @Override
    protected void bindListener() {
        addressEt.addTextChangedListener(new AfterTextChangeWatcher() {
            @Override
            protected void onAfterTextChanged(Editable editable) {
                checkEnable();
            }
        });

        recipientEt.addTextChangedListener(new AfterTextChangeWatcher() {
            @Override
            protected void onAfterTextChanged(Editable editable) {
                checkEnable();
            }
        });

        phoneEt.addTextChangedListener(new AfterTextChangeWatcher() {
            @Override
            protected void onAfterTextChanged(Editable editable) {
                checkEnable();
            }
        });

        saveTv.setOnClickListener(this::handleSave);
        deleteTv.setOnClickListener(this::deleteAddress);
    }


    private void checkEnable() {
        String address = addressEt.getText().toString().trim();
        String recipient = recipientEt.getText().toString().trim();
        String phone = phoneEt.getText().toString().trim();
        if (!TextUtils.isEmpty(address) && !TextUtils.isEmpty(recipient) && !TextUtils.isEmpty(phone)) {
            saveTv.setBackground(ResourceUtils.getDrawable(R.drawable.shape_gradient_orange));
        } else {
            saveTv.setBackground(ResourceUtils.getDrawable(R.drawable.shape_gradient_orange_un_enable));
        }
    }


    private void handleSave(View view) {

        String address = addressEt.getText().toString().trim();
        String recipient = recipientEt.getText().toString().trim();
        String phone = phoneEt.getText().toString().trim();
        if (manRb.isChecked()) {
            mSex = 1;
        } else {
            mSex = 2;
        }

        presenter.handleSave(address, recipient, phone, mSex, isChange, mAddressId);
    }

    private void deleteAddress(View view) {

        new AlertDialog.Builder(this)
                .setTitle(R.string.hint)
                .setCancelable(false)
                .setMessage(getString(R.string.confirm_delete_address))
                .setPositiveButton(R.string.confirm, (dialogInterface, i) -> presenter.deleteAddress(mAddressId))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .create().show();


    }


    @Override
    public void editSuccess() {
        ToastUtils.showShort("edit success");
        EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_ADDRESS));
        finish();
    }

    @Override
    public void addAddressSuccess() {
        ToastUtils.showShort("add success");
        EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_ADDRESS));
        finish();
    }

    @Override
    public void deleteAddressSuccess() {
        ToastUtils.showShort("delete success");
        EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_ADDRESS));
        finish();
    }
}
