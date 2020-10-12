package com.tnrlab.travelassistant.ui.created_maps.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tnrlab.travelassistant.R;
import com.tnrlab.travelassistant.models.institute.Institution;
import com.tnrlab.travelassistant.ui.created_maps.CreatedMapsView;

import java.util.ArrayList;
import java.util.List;

public class CreatedMapsAdapter extends RecyclerView.Adapter<CreatedMapAdapterViewHolder> {
    List<Institution> institutionList = new ArrayList<>();
    CreatedMapsView createdMapsView;


    public CreatedMapsAdapter(List<Institution> institutionList, CreatedMapsView createdMapsView) {
        this.institutionList = institutionList;
        this.createdMapsView = createdMapsView;
    }

    @NonNull
    @Override
    public CreatedMapAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_find_map, parent, false);
        return new CreatedMapAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreatedMapAdapterViewHolder holder, int position) {
        holder.tvInstituteName.setText(institutionList.get(position).getInstitutionName());
        holder.tvInstituteAddress.setText(institutionList.get(position).getMapPickerInsAddress());
        if (institutionList.get(position).isActive()) {
            holder.tvIsVerified.setVisibility(View.VISIBLE);
        } else {
            holder.tvIsVerified.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createdMapsView.showCreatedMapClicked(institutionList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return institutionList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
