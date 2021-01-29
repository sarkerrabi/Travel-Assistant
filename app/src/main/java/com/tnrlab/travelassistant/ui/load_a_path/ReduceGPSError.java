package com.tnrlab.travelassistant.ui.load_a_path;

import com.tnrlab.travelassistant.models.creaet_path.RoutePath;

import java.util.List;

public class ReduceGPSError {

    List<RoutePath> routePathList;

    public ReduceGPSError(List<RoutePath> routePathList) {
        this.routePathList = routePathList;
    }

    public List<RoutePath> reduceGPSError(){
        KalmanLatLong kalmanLatLong = new KalmanLatLong((float) 0.5);
        kalmanLatLong.SetState(routePathList.get(0).getLatitude(),routePathList.get(0).getLongitude(),routePathList.get(0).getAccuracy(),routePathList.get(0).getLocationTime());
        for (int i = 1; i < routePathList.size(); i++) {
            kalmanLatLong.Process(routePathList.get(i).getLatitude(),routePathList.get(i).getLongitude(),routePathList.get(i).getAccuracy(),routePathList.get(i).getLocationTime());
            routePathList.get(i).setLatitude(kalmanLatLong.get_lat());
            routePathList.get(i).setLongitude(kalmanLatLong.get_lng());
            routePathList.get(i).setAccuracy(kalmanLatLong.get_accuracy());

        }
        return routePathList;

    }

    public List<RoutePath> getRoutePathList() {
        return routePathList;
    }

    public void setRoutePathList(List<RoutePath> routePathList) {
        this.routePathList = routePathList;
    }
}
