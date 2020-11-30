package com.jcs.where.codepicker;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.jcs.where.R;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<VH> {

    private final LayoutInflater inflater;
    private final Context context;
    private ArrayList<Country> selectedCountries = new ArrayList<>();
    private OnPick onPick = null;
    private int itemHeight = -1;


    public Adapter(Context ctx) {
        inflater = LayoutInflater.from(ctx);
        context = ctx;
    }

    public void setSelectedCountries(ArrayList<Country> selectedCountries) {
        this.selectedCountries = selectedCountries;
        notifyDataSetChanged();
    }

    public void setOnPick(OnPick onPick) {
        this.onPick = onPick;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(inflater.inflate(R.layout.item_country, parent, false));
    }

    public void setItemHeight(float dp) {
        itemHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, context.getResources().getDisplayMetrics());
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        final Country country = selectedCountries.get(position);
        holder.ivFlag.setImageResource(country.flag);
        holder.tvName.setText(country.name);
        holder.tvCode.setText("+" + country.code);
        if (itemHeight != -1) {
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            params.height = itemHeight;
            holder.itemView.setLayoutParams(params);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPick != null) onPick.onPick(country);
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectedCountries.size();
    }

}
