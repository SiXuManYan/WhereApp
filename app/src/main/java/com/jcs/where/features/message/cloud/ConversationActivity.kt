package com.jcs.where.features.message.cloud;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jcs.where.R;
import com.jcs.where.utils.Constant;
import com.jcs.where.widget.JcsTitle;

import io.rong.imkit.fragment.ConversationFragment;

/**
 * Created by Wangsw  2021/2/24 10:49.
 * 融云会话页面
 */
public class ConversationActivity extends FragmentActivity {

    protected JcsTitle mJcsTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_conversation);

        Intent intent = getIntent();
        String title = intent.getData().getQueryParameter("title");


        mJcsTitle = findViewById(R.id.jcsTitle);
        mJcsTitle.setBackIvClickListener(view -> finish());
        mJcsTitle.setMiddleTitle(title);

        Bundle bundle = intent.getExtras();
        if (bundle != null) {

            String phone = bundle.getString(Constant.PARAM_PHONE, "");
            mJcsTitle.setFirstRightIvClickListener(v -> {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:" + phone));
                startActivity(i);
            });

        }


        // 添加会话界面
        ConversationFragment conversationFragment = new ConversationFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, conversationFragment);
        transaction.commit();
    }


}