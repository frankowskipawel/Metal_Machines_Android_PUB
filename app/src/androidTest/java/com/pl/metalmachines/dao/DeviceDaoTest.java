package com.pl.metalmachines.dao;

import com.pl.metalmachines.model.Category;
import com.pl.metalmachines.model.Customer;
import com.pl.metalmachines.model.Device;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class DeviceDaoTest {

    private static String LOGIN = "pawfra";
    private static String PASSWORD = "haslopawla";
    private static Category category;
    private static Customer customer;
    private static Device deviceExampleOne;
    private static Device deviceExampleTwo;

    @Before
    public void setUp() throws Exception {
        UtilsDao.getAutenthification(LOGIN, PASSWORD);
        UtilsDao.getConnection().setAutoCommit(false);
        DeviceDao.setIsTest(true);
        category = new Category(null, "NAZWA KATEGORI 123123", null, null);
        CategoryDao.insertCategory(category);
        customer = new Customer(null, "KONTRAHENT 123123", null, null, null, null, null, null, null, null);
        CustomerDao.insertCustomer(customer);
        deviceExampleOne = getDeviceExampleOne();
        deviceExampleTwo = getDeviceExampleTwo();
        DeviceDao.insertDevice(deviceExampleOne);
        DeviceDao.insertDevice(deviceExampleTwo);
    }

    private Device getDeviceExampleOne() {
        return new Device(null, "NAZWA MASZYNY 1",
                "SN-1",
                "100W",
                customer.getId(),
                category.getId(),
                "2020-01-01");
    }

    private Device getDeviceExampleTwo() {
        return new Device(null,
                "NAZWA MASZYNY 2",
                "SN-2",
                "200W",
                customer.getId(),
                category.getId(),
                "2020-02-02");
    }

    @Test
    public void schouldInsertDeviceTrue() throws SQLException, ClassNotFoundException {
        //given
        //when
        Device foundDeviceOne = DeviceDao.findDeviceById(deviceExampleOne.getId());
        Device foundDeviceTwo = DeviceDao.findDeviceById(deviceExampleTwo.getId());
        //then
        assertTrue(foundDeviceOne.equals(deviceExampleOne));
        assertTrue(foundDeviceTwo.equals(deviceExampleTwo));
    }

    @Test
    public void schouldInsertDeviceFalse() throws SQLException, ClassNotFoundException {
        //given
        //when
        Device foundDeviceOne = DeviceDao.findDeviceById(deviceExampleOne.getId());
        //then
        assertFalse(foundDeviceOne.equals(deviceExampleTwo));
    }

    @Test
    public void schouldUpdateDeviceTrue() throws SQLException, ClassNotFoundException {
        //given
        deviceExampleOne.setName("Nowa nazwa");
        deviceExampleOne.setTransferDate("2000-01-01");
        deviceExampleOne.setSerialNumber("9999");
        //when
        DeviceDao.updateDevice(deviceExampleOne);
        Device foundDevice = DeviceDao.findDeviceById(deviceExampleOne.getId());
        //then
        assertTrue(foundDevice.equals(deviceExampleOne));
    }

    @Test
    public void schouldUpdateDeviceFalse() throws SQLException, ClassNotFoundException {
        //given
        deviceExampleOne.setName("Nowa nazwa");
        deviceExampleOne.setTransferDate("2000-01-01");
        deviceExampleOne.setSerialNumber("9999");
        //when
        DeviceDao.updateDevice(deviceExampleOne);
        Device foundDevice = DeviceDao.findDeviceById(deviceExampleOne.getId());
        deviceExampleOne = getDeviceExampleOne();
        //then
        assertFalse(foundDevice.equals(deviceExampleOne));
    }

    @Test
    public void schouldDeleteDeviceTrue() throws SQLException, ClassNotFoundException {
        //given
        //when
        DeviceDao.deleteDevice(deviceExampleOne);
        //then
        Device foundDevice = DeviceDao.findDeviceById(deviceExampleOne.getId());
        assertTrue(foundDevice == null);
    }

    @Test
    public void schouldDeleteDeviceFalse() throws SQLException, ClassNotFoundException {
        //given
        //when
        DeviceDao.deleteDevice(deviceExampleOne);
        //then
        Device foundDevice = DeviceDao.findDeviceById(deviceExampleOne.getId());
        assertFalse(foundDevice != null);
    }

    @Test
    public void schouldFindAllDevicesForListViewSizeIsMoreThanTwo() throws SQLException, ClassNotFoundException {
        //given

        //when
        TreeSet<Device> foundSet = DeviceDao.findAllDevicesForListView();
        //then
        assertTrue(foundSet.size() >= 2);
    }

    @Test
    public void schouldFindAllDevicesForListViewTrue() throws SQLException, ClassNotFoundException {
        //given
        //when
        TreeSet<Device> foundSet = DeviceDao.findAllDevicesForListView();
        //then
        boolean isFound = false;
        for (Device device : foundSet) {
            if (device.getId().equals(deviceExampleOne.getId())) {
                isFound = true;
            }
        }
        assertTrue(isFound);
    }

    @Test
    public void schouldFindAllDevicesForListViewFalse() throws SQLException, ClassNotFoundException {
        //given
        //when
        DeviceDao.deleteDevice(deviceExampleOne);
        TreeSet<Device> foundSet = DeviceDao.findAllDevicesForListView();
        //then
        boolean isFound = false;
        for (Device device : foundSet) {
            if (device.getId().equals(deviceExampleOne.getId())) {
                isFound = true;
            }
        }
        assertFalse(isFound);
    }

    @After
    public void rollback() throws SQLException, ClassNotFoundException {
        UtilsDao.getConnection().rollback();
        UtilsDao.getConnection().setAutoCommit(true);
        DeviceDao.setIsTest(false);
    }
}