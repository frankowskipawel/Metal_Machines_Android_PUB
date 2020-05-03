package com.pl.metalmachines.dao;

import android.util.Log;

import com.pl.metalmachines.model.Customer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class CustomerDaoTest {

    private static String LOGIN = "pawfra";
    private static String PASSWORD = "haslopawla";
    private static Customer customerExampleOne;
    private static Customer customerExampleTwo;

    @Before
    public void setUp() throws IOException, SQLException, ClassNotFoundException {
        UtilsDao.getAutenthification(LOGIN, PASSWORD);
        UtilsDao.getConnection().setAutoCommit(false);
        customerExampleOne = getCustomerExampleOne();
        customerExampleTwo = getCustomerExampleTwo();
        CustomerDao.insertCustomer(customerExampleOne);
        CustomerDao.insertCustomer(customerExampleTwo);
    }

    private Customer getCustomerExampleOne() {
        return new Customer(null, "JAN KOWALSKI#@!123",
                "Jan Kowalski",
                "Szeroka 22/12",
                "00-100",
                "Warszawa",
                "1234567890",
                "1234567",
                "0100100100",
                "jan.kowslaki@gmail.com");
    }

    private Customer getCustomerExampleTwo() {
        return new Customer(null, "ZBIGNIEW NOWAK#@!123",
                "Zbigniew Nowak",
                "Prosta 11/12",
                "30-100",
                "Gdańsk",
                "2233445566",
                "4323456",
                "0900900999",
                "nowak.zbigniew@wp.pl");
    }

    @Test
    public void schouldInsertCustomerTrue() throws SQLException, ClassNotFoundException {
        //given
        //when
        //then
        Customer foundCustomer = CustomerDao.findCustomerByShortName("JAN KOWALSKI#@!123");
        assertTrue(customerExampleOne.equals(foundCustomer));
    }

    @Test
    public void schouldInsertCustomerFalse() throws SQLException, ClassNotFoundException {
        //given
        //when
        //then
        Customer foundCustomer = CustomerDao.findCustomerByShortName("JAN KOWALSKI#@!123xxxxxxxx");
        assertFalse(customerExampleOne.equals(foundCustomer));
    }

    @Test
    public void schouldUpdateCustomerTrue() throws SQLException, ClassNotFoundException {
        //given
        customerExampleOne.setShortName("KOTKOWSKI PIOTR");
        customerExampleOne.setFullName("Piotr Kotkowski");
        customerExampleOne.setStreetAddress("Jagodowa 1/1");
        //when
        CustomerDao.updateCustomer(customerExampleOne);
        Customer foundCustomer = CustomerDao.findCustomerById(customerExampleOne.getId());
        //then
        Log.e("getCustomerById", foundCustomer.toString());
        assertTrue(foundCustomer.getShortName().equals("KOTKOWSKI PIOTR"));
        assertTrue(foundCustomer.getFullName().equals("Piotr Kotkowski"));
        assertTrue(foundCustomer.getStreetAddress().equals("Jagodowa 1/1"));
    }

    @Test
    public void schouldUpdateCustomerFalse() throws SQLException, ClassNotFoundException {
        //given
        customerExampleOne.setShortName("KOTKOWSKI PIOTR");
        customerExampleOne.setFullName("Piotr Kotkowski");
        customerExampleOne.setStreetAddress("Jagodowa 1/1");
        //when
        CustomerDao.updateCustomer(customerExampleOne);
        Customer foundCustomer = CustomerDao.findCustomerById(customerExampleOne.getId());
        //then
        Log.e("getCustomerById", foundCustomer.toString());
        assertFalse(foundCustomer.getShortName().equals("JAN KOWALSKI#@!123"));
        assertFalse(foundCustomer.getFullName().equals("Jan Kowalski"));
        assertFalse(foundCustomer.getStreetAddress().equals("Szeroka 22/12"));
    }

    @Test
    public void schouldFindCustomerByIdTrue() throws SQLException, ClassNotFoundException {
        //given
        //when
        Customer foundCustomer = CustomerDao.findCustomerById(customerExampleOne.getId());
        //then
        assertTrue(customerExampleOne.equals(foundCustomer));
    }

    @Test
    public void schouldFindCustomerByIdFalse() throws SQLException, ClassNotFoundException {
        //given
        //when
        Customer foundCustomer = CustomerDao.findCustomerById(customerExampleOne.getId());
        //then
        assertFalse(customerExampleTwo.equals(foundCustomer));
    }

    @Test
    public void schouldFindIdCustomerByShortNameTrue() throws SQLException, ClassNotFoundException {
        //given
        //when
        String foundIdCustomer = CustomerDao.findIdCustomerByShortName("JAN KOWALSKI#@!123");
        //then
        assertTrue(customerExampleOne.getId().equals(foundIdCustomer));
    }

    @Test
    public void schouldFindIdCustomerByShortNameFalse() throws SQLException, ClassNotFoundException {
        //given
        //when
        String foundIdCustomer = CustomerDao.findIdCustomerByShortName("ZBIGNIEW NOWAK#@!123");
        //then
        assertFalse(customerExampleOne.getId().equals(foundIdCustomer));
    }

    @Test
    public void schouldFindCustomerByShortNameTrue() throws SQLException, ClassNotFoundException {
        //given
        //when
        Customer foundCustomer = CustomerDao.findCustomerByShortName("JAN KOWALSKI#@!123");
        //then
        assertTrue(customerExampleOne.equals(foundCustomer));
    }

    @Test
    public void schouldFindCustomerByShortNameFalse() throws SQLException, ClassNotFoundException {
        //given
        //when
        Customer foundCustomer = CustomerDao.findCustomerByShortName("ZBIGNIEW NOWAK#@!123");
        //then
        assertFalse(customerExampleOne.equals(foundCustomer));
    }

    @Test
    public void schouldFindAllCustomersForListViewTrue() throws SQLException, ClassNotFoundException {
        //given
        boolean isFound = false;
        //when
        List<Map<String, String>> customersList = CustomerDao.findAllCustomersForListView();
        //then
        for (Map<String, String> stringStringMap : customersList) {
            if (stringStringMap.containsValue("#" + customerExampleOne.getId() + " " + customerExampleOne.getShortName()) && stringStringMap.containsKey("First Line")) {
                isFound = true;
            }
        }
        if (isFound) {
            isFound = false;
            for (Map<String, String> stringStringMap : customersList) {
                if (stringStringMap.containsValue("#" + customerExampleTwo.getId() + " " + customerExampleTwo.getShortName())) {
                    isFound = true;
                }
            }
        }
        //then
        Assert.assertTrue(customersList.size() >= 2);
        Assert.assertTrue(isFound);
    }

    @Test
    public void schouldFindAllCustomersForListViewFalse() throws SQLException, ClassNotFoundException {
        //given
        boolean isFound = false;
        //when
        List<Map<String, String>> customersList = CustomerDao.findAllCustomersForListView();
        //then
        for (Map<String, String> stringStringMap : customersList) {
            if (stringStringMap.containsValue("xxxxxxxxxxx") && stringStringMap.containsKey("First Line")) {
                isFound = true;
            }
        }
        if (isFound) {
            isFound = false;
            for (Map<String, String> stringStringMap : customersList) {
                if (stringStringMap.containsValue("yyyyyy" + customerExampleTwo.getShortName())) {
                    isFound = true;
                }
            }
        }
        //then
        Assert.assertFalse(isFound);
    }

    @Test
    public void schouldDeleteCustomerTrue() throws SQLException, ClassNotFoundException {
        //given
        //when
        CustomerDao.deleteCustomer(customerExampleOne);
        //then
        assertTrue(CustomerDao.findCustomerById(customerExampleOne.getId())==null);
    }

    @Test
    public void schouldDeleteCustomerFalse() throws SQLException, ClassNotFoundException {
        //given
        //when
        CustomerDao.deleteCustomer(customerExampleOne);
        //then
        assertFalse(CustomerDao.findCustomerById(customerExampleOne.getId())!=null);
    }

    @After
    public void rollback() throws SQLException, ClassNotFoundException {
        UtilsDao.getConnection().rollback();
        UtilsDao.getConnection().setAutoCommit(true);
    }
}