package com.tnrlab.travelassistant.models.creaet_path;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

public class RouteDetails {

    String fireDBRouteKey = "na";
    RouteReview routeReview;
    List<RoutePath> routePathList;

    public RouteDetails() {
    }

    public RouteReview getRouteReview() {
        return routeReview;
    }

    public void setRouteReview(RouteReview routeReview) {
        this.routeReview = routeReview;
    }

    public List<RoutePath> getRoutePathList() {
        return routePathList;
    }

    public void setRoutePathList(List<RoutePath> routePathList) {
        this.routePathList = routePathList;
    }

    public String getFireDBRouteKey() {
        return fireDBRouteKey;
    }

    public void setFireDBRouteKey(String fireDBRouteKey) {
        this.fireDBRouteKey = fireDBRouteKey;
    }

    @Exclude
    public List<Integer> getRoutePathIDList() {
        List<Integer> ids = new ArrayList<>();
        if (routePathList != null) {
            for (RoutePath routePath : routePathList) {
                ids.add(routePath.getId());
            }

        }
        return ids;
    }


}
