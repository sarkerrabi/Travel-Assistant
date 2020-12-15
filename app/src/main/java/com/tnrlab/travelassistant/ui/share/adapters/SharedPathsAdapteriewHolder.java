package com.tnrlab.travelassistant.ui.share.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tnrlab.travelassistant.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SharedPathsAdapteriewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvRoutePathID)
    TextView tvRoutePathID;
    @BindView(R.id.tvStartLocation)
    TextView tvStartLocation;
    @BindView(R.id.tvEndLocation)
    TextView tvEndLocation;
    @BindView(R.id.tvDesc)
    TextView tvDesc;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.linLay)
    LinearLayout linLay;
    @BindView(R.id.ivDelete)
    ImageView ivDelete;
    @BindView(R.id.sw_share)
    Switch swShare;

    public SharedPathsAdapteriewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}