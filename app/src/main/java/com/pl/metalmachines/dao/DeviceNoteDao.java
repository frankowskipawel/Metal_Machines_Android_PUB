package com.pl.metalmachines.dao;
import android.util.Log;

import com.pl.metalmachines.dao.enumDao.columnsNames.NoteColumnsNamesDAO;
import com.pl.metalmachines.model.DeviceNote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class DeviceNoteDao {

    private static final String DEVICES_NOTES = "devices_notes";

    public static boolean insertDeviceNote(DeviceNote deviceNote) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        connection.setAutoCommit(false);
        String query = "INSERT INTO " + DEVICES_NOTES +
                " (" + NoteColumnsNamesDAO.NOTE.idDevice + ", " +
                NoteColumnsNamesDAO.NOTE.text
                + ") " +
                "VALUES (?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, deviceNote.getIdDevice());
        preparedStatement.setString(2, deviceNote.getText());
        Log.i("addNewDeviceNote query", preparedStatement.toString());
        boolean isExecute = preparedStatement.execute();
        String getIdFromNewInsertQuery = "SELECT max(" + NoteColumnsNamesDAO.NOTE.id + ") FROM " + DEVICES_NOTES;
        Log.i("addNewDeviceNote query2", getIdFromNewInsertQuery);
        preparedStatement = connection.prepareStatement(getIdFromNewInsertQuery);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString("max(" + NoteColumnsNamesDAO.NOTE.id + ")");
            deviceNote.setId(id);
        }
        connection.commit();
        connection.setAutoCommit(true);
        preparedStatement.close();
        return isExecute;
    }

    public static boolean updateDeviceNote(DeviceNote deviceNote) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "UPDATE " + DEVICES_NOTES + " SET " +
                NoteColumnsNamesDAO.NOTE.idDevice + "=?, " +
                NoteColumnsNamesDAO.NOTE.text + "=? " +
                "WHERE (" + NoteColumnsNamesDAO.NOTE.id + "=?);";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, deviceNote.getIdDevice());
        preparedStatement.setString(2, deviceNote.getText());
        preparedStatement.setString(3, deviceNote.getId());
        Log.i("updateDeviceNote query", preparedStatement.toString());
        boolean isExecute = preparedStatement.execute();
        LogDao.insertLog(preparedStatement.toString());
        preparedStatement.close();
        return isExecute;
    }

    public static boolean deleteDeviceNote(DeviceNote deviceNote) throws SQLException, ClassNotFoundException {
        Connection con = UtilsDao.getConnection();
        String query = "DELETE FROM " + DEVICES_NOTES + " WHERE " + NoteColumnsNamesDAO.NOTE.id + "=?;";
        PreparedStatement prepareStatement = con.prepareStatement(query);
        prepareStatement.setString(1, deviceNote.getId());
        Log.i("deleteDeviceNote query", prepareStatement.toString());
        boolean isExecute = prepareStatement.execute();
        LogDao.insertLog(prepareStatement.toString());
        prepareStatement.close();
        return isExecute;
    }

    public static List<Map<String, String>> findAllNotesByIdDevice(String idDevice) throws SQLException, ClassNotFoundException {
        Connection con = UtilsDao.getConnection();
        String query;
        query = "SELECT * FROM " + DEVICES_NOTES + " WHERE " + NoteColumnsNamesDAO.NOTE.idDevice + "=? ORDER BY " + NoteColumnsNamesDAO.NOTE.id + " DESC";
        Log.i("SQL query", query);
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setString(1, idDevice);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, String>> notesList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt(NoteColumnsNamesDAO.NOTE.id);
            String text = resultSet.getString(NoteColumnsNamesDAO.NOTE.text);
            Map<String, String> row = new HashMap<>(2);
            row.put("First Line", id + "");
            row.put("Second Line", text);
            notesList.add(row);
        }
        preparedStatement.close();
        return notesList;
    }
}
