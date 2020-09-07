package com.tnrlab.travelassistant.ui;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tnrlab.travelassistant.databases.AppDatabase;
import com.tnrlab.travelassistant.models.creaet_path.RouteDetails;
import com.tnrlab.travelassistant.models.creaet_path.RoutePath;
import com.tnrlab.travelassistant.models.creaet_path.RouteReview;

import java.util.ArrayList;
import java.util.List;

public class RouteReviewViewModel extends ViewModel {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private AppDatabase mDB;
    private List<RouteDetails> routeDetailsList;

    public RouteReviewViewModel() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    public void sendDataToFirebaseDB(Context context) {
        mDB = AppDatabase.getInstance(context);
        routeDetailsList = new ArrayList<>();
        mDatabase.keepSynced(true);
        DatabaseReference newRef = mDatabase.child("route_paths").push();

        List<RouteReview> routeReviewList = mDB.routeReviewDao().loadAllRouteReview();

        for (RouteReview routeReview : routeReviewList) {
            RouteDetails routeDetails = new RouteDetails();
            routeDetails.setRouteReview(routeReview);

            List<RoutePath> routePathList = mDB.routePathDao().loadRoutePathsByRouteID(routeReview.getRoutePathID());

            if (routePathList != null && routePathList.size() > 0) {

                routeDetails.setRoutePathList(routePathList);
            }
            routeDetailsList.add(routeDetails);
        }

        for (RouteDetails routeDetails :
                routeDetailsList) {
            newRef.setValue(routeDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show();
                        mDB.routePathDao().deleteRouteReviewListById(routeDetails.getRoutePathIDList());
                        mDB.routeReviewDao().deleteRouteReview(routeDetails.getRouteReview());

                    }

                }
            });
        }


    }


}