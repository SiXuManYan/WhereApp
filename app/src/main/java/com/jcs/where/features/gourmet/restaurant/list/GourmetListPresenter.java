package com.jcs.where.features.gourmet.restaurant.list;

import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.features.gourmet.restaurant.list.GourmetListView;

/**
 * Created by Wangsw  2021/3/24 13:57.
 */
public class GourmetListPresenter extends BaseMvpPresenter {

    private GourmetListView view;

    public GourmetListPresenter(GourmetListView view) {
        super(view);
        this.view = view;
    }



}
