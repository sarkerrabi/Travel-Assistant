package com.tnrlab.travelassistant.ui.send;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;
import com.tnrlab.travelassistant.R;
import com.tnrlab.travelassistant.models.creaet_path.RouteDetails;
import com.tnrlab.travelassistant.ui.send.adapters.ShowPathsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowCreatedPathFragment extends Fragment implements ShowPathsView {

    @BindView(R.id.rvCreatedPaths)
    RecyclerView rvCreatedPaths;
    List<RouteDetails> routeDetailsList;
    FirebaseAuth mAuth;
    private ShowCreatedPathViewModel showCreatedPathViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        showCreatedPathViewModel = ViewModelProviders.of(this).get(ShowCreatedPathViewModel.class);
        View root = inflater.inflate(R.layout.fragment_show_created_path, container, false);
        ButterKnife.bind(this, root);
        mAuth = FirebaseAuth.getInstance();

        showCreatedPathViewModel.getAllMyRoutePaths(this).observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                routeDetailsList = new ArrayList<>();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {

                    RouteDetails routeDetails = mDataSnapshot.getValue(RouteDetails.class);
                    if (routeDetails.getRouteReview().getUid().equals(mAuth.getCurrentUser().getUid())) {
                        routeDetailsList.add(routeDetails);
                    }


                }

                setDataOnRecylerView(routeDetailsList);


            }

        });


        return root;
    }


    private void setDataOnRecylerView(List<RouteDetails> routeDetailsList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvCreatedPaths.setLayoutManager(linearLayoutManager);
        ShowPathsAdapter recycleViewAdapter = new ShowPathsAdapter(routeDetailsList, this);

        rvCreatedPaths.setAdapter(recycleViewAdapter);
    }


    @Override
    public void onFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onShowCreatedPathClicked(RouteDetails routeDetails) {
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        String routeData = gson.toJson(routeDetails);
        bundle.putString("route_path", routeData);
        Navigation.findNavController(getView()).navigate(R.id.action_nav_created_path_history_to_loadAPathFragment, bundle);


    }
}