package com.jcs.where.utils;

import android.os.Build;
import android.transition.ChangeImageTransform;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;

import java.util.List;

public class ImagePreviewActivity extends BaseActivity {
    private ImageView mBigIv;
    private ViewPager mViewPager;
    private List<String> mImageUrls;
    public static final String IMAGES_URL_KEY = "imageUrls";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        // or create directly
        ChangeImageTransform changeImageTransform = new ChangeImageTransform();
        getWindow().setSharedElementEnterTransition(changeImageTransform);
        mViewPager = findViewById(R.id.viewpager);
    }

    @Override
    protected void initData() {
        mImageUrls = getIntent().getStringArrayListExtra(IMAGES_URL_KEY);

        mViewPager.setAdapter(new ImagePreviewAdapter());
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_preview;
    }

    class ImagePreviewAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageUrls == null ? 0 : mImageUrls.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(ImagePreviewActivity.this);
            imageView.setTransitionName(getString(R.string.comment_icon));
            imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            Glide.with(ImagePreviewActivity.this).load(mImageUrls.get(position)).into(imageView);
            ((ViewPager) container).addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finishAfterTransition();
                }
            });
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }

}
