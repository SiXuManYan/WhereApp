package com.jcs.where.features.message;

import android.content.Context;
import android.net.Uri;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.StringUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.features.message.notice.SystemNoticeFragment;
import com.jcs.where.view.empty.EmptyView;

import io.rong.imkit.conversationlist.ConversationListFragment;

/**
 * Created by Wangsw  2021/2/19 17:07.
 * 消息中心
 */
public class MessageCenterActivity extends BaseActivity {


    private ViewPager pager;
    private SlidingTabLayout tabs_type;
    private final String[] TAB_TITLES = {StringUtils.getString(R.string.business_conversation), StringUtils.getString(R.string.system_notification)};
    private Uri mUri;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_center;
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected void initView() {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.white));
        pager = findViewById(R.id.pager);
        tabs_type = findViewById(R.id.tabs_type);
        pager.setOffscreenPageLimit(TAB_TITLES.length);


    }

    @Override
    protected void initData() {
        EmptyView emptyView = new EmptyView(this);
        ViewGroup.LayoutParams layoutParams = emptyView.parent_ll.getLayoutParams();
        layoutParams.width = ScreenUtils.getScreenWidth();

        InnerPagerAdapter adapter = new InnerPagerAdapter(getSupportFragmentManager(), 0);

        adapter.emptyView = emptyView;
        adapter.emptyView.showEmptyDefault();

        pager.setAdapter(adapter);
        tabs_type.setViewPager(pager, TAB_TITLES);
    }


    @Override
    protected void bindListener() {

    }


    private static class InnerPagerAdapter extends FragmentPagerAdapter {


        private EmptyView emptyView ;

        public InnerPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                ConversationListFragment listFragment = new ConversationListFragment();
                listFragment.setEmptyView(emptyView);

                return listFragment;
            } else {
                return new SystemNoticeFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


}
