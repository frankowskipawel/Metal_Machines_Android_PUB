package com.pl.metalmachines.dao;

import android.util.Log;

import com.pl.metalmachines.dao.enumDao.columnsNames.CategoryColumnsNamesDAO;
import com.pl.metalmachines.dao.enumDao.columnsNames.CustomersColumnsNameDAO;
import com.pl.metalmachines.dao.enumDao.columnsNames.DevicesColumnsNameDAO;
import com.pl.metalmachines.model.Device;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class DeviceDao {
    private static final String TABLE_NAME = "devices";
    private static boolean isTest = false;

    public static boolean insertDevice(Device device) throws SQLException, ClassNotFoundException {
        Log.e("insertDevice", "device: " + device.toString());
        Connection connection = UtilsDao.getConnection();
        connection.setAutoCommit(false);
        device.setName(device.getName().toUpperCase());
        String query = "INSERT INTO " + TABLE_NAME +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, device.getId());
        preparedStatement.setString(2, device.getName());
        preparedStatement.setString(3, device.getSerialNumber());
        preparedStatement.setString(4, device.getSourcePower());
        preparedStatement.setString(5, device.getCustomerId());
        preparedStatement.setString(6, device.getCategoryId());
        preparedStatement.setString(7, device.getTransferDate());
        boolean isExecute = preparedStatement.execute();
        String findIdQuery = "SELECT max(" + DevicesColumnsNameDAO.DEVICES.id + ") FROM " + TABLE_NAME + ";";
        PreparedStatement prestForFindId = connection.prepareStatement(findIdQuery);
        prestForFindId.execute();
        ResultSet resultSet = prestForFindId.executeQuery();
        String id = null;
        while (resultSet.next()) {
            id = resultSet.getString("max(" + DevicesColumnsNameDAO.DEVICES.id + ")");
        }
        device.setId(id);
        LogDao.insertLog(preparedStatement.toString());
        preparedStatement.close();
        prestForFindId.close();
        if (isTest == false) {
            connection.commit();
            connection.setAutoCommit(true);
        }
        return isExecute;
    }

    public static boolean updateDevice(Device device) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "UPDATE " + TABLE_NAME + " SET "
                + DevicesColumnsNameDAO.DEVICES.name + "=?, " +
                DevicesColumnsNameDAO.DEVICES.serialNumber + "=?, " +
                DevicesColumnsNameDAO.DEVICES.sourcePower + "=?, " +
                DevicesColumnsNameDAO.DEVICES.customer_id + "=?, " +
                DevicesColumnsNameDAO.DEVICES.category_id + "=?, " +
                DevicesColumnsNameDAO.DEVICES.transferDate + "=?" +
                " WHERE " + DevicesColumnsNameDAO.DEVICES.id + "=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, device.getName().toUpperCase());
        preparedStatement.setString(2, device.getSerialNumber());
        preparedStatement.setString(3, device.getSourcePower());
        preparedStatement.setString(4, device.getCustomerId());
        preparedStatement.setString(5, device.getCategoryId());
        preparedStatement.setString(6, device.getTransferDate());
        preparedStatement.setString(7, device.getId());
        Log.i("query updateDevice", preparedStatement.toString());
        boolean isExecute = preparedStatement.execute();
        LogDao.insertLog(preparedStatement.toString());
        preparedStatement.close();
        return isExecute;
    }

    public static boolean deleteDevice(Device device) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " +
                DevicesColumnsNameDAO.DEVICES.id + "=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, device.getId());
        boolean isExecute = preparedStatement.execute();
        Log.i("query deleteDevice", preparedStatement.toString());
        LogDao.insertLog(preparedStatement.toString());
        preparedStatement.close();
        return isExecute;
    }

    public static TreeSet<Device> findAllDevicesForListView() throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "SELECT * FROM " + TABLE_NAME + " INNER JOIN " +
                CustomerDao.getTableName() + " ON " + TABLE_NAME + "." +
                DevicesColumnsNameDAO.DEVICES.customer_id + "=" + CustomerDao.getTableName() + "." + CustomersColumnsNameDAO.CUSTOMERS.id +
                " INNER JOIN " + CategoryDao.getTableName() + " ON " + TABLE_NAME +
                "." + DevicesColumnsNameDAO.DEVICES.category_id + "=" + CategoryDao.getTableName() + "." + CategoryColumnsNamesDAO.CATEGORY.id;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery(query);
        TreeSet<Device> devices = new TreeSet<>();
        while (resultSet.next()) {
            String id = resultSet.getString(DevicesColumnsNameDAO.DEVICES.id);
            String name = resultSet.getString(DevicesColumnsNameDAO.DEVICES.name);
            String serialNumber = resultSet.getString(DevicesColumnsNameDAO.DEVICES.serialNumber);
            String sorcePower = resultSet.getString(DevicesColumnsNameDAO.DEVICES.sourcePower);
            String customerId = resultSet.getString(DevicesColumnsNameDAO.DEVICES.customer_id);
            String customerShortName = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.short_name);
            String categoryId = resultSet.getString(DevicesColumnsNameDAO.DEVICES.category_id);
            String categoryName = resultSet.getString(CategoryColumnsNamesDAO.CATEGORY.categoryName);
            String transferDate = resultSet.getString(DevicesColumnsNameDAO.DEVICES.transferDate);
            Device device = new Device(id, name, serialNumber, sorcePower, customerId, categoryId, transferDate);
            device.setCustomerName(customerShortName);
            device.setCategoryName(categoryName);
            devices.add(device);
        }
        Log.i("SQL query", preparedStatement.toString());
        preparedStatement.close();
        return devices;
    }

    public static Device findDeviceById(String inputId) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "SELECT * FROM " + TABLE_NAME + " INNER JOIN " + CustomerDao.getTableName() + " ON " + TABLE_NAME + "." + DevicesColumnsNameDAO.DEVICES.customer_id + "=" + CustomerDao.getTableName() + "." + CustomersColumnsNameDAO.CUSTOMERS.id + " INNER JOIN " + CategoryDao.getTableName() + " ON " + TABLE_NAME + "." + DevicesColumnsNameDAO.DEVICES.category_id + "=" + CategoryDao.getTableName() + "." + CategoryColumnsNamesDAO.CATEGORY.id + " WHERE " + TABLE_NAME + "." + DevicesColumnsNameDAO.DEVICES.id + "=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, inputId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString(DevicesColumnsNameDAO.DEVICES.id);
            String name = resultSet.getString(DevicesColumnsNameDAO.DEVICES.name);
            String serialNumber = resultSet.getString(DevicesColumnsNameDAO.DEVICES.serialNumber);
            String sourcePower = resultSet.getString(DevicesColumnsNameDAO.DEVICES.sourcePower);
            String customerId = resultSet.getString(DevicesColumnsNameDAO.DEVICES.customer_id);
            String customerShortName = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.short_name);
            String categoryId = resultSet.getString(DevicesColumnsNameDAO.DEVICES.category_id);
            String categoryName = resultSet.getString(CategoryColumnsNamesDAO.CATEGORY.categoryName);
            String transferDate = resultSet.getString(DevicesColumnsNameDAO.DEVICES.transferDate);
            Device device = new Device(id, name, serialNumber, sourcePower, customerId, categoryId, transferDate);
            device.setCategoryName(categoryName);
            device.setCustomerName(customerShortName);
            Log.i("findDeviceById", preparedStatement.toString());
            preparedStatement.close();
            return device;
        }
        return null;
    }

    public static List<Map<String, String>> findCustomerDevicesForListView(String customerId) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "SELECT * FROM " + TABLE_NAME + " INNER JOIN " + CustomerDao.getTableName() + " ON " + TABLE_NAME + "." + DevicesColumnsNameDAO.DEVICES.customer_id + "=" + CustomerDao.getTableName() + "." + CustomersColumnsNameDAO.CUSTOMERS.id + " INNER JOIN " + CategoryDao.getTableName() + " ON " + TABLE_NAME + "." + DevicesColumnsNameDAO.DEVICES.category_id + "=" + CategoryDao.getTableName() + "." + CategoryColumnsNamesDAO.CATEGORY.id + " WHERE " + DeviceDao.getTableName() + "." + DevicesColumnsNameDAO.DEVICES.customer_id + "=" + customerId;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery(query);
        List<Map<String, String>> deviceData = new ArrayList<Map<String, String>>();
        Log.i("SQL query", "findCustomerDevicesForListView : " + preparedStatement.toString());
        while (resultSet.next()) {
            String content = "";
            int id = resultSet.getInt(DevicesColumnsNameDAO.DEVICES.id);
            String name = resultSet.getString(DevicesColumnsNameDAO.DEVICES.name);
            String serialNumber = resultSet.getString(DevicesColumnsNameDAO.DEVICES.serialNumber);
            content += "Nr. ser. : " + serialNumber + "\n";
            String surcePower = resultSet.getString(DevicesColumnsNameDAO.DEVICES.sourcePower);
            content += "Moc źródła : " + surcePower + "\n";
            String customerShortName = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.short_name);
            content += "Kontrahent : " + customerShortName + "\n";
            String categoryName = resultSet.getString(CategoryColumnsNamesDAO.CATEGORY.categoryName);
            content += "Kategoria : " + categoryName + "\n";
            String transferDate = resultSet.getString(DevicesColumnsNameDAO.DEVICES.transferDate);
            content += "Data odbioru : " + transferDate;
            Map<String, String> row = new HashMap<String, String>(2);
            row.put("First Line", "#" + id + " " + name);
            row.put("Second Line", content);
            deviceData.add(row);
        }
        preparedStatement.close();
        return deviceData;
    }

    public static List<Map<String, String>> findCategoryDevicesForListView(String categoryId) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "SELECT * FROM " + TABLE_NAME + " INNER JOIN " + CustomerDao.getTableName() + " ON " + TABLE_NAME + "." + DevicesColumnsNameDAO.DEVICES.customer_id + "=" + CustomerDao.getTableName() + "." + CustomersColumnsNameDAO.CUSTOMERS.id + " INNER JOIN " + CategoryDao.getTableName() + " ON " + TABLE_NAME + "." + DevicesColumnsNameDAO.DEVICES.category_id + "=" + CategoryDao.getTableName() + "." + CategoryColumnsNamesDAO.CATEGORY.id + " WHERE " + DeviceDao.getTableName() + "." + DevicesColumnsNameDAO.DEVICES.category_id + "=" + categoryId;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery(query);
        List<Map<String, String>> deviceData = new ArrayList<Map<String, String>>();
        Log.i("SQL query", query);
        while (resultSet.next()) {
            String content = "";
            int id = resultSet.getInt(DevicesColumnsNameDAO.DEVICES.id);
            String name = resultSet.getString(DevicesColumnsNameDAO.DEVICES.name);
            String serialNumber = resultSet.getString(DevicesColumnsNameDAO.DEVICES.serialNumber);
            content += "Nr. ser. : " + serialNumber + "\n";
            String surcePower = resultSet.getString(DevicesColumnsNameDAO.DEVICES.sourcePower);
            content += "Moc źródła : " + surcePower + "\n";
            String customerShortName = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.short_name);
            content += "Kontrahent : " + customerShortName + "\n";
            String categoryName = resultSet.getString(CategoryColumnsNamesDAO.CATEGORY.categoryName);
            content += "Kategoria : " + categoryName + "\n";
            String transferDate = resultSet.getString(DevicesColumnsNameDAO.DEVICES.transferDate);
            content += "Data odbioru : " + transferDate;
            Map<String, String> row = new HashMap<String, String>(2);
            row.put("First Line", "#" + id + " " + name);
            row.put("Second Line", content);
            deviceData.add(row);
        }
        preparedStatement.close();
        return deviceData;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static void setIsTest(boolean isTest) {
        DeviceDao.isTest = isTest;
    }
}
