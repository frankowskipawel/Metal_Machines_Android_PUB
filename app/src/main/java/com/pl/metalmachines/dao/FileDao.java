package com.pl.metalmachines.dao;

import android.util.Log;

import com.pl.metalmachines.dao.enumDao.columnsNames.FilesColumnsNamesDAO;
import com.pl.metalmachines.model.FileModel;
import com.pl.metalmachines.model.enums.FileType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class FileDao {

    private static final String TABLE_NAME = "files";

    public static boolean insertNewFile(FileModel fileModel) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        connection.setAutoCommit(false);
        String query = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, 0); //ai
        preparedStatement.setString(2, fileModel.getIdDevice());
        preparedStatement.setString(3, fileModel.getIdService());
        preparedStatement.setString(4, fileModel.getDate());
        preparedStatement.setString(5, fileModel.getFileType().toString());
        preparedStatement.setString(6, fileModel.getDescription());
        preparedStatement.setString(7, fileModel.getFilename());
        Log.i("insertNewFile query", preparedStatement.toString());
        boolean isExecute = preparedStatement.execute();
        LogDao.insertLog(preparedStatement.toString());
        String setIdQuery = "SELECT max(" + FilesColumnsNamesDAO.FILES.id + ") FROM " + TABLE_NAME;
        Log.i("setIdQuery", setIdQuery);
        preparedStatement = connection.prepareStatement(setIdQuery);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString("max(" + FilesColumnsNamesDAO.FILES.id + ")");
            fileModel.setId(id);
        }
        connection.commit();
        connection.setAutoCommit(true);
        preparedStatement.close();
        return isExecute;
    }

    public static boolean updateDeviceFile(FileModel fileModel) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query;
        query = "UPDATE " + TABLE_NAME + " SET " +
                FilesColumnsNamesDAO.FILES.idDevice + "=?, " +
                FilesColumnsNamesDAO.FILES.idService + "=?, " +
                FilesColumnsNamesDAO.FILES.date + "=?, " +
                FilesColumnsNamesDAO.FILES.type + "=?, " +
                FilesColumnsNamesDAO.FILES.description + "=?, " +
                FilesColumnsNamesDAO.FILES.fileName + "=? " +
                "WHERE (" + FilesColumnsNamesDAO.FILES.id + "=?);";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, fileModel.getIdDevice());
        preparedStatement.setString(2, fileModel.getIdService());
        preparedStatement.setString(3, fileModel.getDate());
        preparedStatement.setString(4, fileModel.getFileType().toString());
        preparedStatement.setString(5, fileModel.getDescription());
        preparedStatement.setString(6, fileModel.getFilename());
        preparedStatement.setInt(7, Integer.parseInt(fileModel.getId()));
        Log.i("updateDeviceFile query", preparedStatement.toString());
        Log.e(TAG, preparedStatement.toString());
        boolean isExecute = preparedStatement.execute();
        LogDao.insertLog(preparedStatement.toString());
        preparedStatement.close();
        return isExecute;
    }

    public static boolean deleteDeviceFile(FileModel fileModel) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "DELETE FROM "+ TABLE_NAME +" WHERE "+ FilesColumnsNamesDAO.FILES.id+"=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,Integer.parseInt(fileModel.getId()));
        boolean isExecute = preparedStatement.execute();
        LogDao.insertLog(preparedStatement.toString());
        preparedStatement.close();
        return  isExecute;
    }

    public static ArrayList<FileModel> findAllFilesByIdDevice(String idDevice) throws SQLException, ClassNotFoundException {
        Connection con = UtilsDao.getConnection();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + FilesColumnsNamesDAO.FILES.idDevice + "=?";
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setString(1, idDevice);
        Log.i("SQL query", "findAllFilesByIdDevice : "+preparedStatement.toString());
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<FileModel> fileList = new ArrayList<>();
        while (resultSet.next()) {
            String id = resultSet.getString(FilesColumnsNamesDAO.FILES.id);
            String idDev = resultSet.getString(FilesColumnsNamesDAO.FILES.idDevice);
            String idService = resultSet.getString(FilesColumnsNamesDAO.FILES.idService);
            String date = resultSet.getString(FilesColumnsNamesDAO.FILES.date);
            String type = resultSet.getString(FilesColumnsNamesDAO.FILES.type);
            String description = resultSet.getString(FilesColumnsNamesDAO.FILES.description);
            String filename = resultSet.getString(FilesColumnsNamesDAO.FILES.fileName);
            fileList.add(new FileModel(id,idDev,idService,date,FileType.valueOf(type),description,filename));
        }
        preparedStatement.close();
        return  fileList;
    }

    public static ArrayList<FileModel> findAllFilesByIdService(String idService) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + FilesColumnsNamesDAO.FILES.idService + "=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, idService);
        Log.i("SQL query", "findAllFilesByIdService : "+preparedStatement.toString());
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<FileModel> filesList = new ArrayList<>();
        while (resultSet.next()) {
            String id = resultSet.getString(FilesColumnsNamesDAO.FILES.id);
            String idDev = resultSet.getString(FilesColumnsNamesDAO.FILES.idDevice);
            String idServ = resultSet.getString(FilesColumnsNamesDAO.FILES.idService);
            String date = resultSet.getString(FilesColumnsNamesDAO.FILES.date);
            String type = resultSet.getString(FilesColumnsNamesDAO.FILES.type);
            String description = resultSet.getString(FilesColumnsNamesDAO.FILES.description);
            String filename = resultSet.getString(FilesColumnsNamesDAO.FILES.fileName);
            filesList.add(new FileModel(id,idDev,idServ,date,FileType.valueOf(type),description,filename));
        }
        preparedStatement.close();
        return  filesList;
    }
}
