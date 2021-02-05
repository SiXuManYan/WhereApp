package com.jcs.where.features.setting.information;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.ToastUtils;
import com.jcs.where.R;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.storage.entity.User;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.FeaturesUtil;
import com.jcs.where.utils.GlideUtil;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Wangsw  2021/2/4 12:00.
 * 修改个人信息
 */
public class ModifyInfoActivity extends BaseMvpActivity<ModifyInfoPresenter> implements ModifyInfoView {


    private AppCompatEditText nickname_aet;
    private CircleImageView avatar_riv;
    private String mName;
    private String mLink;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_info;
    }

    @Override
    protected void initView() {
        nickname_aet = findViewById(R.id.nickname_aet);
        avatar_riv = findViewById(R.id.avatar_riv);
    }


    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected void initData() {
        presenter = new ModifyInfoPresenter(this);
        User instance = User.getInstance();
        String nickName = instance.nickName;
        String avatar = instance.avatar;
        if (!TextUtils.isEmpty(nickName)) {
            nickname_aet.setText(nickName);
        }
        if (!TextUtils.isEmpty(avatar)) {
            GlideUtil.load(this, avatar, avatar_riv);
        }

    }

    @Override
    protected void bindListener() {
        findViewById(R.id.avatar_tl).setOnClickListener(this::onAvatarLayoutClick);
        findViewById(R.id.confirm_tv).setOnClickListener(this::onConfirmClick);
    }

    private void onConfirmClick(View view) {
        User user = User.getInstance();
        mName = nickname_aet.getText().toString().trim();

        if ((TextUtils.isEmpty(mName) || mName.equals(user.nickName)) && TextUtils.isEmpty(mLink)) {
            ToastUtils.showShort(R.string.modify_none);
            return;
        }
        presenter.modifyInfo(mLink, mName);

    }

    private void onAvatarLayoutClick(View view) {
        FeaturesUtil.handleMediaSelect(this, Constant.IMG, view.getId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }
        List<String> elements = Matisse.obtainPathResult(data);
        if (!elements.isEmpty()) {
            String mFileDirPath = elements.get(0);
            GlideUtil.load(this, mFileDirPath, avatar_riv);
            presenter.uploadFile(mFileDirPath, mName);
        }
    }

    @Override
    public void uploadFileSuccess(String link) {
        mLink = link;
    }

    @Override
    public void modifyInfoSuccess() {
        ToastUtils.showShort(R.string.modify_success);
        EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_REFRESH_USER_INFO));
        finish();
    }
}
