package com.jcs.where.utils;

import android.os.Build;
import android.transition.ChangeImageTransform;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ImagePreviewActivity extends BaseActivity {
    private ViewPager mViewPager;
    private TextView mImgPositionTv;
    private TextView mSaveTv;

    private List<String> mImageUrls;
    private int mShowPosition;
    public static final String IMAGES_URL_KEY = "imageUrls";
    public static final String IMAGE_POSITION = "imagePosition";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        // or create directly
        ChangeImageTransform changeImageTransform = new ChangeImageTransform();
        getWindow().setSharedElementEnterTransition(changeImageTransform);
        mViewPager = findViewById(R.id.viewpager);
        mImgPositionTv = findViewById(R.id.imgPositionTv);
        mSaveTv = findViewById(R.id.saveTv);
    }

    @Override
    protected void initData() {
        mImageUrls = getIntent().getStringArrayListExtra(IMAGES_URL_KEY);
        mShowPosition = getIntent().getIntExtra(IMAGE_POSITION, -1);

        mViewPager.setAdapter(new ImagePreviewAdapter());

        String msg = 1 +
                "/" +
                mImageUrls.size();
        mImgPositionTv.setText(msg);

        if (mShowPosition != -1 && mShowPosition < mImageUrls.size()) {
            mViewPager.setCurrentItem(mShowPosition);
        }

    }

    @Override
    protected void bindListener() {
        mSaveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("保存");
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String msg = (position + 1) +
                        "/" +
                        mImageUrls.size();
                mImgPositionTv.setText(msg);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
                    finish();
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
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
