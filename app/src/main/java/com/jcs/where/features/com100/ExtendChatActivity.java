package com.jcs.where.features.com100;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.frames.common.Html5Url;

/**
 * Created by allon on 5/31/2018.
 * comm 100 聊天页面
 */
public class ExtendChatActivity extends BaseActivity {

    private Common100WebView web_view;
    private ImageView chat_iv;
    private ImageView back_iv;
    private ImageView close_iv;
    private RelativeLayout phone_rl;
    private TextView call_tv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_extend_chat;
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected void initView() {

        web_view = findViewById(R.id.web_view);
        chat_iv = findViewById(R.id.chat_iv);
        back_iv = findViewById(R.id.back_iv);
        close_iv = findViewById(R.id.close_iv);
        phone_rl = findViewById(R.id.phone_rl);
        call_tv = findViewById(R.id.call_tv);
        web_view.loadUrl(Html5Url.COMM100_CHAT_URL);
        CookieManager.getInstance().setAcceptCookie(true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindListener() {
        back_iv.setOnClickListener(v -> finish());
        chat_iv.setOnClickListener(v -> phone_rl.setVisibility(View.VISIBLE));
        close_iv.setOnClickListener(v -> phone_rl.setVisibility(View.GONE));
        call_tv.setOnClickListener(v -> {
            Uri data = Uri.parse("tel:09177039784");
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(data);
            startActivity(intent);
            phone_rl.setVisibility(View.GONE);
        });


    }

    protected void onResume() {
        super.onResume();
        if (web_view != null) {
            web_view.onResume();
        }
    }

    protected void onPause() {
        super.onPause();
        if (web_view != null) {
            web_view.onPause();
        }
    }


}
