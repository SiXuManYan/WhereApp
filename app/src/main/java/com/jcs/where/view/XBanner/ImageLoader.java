package com.jcs.where.view.XBanner;

import android.content.Context;
import android.widget.ImageView;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Abby on 9/22/2017.
 */

public  interface ImageLoader {
    void loadImages(Context context, String url, ImageView image);
    void loadGifs(Context context, String url, GifImageView gifImageView, ImageView.ScaleType scaleType);
}
