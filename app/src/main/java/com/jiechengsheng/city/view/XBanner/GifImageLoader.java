package com.jiechengsheng.city.view.XBanner;

import android.content.Context;
import android.widget.ImageView;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Abby on 10/5/2017.
 */

public class GifImageLoader implements ImageLoader {

    /**
     * Default gif loader
     */
    @Override
    public void loadGifs(Context context, String url, GifImageView gifImageView, ImageView.ScaleType scaleType) {
        GifDownloadManager.downloagGif(url, gifImageView, scaleType);
    }

    @Override
    public void loadImages(Context context, String url, ImageView image) {

    }


}
