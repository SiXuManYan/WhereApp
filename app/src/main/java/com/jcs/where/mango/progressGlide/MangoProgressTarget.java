package com.jcs.where.mango.progressGlide;

import android.content.Context;

import com.bumptech.glide.request.target.Target;
import com.jcs.where.mango.progressview.RingProgressView;

/**
 * show thumbnails image target
 * Created by Jelly on 2017/9/2.
 */

public class MangoProgressTarget<Z> extends ProgressTarget<String, Z> {

    private static final String TAG = MangoProgressTarget.class.getName();
    private final RingProgressView progressView;

    public MangoProgressTarget(Context context, Target<Z> target, RingProgressView progressView) {
        super(context, target);
        this.progressView = progressView;
    }


    @Override
    public float getGranualityPercentage() {
        return super.getGranualityPercentage();
    }

    @Override
    protected void onConnecting() {

    }

    @Override
    protected void onDownloading(long bytesRead, long expectedLength) {
        progressView.setProgress((int) (100 * bytesRead / expectedLength));
    }

    @Override
    protected void onDownloaded() {

    }

    @Override
    protected void onDelivered() {
        progressView.setProgress(100);
    }
}
