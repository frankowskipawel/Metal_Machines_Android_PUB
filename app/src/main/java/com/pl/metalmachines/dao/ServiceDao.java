package com.pl.metalmachines.dao;

import android.location.Location;
import android.util.Log;

import com.google.gson.Gson;
import com.pl.metalmachines.dao.enumDao.columnsNames.ServiceColumnsNamesDAO;
import com.pl.metalmachines.model.DeviceInspectionReport;
import com.pl.metalmachines.model.enums.PaymentType;
import com.pl.metalmachines.model.Service;
import com.pl.metalmachines.model.enums.ServiceType;
import com.pl.metalmachines.utils.GPSUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ServiceDao {

    private static final String TABLE_NAME = "services";

    public static String[] insertService(Service service, Boolean addNextNumber) throws SQLException, ClassNotFoundException {
        Connection con = UtilsDao.getConnection();
        con.setAutoCommit(false);
        String query1 = "SELECT " + ServiceColumnsNamesDAO.SERVICE.id + ", " + ServiceColumnsNamesDAO.SERVICE.number +
                " FROM " + TABLE_NAME +
                " WHERE (" + ServiceColumnsNamesDAO.SERVICE.number + " IS NOT NULL AND " +
                "LENGTH(" + ServiceColumnsNamesDAO.SERVICE.number + ") > 4 and substring(" +
                ServiceColumnsNamesDAO.SERVICE.number + ",length(" + ServiceColumnsNamesDAO.SERVICE.number +
                ")-3,length(" + ServiceColumnsNamesDAO.SERVICE.number + ")) in ('" + service.getDate().substring(0, 4) +
                "'))  ORDER BY " + ServiceColumnsNamesDAO.SERVICE.id + " desc limit 1;";
        Log.i(TAG, "insertService: " + query1);
        PreparedStatement preparedStatement1 = con.prepareStatement(query1);
        ResultSet resultSet3 = preparedStatement1.executeQuery(query1);
        String lastNumber = null;
        while (resultSet3.next()) {
            lastNumber = resultSet3.getString(ServiceColumnsNamesDAO.SERVICE.number);
        }
        if (lastNumber != null) {
            String[] lastNumberSplit = lastNumber.split("/");
            String[] dateSplit = service.getDate().split("-");
            String currentYear = dateSplit[0];
            if (lastNumberSplit[1].equals(currentYear)) {
                String currentNumber = (Integer.parseInt(lastNumberSplit[0]) + 1) + "/" + currentYear;
                service.setNumber(currentNumber);
            } else {
                service.setNumber("1/" + currentYear);
            }
        } else {
            String[] dateSplit = service.getDate().split("-");
            String currentYear = dateSplit[0];
            String currentNumber = "1/" + currentYear;
            service.setNumber(currentNumber);
        }
        String number;
        if (addNextNumber) {
            number = "'" + service.getNumber() + "'";
        } else {
            number = "null";
            service.setNumber(null);
        }
        String gpsLocation = "null";
        if (GPSUtils.location != null) {
            gpsLocation = GPSUtils.location.getLatitude() + ";" + GPSUtils.location.getLongitude();
        }
        Gson gson = new Gson();
        String query2 = "INSERT INTO " + TABLE_NAME +
                " VALUES (null, " +
                number + ", '" +
                service.getDate() + "', '" +
                service.getIdCustomer() + "', '" +
                service.getIdDevice() + "', '" +
                service.getServiceType().toString() + "', '" +
                service.getPaymentType() + "', '" +
                gson.toJson(service.getDeviceInspectionReport()) + "', '" +
                service.getFaultDescription() + "', '" +
                service.getRangeOfWorks() + "', '" +
                service.getMaterialsUsed() + "', '" +
                service.getComments() + "', '" +
                service.getStartDate() + "', '" +
                service.getStartTime() + "', '" +
                service.getEndDate() + "', '" +
                service.getEndTime() + "', '" +
                service.getWorkingTime() + "', '" +
                service.getDriveDistance() + "', '" +
                service.getDaysAtHotel() + "', '" +
                service.getUser() + "', '" +
                service.getState() + "', '" +
                gpsLocation
                + "');";
        String query3 = "SELECT max(" + ServiceColumnsNamesDAO.SERVICE.id + ") FROM " + TABLE_NAME + ";";

        Log.i(TAG, "addNewServiceToDatabase: " + query2);
        Log.i(TAG, "addNewServiceToDatabase: " + query3);

        PreparedStatement preparedStatement2 = con.prepareStatement(query2);
        boolean isExecute1 = preparedStatement2.execute();
        PreparedStatement preparedStatement3 = con.prepareStatement(query3);
        ResultSet resultSet = preparedStatement3.executeQuery(query3);
        int id = 0;
        while (resultSet.next()) {
            id = resultSet.getInt("max(" + ServiceColumnsNamesDAO.SERVICE.id + ")");
        }
        con.commit();
        con.setAutoCommit(true);
        preparedStatement2.close();
        preparedStatement3.close();
        LogDao.insertLog(preparedStatement2.toString());
        String[] idAndNumber = new String[2];
        idAndNumber[0] = id + "";
        return idAndNumber;
    }

    public static boolean updateService(Service service, Boolean addNextNumber) throws SQLException, ClassNotFoundException {
        Connection con = UtilsDao.getConnection();
        con.setAutoCommit(false);
        String query1 = "SELECT " + ServiceColumnsNamesDAO.SERVICE.id + ", " +
                ServiceColumnsNamesDAO.SERVICE.number +
                ", cast(substring(" + ServiceColumnsNamesDAO.SERVICE.number + ",1,length(" +
                ServiceColumnsNamesDAO.SERVICE.number + ")-5) as UNSIGNED) number_int FROM " + TABLE_NAME +
                " WHERE (" + ServiceColumnsNamesDAO.SERVICE.number + " IS NOT NULL AND LENGTH(" +
                ServiceColumnsNamesDAO.SERVICE.number + ") > 4 and substring(" + ServiceColumnsNamesDAO.SERVICE.number +
                ",length(" + ServiceColumnsNamesDAO.SERVICE.number + ")-3,length(" +
                ServiceColumnsNamesDAO.SERVICE.number + ")) in ('" + service.getDate().substring(0, 4) +
                "'))  ORDER BY number_int desc limit 1;";
        PreparedStatement preparedStatement1 = con.prepareStatement(query1);
        ResultSet resultSet3 = preparedStatement1.executeQuery(query1);
        String lastNumber = null;
        while (resultSet3.next()) {
            lastNumber = resultSet3.getString(ServiceColumnsNamesDAO.SERVICE.number);
        }
        if (lastNumber != null && (service.getNumber() == null || service.getNumber().length() <= 4)) {
            String[] lastNumberSplit = lastNumber.split("/");
            String[] dateSplit = service.getDate().split("-");
            String currentYear = dateSplit[0];
            if (lastNumberSplit[1].equals(currentYear)) {
                String currentNumber = (Integer.parseInt(lastNumberSplit[0]) + 1) + "/" + currentYear;
                service.setNumber(currentNumber);
            } else {
                service.setNumber("1/" + currentYear);
            }
        } else if (service.getNumber() == null || service.getNumber().length() < 4) {
            String[] dateSplit = service.getDate().split("-");
            String currentYear = dateSplit[0];
            String currentNumber = "1/" + currentYear;
            service.setNumber(currentNumber);
        }
        String number;
        if (addNextNumber || service.getNumber().length() > 4) {
            number = "'" + service.getNumber() + "'";
        } else {
            number = "null";
        }
        Log.i(TAG, "updateServiceInDatabase: " + service.toString());
        String gpsLocation = "null";
        if (GPSUtils.location != null) {
            gpsLocation = GPSUtils.location.getLatitude() + ";" + GPSUtils.location.getLongitude();
        }
        Gson gson = new Gson();
        String query2 = "UPDATE " + TABLE_NAME +
                " SET " +
                ServiceColumnsNamesDAO.SERVICE.number + "=" + number + ", " +
                ServiceColumnsNamesDAO.SERVICE.date + "='" + service.getDate() + "', " +
                ServiceColumnsNamesDAO.SERVICE.idCustomer + "='" + service.getIdCustomer() + "', " +
                ServiceColumnsNamesDAO.SERVICE.idDevice + "='" + service.getIdDevice() + "', " +
                ServiceColumnsNamesDAO.SERVICE.serviceType + "='" + service.getServiceType().toString() + "', " +
                ServiceColumnsNamesDAO.SERVICE.paymentType + "='" + service.getPaymentType() + "', " +
                ServiceColumnsNamesDAO.SERVICE.inspectionReport + "='" + gson.toJson(service.getDeviceInspectionReport()) + "', " +
                ServiceColumnsNamesDAO.SERVICE.faultDescription + "='" + service.getFaultDescription() + "', " +
                ServiceColumnsNamesDAO.SERVICE.rangeOfWorks + "='" + service.getRangeOfWorks() + "', " +
                ServiceColumnsNamesDAO.SERVICE.materialsUsed + "='" + service.getMaterialsUsed() + "', " +
                ServiceColumnsNamesDAO.SERVICE.comments + "='" + service.getComments() + "', " +
                ServiceColumnsNamesDAO.SERVICE.startDate + "='" + service.getStartDate() + "', " +
                ServiceColumnsNamesDAO.SERVICE.startTime + "='" + service.getStartTime() + "', " +
                ServiceColumnsNamesDAO.SERVICE.endDate + "='" + service.getEndDate() + "', " +
                ServiceColumnsNamesDAO.SERVICE.endTime + "='" + service.getEndTime() + "', " +
                ServiceColumnsNamesDAO.SERVICE.workingTime + "='" + service.getWorkingTime() + "', " +
                ServiceColumnsNamesDAO.SERVICE.driveDistance + "='" + service.getDriveDistance() + "', " +
                ServiceColumnsNamesDAO.SERVICE.daysAtHotel + "='" + service.getDaysAtHotel() + "', " +
                ServiceColumnsNamesDAO.SERVICE.user + "='" + service.getUser() + "', " +
                ServiceColumnsNamesDAO.SERVICE.state + "='" + service.getState() + "', " +
                ServiceColumnsNamesDAO.SERVICE.gpsLocation + "='" + gpsLocation
                + "' WHERE " + ServiceColumnsNamesDAO.SERVICE.id + "=" + service.getId() + ";";
        Log.i("SQL query", query2);
        PreparedStatement preparedStatement2 = con.prepareStatement(query2);
        preparedStatement2.execute();
        con.commit();
        con.setAutoCommit(true);
        LogDao.insertLog(preparedStatement2.toString());
        preparedStatement2.close();
        return true;
    }

    public static List<Map<String, String>> findAlldeviceServicesForListView(String idDevice) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ServiceColumnsNamesDAO.SERVICE.idDevice + "=" + idDevice + " ORDER BY " + ServiceColumnsNamesDAO.SERVICE.id + " DESC;";
        Log.i("SQL query", query);
        Statement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery(query);
        List<Map<String, String>> categoriesData = new ArrayList<Map<String, String>>();
        while (resultSet.next()) {
            String id = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.id);
            String number = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.number);
            if (number != null) {
                number = "(" + number + ")";
            } else {
                number = "";
            }
            String date = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.date);
            String type = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.serviceType);
            String payment = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.paymentType);
            String user = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.user);
            String state = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.state);
            Map<String, String> row = new HashMap<String, String>(2);
            row.put("First Line", "#" + id + " (" + date + ") " + type);
            row.put("Second Line", state + " " + number + "\n" + user + ", \n" + payment);
            categoriesData.add(row);
        }
        statement.close();
        return categoriesData;
    }

    public static Service findServiceAfterId(String idService) throws SQLException, ClassNotFoundException {
        Connection connection = UtilsDao.getConnection();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ServiceColumnsNamesDAO.SERVICE.id + "=" + idService + ";";
        Log.i("SQL query", query);
        PreparedStatement prest = connection.prepareStatement(query);
        ResultSet resultSet = prest.executeQuery(query);
        Service service = null;
        while (resultSet.next()) {
            String id = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.id);
            String number = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.number);
            String date = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.date);
            String idCustomer = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.idCustomer);
            String idDevice = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.idDevice);
            String serviceType = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.serviceType);
            String paymentType = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.paymentType);
            String inspectionReport = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.inspectionReport);
            String faultDescription = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.faultDescription);
            String rangeOfWorks = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.rangeOfWorks);
            String materialUsed = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.materialsUsed);
            String comments = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.comments);
            String startDate = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.startDate);
            String startTime = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.startTime);
            String endDate = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.endDate);
            String endTime = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.endTime);
            String workingTime = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.workingTime);
            String driveDistance = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.driveDistance);
            String daysAtHotel = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.daysAtHotel);
            String user = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.user);
            String state = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.state);
            String[] serviceTypeTab = serviceType.substring(1, serviceType.length() - 1).split(",");
            List<ServiceType> serviceTypeList = new ArrayList();
            for (int i = 0; i < serviceTypeTab.length; i++) {
                serviceTypeList.add(ServiceType.valueOf(serviceTypeTab[i].trim()));
            }
            Gson gson = new Gson();
            service = new Service(
                    id,
                    number,
                    date,
                    idCustomer,
                    idDevice,
                    serviceTypeList,
                    PaymentType.valueOf(paymentType),
                    gson.fromJson(inspectionReport, DeviceInspectionReport.class),
                    faultDescription,
                    rangeOfWorks,
                    materialUsed,
                    comments,
                    startDate,
                    startTime,
                    endDate,
                    endTime,
                    workingTime,
                    driveDistance,
                    daysAtHotel,
                    user,
                    state
            );
        }
        prest.close();
        return service;
    }

    public static Location findLocationGPSofService(String idService) throws SQLException, ClassNotFoundException {
        Connection con = UtilsDao.getConnection();
        String query = "SELECT " + ServiceColumnsNamesDAO.SERVICE.gpsLocation + " FROM " + TABLE_NAME + " WHERE " + ServiceColumnsNamesDAO.SERVICE.id + "=" + idService + ";";
        Log.i("SQL query", query);
        PreparedStatement preparedStatement = con.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery(query);
        String gpsLocation = null;
        Location location = null;
        while (resultSet.next()) {
            gpsLocation = resultSet.getString(ServiceColumnsNamesDAO.SERVICE.gpsLocation);
        }
        if (gpsLocation != null) {
            String[] gpsLocationSplit = gpsLocation.split(";");
            location = new Location("");
            location.setLatitude(Double.parseDouble(gpsLocationSplit[0]));
            location.setLongitude(Double.parseDouble(gpsLocationSplit[1]));
        }
        preparedStatement.close();
        return location;
    }
}
