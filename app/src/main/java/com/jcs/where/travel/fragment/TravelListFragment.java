package com.jcs.where.travel.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jcs.where.R;

import co.tton.android.base.app.fragment.BaseFragment;
import co.tton.android.base.utils.V;

public class TravelListFragment extends BaseFragment {

    private View view;

    public static TravelListFragment newInstance(String travelId) {
        Bundle args = new Bundle();
        args.putString("travelId", travelId);
        TravelListFragment fragment = new TravelListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected View initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_travellist, container, false);
        TextView testTv = V.f(view, R.id.tv_test);
        testTv.setText(getArguments().getString("travelId"));
        return view;
    }
}
