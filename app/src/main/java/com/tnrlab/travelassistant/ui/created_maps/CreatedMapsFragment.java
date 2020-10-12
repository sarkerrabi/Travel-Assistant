package com.tnrlab.travelassistant.ui.created_maps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tnrlab.travelassistant.R;
import com.tnrlab.travelassistant.models.created_maps.CreatedMapData;
import com.tnrlab.travelassistant.models.institute.Institution;
import com.tnrlab.travelassistant.ui.created_maps.adapters.CreatedMapsAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreatedMapsFragment extends Fragment implements CreatedMapsView {

    @BindView(R.id.rvMaps)
    RecyclerView rvMaps;
    private CreatedMapsViewModel mViewModel;
    private Bundle bundle;

    public static CreatedMapsFragment newInstance() {
        return new CreatedMapsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.created_maps_fragment, container, false);
        ButterKnife.bind(this, root);


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CreatedMapsViewModel.class);
        mViewModel.getAllCreatedMaps(this);

    }

    @Override
    public void onMapListReady(List<CreatedMapData> institutionList) {

    }

    @Override
    public void showCreatedMapClicked(Institution institution) {
        bundle = new Bundle();
        Gson gson = new Gson();
        String map_data = gson.toJson(institution);
        bundle.putString("institution_data", map_data);
        Navigation.findNavController(getView()).navigate(R.id.action_nav_created_maps_to_showMapUserFragment, bundle);
    }

    @Override
    public void onInstituteListReady(List<Institution> institutionList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvMaps.setLayoutManager(linearLayoutManager);
        CreatedMapsAdapter recycleViewAdapter = new CreatedMapsAdapter(institutionList, this);
        rvMaps.setAdapter(recycleViewAdapter);
    }


}