package com.storozhevykh.forexfourieranalyzer.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ParameterDao {

    @Query("SELECT * FROM ParameterUnit")
    List<ParameterUnit> getAll();

    @Insert
    void insert(ParameterUnit parameter);

    @Update
    void update(ParameterUnit parameter);

}
