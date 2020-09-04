package com.tnrlab.travelassistant.databases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tnrlab.travelassistant.models.creaet_path.RoutePath;

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
}
