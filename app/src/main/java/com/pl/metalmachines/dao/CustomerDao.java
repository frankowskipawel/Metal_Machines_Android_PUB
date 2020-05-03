package com.pl.metalmachines.dao;

import android.util.Log;

import com.pl.metalmachines.dao.enumDao.columnsNames.CustomersColumnsNameDAO;
import com.pl.metalmachines.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomerDao {

    private static final String TABLE_NAME = "customers";

    public static boolean insertCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "INSERT INTO " + TABLE_NAME +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, null);
        preparedStatement.setString(2, customer.getShortName());
        preparedStatement.setString(3, customer.getFullName());
        preparedStatement.setString(4, customer.getStreetAddress());
        preparedStatement.setString(5, customer.getZipCode());
        preparedStatement.setString(6, customer.getCity());
        preparedStatement.setString(7, customer.getNip());
        preparedStatement.setString(8, customer.getRegon());
        preparedStatement.setString(9, customer.getPhoneNumber());
        preparedStatement.setString(10, customer.getEmail());
        Log.i("query insertCustomer", preparedStatement.toString());
        LogDao.insertLog(preparedStatement.toString()); ///Log to database
        boolean isExecute = preparedStatement.execute();
        preparedStatement.close();
        customer.setId(CustomerDao.findIdCustomerByShortName(customer.getShortName()));
        return isExecute;
    }

    public static boolean updateCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        Connection con = UtilsDao.getConnection();
        String query = "UPDATE " + TABLE_NAME + " SET " +
                CustomersColumnsNameDAO.CUSTOMERS.short_name + "=?, " +
                CustomersColumnsNameDAO.CUSTOMERS.full_name + "=?, " +
                CustomersColumnsNameDAO.CUSTOMERS.street_address + "=?, " +
                CustomersColumnsNameDAO.CUSTOMERS.zip_code + "=?, " +
                CustomersColumnsNameDAO.CUSTOMERS.city + "=?, " +
                CustomersColumnsNameDAO.CUSTOMERS.nip + "=?, " +
                CustomersColumnsNameDAO.CUSTOMERS.regon + "=?, " +
                CustomersColumnsNameDAO.CUSTOMERS.phone_number + "=?, " +
                CustomersColumnsNameDAO.CUSTOMERS.email + "=? " +
                "WHERE " +
                CustomersColumnsNameDAO.CUSTOMERS.id + "=?;";
        PreparedStatement prest = con.prepareStatement(query);
        prest.setString(1, customer.getShortName());
        prest.setString(2, customer.getFullName());
        prest.setString(3, customer.getStreetAddress());
        prest.setString(4, customer.getZipCode());
        prest.setString(5, customer.getCity());
        prest.setString(6, customer.getNip());
        prest.setString(7, customer.getRegon());
        prest.setString(8, customer.getPhoneNumber());
        prest.setString(9, customer.getEmail());
        prest.setString(10, customer.getId());
        boolean isExecute = prest.execute();
        Log.i("query updateCustomer", prest.toString());
        LogDao.insertLog(prest.toString()); ///Log to database
        prest.close();
        return isExecute;
    }

    public static Customer findCustomerById(String idCustomer) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + CustomersColumnsNameDAO.CUSTOMERS.id + "=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, idCustomer);
        Log.i("query indCustomerById", preparedStatement.toString());
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.id);
            String shortName = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.short_name);
            String fullName = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.full_name);
            String streetAddress = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.street_address);
            String zipCode = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.zip_code);
            String city = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.city);
            String nip = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.nip);
            String regon = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.regon);
            String phoneNumber = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.phone_number);
            String email = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.email);
            preparedStatement.close();
            return new Customer(id, shortName, fullName, streetAddress, zipCode, city, nip, regon, phoneNumber, email);
        }
        return null;
    }

    public static String findIdCustomerByShortName(String shortName) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + CustomersColumnsNameDAO.CUSTOMERS.short_name + "=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, shortName);
        ResultSet resultSet = preparedStatement.executeQuery();
        Log.i("SQL query", "findIdCustomerByShortName : " + preparedStatement.toString());
        while (resultSet.next()) {
            String id = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.id);
            preparedStatement.close();
            return id;
        }
        return null;
    }

    public static Customer findCustomerByShortName(String inputShortName) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + CustomersColumnsNameDAO.CUSTOMERS.short_name + "=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, inputShortName);
        ResultSet resultSet = preparedStatement.executeQuery();
        Log.i("findCustomerByShortName", preparedStatement.toString());
        while (resultSet.next()) {
            String id = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.id);
            String shortName = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.short_name);
            String fullName = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.full_name);
            String streetAddress = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.street_address);
            String zipCode = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.zip_code);
            String city = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.city);
            String nip = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.nip);
            String regon = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.regon);
            String phoneNumber = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.phone_number);
            String email = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.email);
            Customer customer = new Customer(id, shortName, fullName, streetAddress, zipCode, city, nip, regon, phoneNumber, email);
            preparedStatement.close();
            return customer;
        }
        return null;
    }

    public static List<Map<String, String>> findAllCustomersForListView() throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "SELECT * FROM " + TABLE_NAME + ";";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery(query);
        List<Map<String, String>> customersData = new ArrayList<Map<String, String>>();
        Log.i("SQL query", query);
        while (resultSet.next()) {
            String dateRow = "";
            String id = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.id);
            String shortName = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.short_name);
            String fullName = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.full_name);
            if (!fullName.equals("")) {
                dateRow += fullName + "\n";
            }
            String streetAddress = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.street_address);
            if (!streetAddress.equals("")) {
                dateRow += streetAddress + ", ";
            }
            String zipCode = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.zip_code);
            if (!zipCode.equals("")) {
                dateRow += zipCode + " ";
            }
            String city = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.city);
            if (!city.equals("")) {
                dateRow += city + "\n";
            }
            String nip = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.nip);
            if (!nip.equals("")) {
                dateRow += "NIP : " + nip + "\n";
            }
            String regon = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.regon);
            if (!nip.equals("")) {
                dateRow += "REGON : " + regon + "\n";
            }
            String phoneNumber = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.phone_number);
            if (!phoneNumber.equals("")) {
                dateRow += "tel. " + phoneNumber + "\n";
            }
            String email = resultSet.getString(CustomersColumnsNameDAO.CUSTOMERS.email);
            if (!email.equals("")) {
                dateRow += "email. " + email + "\n";
            }
            Map<String, String> row = new HashMap<String, String>(2);
            row.put("First Line", "#" + id + " " + shortName);
            row.put("Second Line", dateRow);
            customersData.add(row);
        }
        preparedStatement.close();
        return customersData;
    }

    public static boolean deleteCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " +
                CustomersColumnsNameDAO.CUSTOMERS.id + "=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, customer.getId());
        Log.i("query deleteCustomer", query);
        LogDao.insertLog(preparedStatement.toString()); ///Log to database
        boolean isExecute = preparedStatement.execute();
        preparedStatement.close();
        return isExecute;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }
}
