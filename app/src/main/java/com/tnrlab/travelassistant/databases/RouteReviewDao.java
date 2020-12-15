package com.tnrlab.travelassistant.databases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.tnrlab.travelassistant.models.creaet_path.RouteReview;

import java.util.List;

@Dao
public interface RouteReviewDao {
    @Query("SELECT * FROM Route_Review ORDER BY ID")
    List<RouteReview> loadAllRouteReview();

    @Insert
    long insertRouteReview(RouteReview routeReview);

    @Delete
    void deleteRouteReview(RouteReview routeReview);
}
