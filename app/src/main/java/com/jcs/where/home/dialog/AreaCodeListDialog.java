package com.jcs.where.home.dialog;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.base.BaseDialog;

public class AreaCodeListDialog extends BaseDialog {

    private Button mCancelBtn;
    private TextView mChinaArea, mPhilippinesArea;
    private AreaCodeListCallback mCallback;

    @Override
    protected int getLayout() {
        return R.layout.dialog_area_code_list;
    }

    @Override
    protected int getHeight() {
        return 300;
    }

    @Override
    protected void initView(View view) {
        mCancelBtn = view.findViewById(R.id.cancelBtn);

        mChinaArea = view.findViewById(R.id.chinaArea);
        mPhilippinesArea = view.findViewById(R.id.philippinesArea);
    }

    @Override
    protected void initData() {

    }

    public void injectCallback(AreaCodeListCallback callback) {
        this.mCallback = callback;
    }


    @Override
    protected void bindListener() {
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mChinaArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null) {
                    mCallback.select(mChinaArea.getText().toString().split(" ")[1]);
                }
                dismiss();
            }
        });

        mPhilippinesArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null) {
                    mCallback.select(mPhilippinesArea.getText().toString().split(" ")[1]);
                }
                dismiss();
            }
        });
    }

    public interface AreaCodeListCallback {
        void select(String area);
    }

}
