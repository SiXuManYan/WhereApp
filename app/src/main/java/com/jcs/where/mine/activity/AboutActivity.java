package com.jcs.where.mine.activity;

import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.SpanUtils;
import com.jcs.where.BuildConfig;
import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.features.web.WebViewActivity;

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
            buffer.append(" TestVersion");
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
