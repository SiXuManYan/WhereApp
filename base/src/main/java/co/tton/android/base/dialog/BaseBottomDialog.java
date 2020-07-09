package co.tton.android.base.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;

import co.tton.android.base.R;

public abstract class BaseBottomDialog {

    protected Activity mActivity;
    protected Dialog mDialog;

    private Dialog createDialog() {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.dialog_bottom, null);
        ViewStub contentVs = (ViewStub) root.findViewById(R.id.vs_dialog_content);
        initContentView(contentVs);

        // 创建Dialog
        Dialog dialog = new Dialog(mActivity, R.style.CommonDialogStyle);
        dialog.setContentView(root);
        dialog.setCanceledOnTouchOutside(true);

        // 设置dialog宽高
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.gravity = Gravity.BOTTOM;
        lp.width = mActivity.getResources().getDisplayMetrics().widthPixels;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        return dialog;
    }

    protected abstract void initContentView(ViewStub root);

    // 不能传ApplicationContext或其他不能获取DecorView的Context
    public void show(Activity activity) {
        if (mDialog == null) {
            mActivity = activity;
            mDialog = createDialog();
        }
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public boolean isShowing() {
        return mDialog != null && mDialog.isShowing();
    }

    public void dismiss() {
        if (isShowing()) {
            mDialog.dismiss();
        }
    }

}
