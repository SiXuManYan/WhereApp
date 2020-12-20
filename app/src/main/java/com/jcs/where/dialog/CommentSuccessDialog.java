package com.jcs.where.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcs.where.R;

public class CommentSuccessDialog extends Dialog implements View.OnClickListener {
    private TextView sureTv;

    private final OnCloseListener listener;
    private final Context mContext;

    public CommentSuccessDialog(Context context, int themeResId, CommentSuccessDialog.OnCloseListener listener) {
        super(context, themeResId);
        this.listener = listener;
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_commentsuccess);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setCanceledOnTouchOutside(false);
        initView();
    }

    protected void initView() {
        sureTv = (TextView) findViewById(R.id.tv_sure);
        sureTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sure:
                if (listener != null) {
                    listener.onClose(this);
                }
                break;
        }
    }

    public interface OnCloseListener {
        void onClose(Dialog dialog);

        void onConfirm(Dialog dialog);
    }
}
