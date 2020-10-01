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
import com.tnrlab.travelassistant.loader.Loader;
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
    Loader loader;
    private ShowCreatedPathViewModel showCreatedPathViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        showCreatedPathViewModel = ViewModelProviders.of(this).get(ShowCreatedPathViewModel.class);
        View root = inflater.inflate(R.layout.fragment_show_created_path, container, false);
        ButterKnife.bind(this, root);
        mAuth = FirebaseAuth.getInstance();
        loader = new Loader(getActivity());

        loader.showDialog();

        showCreatedPathViewModel.getAllMyRoutePaths(this).observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                routeDetailsList = new ArrayList<>();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {

                    RouteDetails routeDetails = mDataSnapshot.getValue(RouteDetails.class);


                    assert routeDetails != null;
                    routeDetails.setFireDBRouteKey(mDataSnapshot.getKey());


/*                    if (routeDetails.getRouteReview().getUid().equals(mAuth.getCurrentUser().getUid())) {
                        routeDetailsList.add(routeDetails);
                    }*/

                    routeDetailsList.add(routeDetails);


                }

                setDataOnRecyclerView(routeDetailsList);


            }

        });


        loader.hideDialog();
        return root;
    }


    private void setDataOnRecyclerView(List<RouteDetails> routeDetailsList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvCreatedPaths.setLayoutManager(linearLayoutManager);
        ShowPathsAdapter recycleViewAdapter = new ShowPathsAdapter(routeDetailsList, this);

        rvCreatedPaths.setAdapter(recycleViewAdapter);
    }


    @Override
    public void onFailed(String message) {
        loader.hideDialog();
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

    @Override
    public void onPathDeleteClicked(RouteDetails routeDetails) {

        showCreatedPathViewModel.removePathFromDB(routeDetails, this);
        loader.showDialog();


    }

    @Override
    public void onPathDeletedSuccessfully() {
        loader.hideDialog();
        Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShareChanged(RouteDetails details, boolean isChecked) {
        loader.showDialog();
        showCreatedPathViewModel.shareWithOthers(details, isChecked, this);
    }

    @Override
    public void onPathSharedSuccessfully(boolean isShared) {
        loader.hideDialog();
        if (isShared) {
            Toast.makeText(getContext(), "Now, Your path will be shared with others ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Removed access to follow your path", Toast.LENGTH_SHORT).show();
        }
    }
}