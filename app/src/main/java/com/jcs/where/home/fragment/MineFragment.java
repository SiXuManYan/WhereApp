package com.jcs.where.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.UserBean;
import com.jcs.where.hotel.CityPickerActivity;
import com.jcs.where.hotel.WriteCommentActivity;
import com.jcs.where.login.LoginActivity;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.manager.UserManager;
import com.jcs.where.presenter.UploadFilePresenter;
import com.jcs.where.utils.GlideEngine;

import java.util.ArrayList;
import java.util.List;

import co.tton.android.base.app.fragment.BaseFragment;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.ToastUtils;
import rx.Subscriber;

import static android.app.Activity.RESULT_OK;

public class MineFragment extends BaseFragment implements View.OnClickListener {


    private static final int REQ_SELECT_CITY = 100;
    private View view;
    private ImageView settingIv;
    private TextView nameTv, accountTv;
    private UploadFilePresenter mUploadPresenter;
    private int startGroup = -1;
    private int endGroup = -1;
    private int startChild = -1;
    private int endChild = -1;

    @Override
    protected View initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView();
        return view;
    }

    private void initView() {
        settingIv = V.f(view, R.id.iv_setting);
        settingIv.setOnClickListener(this);
        nameTv = V.f(view, R.id.tv_name);
        accountTv = V.f(view, R.id.tv_account);
        mUploadPresenter = new UploadFilePresenter(getContext());
        V.f(view, R.id.ll_changelangue).setOnClickListener(this);
        V.f(view, R.id.ll_settlement).setOnClickListener(this);
        initData();
    }

    private void initData() {
        showLoading();
        HttpUtils.doHttpReqeust("GET", "userapi/v1/user/info", null, "", TokenManager.get().getToken(getContext()), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    UserBean userBean = new Gson().fromJson(result, UserBean.class);
                    UserManager.get().login(getContext(), userBean);
                    nameTv.setText(userBean.name);
                    accountTv.setText(userBean.id);
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(getContext(), errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(getContext(), e.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
                //
                //选择照片
                EasyPhotos.createAlbum(this, true, GlideEngine.getInstance())
                        .setFileProviderAuthority("com.huantansheng.easyphotos.demo.fileprovider")
                        .setCount(9)
                        .start(new SelectCallback() {
                            @Override
                            public void onResult(ArrayList<Photo> photos, boolean isOriginal) {
                                List<String> selectedPaths = new ArrayList<>();
                                for (int i = 0; i < photos.size(); i++) {
                                    selectedPaths.add(photos.get(i).path);
                                }
                                addSubscription(mUploadPresenter.uploadFiles(selectedPaths)
                                        .subscribe(new Subscriber<List<String>>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                e.printStackTrace();
                                                ToastUtils.showLong(getContext(), e.getMessage());
                                            }

                                            @Override
                                            public void onNext(List<String> strings) {
                                                Log.d("ssss", strings + "");
                                            }
                                        }));
                            }
                        });
                //条件选择
                //createCustomDatePicker(view);
                //  CityPickerActivity.goTo((Activity) getContext(), REQ_SELECT_CITY);
//                Intent intent = new Intent(getContext(), CityPickerActivity.class);
//                startActivityForResult(intent, REQ_SELECT_CITY);
                break;
            case R.id.ll_settlement:
                LoginActivity.goTo(getContext());
                break;
            case R.id.ll_changelangue:
                WriteCommentActivity.goTo(getContext(), 1, "ssss");
                break;
            default:
        }
    }

    private void createCustomDatePicker(View view) {
        //时间选择
//        new DatePopupWindow
//                .Builder((Activity) getContext(), Calendar.getInstance().getTime(), view)
//                .setInitSelect(startGroup, startChild, endGroup, endChild)
//                .setInitDay(false)
//                .setDateOnClickListener(new DatePopupWindow.DateOnClickListener() {
//                    @Override
//                    public void getDate(String startDate, String endDate, String startWeek, String endWeek, int startGroupPosition, int startChildPosition, int endGroupPosition, int endChildPosition) {
//                        startGroup = startGroupPosition;
//                        startChild = startChildPosition;
//                        endGroup = endGroupPosition;
//                        endChild = endChildPosition;
//                        String mStartTime = CalendarUtil.FormatDateYMD(startDate);
//                        String mEndTime = CalendarUtil.FormatDateYMD(endDate);
//                        ToastUtils.showLong(getContext(), "您选择了：" + mStartTime + startWeek + "到" + mEndTime + endWeek);
//                    }
//                }).builder();
//价格选择
//        new ChoosePricePop.Builder((Activity) getContext(), view)
//                .setPriceOnClickListener(new ChoosePricePop.PriceOnClickListener() {
//                    @Override
//                    public void getDate(String price, String star) {
//
//                        ToastUtils.showLong(getContext(), "您选择了：" + price + "到" + star);
//                    }
//                }).builder();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQ_SELECT_CITY) {
            ToastUtils.showLong(getContext(), "您选择了：" + data.getStringExtra(CityPickerActivity.EXTRA_CITY));
        }
    }
}
