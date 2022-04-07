package com.jcs.where.features.message.notice.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.utils.Constant;

/**
 * Created by Wangsw  2021/2/23 15:32.
 * <p>
 * 系统消息详情
 */
public class SystemMessageDetailActivity extends BaseActivity {

    private TextView
            title_tv,
            content_tv,
            date_tv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_system_detail;
    }

    @Override
    protected void initView() {
        title_tv = findViewById(R.id.title_tv);
        content_tv = findViewById(R.id.content_tv);
        date_tv = findViewById(R.id.date_tv);
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }


    public static void goTo(Context context, String title, String content, String createDate) {
        Intent intent = new Intent(context, SystemMessageDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constant.PARAM_TITLE, title);
        intent.putExtra(Constant.PARAM_CONTENT, content);
        intent.putExtra(Constant.PARAM_CREATE_DATE, createDate);

        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void initData() {
        String title = getIntent().getStringExtra(Constant.PARAM_TITLE);
        String content = getIntent().getStringExtra(Constant.PARAM_CONTENT);
        String createDate = getIntent().getStringExtra(Constant.PARAM_CREATE_DATE);

        title_tv.setText(title);
        content_tv.setText(content);
        date_tv.setText(createDate);
    }

    @Override
    protected void bindListener() {

    }

}
