package com.jcs.where.home.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.atuan.datepickerlibrary.CalendarUtil;
import com.atuan.datepickerlibrary.DatePopupWindow;
import com.google.gson.Gson;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.UserBean;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.manager.UserManager;
import com.jcs.where.presenter.UploadFilePresenter;

import java.util.Calendar;

import co.tton.android.base.app.fragment.BaseFragment;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.ToastUtils;

public class MineFragment extends BaseFragment implements View.OnClickListener {


    private static final int REQ_SELECT_HEADER = 100;
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
        initData();
    }

    private void initData() {
        showLoading();
        HttpUtils.doHttpReqeust("GET", "userapi/v1/user/info", null, "", TokenManager.get().getToken(getContext()), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                Log.d("Ssss", result);
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
                // LoginActivity.goTo(getContext());=

//                EasyPhotos.createAlbum(this, true, GlideEngine.getInstance())
//                        .setFileProviderAuthority("com.huantansheng.easyphotos.demo.fileprovider")
//                        .setCount(9)
//                        .start(new SelectCallback() {
//                            @Override
//                            public void onResult(ArrayList<Photo> photos, boolean isOriginal) {
//                                List<String> selectedPaths = new ArrayList<>();
//                                for (int i = 0; i < photos.size(); i++) {
//                                    selectedPaths.add(photos.get(i).path);
//                                }
//                                addSubscription(mUploadPresenter.uploadFiles(selectedPaths)
//                                        .subscribe(new Subscriber<List<String>>() {
//                                            @Override
//                                            public void onCompleted() {
//
//                                            }
//
//                                            @Override
//                                            public void onError(Throwable e) {
//
//                                            }
//
//                                            @Override
//                                            public void onNext(List<String> strings) {
//
//                                            }
//                                        }));
//                            }
//                        });
                createCustomDatePicker(view);
                break;
            default:
        }
    }

    private void createCustomDatePicker(View view) {
        new DatePopupWindow
                .Builder((Activity) getContext(), Calendar.getInstance().getTime(), view)
                .setInitSelect(startGroup, startChild, endGroup, endChild)
                .setInitDay(false)
                .setDateOnClickListener(new DatePopupWindow.DateOnClickListener() {
                    @Override
                    public void getDate(String startDate, String endDate, String startWeek, String endWeek, int startGroupPosition, int startChildPosition, int endGroupPosition, int endChildPosition) {
                        startGroup = startGroupPosition;
                        startChild = startChildPosition;
                        endGroup = endGroupPosition;
                        endChild = endChildPosition;
                        String mStartTime = CalendarUtil.FormatDateYMD(startDate);
                        String mEndTime = CalendarUtil.FormatDateYMD(endDate);
                        ToastUtils.showLong(getContext(), "您选择了：" + mStartTime + startWeek + "到" + mEndTime + endWeek);
                    }
                }).builder();
    }

}
