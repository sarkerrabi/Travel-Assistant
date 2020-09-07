package com.tnrlab.travelassistant.databases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tnrlab.travelassistant.models.creaet_path.RoutePath;
import com.tnrlab.travelassistant.models.creaet_path.RouteReview;

import java.util.List;

@Dao
public interface RoutePathDao {
    @Query("SELECT * FROM Route_Path ORDER BY ID")
    List<RoutePath> loadAllPersons();

    @Insert
    long insertRoutePath(RoutePath routePath);

    @Update
    void updatePerson(RoutePath routePath);

    @Delete
    void delete(RoutePath routePath);

    @Query("SELECT * FROM Route_Path WHERE id = :id")
    RoutePath loadPersonById(int id);

/*    @Query("UPDATE Route_Path SET endAddress = :endPlace  WHERE routePathID = :routeID")
    void updateRoutePathEndAddressWhereRouteID(String endPlace, long routeID);

    @Query("UPDATE Route_Path SET startAddress = :startPlace  WHERE routePathID = :routeID")
    void updateRoutePathStartAddressWhereRouteID(String startPlace,long routeID);*/

    @Insert
    long insertRouteReview(RouteReview routeReview);


}
