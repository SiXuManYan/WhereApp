package com.jcs.where.view.XBanner;

import android.content.Context;
import android.widget.ImageView;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Abby on 10/14/2017.
 */

public abstract class AbstractUrlLoader implements ImageLoader {

    @Override
    public abstract void loadImages(Context context, String url, ImageView image);

    @Override
    public abstract void loadGifs(Context context, String url, GifImageView gifImageView, ImageView.ScaleType scaleType);

}