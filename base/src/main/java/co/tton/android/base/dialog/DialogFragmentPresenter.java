package co.tton.android.base.dialog;

import android.view.View;

import java.util.ArrayList;

import co.tton.android.base.app.presenter.BaseFragmentPresenter;

public class DialogFragmentPresenter extends BaseFragmentPresenter {

    private ArrayList<CommonDialog> mDialogList;

    @Override
    public void initContentView(View view) {
        mDialogList = new ArrayList<>();
        super.initContentView(view);
    }

    public void addDialog(CommonDialog dialog) {
        mDialogList.add(dialog);
    }

    @Override
    public void onDestroyView() {
        for (CommonDialog dialog : mDialogList) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        super.onDestroyView();
    }
}
