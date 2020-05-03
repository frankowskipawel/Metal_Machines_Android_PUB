package com.pl.metalmachines.dao;

import android.util.Log;

import com.google.gson.Gson;
import com.pl.metalmachines.dao.enumDao.columnsNames.CategoryColumnsNamesDAO;
import com.pl.metalmachines.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryDao {

    private static final String TABLE_NAME = "categories";

    public static boolean insertCategory(Category category) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "INSERT INTO " + TABLE_NAME + " VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        Gson gson = new Gson();
        preparedStatement.setString(1, category.getId());
        preparedStatement.setString(2, category.getName());
        preparedStatement.setString(3, gson.toJson(category.getServiceOperationsList()));
        preparedStatement.setString(4, gson.toJson(category.getTrainingOperationsMap()));
        Log.i("insertCategory query", preparedStatement.toString());
        LogDao.insertLog(preparedStatement.toString());
        boolean isExecute = preparedStatement.execute();
        category.setId(findCategoryByName(category.getName()).getId());
        preparedStatement.close();
        return isExecute;
    }

    public static Category findCategoryById(String idCategory) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + CategoryColumnsNamesDAO.CATEGORY.id + "=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, idCategory);
        ResultSet resultSet = preparedStatement.executeQuery();
        Log.i("findCategoryById query", preparedStatement.toString());
        while (resultSet.next()) {

            String name = resultSet.getString(CategoryColumnsNamesDAO.CATEGORY.categoryName);
            String operationsList = resultSet.getString(CategoryColumnsNamesDAO.CATEGORY.serviceOperationsList);
            String operatorTrainingList = resultSet.getString(CategoryColumnsNamesDAO.CATEGORY.operatorTrainingList);
            Gson gson = new Gson();
            Category category = new Category(idCategory, name, gson.fromJson(operationsList, ArrayList.class), gson.fromJson(operatorTrainingList, HashMap.class));
            preparedStatement.close();

            return category;
        }
        return null;
    }

    public static Category findCategoryByName(String name) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + CategoryColumnsNamesDAO.CATEGORY.categoryName + "=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        Log.i("getCategoryByName query", preparedStatement.toString());
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString(CategoryColumnsNamesDAO.CATEGORY.id);
            String nameResult = resultSet.getString(CategoryColumnsNamesDAO.CATEGORY.categoryName);
            String serviceOperationsString = resultSet.getString(CategoryColumnsNamesDAO.CATEGORY.serviceOperationsList);
            String operatorTrainingList = resultSet.getString(CategoryColumnsNamesDAO.CATEGORY.operatorTrainingList);
            Gson gson = new Gson();
            preparedStatement.close();
            return new Category(id,
                    nameResult,
                    gson.fromJson(serviceOperationsString, ArrayList.class),
                    gson.fromJson(operatorTrainingList, HashMap.class));
        }
        return null;
    }

    public static List<Map<String, String>> findAllCategoryForListView() throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "SELECT * FROM " + TABLE_NAME + ";";
        Log.i("query", "findAllCategoryForListView : " + query);
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery(query);
        List<Map<String, String>> categoriesData = new ArrayList<Map<String, String>>();
        while (resultSet.next()) {
            String id = resultSet.getString(CategoryColumnsNamesDAO.CATEGORY.id);
            String name = resultSet.getString(CategoryColumnsNamesDAO.CATEGORY.categoryName);
            Map<String, String> row = new HashMap<String, String>(2);
            row.put("First Line", name);
            row.put("Second Line", "id: " + id);
            categoriesData.add(row);
        }
        preparedStatement.close();
        return categoriesData;
    }

    public static boolean updateCategory(Category category) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        Gson gson = new Gson();
        String query = "UPDATE " + TABLE_NAME + " SET " +
                CategoryColumnsNamesDAO.CATEGORY.categoryName + "=?, " +
                CategoryColumnsNamesDAO.CATEGORY.serviceOperationsList + "=?, " +
                CategoryColumnsNamesDAO.CATEGORY.operatorTrainingList + "=? " +
                "WHERE " +
                CategoryColumnsNamesDAO.CATEGORY.id + "=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, category.getName());
        preparedStatement.setString(2, gson.toJson(category.getServiceOperationsList()));
        preparedStatement.setString(3, gson.toJson(category.getTrainingOperationsMap()));
        preparedStatement.setString(4, category.getId());
        Log.i("SQL query", preparedStatement.toString());
        LogDao.insertLog(preparedStatement.toString());
        boolean isExecute = preparedStatement.execute();
        preparedStatement.close();
        return isExecute;
    }

    public static String findIdCategoryByName(String name) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + CategoryColumnsNamesDAO.CATEGORY.categoryName + "=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        Log.i("query", "findIdCategoryByName : " + preparedStatement.toString());
        while (resultSet.next()) {
            String id = resultSet.getString(CategoryColumnsNamesDAO.CATEGORY.id);
            preparedStatement.close();
            return id;
        }
        return null;
    }

    public static boolean deleteCategoryByName(String name) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " +
                CategoryColumnsNamesDAO.CATEGORY.categoryName + "=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        Log.i("deleteCategoryByName", preparedStatement.toString());
        LogDao.insertLog(preparedStatement.toString()); ///Log to database
        boolean isExecute = preparedStatement.execute();
        preparedStatement.close();
        return isExecute;
    }

    public static ArrayList<String> findAllServiceOperationsForListView(Category category) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + CategoryColumnsNamesDAO.CATEGORY.id + "=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, category.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
//        Log.i("SQL query", "findAllServiceOperationsForListView : " + preparedStatement.toString());
        String serviceOperations = null;
        while (resultSet.next()) {
            serviceOperations = resultSet.getString(CategoryColumnsNamesDAO.CATEGORY.serviceOperationsList);
        }
        Gson gson = new Gson();
        ArrayList<String> listOperations = gson.fromJson(serviceOperations, ArrayList.class);
        preparedStatement.close();
        return listOperations;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }
}

