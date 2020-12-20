package com.jcs.where.codepicker;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcs.where.R;

import androidx.recyclerview.widget.RecyclerView;

class VH extends RecyclerView.ViewHolder {

    TextView tvName;
    TextView tvCode;
    ImageView ivFlag;

    VH(View itemView) {
        super(itemView);
        ivFlag = (ImageView) itemView.findViewById(R.id.iv_flag);
        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvCode = (TextView) itemView.findViewById(R.id.tv_code);
    }
}
