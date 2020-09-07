package com.tnrlab.travelassistant.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.tnrlab.travelassistant.R;
import com.tnrlab.travelassistant.databases.AppDatabase;
import com.tnrlab.travelassistant.models.creaet_path.RouteReview;
import com.tnrlab.travelassistant.shared_db.SharedDB;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RouteReviewFragment extends Fragment {

    @BindView(R.id.tvStartLocation)
    TextView tvStartLocation;
    @BindView(R.id.tvEndLocation)
    TextView tvEndLocation;
    @BindView(R.id.routeDesc)
    EditText routeDesc;
    SharedDB sharedDB;
    private RouteReviewViewModel mViewModel;
    private AppDatabase mDb;

    public static RouteReviewFragment newInstance() {
        return new RouteReviewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.route_review_fragment, container, false);
        ButterKnife.bind(this, root);
        sharedDB = new SharedDB(getContext());
        mDb = AppDatabase.getInstance(getContext());


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RouteReviewViewModel.class);

        tvStartLocation.setText(sharedDB.getStartPlaceInfo());
        tvEndLocation.setText(sharedDB.getEndPlaceInfo());

    }

    @OnClick(R.id.button)
    public void onViewClicked() {

        if (routeDesc.getText().toString().isEmpty()) {
            routeDesc.setError("Please tell us something about your path*");
            return;
        }

        RouteReview routeReview = new RouteReview();
        routeReview.setDescription(routeDesc.getText().toString());

        routeReview.setStartPlaceInfo(sharedDB.getStartPlaceInfo());
        routeReview.setEndPlaceInfo(sharedDB.getEndPlaceInfo());
        routeReview.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        routeReview.setRoutePathID(sharedDB.getRouteID());


        if (mDb.routeReviewDao().insertRouteReview(routeReview) > 0) {
            mViewModel.sendDataToFirebaseDB(getContext());
            Toast.makeText(getContext(), "Save Successful", Toast.LENGTH_SHORT).show();
            sharedDB.saveIsLastReviewDone(true);
            sharedDB.clearRouteDetails();

            try {
                Navigation.findNavController(getView()).popBackStack();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            sharedDB.saveIsLastReviewDone(false);
        }


    }
}