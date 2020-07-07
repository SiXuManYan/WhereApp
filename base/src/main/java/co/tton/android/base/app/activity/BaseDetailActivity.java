package co.tton.android.base.app.activity;

import android.os.Bundle;
import android.view.View;

import co.tton.android.base.R;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.CommonLayout;
import rx.Subscription;

public abstract class BaseDetailActivity extends BaseActivity {

    protected CommonLayout mCommonLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCommonLayout = V.f(this, R.id.common_layout);
        mCommonLayout.setContentLayoutId(getContentLayoutId());
        mCommonLayout.setOnErrorClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDetail();
            }
        });
        requestDetail();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_common_layout;
    }

    protected abstract int getContentLayoutId();

    private void requestDetail() {
        mCommonLayout.showLoading();
        Subscription subscription = getDetailRequest();
        addSubscription(subscription);
    }

    protected abstract Subscription getDetailRequest();
}
