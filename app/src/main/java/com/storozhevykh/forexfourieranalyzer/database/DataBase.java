package com.storozhevykh.forexfourieranalyzer.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ParameterUnit.class}, version = 1)
public abstract class DataBase extends RoomDatabase {

    public abstract ParameterDao parameterDao();
}
