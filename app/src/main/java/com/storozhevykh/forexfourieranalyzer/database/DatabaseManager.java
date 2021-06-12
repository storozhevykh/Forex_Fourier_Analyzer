package com.storozhevykh.forexfourieranalyzer.database;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DatabaseManager {

    public static List<ParameterUnit> getParameters() {
        //Log.i("roomData", "Trying to get parameters from database");
        AsyncTask<Void, Void, List<ParameterUnit>> task = new AsyncTask<Void, Void, List<ParameterUnit>>() {
            @Override
            protected List<ParameterUnit> doInBackground(Void... Voids) {
                return getParameterDao().getAll();
            }
        };
        task.execute();
        try {
            return task.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void insertParametersList(List<ParameterUnit> parameterUnitList) {
        ParameterDao parameterDao = getParameterDao();
        //Log.i("roomData", "Trying to insert parameters to database");
        AsyncTask<List<ParameterUnit>, Void, Void> task = new AsyncTask<List<ParameterUnit>, Void, Void>() {
            @Override
            protected Void doInBackground(List<ParameterUnit>... lists) {
                for (ParameterUnit parameterUnit : lists[0]) {
                    parameterDao.insert(parameterUnit);
                }
                return null;
            }
        };
        task.execute(parameterUnitList);
    }

    public static void updateParametersList(List<ParameterUnit> parameterUnitList) {
        ParameterDao parameterDao = getParameterDao();
        //Log.i("roomData", "Trying to update parameters in database");
        AsyncTask<List<ParameterUnit>, Void, Void> task = new AsyncTask<List<ParameterUnit>, Void, Void>() {
            @Override
            protected Void doInBackground(List<ParameterUnit>... lists) {
                for (ParameterUnit parameterUnit : lists[0]) {
                    parameterDao.update(parameterUnit);
                }
                return null;
            }
        };
        task.execute(parameterUnitList);
    }

    private static ParameterDao getParameterDao() {
        DataBase db = App.getInstance().getDatabase();
        ParameterDao parameterDao = db.parameterDao();
        return parameterDao;
    }

}
