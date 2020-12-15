package com.tnrlab.travelassistant.ui.created_maps.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tnrlab.travelassistant.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreatedMapAdapterViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvInstituteName)
    TextView tvInstituteName;
    @BindView(R.id.tvInstituteAddress)
    TextView tvInstituteAddress;
    @BindView(R.id.tvIsVerified)
    TextView tvIsVerified;

    public CreatedMapAdapterViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}