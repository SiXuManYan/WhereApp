package com.jiechengsheng.city.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jiechengsheng.city.R;
import com.jiechengsheng.city.api.response.gourmet.comment.CommentResponse;
import com.jiechengsheng.city.utils.GlideUtil;
import com.jiechengsheng.city.utils.image.GlideRoundedCornersTransform;
import com.jiechengsheng.city.widget.ratingstar.RatingStarView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Wangsw  2021/4/1 17:11.
 * 评价列表
 */
public class CommentView extends LinearLayout {


    private LinearLayout container_ll;


    public CommentView(Context context) {
        super(context);
        initView();
    }


    public CommentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CommentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public CommentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_evaluation, this, true);

        container_ll = view.findViewById(R.id.container_ll);


    }


    public void setData(List<CommentResponse> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        container_ll.removeAllViews();
        for (CommentResponse data : list) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_evaluation, null);

            CircleImageView avatarCiv = view.findViewById(R.id.avatar_civ);
            TextView nameTv = view.findViewById(R.id.name_tv);
            RatingStarView starRs = view.findViewById(R.id.star_rs);
            TextView commentTimeTv = view.findViewById(R.id.comment_time_tv);
            TextView contentTv = view.findViewById(R.id.content_tv);
            LinearLayout imageContainerLl = view.findViewById(R.id.image_container_ll);

            GlideUtil.load(getContext(), data.avatar, avatarCiv);
            nameTv.setText(data.username);
            starRs.setRating(data.star);
            commentTimeTv.setText(data.created_at);
            contentTv.setText(data.content);
            ArrayList<String> images = data.images;
            if (images != null && !images.isEmpty()) {

                RequestOptions options = RequestOptions.bitmapTransform(new GlideRoundedCornersTransform(2, GlideRoundedCornersTransform.CornerType.ALL))
                        .error(R.mipmap.ic_empty_gray)
                        .placeholder(R.mipmap.ic_empty_gray);

                imageContainerLl.removeAllViews();
                for (int i = 0; i < images.size(); i++) {
                    if (i > 3) {
                        return;
                    }
                    LayoutParams params = new LayoutParams(SizeUtils.dp2px(79), SizeUtils.dp2px(79));
                    params.setMarginEnd(SizeUtils.dp2px(11));
                    ImageView imageView = new ImageView(getContext());
                    imageView.setLayoutParams(params);
                    Glide.with(getContext()).load(images.get(i)).apply(options).into(imageView);
                    imageContainerLl.addView(imageView);
                }

            }

            container_ll.addView(view);

        }





    }
}
