package com.jcs.where.government.dialog;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.base.BaseBottomDialog;
import com.jcs.where.base.BaseDialog;

/**
 * 拨打电话
 * create by zyf on 2021/1/2 7:07 PM
 */
public class CallPhoneDialog extends BaseBottomDialog {

    private Button mCancelBtn;
    private TextView mPhoneTv;
    private String mPhoneNumber;

    @Override
    protected int getLayout() {
        return R.layout.dialog_call_phone;
    }

    @Override
    protected int getHeight() {
        return 200;
    }

    @Override
    protected void initView(View view) {
        mCancelBtn = view.findViewById(R.id.cancelBtn);

        mPhoneTv = view.findViewById(R.id.phoneTv);

        String phoneStr = getString(R.string.call) + "  " + this.mPhoneNumber;
        mPhoneTv.setText(phoneStr);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindListener() {
        mCancelBtn.setOnClickListener(view -> dismiss());

        mPhoneTv.setOnClickListener(this::onPhoneClicked);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    private void onPhoneClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + mPhoneNumber);
        intent.setData(data);
        startActivity(intent);
    }

}

