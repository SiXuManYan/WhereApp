package co.tton.android.base.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;

import co.tton.android.base.R;

public class CommonDialogUtils {

    private static final String TAG_DIALOG_TEXT = "dialog_text";
    private static final String TAG_DIALOG_PROGRESS = "dialog_progress";

    public static void show(Activity activity, CommonDialog dialog) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show(activity);
        }
    }

    public static void dismiss(CommonDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static CommonDialog newTextDialog(String title, String content, String leftBtn, String rightBtn) {
        CommonDialog.DialogParams dialogParams = new CommonDialog.DialogParams();
        dialogParams.mLayoutId = R.layout.dialog_common_content;
        dialogParams.mTitle = title;
        dialogParams.mContent = content;
        dialogParams.mBtnLeftText = leftBtn;
        dialogParams.mBtnRightText = rightBtn;
        dialogParams.mCanceledOnTouchOutside = false;
        dialogParams.mTag = TAG_DIALOG_TEXT;
        if (TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
            dialogParams.mTextGravity = Gravity.CENTER;
        }

        CommonDialog dialog = new CommonDialog();
        dialog.setParams(dialogParams);
        return dialog;
    }

    public static CommonDialog newProgressDialog(String waitingText) {
        CommonDialog.DialogParams dialogParams = new CommonDialog.DialogParams();
        dialogParams.mLayoutId = R.layout.dialog_progress;
        dialogParams.mContent = waitingText;
        dialogParams.mCanceledOnTouchOutside = false;
        dialogParams.mTag = TAG_DIALOG_PROGRESS;

        CommonDialog dialog = new CommonDialog();
        dialog.setParams(dialogParams);
        return dialog;
    }
}
