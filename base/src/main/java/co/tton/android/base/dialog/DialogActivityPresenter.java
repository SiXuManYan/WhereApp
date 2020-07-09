package co.tton.android.base.dialog;

import android.os.Bundle;

import java.util.ArrayList;

import co.tton.android.base.app.presenter.BaseActivityPresenter;

public class DialogActivityPresenter extends BaseActivityPresenter {

    private ArrayList<CommonDialog> mDialogList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogList = new ArrayList<>();
    }

    public void addDialog(CommonDialog dialog) {
        mDialogList.add(dialog);
    }

    @Override
    public void onDestroy() {
        for (CommonDialog dialog : mDialogList) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        super.onDestroy();
    }
}
