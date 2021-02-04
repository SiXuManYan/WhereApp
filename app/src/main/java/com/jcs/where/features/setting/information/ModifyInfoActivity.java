package com.jcs.where.features.setting.information;

import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.jcs.where.R;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.storage.entity.User;


/**
 * Created by Wangsw  2021/2/4 12:00.
 * 修改个人信息
 */
public class ModifyInfoActivity extends BaseMvpActivity<ModifyInfoPresenter> implements ModifyInfoView {


    private AppCompatEditText nickname_aet;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_info;
    }

    @Override
    protected void initView() {
        nickname_aet = findViewById(R.id.nickname_aet);
    }


    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected void initData() {
        presenter = new ModifyInfoPresenter(this);
        String nickName = User.getInstance().nickName;
        if (!TextUtils.isEmpty(nickName)) {
            nickname_aet.setText(nickName);
            nickname_aet.setSelection(nickName.length());
        }
    }

    @Override
    protected void bindListener() {
        findViewById(R.id.avatar_tl).setOnClickListener(this::onAvatarLayoutClick);
        findViewById(R.id.confirm_tv).setOnClickListener(this::onConfirmClick);
    }


    private void onAvatarLayoutClick(View view) {

        // todo 图片上传
    }


    private void onConfirmClick(View view) {
        String name = nickname_aet.getText().toString().trim();
        if (TextUtils.isEmpty(name) || name.equals(User.getInstance().nickName)) {
            return;
        }


    }


}
