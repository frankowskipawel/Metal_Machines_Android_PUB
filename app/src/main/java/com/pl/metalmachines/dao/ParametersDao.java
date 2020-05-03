package com.pl.metalmachines.dao;

import android.util.Log;

import com.google.gson.Gson;
import com.pl.metalmachines.dao.enumDao.columnsNames.ParametersColumnNameDAO;
import com.pl.metalmachines.model.parametersDevice.ParametersDevice;
import com.pl.metalmachines.model.parametersDevice.StringParameters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class ParametersDao {

    private static final String TABLE_NAME = "parameters";

    public static boolean insertParameters(ParametersDevice parametersDevice) throws SQLException, ClassNotFoundException {
        Gson gson = new Gson();
        String gSonParametersToString = gson.toJson(parametersDevice.getStringParameters());
        Connection connection = UtilsDao.getConnection();
        String query = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, parametersDevice.getId());
        preparedStatement.setString(2, parametersDevice.getIdDevice());
        preparedStatement.setString(3, parametersDevice.getMaterial());
        preparedStatement.setString(4, parametersDevice.getGrubosc());
        preparedStatement.setString(5, parametersDevice.getModelDyszy());
        preparedStatement.setString(6, parametersDevice.getGaz());
        preparedStatement.setString(7, gSonParametersToString);
        Log.i("SQL query", preparedStatement.toString());
        boolean isExecute = preparedStatement.execute();
        LogDao.insertLog(preparedStatement.toString());
        preparedStatement.close();
        return isExecute;
    }

    public static boolean updateParameters(ParametersDevice parametersDevice) throws SQLException, ClassNotFoundException {
        Gson gson = new Gson();
        String gSonParametersToString = gson.toJson(parametersDevice.getStringParameters());
        Connection connection = UtilsDao.getConnection();
        String query = "UPDATE " + TABLE_NAME + " SET " + ParametersColumnNameDAO.PARAMETERS.GSonParameters + "=? WHERE " +
                ParametersColumnNameDAO.PARAMETERS.idDevice + "=? AND " +
                ParametersColumnNameDAO.PARAMETERS.material + "=? AND " +
                ParametersColumnNameDAO.PARAMETERS.grubosc + "=? AND " +
                ParametersColumnNameDAO.PARAMETERS.modelDyszy + "=? AND " +
                ParametersColumnNameDAO.PARAMETERS.gaz + "=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, gSonParametersToString);
        preparedStatement.setString(2, parametersDevice.getIdDevice());
        preparedStatement.setString(3, parametersDevice.getMaterial());
        preparedStatement.setString(4, parametersDevice.getGrubosc());
        preparedStatement.setString(5, parametersDevice.getModelDyszy());
        preparedStatement.setString(6, parametersDevice.getGaz());
        Log.i("updateParameters query", preparedStatement.toString());
        boolean isExecute = preparedStatement.execute();
        LogDao.insertLog(preparedStatement.toString());
        preparedStatement.close();
        return isExecute;
    }

    public static StringParameters findStringParameters(String deviceId, String material, String grubosc, String modelDyszy, String gaz) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                ParametersColumnNameDAO.PARAMETERS.idDevice + "=?" +
                " AND " + ParametersColumnNameDAO.PARAMETERS.material + "=? " +
                "AND " + ParametersColumnNameDAO.PARAMETERS.grubosc + "=?" +
                " AND " + ParametersColumnNameDAO.PARAMETERS.modelDyszy + "=? " +
                " AND " + ParametersColumnNameDAO.PARAMETERS.gaz + "=?";
        Log.i("SQL query ", "findStringParameters  " + query);
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, deviceId);
        preparedStatement.setString(2, material);
        preparedStatement.setString(3, grubosc);
        preparedStatement.setString(4, modelDyszy);
        preparedStatement.setString(5, gaz);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String parametersGson = resultSet.getString(ParametersColumnNameDAO.PARAMETERS.GSonParameters);
            preparedStatement.close();
            Gson gson = new Gson();
            StringParameters stringParameters = gson.fromJson(parametersGson,StringParameters.class);
            return stringParameters;
        }
        return null;
    }

    public static ArrayList<ParametersDevice> getAllParameters(String deviceId) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ParametersColumnNameDAO.PARAMETERS.idDevice + "=? ORDER BY " + ParametersColumnNameDAO.PARAMETERS.material + "," + ParametersColumnNameDAO.PARAMETERS.grubosc + "," + ParametersColumnNameDAO.PARAMETERS.modelDyszy + "," + ParametersColumnNameDAO.PARAMETERS.gaz + ";";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, deviceId);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<ParametersDevice> parametersDevices = new ArrayList<>();
        while (resultSet.next()) {
            String idDevice = resultSet.getString(ParametersColumnNameDAO.PARAMETERS.idDevice);
            String grubosc = resultSet.getString(ParametersColumnNameDAO.PARAMETERS.grubosc);
            String material = resultSet.getString(ParametersColumnNameDAO.PARAMETERS.material);
            String modelDyszy = resultSet.getString(ParametersColumnNameDAO.PARAMETERS.modelDyszy);
            String gaz = resultSet.getString(ParametersColumnNameDAO.PARAMETERS.gaz);
            String gsonParameters = resultSet.getString(ParametersColumnNameDAO.PARAMETERS.GSonParameters);
            Gson gson = new Gson();
            StringParameters stringParameters = gson.fromJson(gsonParameters, StringParameters.class);
            parametersDevices.add(new ParametersDevice(null, idDevice, material, grubosc, modelDyszy, gaz, stringParameters));
        }
        preparedStatement.close();
        return parametersDevices;
    }

    public static boolean deleteParameters(ParametersDevice parametersDevice) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " +
                ParametersColumnNameDAO.PARAMETERS.idDevice + "=? AND " +
                ParametersColumnNameDAO.PARAMETERS.material + "=? AND " +
                ParametersColumnNameDAO.PARAMETERS.grubosc + "=? AND " +
                ParametersColumnNameDAO.PARAMETERS.modelDyszy + "=? AND " +
                ParametersColumnNameDAO.PARAMETERS.gaz + "=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, parametersDevice.getIdDevice());
        preparedStatement.setString(2, parametersDevice.getMaterial());
        preparedStatement.setString(3, parametersDevice.getGrubosc());
        preparedStatement.setString(4, parametersDevice.getModelDyszy());
        preparedStatement.setString(5, parametersDevice.getGaz());
        Log.i("deleteParameters query", preparedStatement.toString());
        LogDao.insertLog(preparedStatement.toString());
        boolean isExecute = preparedStatement.execute();
        preparedStatement.close();
        return isExecute;
    }

    public static Map<String, TreeSet<String>> findAllBaseParameters() throws SQLException, ClassNotFoundException {
        TreeSet<String> materialList = new TreeSet<>();
        TreeSet<String> gruboscList = new TreeSet<>();
        TreeSet<String> modelDyszyList = new TreeSet<>();
        TreeSet<String> gazList = new TreeSet<>();
        Map<String, TreeSet<String>> parametersTreeMap = new TreeMap<>();
        parametersTreeMap.put(ParametersColumnNameDAO.PARAMETERS.material, materialList);
        parametersTreeMap.put(ParametersColumnNameDAO.PARAMETERS.grubosc, gruboscList);
        parametersTreeMap.put(ParametersColumnNameDAO.PARAMETERS.modelDyszy, modelDyszyList);
        parametersTreeMap.put(ParametersColumnNameDAO.PARAMETERS.gaz, gazList);
        Connection connection = UtilsDao.getConnection();
        String query = "SELECT * FROM " + TABLE_NAME + ";";
        Log.i("findAllBaseParameters()", query);
        Statement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            String material = resultSet.getString(ParametersColumnNameDAO.PARAMETERS.material);
            String grubosc = resultSet.getString(ParametersColumnNameDAO.PARAMETERS.grubosc);
            String modelDyszy = resultSet.getString(ParametersColumnNameDAO.PARAMETERS.modelDyszy);
            String gaz = resultSet.getString(ParametersColumnNameDAO.PARAMETERS.gaz);
            materialList.add(material);
            gruboscList.add(grubosc);
            modelDyszyList.add(modelDyszy);
            gazList.add(gaz);
        }
        statement.close();
        return parametersTreeMap;
    }
}