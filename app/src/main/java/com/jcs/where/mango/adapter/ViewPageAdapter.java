package com.jcs.where.mango.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.jcs.where.R;
import com.jcs.where.mango.ImageBrowseActivity;
import com.jcs.where.mango.Mango;
import com.jcs.where.mango.MultiplexImage;
import com.jcs.where.mango.progressGlide.MangoBitmapTarget;
import com.jcs.where.mango.progressGlide.MangoGIFDrawableTarget;
import com.jcs.where.mango.progressGlide.MangoProgressTarget;
import com.jcs.where.mango.progressGlide.OMangoProgressTarget;
import com.jcs.where.mango.progressview.RingProgressView;

import java.lang.ref.SoftReference;
import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Mango ViewPageAdapter
 * Created by Jelly on 2016/3/10.
 */
public class ViewPageAdapter extends PagerAdapter {

    private static final String TAG = ViewPageAdapter.class.getName();

    private final Context context;
    /**
     * Image source
     */
    private final List<MultiplexImage> images;
    private final SparseArray<SoftReference<View>> cacheView;
    /**
     * Previous Position
     */
    private int prePosition;
    /**
     * Curr Position
     */
    private int position;

    public ViewPageAdapter(Context context, List<MultiplexImage> images) {
        this.context = context;
        this.images = images;
        cacheView = new SparseArray<>(images.size());
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View view = cacheView.get(position) != null ? cacheView.get(position).get() : null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.vp_item_image, container, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) view.findViewById(R.id.image);
            viewHolder.progressView = (RingProgressView) view.findViewById(R.id.progress);
            viewHolder.oImage = (ImageView) view.findViewById(R.id.oImage);

            if (Mango.isShowLoading) {
                viewHolder.progressView.initProgress();
            } else {
                viewHolder.progressView.setVisibility(View.GONE);
            }

            //if is load original image before,hidden thumbnails ImageView and load original image
            if (images.get(position).isLoading()) {
                viewHolder.photoViewAttacher = new PhotoViewAttacher(viewHolder.oImage);
                viewHolder.image.setVisibility(View.INVISIBLE);
                glideLoadImage(viewHolder.photoViewAttacher, viewHolder.progressView, viewHolder.image, position, true);
            } else {
                viewHolder.photoViewAttacher = new PhotoViewAttacher(viewHolder.image);
                glideLoadImage(viewHolder.photoViewAttacher, viewHolder.progressView, viewHolder.image, position, false);
            }
            viewHolder.photoViewAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    Activity activity = (Activity) context;
                    activity.finish();
                }
            });
            view.setTag(viewHolder);
            cacheView.put(position, new SoftReference<>(view));
        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    /**
     * Update PhotoView on the slide the ViewPager
     *
     * @param position ViewPager position
     */
    public void updatePhotoView(int position) {
        View view = cacheView.get(position) != null ? cacheView.get(position).get() : null;
        if (view != null) {
            PhotoViewAttacher photoViewAttacher = ((ViewHolder) view.getTag()).photoViewAttacher;
            photoViewAttacher.update();
        }
    }

    /**
     * Get previous position after the slide
     *
     * @return previous position
     */
    public int getPrePosition() {
        return prePosition;
    }

    /**
     * Get current position
     *
     * @return current position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Set previous position and current position
     *
     * @param position ViewPager position
     */
    public void setPosition(int position) {
        this.prePosition = this.position;
        this.position = position;
    }


    public void glideLoadImage(PhotoViewAttacher photoViewAttacher, final RingProgressView progressView, ImageView image, int position, boolean isO) {
        int type = images.get(position).getType(); //image type
        //get image url(Thumbnails or Original)
        String model = null;
        if (isO) {
            model = TextUtils.isEmpty(images.get(position).getOPath()) ? images.get(position).getTPath() : images.get(position).getOPath();
        } else {
            model = TextUtils.isEmpty(images.get(position).getTPath()) ? images.get(position).getOPath() : images.get(position).getTPath();
        }
        if (type == MultiplexImage.ImageType.GIF) {
            if (isO) {
                OMangoProgressTarget<GifDrawable> gifTarget = new OMangoProgressTarget<>(context, new MangoGIFDrawableTarget(photoViewAttacher), progressView, image);
                gifTarget.setModel(model);
                Glide.with(context).asGif().load(model).into(gifTarget);
            } else {
                MangoProgressTarget<GifDrawable> gifTarget = new MangoProgressTarget<>(context, new MangoGIFDrawableTarget(photoViewAttacher), progressView);
                gifTarget.setModel(model);
                Glide.with(context).asGif().load(model).into(gifTarget);
            }
        } else {
            if (isO) {
                OMangoProgressTarget<Bitmap> otherTarget = new OMangoProgressTarget<>(context, new MangoBitmapTarget(photoViewAttacher), progressView, image);
                otherTarget.setModel(model);
                Glide.with(context).asBitmap().load(model).into(otherTarget);
            } else {
                MangoProgressTarget<Bitmap> otherTarget = new MangoProgressTarget<>(context, new MangoBitmapTarget(photoViewAttacher), progressView);
                otherTarget.setModel(model);
                Glide.with(context).asBitmap().load(model).into(otherTarget);
            }
        }

    }

    /**
     * Start original image
     */
    public void loadOriginalPicture() {
        //If is not exists original image,return
        if (TextUtils.isEmpty(images.get(position).getOPath())) return;
        //If is loading,return
        if (images.get(position).isLoading()) return;
        images.get(position).setLoading(true);
        ((ImageBrowseActivity) context).hiddenOriginalButton(position);
        View view = cacheView.get(position) != null ? cacheView.get(position).get() : null;
        if (view != null) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            viewHolder.progressView.setVisibility(View.VISIBLE);
            viewHolder.photoViewAttacher = new PhotoViewAttacher(viewHolder.oImage);
            viewHolder.progressView.initProgress();

            glideLoadImage(viewHolder.photoViewAttacher, viewHolder.progressView, viewHolder.image, position, true);
        }
    }

    /**
     * Cache ViewPager ViewHolder
     */
    private class ViewHolder {
        ImageView image;
        ImageView oImage;
        RingProgressView progressView;
        PhotoViewAttacher photoViewAttacher;
    }

}
