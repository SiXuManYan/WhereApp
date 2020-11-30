package com.jcs.where.mine;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.UserBean;
import com.jcs.where.login.event.LoginEvent;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.presenter.UploadFilePresenter;
import com.jcs.where.utils.SoftKeyBoardListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.tton.android.base.app.activity.BaseActivity;
import co.tton.android.base.manager.ImageLoader;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.ToastUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalDataActivity extends BaseActivity {

    private CircleImageView headerIv;
    private UploadFilePresenter mUploadPresenter;
    private EditText nameEt;
    private TextView createTimeTv;

    public static void goTo(Context context) {
        Intent intent = new Intent(context, PersonalDataActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        mUploadPresenter = new UploadFilePresenter(PersonalDataActivity.this);
        initView();
    }

    private void initView() {
        headerIv = V.f(this, R.id.iv_header);
        headerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        nameEt = V.f(this, R.id.et_name);


        nameEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard(nameEt);
                    nameEt.clearFocus();
                    return true;
                }
                return false;
            }
        });

        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
            }

            @Override
            public void keyBoardHide(int height) {
                AlertDialog alertDialog2 = new AlertDialog.Builder(PersonalDataActivity.this)
                        .setTitle("提示")
                        .setMessage("昵称将更改为" + nameEt.getText().toString())
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                changeName(nameEt.getText().toString());
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
        createTimeTv = V.f(this, R.id.tv_creattime);
        initData();
    }

    private void initData() {
        showLoading();
        HttpUtils.doHttpReqeust("GET", "userapi/v1/user/info", null, "", TokenManager.get().getToken(PersonalDataActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    UserBean userBean = new Gson().fromJson(result, UserBean.class);
                    ImageLoader.get().loadAvatar(headerIv, userBean.avatar);
                    nameEt.setText(userBean.nickname);
                    createTimeTv.setText("创建时间  " + userBean.created_at);
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(PersonalDataActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(PersonalDataActivity.this, e.getMessage());
            }
        });
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
                    ToastUtils.showLong(PersonalDataActivity.this, "头像修改成功");
                    EventBus.getDefault().post(LoginEvent.LOGIN);
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(PersonalDataActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(PersonalDataActivity.this, e.getMessage());
            }
        });
    }

    private void changeName(String name) {
        showLoading();
        Map<String, String> params1 = new HashMap<>();
        params1.put("nickname", name);
        HttpUtils.doHttpReqeust("PATCH", "userapi/v1/users", params1, "", TokenManager.get().getToken(PersonalDataActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    ToastUtils.showLong(PersonalDataActivity.this, "昵称修改成功");
                    EventBus.getDefault().post(LoginEvent.LOGIN);
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(PersonalDataActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(PersonalDataActivity.this, e.getMessage());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_persiondata;
    }

    public void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
