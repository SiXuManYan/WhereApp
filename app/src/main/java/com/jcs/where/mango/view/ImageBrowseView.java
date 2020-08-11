package com.jcs.where.mango.view;

import android.content.Context;
import android.content.Intent;

import com.jcs.where.mango.MultiplexImage;

import java.util.List;

/**
 * Created by Jelly on 2016/9/3.
 */
public interface ImageBrowseView {

    Intent getDataIntent();

    void setImageBrowse(List<MultiplexImage> images, int position);

    Context getMyContext();

    int getPosition();

}
