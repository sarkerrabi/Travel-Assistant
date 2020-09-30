package com.tnrlab.travelassistant.ui.send.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tnrlab.travelassistant.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowPathsAdapteriewHolder extends RecyclerView.ViewHolder {
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

    public ShowPathsAdapteriewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}