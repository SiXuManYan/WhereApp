package com.jiechengsheng.city.features.setting.phone.verify.code;

import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonElement;
import com.jiechengsheng.city.R;
import com.jiechengsheng.city.api.ErrorResponse;
import com.jiechengsheng.city.api.network.BaseMvpObserver;
import com.jiechengsheng.city.api.network.BaseMvpPresenter;
import com.jiechengsheng.city.api.request.SendCodeRequest;
import com.jiechengsheng.city.api.request.modify.ModifyPhoneRequest;
import com.jiechengsheng.city.storage.entity.User;
import com.jiechengsheng.city.utils.Constant;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Wangsw  2021/2/4 15:11.
 */
public class CodeVerifyPresenter extends BaseMvpPresenter {

    private CodeVerifyView mView;

    public CodeVerifyPresenter(CodeVerifyView baseMvpView) {
        super(baseMvpView);
        mView = baseMvpView;
    }


    /**
     * 获取验证码
     *
     * @param getVerifyTv
     * @param target_tv
     */
    public void getVerifyCode(TextView getVerifyTv, TextView target_tv) {
        User user = User.getInstance();
        SendCodeRequest request = new SendCodeRequest();
        request.setPhone(user.phone);
        request.setCountryCode(user.countryCode);
        request.setType(Constant.VERIFY_CODE_TYPE_4_CHANGE_PHONE);

        requestApi(mRetrofit.getVerifyCode(request), new BaseMvpObserver<JsonElement>(mView) {
            @Override
            protected void onSuccess(JsonElement response) {

                getVerifyTv.setClickable(false);
                countdown(getVerifyTv, StringUtils.getString(R.string.get_again));
                ToastUtils.showShort(R.string.verify_code_send_success);
                target_tv.setText(StringUtils.getString(R.string.verify_code_send_to_format, user.phone));
            }

            @Override
            protected void onError(ErrorResponse errorResponse) {
                super.onError(errorResponse);
                getVerifyTv.setClickable(true);

            }
        });
    }

    /**
     * 倒计时
     *
     * @param countdownView 倒计时显示的view
     * @param defaultStr    默认显示的文字
     */
    private void countdown(TextView countdownView, String defaultStr) {
        Flowable.intervalRange(0, Constant.WAIT_DELAYS, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(aLong -> {
                    String string = StringUtils.getString(R.string.resend_format, Constant.WAIT_DELAYS - aLong);
                    countdownView.setText(string);
                })
                .doOnComplete(() -> {
                    countdownView.setText(defaultStr);
                    countdownView.setClickable(true);
                }).subscribe();
    }

    /**
     * 修改手机号
     */
    public void modifyPhone(String account, String countryCode) {
        ModifyPhoneRequest request = ModifyPhoneRequest.Builder.aModifyPhoneRequest()
                .phone(account)
                .country_code(countryCode)
                .build();

        requestApi(mRetrofit.modifyPhone(request), new BaseMvpObserver<JsonElement>(mView) {
            @Override
            protected void onSuccess(JsonElement response) {
                mView.modifyPhoneSuccess();
            }
        });

    }



    public void chekVerifyCode(String verifyCode){
        User user = User.getInstance();
        requestApi(mRetrofit.checkVerifyCode(1,verifyCode,user.phone,null),new BaseMvpObserver<JsonElement>(mView){

            @Override
            protected void onSuccess(JsonElement response) {
                mView.verified();
            }
        });

    }

}
