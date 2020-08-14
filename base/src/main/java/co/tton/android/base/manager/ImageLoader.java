package co.tton.android.base.manager;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

import co.tton.android.base.R;
import co.tton.android.base.utils.ValueUtils;

public class ImageLoader {

    private static ImageLoader sInstance;

    public static ImageLoader get() {
        if (sInstance == null) {
            sInstance = new ImageLoader();
        }
        return sInstance;
    }

    private ImageLoader() {

    }

    public void load(ImageView view, String uri) {
        load(view, uri, true);
    }

    public void load(ImageView view, String uri, boolean hasBackground) {
        load(view, uri, R.color.bg_image_placeholder, hasBackground);
    }

    public void loadAvatar(ImageView view, String uri) {
        load(view, uri, R.drawable.ic_noheader, false);
    }

    public void load(ImageView view, String uri, int placeHolder, boolean hasBackground) {
//        if (!(view instanceof CircularImageView)) {
//            view.setScaleType(ImageView.ScaleType.CENTER);
//        }
        if (hasBackground) {
            view.setBackgroundColor(ValueUtils.getColor(view.getContext(), R.color.bg_image_placeholder));
        }
        Glide.with(view.getContext().getApplicationContext())
                .load(uri)
                .into(view);
    }

    public void loadFitCenter(ImageView view, String uri, boolean hasBackground) {
        if (hasBackground) {
            view.setBackgroundColor(ValueUtils.getColor(view.getContext(), R.color.bg_image_placeholder));
        }

        Glide.with(view.getContext().getApplicationContext())
                .load(uri)
                .into(view);
    }

    public void loadPreview(ImageView view, String uri) {
        view.setScaleType(ImageView.ScaleType.CENTER);
        view.setBackgroundColor(ValueUtils.getColor(view.getContext(), R.color.bg_image_placeholder));

        Glide.with(view.getContext().getApplicationContext())
                .load(uri)
                .into(view);
    }


    public void loadBig(ImageView view, String uri) {
        Glide.with(view.getContext().getApplicationContext())
                .load(uri)
                .into(view);
    }

}
