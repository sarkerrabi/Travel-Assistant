package com.tnrlab.travelassistant.ui.share;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.tnrlab.travelassistant.ui.send.ShowPathsView;
import com.tnrlab.travelassistant.ui.share.adapters.SharedPathsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareFragment extends Fragment implements ShowPathsView {

    @BindView(R.id.rvSharedPathList)
    RecyclerView rvSharedPathList;
    private ShareViewModel shareViewModel;
    List<RouteDetails> routeDetailsList;
    FirebaseAuth mAuth;
    Loader loader;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);

        ButterKnife.bind(this, root);
        mAuth = FirebaseAuth.getInstance();
        loader = new Loader(getActivity());


        shareViewModel.getAllSharedRoutePaths(this).observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                routeDetailsList = new ArrayList<>();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {

                    RouteDetails routeDetails = mDataSnapshot.getValue(RouteDetails.class);

                    assert routeDetails != null;
                    routeDetails.setFireDBRouteKey(mDataSnapshot.getKey());


                    if (!routeDetails.getRouteReview().getUid().equals(mAuth.getCurrentUser().getUid()) && routeDetails.getIsShared()) {
                        routeDetailsList.add(routeDetails);
                    }

//                    routeDetailsList.add(routeDetails);


                }

                setDataOnRecyclerView(routeDetailsList);


            }

        });
        return root;
    }

    private void setDataOnRecyclerView(List<RouteDetails> routeDetailsList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvSharedPathList.setLayoutManager(linearLayoutManager);
        SharedPathsAdapter recycleViewAdapter = new SharedPathsAdapter(routeDetailsList, this);
        rvSharedPathList.setAdapter(recycleViewAdapter);
    }

    @Override
    public void onFailed(String message) {

    }

    @Override
    public void onShowCreatedPathClicked(RouteDetails routeDetails) {
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        String routeData = gson.toJson(routeDetails);
        bundle.putString("route_path", routeData);
        Navigation.findNavController(getView()).navigate(R.id.action_nav_share_to_loadAPathFragment, bundle);

    }

    @Override
    public void onPathDeleteClicked(RouteDetails routeDetails, ShowPathsView showPathsView) {

    }


    @Override
    public void onPathDeletedSuccessfully() {

    }

    @Override
    public void onShareChanged(RouteDetails details, boolean isChecked) {

    }

    @Override
    public void onPathSharedSuccessfully(boolean isShared) {

    }
}