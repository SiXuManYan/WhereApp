package co.tton.android.base.dialog;

/**
 * Created by Administrator on 2016/11/29.
 */


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;


import co.tton.android.base.R;


public class CustomProgressDialog extends ProgressDialog {

    private Context mContext;
    private String mLoadingTip;
    private TextView mLoadingTv;


    public CustomProgressDialog(Context context, String content) {
        super(context, R.style.dialog);
        this.mContext = context;
        if (TextUtils.isEmpty(content)) {
            this.mLoadingTip = "加载中...";
        } else {
            this.mLoadingTip = content;
        }
        setCanceledOnTouchOutside(false);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_layout);
        initView();
        initData();
    }

    private void initData() {
        mLoadingTv.setText(mLoadingTip);

    }

    public void setContent(String str) {
        mLoadingTv.setText(str);
    }

    private void initView() {
        mLoadingTv = (TextView) findViewById(R.id.progress_tv);
    }


}
