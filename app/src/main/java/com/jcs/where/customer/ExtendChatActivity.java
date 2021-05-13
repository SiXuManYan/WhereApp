package com.jcs.where.customer;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.comm100.livechat.core.VisitorClientCore;
import com.comm100.livechat.view.ChatWindowWebView;
import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;

/**
 * Created by allon on 5/31/2018.
 * comm 100 聊天页面
 */
public class ExtendChatActivity extends BaseActivity {

    private ChatWindowWebView web_view;
    private ImageView chat_iv;
    private LinearLayout web_container_ll;
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
        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0 || ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") != 0)) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"}, 1);
        }
        chat_iv = findViewById(R.id.chat_iv);
        web_container_ll = findViewById(R.id.web_container_ll);
        back_iv = findViewById(R.id.back_iv);
        close_iv = findViewById(R.id.close_iv);
        phone_rl = findViewById(R.id.phone_rl);
        call_tv = findViewById(R.id.call_tv);
        web_view = new ChatWindowWebView(this);
        web_container_ll.removeAllViews();
        web_container_ll.addView(web_view);
        web_view.loadUrl(VisitorClientCore.getInstance().getChatUrl());
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (web_view != null) {
            web_view.onActivityResult(requestCode, resultCode, data);
        }

    }


}
