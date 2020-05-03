package com.pl.metalmachines.dao;

import com.pl.metalmachines.model.Category;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CategoryDaoTest {
    private static String LOGIN = "pawfra";
    private static String PASSWORD = "haslopawla";
    private static Category categoryExampleOne;
    private static Category categoryExampleTwo;

    @Before
    public void setUp() throws IOException, SQLException, ClassNotFoundException {
        UtilsDao.getAutenthification(LOGIN, PASSWORD);
        UtilsDao.getConnection().setAutoCommit(false);
        categoryExampleOne = getCategoryExampleOne();
        categoryExampleTwo = getCategoryExampleTwo();
        CategoryDao.insertCategory(categoryExampleOne);
        CategoryDao.insertCategory(categoryExampleTwo);
    }

    public Category getCategoryExampleOne() {
        ArrayList<String> serviceOperationsList = new ArrayList<>();
        serviceOperationsList.add("Operacja ???A1");
        serviceOperationsList.add("Operacja ???A2");
        serviceOperationsList.add("Operacja ???A3");
        return new Category(null, "KATEGORIA TESTOWA XYZ", serviceOperationsList, null);
    }

    public Category getCategoryExampleTwo() {
        ArrayList<String> serviceOperationsList = new ArrayList<>();
        serviceOperationsList.add("Operacja ###1");
        serviceOperationsList.add("Operacja ###2");
        serviceOperationsList.add("Operacja ###3");
        serviceOperationsList.add("Operacja ###4");
        return new Category(null, "KATEGORIA TESTOWA ABC", serviceOperationsList, null);
    }

    @Test
    public void schouldInsertCategoryTrue() throws SQLException, ClassNotFoundException {
        //given
        //when
        //then
        Category categoryFound = CategoryDao.findCategoryByName("KATEGORIA TESTOWA XYZ");
        assertTrue(categoryExampleOne.equals(categoryFound));
    }

    @Test
    public void schouldInsertCategoryFalse() throws SQLException, ClassNotFoundException {
        //given
        //when
        //then
        Category categoryFound = CategoryDao.findCategoryByName("KATEGORIA TESTOWA ABC");
        assertFalse(categoryExampleOne.equals(categoryFound));
    }

    @Test
    public void schouldfindCategoryByNameTrue() throws SQLException, ClassNotFoundException {
        //given
        //when
        Category categoryFound = CategoryDao.findCategoryByName("KATEGORIA TESTOWA XYZ");
        //then
        assertTrue(categoryExampleOne.equals(categoryFound));
    }

    @Test
    public void schouldfindCategoryByNameFalse() throws SQLException, ClassNotFoundException {
        //given
        //when
        Category categoryFound = CategoryDao.findCategoryByName("NIEZNANA KATEGORIA XXX");
        //then
        assertFalse(categoryExampleOne.equals(categoryFound));
    }

    @Test
    public void schouldfindAllCategoryForListViewTrue() throws SQLException, ClassNotFoundException {
        //given
        boolean isFound = false;
        //when
        List<Map<String, String>> categoryList = CategoryDao.findAllCategoryForListView();
        for (Map<String, String> stringStringMap : categoryList) {
            if (stringStringMap.containsValue("KATEGORIA TESTOWA XYZ") && stringStringMap.containsKey("First Line")) {
                isFound = true;
            }
        }
        if (isFound) {
            isFound = false;
            for (Map<String, String> stringStringMap : categoryList) {
                if (stringStringMap.containsValue("KATEGORIA TESTOWA ABC")) {
                    isFound = true;
                }
            }
        }
        //then
        assertTrue(categoryList.size() >= 2);
        assertTrue(isFound);
    }

    @Test
    public void schouldfindAllCategoryForListViewFalse() throws SQLException, ClassNotFoundException {
        //given
        boolean isFound = false;
        //when
        List<Map<String, String>> categoryList = CategoryDao.findAllCategoryForListView();
        for (Map<String, String> stringStringMap : categoryList) {
            if (stringStringMap.containsValue("NOTHINGNOTHING123ABCXYZ") && stringStringMap.containsKey("First Line")) {
                isFound = true;
            }
        }
        if (isFound) {
            isFound = false;
            for (Map<String, String> stringStringMap : categoryList) {
                if (stringStringMap.containsValue("KATEGORIA TESTOWA ABC")) {
                    isFound = true;
                }
            }
        }
        //then
        assertFalse(isFound);
    }

    @Test
    public void schouldUpdateCategoryTrue() throws SQLException, ClassNotFoundException {
        //given

        categoryExampleTwo.setName("ZMIENIONA KATEGORIA #123#ABC###");
        //when
        CategoryDao.updateCategory(categoryExampleTwo);
        Category foundCategory = CategoryDao.findCategoryByName("ZMIENIONA KATEGORIA #123#ABC###");
        //then
        assertTrue(foundCategory.getName().equals("ZMIENIONA KATEGORIA #123#ABC###"));
    }

    @Test
    public void schouldUpdateCategoryFalse() throws SQLException, ClassNotFoundException {
        //given
        categoryExampleTwo.setName("ZMIENIONA KATEGORIA #123#ABC###");
        //when
        CategoryDao.updateCategory(categoryExampleTwo);
        Category foundCategory = CategoryDao.findCategoryByName("ZMIENIONA KATEGORIA #123#ABC###");
        //then
        assertFalse(foundCategory.getName().equals("KATEGORIA TESTOWA ABC"));
    }

    @Test
    public void schouldFindCategoryByIdTrue() throws SQLException, ClassNotFoundException {
        //given
        //when
        Category foundCategory = CategoryDao.findCategoryById(categoryExampleTwo.getId());
        //then
        assertTrue(categoryExampleTwo.equals(foundCategory));
    }

    @Test
    public void schouldFindCategoryByIdFalse() throws SQLException, ClassNotFoundException {
        //given
        //when
        Category foundCategory = CategoryDao.findCategoryById("-1");
        //then
        assertFalse(categoryExampleTwo.equals(foundCategory));
    }

    @Test
    public void schouldFindIdCategoryByNameTrue() throws SQLException, ClassNotFoundException {
        //given
        //when
        String foundId = CategoryDao.findIdCategoryByName("KATEGORIA TESTOWA ABC");
        //then
        assertTrue(categoryExampleTwo.getId().equals(foundId));
    }

    @Test
    public void schouldFindIdCategoryByNameFalse() throws SQLException, ClassNotFoundException {
        //given
        //when
        String foundId = CategoryDao.findIdCategoryByName("KATEGORIA TESTOWA ABC");
        //then
        assertFalse(categoryExampleOne.getId().equals(foundId));
    }

    @Test
    public void schouldDeleteCategoryByNameTrue() throws SQLException, ClassNotFoundException {
        //given
        //when
        CategoryDao.deleteCategoryByName("KATEGORIA TESTOWA XYZ");
        //then
        assertTrue(CategoryDao.findIdCategoryByName("KATEGORIA TESTOWA XYZ") == null);
    }

    @Test
    public void schouldDeleteCategoryByNameFalse() throws SQLException, ClassNotFoundException {
        //given
        //when
        CategoryDao.deleteCategoryByName("KATEGORIA TESTOWA ABC");
        //then
        assertFalse(CategoryDao.findIdCategoryByName("KATEGORIA TESTOWA XYZ") == null);
    }

    @Test
    public void schouldFindAllServiceOperationsForListViewTrue() throws SQLException, ClassNotFoundException {
        //given
        //when
        ArrayList<String> serviceOperationsList = CategoryDao.findAllServiceOperationsForListView(categoryExampleOne);
        //then
        assertTrue(serviceOperationsList.size() == 3);
        assertTrue(serviceOperationsList.get(0).equals("Operacja ???A1"));
        assertTrue(serviceOperationsList.get(1).equals("Operacja ???A2"));
        assertTrue(serviceOperationsList.get(2).equals("Operacja ???A3"));
    }

    @Test
    public void schouldFindAllServiceOperationsForListViewFalse() throws SQLException, ClassNotFoundException {
        //given
        //when
        ArrayList<String> serviceOperationsList = CategoryDao.findAllServiceOperationsForListView(categoryExampleOne);
        //then
        assertFalse(serviceOperationsList.size() == 0);
        assertFalse(serviceOperationsList.get(2).equals("Operacja ???A1"));
        assertFalse(serviceOperationsList.get(0).equals("Operacja ???A2"));
        assertFalse(serviceOperationsList.get(1).equals("Operacja ???A3"));
    }

    @After
    public void rollback() throws SQLException, ClassNotFoundException {
        UtilsDao.getConnection().rollback();
        UtilsDao.getConnection().setAutoCommit(true);
    }
}