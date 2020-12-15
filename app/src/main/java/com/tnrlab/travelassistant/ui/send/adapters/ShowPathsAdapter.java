package com.tnrlab.travelassistant.ui.send.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tnrlab.travelassistant.R;
import com.tnrlab.travelassistant.models.creaet_path.RouteDetails;
import com.tnrlab.travelassistant.ui.send.ShowPathsView;

import java.util.ArrayList;
import java.util.List;

public class ShowPathsAdapter extends RecyclerView.Adapter<ShowPathsAdapteriewHolder> {
    List<RouteDetails> routeDetailsList = new ArrayList<>();
    ShowPathsView showPathsView;

    public ShowPathsAdapter(List<RouteDetails> routeDetailsList, ShowPathsView showPathsView) {
        this.routeDetailsList = routeDetailsList;
        this.showPathsView = showPathsView;
    }

    @NonNull
    @Override
    public ShowPathsAdapteriewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_find_path, parent, false);
        return new ShowPathsAdapteriewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowPathsAdapteriewHolder holder, int position) {
        holder.tvRoutePathID.setText("Route Path ID: " + routeDetailsList.get(position).getRouteReview().getRoutePathID());
        holder.tvStartLocation.setText(routeDetailsList.get(position).getRouteReview().getStartPlaceInfo());
        holder.tvEndLocation.setText(routeDetailsList.get(position).getRouteReview().getEndPlaceInfo());
        holder.tvDesc.setText(routeDetailsList.get(position).getRouteReview().getDescription() + " ");
        if (routeDetailsList.get(position).getRoutePathList() != null && routeDetailsList.get(position).getRoutePathList().size() > 0) {
            holder.tvDate.setText(routeDetailsList.get(position).getRoutePathList().get(0).getTimeDetails());
        }
        holder.swShare.setChecked(routeDetailsList.get(position).getIsShared());

        holder.swShare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showPathsView.onShareChanged(routeDetailsList.get(position), isChecked);
            }
        });


        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPathsView.onPathDeleteClicked(routeDetailsList.get(position), showPathsView);
            }
        });


        holder.linLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPathsView.onShowCreatedPathClicked(routeDetailsList.get(position));

            }
        });


    }

    @Override
    public int getItemCount() {
        return routeDetailsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
