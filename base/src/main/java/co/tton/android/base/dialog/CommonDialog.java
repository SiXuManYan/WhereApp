package co.tton.android.base.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.TextView;

import co.tton.android.base.R;
import co.tton.android.base.utils.ValueUtils;

public class CommonDialog implements View.OnClickListener {

    public static class DialogParams {
        public String mTitle;        // 标题, 不设置则不显示标题
        public String mContent;
        public int mLayoutId;        // 内容布局ID
        // 设置单按钮文字任选以下两个变量的其中之一
        public String mBtnLeftText;  // 左侧按钮文字
        public String mBtnRightText; // 右侧按钮文字
        public String mTag;          // Dialog Tag
        public boolean mCanceledOnTouchOutside = true;
        public int mTextGravity;
    }

    protected Activity mActivity;

    protected ViewGroup mRoot;
    protected View mContentView;
    protected TextView mContentTv;

    protected TextView mLeftBtn;
    protected TextView mRightBtn;

    protected Dialog mDialog;
    protected DialogParams mParams;
    protected DialogButtonClickListener mListener;
    protected DialogInterface.OnDismissListener mOnDismissListener;

    // 不能传ApplicationContext或其他不能获取DecorView的Context
    public void show(Activity activity) {
        mActivity = activity;
        mDialog = createDialog();
        mDialog.show();
    }

    private Dialog createDialog() {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        mRoot = (ViewGroup) inflater.inflate(R.layout.dialog_common, null);
        ViewStub contentVs = (ViewStub) mRoot.findViewById(R.id.vs_dialog_content);

        // 设置标题
        initTitleView(mRoot);
        // 设置内容
        onContentCreated(contentVs);
        // 设置按钮文字与事件
        initButtons(mRoot);

        // 创建Dialog
        Dialog dialog = new Dialog(mActivity, R.style.CommonDialog);
        dialog.setContentView(mRoot);
        dialog.setCanceledOnTouchOutside(mParams.mCanceledOnTouchOutside);
        dialog.setOnDismissListener(mOnDismissListener);

        // 设置dialog宽高
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {
            lp.copyFrom(dialog.getWindow().getAttributes());
        }
        lp.width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.85);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        return dialog;
    }

    protected void initTitleView(ViewGroup root) {
        TextView tvTitle = (TextView) root.findViewById(R.id.tv_dialog_title);
        if (!TextUtils.isEmpty(mParams.mTitle)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(mParams.mTitle);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
    }

    protected void initButtons(ViewGroup root) {
        boolean hasLeft = !TextUtils.isEmpty(mParams.mBtnLeftText);
        boolean hasRight = !TextUtils.isEmpty(mParams.mBtnRightText);
        mLeftBtn = (TextView) root.findViewById(R.id.tv_dialog_left_btn);
        mRightBtn = (TextView) root.findViewById(R.id.tv_dialog_right_btn);
        if (hasLeft && hasRight) { // double buttons
            mLeftBtn.setText(mParams.mBtnLeftText);
            mLeftBtn.setOnClickListener(this);
            mRightBtn.setText(mParams.mBtnRightText);
            mRightBtn.setOnClickListener(this);
        } else if (hasLeft || hasRight) { // single button
            mLeftBtn.setVisibility(View.GONE);
            mRightBtn.setOnClickListener(this);
            if (hasLeft) {
                mRightBtn.setText(mParams.mBtnLeftText);
            } else {
                mRightBtn.setText(mParams.mBtnRightText);
            }
            root.findViewById(R.id.divider).setVisibility(View.GONE);
        } else { // no buttons
            ViewGroup buttons = (ViewGroup) root.findViewById(R.id.fl_dialog_below_button);
            buttons.setVisibility(View.GONE);

            root.findViewById(R.id.line).setVisibility(View.GONE);
        }
    }

    protected void onContentCreated(ViewStub contentVs) {
        if (TextUtils.isEmpty(mParams.mContent)) return;

        contentVs.setLayoutResource(mParams.mLayoutId);
        mContentView = contentVs.inflate();
        if (mParams.mLayoutId == R.layout.dialog_common_content) {
            mContentTv = (TextView) mContentView;
            mContentTv.setText(mParams.mContent);
            if (mParams.mTextGravity == 0) {
                mContentTv.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            } else {
                mContentTv.setGravity(mParams.mTextGravity);
            }
            if (TextUtils.isEmpty(mParams.mTitle)) {
                mContentTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mActivity.getResources().getDimensionPixelSize(R.dimen.dialog_title));
                mContentTv.setTextColor(ValueUtils.getColor(mActivity, R.color.dialog_title));
                int dp16 = ValueUtils.dpToPx(mActivity, 16);
                mContentTv.setPadding(dp16, dp16, dp16, dp16);
            } else {
                mContentTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mActivity.getResources().getDimensionPixelSize(R.dimen.dialog_content));
                mContentTv.setTextColor(ValueUtils.getColor(mActivity, R.color.dialog_content));
                int dp16 = ValueUtils.dpToPx(mActivity, 16);
                int dp14 = ValueUtils.dpToPx(mActivity, 14);
                mContentTv.setPadding(dp16, 0, dp16, dp14);
            }
        }
    }

    public void setContentTvCenter() {
        if (mContentTv != null) {
            mContentTv.setGravity(Gravity.CENTER);
        }
    }

    @Override
    public void onClick(View v) {
        mDialog.dismiss();
        if (mListener != null) {
            int which = DialogInterface.BUTTON_NEGATIVE;
            if (v.getId() == R.id.tv_dialog_right_btn) {
                which = DialogInterface.BUTTON_POSITIVE;
            }
            mListener.onDialogButtonClick(this, which, mParams.mTag);
        }
    }

    public void setParams(DialogParams params) {
        if (params == null) {
            throw new IllegalArgumentException("DialogParams is NULL!");
        }
        this.mParams = params;
    }

    public void setRootMinHight(int minHight) {
        mRoot.setMinimumHeight(minHight);
    }

    public boolean isShowing() {
        return mDialog != null && mDialog.isShowing();
    }

    public void dismiss() {
        if (isShowing()) {
            mDialog.dismiss();
        }
    }

    public void setDialogButtonClickListener(DialogButtonClickListener listener) {
        mListener = listener;
    }

    public void setDialogButtonClickListener(DialogInterface.OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    public interface DialogButtonClickListener {

        /**
         * Dialog的按钮点击监听事件
         *
         * @param dialog
         * @param which  识别哪个按钮被点击
         *               DialogInterface.BUTTON_NEGATIVE表示左边按钮
         *               DialogInterface.BUTTON_POSITIVE表示右边按钮
         * @param tag    Dialog标签
         */
        void onDialogButtonClick(CommonDialog dialog, int which, String tag);
    }


}
