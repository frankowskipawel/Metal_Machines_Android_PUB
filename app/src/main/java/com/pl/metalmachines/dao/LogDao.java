package com.pl.metalmachines.dao;

import android.util.Log;

import com.pl.metalmachines.dao.enumDao.columnsNames.LogColumnsNameDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogDao {

    private static final String TABLE_NAME = "logs";
    private static final int MAX_LENGTH_STRING_FOR_LIST_VIEW = 150;

    public static boolean insertLog(String logString) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "INSERT INTO " + TABLE_NAME +
                "(" + LogColumnsNameDAO.LOG.date + ", " +
                LogColumnsNameDAO.LOG.user + ", " +
                LogColumnsNameDAO.LOG.log + ""
                + ") " +
                "VALUES (now(),'" + UtilsDao.getUSER() + "','" + logString.replace("\'", "`") + "');";
        Log.i("SQL query", query);
        PreparedStatement prest = connection.prepareStatement(query);
        boolean isExecute = prest.execute();
        prest.close();
        return isExecute;
    }

    public static List<Map<String, String>> findAllLogsForListView() throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + LogColumnsNameDAO.LOG.id + " DESC LIMIT 500;";
        PreparedStatement prest = connection.prepareStatement(query);
        ResultSet resultSet = prest.executeQuery(query);
        List<Map<String, String>> categoriesData = new ArrayList<Map<String, String>>();
        Log.i("SQL query", "findAllLogsForListView" + query);
        while (resultSet.next()) {
            String id = resultSet.getString(LogColumnsNameDAO.LOG.id);
            String date = resultSet.getString(LogColumnsNameDAO.LOG.date);
            String user = resultSet.getString(LogColumnsNameDAO.LOG.user);
            String log = resultSet.getString(LogColumnsNameDAO.LOG.log);
            Map<String, String> row = new HashMap<String, String>(2);
            row.put("First Line", "#" + id + " (" + date + ") " + user);
            row.put("Second Line", log);
            categoriesData.add(row);
        }
        prest.close();
        return categoriesData;
    }
}
