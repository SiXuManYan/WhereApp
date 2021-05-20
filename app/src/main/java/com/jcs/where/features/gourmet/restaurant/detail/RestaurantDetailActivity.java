package com.jcs.where.features.gourmet.restaurant.detail;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jcs.where.R;
import com.jcs.where.api.response.gourmet.comment.CommentResponse;
import com.jcs.where.api.response.gourmet.dish.DishResponse;
import com.jcs.where.api.response.gourmet.restaurant.RestaurantDetailResponse;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.features.gourmet.cart.ShoppingCartActivity;
import com.jcs.where.features.gourmet.takeaway.TakeawayActivity;
import com.jcs.where.frams.common.Html5Url;
import com.jcs.where.hotel.activity.HotelDetailActivity;
import com.jcs.where.hotel.activity.detail.DetailMediaAdapter;
import com.jcs.where.hotel.activity.detail.MediaData;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.MobUtil;
import com.jcs.where.utils.image.GlideRoundedCornersTransform;
import com.jcs.where.view.CommentView;
import com.jcs.where.view.DishView;
import com.jcs.where.widget.pager.IndicatorView2;
import com.jcs.where.widget.ratingstar.RatingStarView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import static com.jcs.where.utils.Constant.PARAM_ID;


/**
 * Created by Wangsw  2021/4/1 10:28.
 * 餐厅详情
 */
public class RestaurantDetailActivity extends BaseMvpActivity<RestaurantDetailPresenter> implements RestaurantDetailView {


    private ImageView image_iv;
    private TextView name_tv;
    private TextView score_tv;
    private RatingStarView star_view;
    private TextView comment_count_tv;
    private TextView per_price_tv;
    private TextView address_tv;
    private TextView distance_tv;
    private TextView time_tv;
    private TextView support_takeaway_tv;
    private DishView dish_view;
    private CommentView comment_view;
    private ImageView shopping_cart, navigation_iv, chat_iv, tel_iv;
    private View dish_split_v;
    private ViewSwitcher contact_sw;

    private RecyclerView media_rv;
    private IndicatorView2 point_view;
    private DetailMediaAdapter mMediaAdapter;

    /**
     * 餐厅id
     */
    private String mRestaurantId;

    /**
     * 餐厅名称
     */
    private String mRestaurantName;
    private String mLat = "";
    private String mLng = "";

    /**
     * 收藏状态（1：未收藏，2：已收藏）
     */
    private int collect_status = 0;
    private CharSequence mPhone = "";

    /**
     * 商家融云id
     */
    private String mMerUuid;

    /**
     * 商家融云聊天名字
     */
    private String mMerName;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_restaurant_detail;
    }

    @Override
    protected void initView() {

        image_iv = findViewById(R.id.image_iv);
        name_tv = findViewById(R.id.name_tv);
        score_tv = findViewById(R.id.score_tv);
        star_view = findViewById(R.id.star_view);
        comment_count_tv = findViewById(R.id.comment_count_tv);
        per_price_tv = findViewById(R.id.per_price_tv);
        address_tv = findViewById(R.id.address_tv);
        distance_tv = findViewById(R.id.distance_tv);
        time_tv = findViewById(R.id.time_tv);
        support_takeaway_tv = findViewById(R.id.support_takeaway_tv);
        dish_view = findViewById(R.id.dish_view);
        comment_view = findViewById(R.id.comment_view);
        shopping_cart = findViewById(R.id.shopping_cart);
        navigation_iv = findViewById(R.id.navigation_iv);
        chat_iv = findViewById(R.id.chat_iv);
        tel_iv = findViewById(R.id.tel_iv);
        dish_split_v = findViewById(R.id.dish_split_v);
        contact_sw = findViewById(R.id.contact_sw);
        initMedia();

    }

    private void initMedia() {
        mMediaAdapter = new DetailMediaAdapter();
        media_rv = findViewById(R.id.media_rv);
        point_view = findViewById(R.id.point_view);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(media_rv);

        media_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        media_rv.setAdapter(mMediaAdapter);
        media_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@androidx.annotation.NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;

                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();

                    //大于0说明有播放
                    if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                        //当前播放的位置
                        int position = GSYVideoManager.instance().getPlayPosition();

                        // 对应的播放列表TAG
                        if (GSYVideoManager.instance().getPlayTag().equals(DetailMediaAdapter.TAG) && (position < firstItemPosition || position > lastItemPosition)) {
                            if (GSYVideoManager.isFullState(RestaurantDetailActivity.this)) {
                                return;
                            }
                            //如果滑出去了上面和下面就是否
                            GSYVideoManager.releaseAllVideos();
                            mMediaAdapter.notifyDataSetChanged();
                        }

                    }

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        point_view.onPageSelected(firstItemPosition);
                    }


                }

            }

        });

    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected void initData() {
        mRestaurantId = getIntent().getStringExtra(Constant.PARAM_ID);
        presenter = new RestaurantDetailPresenter(this);
        presenter.getDetail(mRestaurantId);
        presenter.getDishList(mRestaurantId);
        presenter.getCommentList(mRestaurantId);
    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }


    @Override
    protected void bindListener() {
        shopping_cart.setOnClickListener(this::onShoppingCartClick);
        tel_iv.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(mPhone)) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + mPhone);
                intent.setData(data);
                startActivity(intent);
            } else {
                ToastUtils.showShort(R.string.no_contact_information);
            }
        });
        support_takeaway_tv.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_ID, mRestaurantId);
            startActivity(TakeawayActivity.class, bundle);
        });

        navigation_iv.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(mLat) && !TextUtils.isEmpty(mLng)) {
                startNaviGoogle(mLat, mLng);
            }
        });

        mJcsTitle.setSecondRightIvClickListener(v -> {
            if (collect_status == 1) {
                presenter.collection(mRestaurantId);
            } else {
                presenter.unCollection(mRestaurantId);
            }

        });

        mJcsTitle.setFirstRightIvClickListener(v -> {
            String url = String.format(Html5Url.SHARE_FACEBOOK, Html5Url.MODEL_RESTAURANT, mRestaurantId);
            MobUtil.shareFacebookWebPage(url, RestaurantDetailActivity.this);
        });

        chat_iv.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(mMerUuid)) {
                RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, mMerUuid, mRestaurantName, null);
            }
        });
    }


    @Override
    public void bindData(RestaurantDetailResponse data) {


        ArrayList<MediaData> mediaList = new ArrayList<>();

        if (!TextUtils.isEmpty(data.video)) {
            MediaData media = new MediaData();
            media.setType(MediaData.VIDEO);
            media.setCover(data.video_image);
            media.setSrc(data.video);
            mediaList.add(media);
        }
        for (String image : data.images) {

            MediaData media = new MediaData();
            media.setType(MediaData.IMAGE);
            media.setCover(image);
            media.setSrc(image);
            mediaList.add(media);
        }
        mMediaAdapter.setNewInstance(mediaList);
        point_view.setPointCount(mediaList.size());

        mRestaurantName = data.title;
        mJcsTitle.setMiddleTitle(mRestaurantName);
        name_tv.setText(mRestaurantName);

        score_tv.setText(String.valueOf(data.grade));
        star_view.setRating(data.grade);
        comment_count_tv.setText(getString(R.string.parentheses_contain_string, data.comment_num));
        per_price_tv.setText(getString(R.string.per_price_format, data.per_price));
        address_tv.setText(data.address);
        time_tv.setText(getString(R.string.time_format, data.start_time, data.end_time));
        if (data.take_out_status == 2) {
            support_takeaway_tv.setVisibility(View.VISIBLE);
        } else {
            support_takeaway_tv.setVisibility(View.GONE);
        }
        if (data.im_status == 1) {
            contact_sw.setDisplayedChild(0);
        } else {
            contact_sw.setDisplayedChild(1);
        }
        distance_tv.setText(getString(R.string.distance_format_2, data.distance));
        mLat = data.lat;
        mLng = data.lng;
        collect_status = data.collect_status;
        if (collect_status == 1) {
            mJcsTitle.setSecondRightIcon(R.mipmap.ic_like_black2);
        } else {
            mJcsTitle.setSecondRightIcon(R.mipmap.ic_like_red);
        }
        mPhone = data.tel;
        mMerUuid = data.mer_uuid;
        mMerName = data.mer_name;
    }

    @Override
    public void bindDishData(List<DishResponse> data) {
        dish_view.setData(data, mRestaurantId, mRestaurantName);
        dish_view.setVisibility(View.VISIBLE);
        dish_split_v.setVisibility(View.VISIBLE);
    }

    @Override
    public void bindCommentData(List<CommentResponse> data) {
        comment_view.setData(data);
        comment_view.setVisibility(View.VISIBLE);
    }

    private void onShoppingCartClick(View view) {
        startActivityAfterLogin(ShoppingCartActivity.class);
    }

    public void startNaviGoogle(String lat, String lng) {
        if (isAvilible(this, "com.google.android.apps.maps")) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lng);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        } else {
            showToast(getString(R.string.no_install_google_map_prompt));
        }
    }

    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    @Override
    public void collectionSuccess() {
        collect_status = 2;
        mJcsTitle.setSecondRightIcon(R.mipmap.ic_like_red);
    }

    @Override
    public void unCollectionSuccess() {
        collect_status = 1;
        mJcsTitle.setSecondRightIcon(R.mipmap.ic_like_black2);
    }
}
