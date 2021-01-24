package com.jcs.where.mine.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.api.request.UpdateUserInfoRequest;
import com.jcs.where.api.response.UserInfoResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.login.event.LoginEvent;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.mine.model.PersonalDataModel;
import com.jcs.where.utils.SoftKeyBoardListener;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalDataActivity extends BaseActivity {

    private CircleImageView headerIv;
    private EditText mNameEt;
    private TextView createTimeTv;
    private PersonalDataModel mModel;

    public static void goTo(Context context) {
        Intent intent = new Intent(context, PersonalDataActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        headerIv = findViewById(R.id.iv_header);
        headerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        mNameEt = findViewById(R.id.et_name);
        mNameEt.setOnEditorActionListener(this::onCompletedActionClicked);

        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
            }

            @Override
            public void keyBoardHide(int height) {
                AlertDialog alertDialog2 = new AlertDialog.Builder(PersonalDataActivity.this)
                        .setTitle("提示")
                        .setMessage("昵称将更改为" + mNameEt.getText().toString())
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                changeName(mNameEt.getText().toString());
                            }
                        })

                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .create();
                alertDialog2.show();
            }
        });
        createTimeTv = findViewById(R.id.tv_creattime);
        initData();
    }

    private boolean onCompletedActionClicked(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            hideInput();
            mNameEt.clearFocus();
            return true;
        }
        return false;
    }

    @Override
    protected void initData() {
        mModel = new PersonalDataModel();
        showLoading();
        mModel.getUserInfo(new BaseObserver<UserInfoResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NotNull UserInfoResponse userInfoResponse) {
                stopLoading();
                if (!userInfoResponse.getAvatar().equals("")) {
                    Glide.with(PersonalDataActivity.this).load(userInfoResponse.getAvatar()).into(headerIv);
                }
                mNameEt.setText(userInfoResponse.getNickname());
                createTimeTv.setText(String.format(getString(R.string.show_create_time), userInfoResponse.getCreatedAt()));
            }
        });
    }

    @Override
    protected void bindListener() {

    }


    private void changeAvater(List<String> path) {
        showLoading();
        Map<String, String> params1 = new HashMap<>();
        params1.put("avatar", path.get(0));
        HttpUtils.doHttpReqeust("PATCH", "userapi/v1/users", params1, "", TokenManager.get().getToken(PersonalDataActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    showToast(getString(R.string.avatar_change_success));
                    EventBus.getDefault().post(LoginEvent.LOGIN);
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    showToast(errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                showToast(e.getMessage());
            }
        });
    }

    private void changeName(String name) {
        showLoading();
        mModel.updateUserInfo(new UpdateUserInfoRequest(name), new BaseObserver<UserInfoResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NotNull UserInfoResponse userInfoResponse) {
                stopLoading();
                showToast(getString(R.string.nickname_change_success));
                mNameEt.setText(Editable.Factory.getInstance().newEditable(userInfoResponse.getNickname()));
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_persion_data;
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

}