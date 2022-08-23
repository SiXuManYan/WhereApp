package com.jcs.where.mine.activity;

import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.SpanUtils;
import com.google.gson.Gson;
import com.jcs.where.BuildConfig;
import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.features.web.WebViewActivity;
import com.jcs.where.storage.entity.User;
import com.jcs.where.utils.BusinessUtils;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Wangsw  2021/2/3 16:48.
 */
public class AboutActivity extends BaseActivity {

    private TextView home_page_tv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        TextView version_tv = (TextView) findViewById(R.id.version_tv);
        StringBuffer buffer = new StringBuffer(BuildConfig.VERSION_NAME);
        if (BuildConfig.FLAVOR.equals("dev")) {
            buffer.append("_测试服务器");
        }
        version_tv.setText(getString(R.string.version_format, buffer.toString()));

        home_page_tv = findViewById(R.id.home_page_tv);
        SpanUtils spanUtils = new SpanUtils();
        SpannableStringBuilder builder = spanUtils.append(getString(R.string.office_address_home_page_1))
                .append(getString(R.string.office_address_home_page_2))
                .setClickSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        WebViewActivity.goTo(AboutActivity.this, getString(R.string.office_address_home_page_2));
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        ds.setColor(getColor(R.color.blue_377BFF));
                        ds.setUnderlineText(false);
                    }
                })
                .create();
        home_page_tv.setMovementMethod(LinkMovementMethod.getInstance());
        home_page_tv.setText(builder);

        if (BuildConfig.FLAVOR.equals("dev")) {
            findViewById(R.id.debug_into_ll).setVisibility(View.VISIBLE);
            TextView user_id_tv = findViewById(R.id.user_id_tv);
            TextView user_phone_tv = findViewById(R.id.user_phone_tv);
            TextView user_all_tv = findViewById(R.id.user_all_tv);
            TextView rong_uuid_tv = findViewById(R.id.rong_uuid_tv);
            TextView rong_token_tv = findViewById(R.id.rong_token_tv);
            TextView push_id_tv = findViewById(R.id.push_id_tv);
            TextView umeng_channel_tv = findViewById(R.id.umeng_channel_tv);

            if (User.isLogon()) {
                User instance = User.getInstance();
                user_id_tv.append(String.valueOf(instance.id));
                user_phone_tv.append(instance.phone);
                if (instance.rongData != null) {
                    rong_uuid_tv.append(instance.rongData.uuid);
                    rong_token_tv.append(instance.rongData.token);
                }
                Gson gson = new Gson();
                user_all_tv.append("\r\n" + gson.toJson(instance));
            } else {
                user_id_tv.append("未登录");
            }
            String registrationID = JPushInterface.getRegistrationID(this);
            push_id_tv.append(registrationID);
            umeng_channel_tv.append(BusinessUtils.INSTANCE.getUmengAppChannel());
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected void bindListener() {

    }


}
