package com.paweldev.maszynypolskie.repository;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paweldev.maszynypolskie.config.Config;
import com.paweldev.maszynypolskie.model.DeviceInspectionReport;
import com.paweldev.maszynypolskie.model.Service;
import com.paweldev.maszynypolskie.model.apiModel.ServiceAPI;
import com.paweldev.maszynypolskie.model.enums.ServiceType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ServiceRepository {

    //API
    public static String insertService(Service service, Boolean addNextNumber) throws SQLException, ClassNotFoundException {

        String url = Config.getApiHostname() + "/api/service/insert";
        try {
            Gson gson = new Gson();
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("addNextNumber", addNextNumber.toString()));
            params.add(new BasicNameValuePair("comment", service.getComments()));
            params.add(new BasicNameValuePair("customerId", service.getCustomer().getId()));
            params.add(new BasicNameValuePair("daysAtHotel", service.getDaysAtHotel()));
            params.add(new BasicNameValuePair("deviceId", service.getDevice().getId()));
            params.add(new BasicNameValuePair("driveDistance", service.getDriveDistance()));
            params.add(new BasicNameValuePair("endDate", service.getEndDate()));
            params.add(new BasicNameValuePair("endTime", service.getEndTime()));
            params.add(new BasicNameValuePair("gpsLocation", service.getGpsLocation()));
            params.add(new BasicNameValuePair("inspectionReport", gson.toJson(service.getDeviceInspectionReport())));
            params.add(new BasicNameValuePair("materialUsed", service.getMaterialsUsed()));
            params.add(new BasicNameValuePair("number", service.getNumber()));
            params.add(new BasicNameValuePair("paymentType", service.getPaymentType().toString()));
            params.add(new BasicNameValuePair("faultDescription", service.getFaultDescription()));
            params.add(new BasicNameValuePair("rangeOfWorks", service.getRangeOfWorks()));
            params.add(new BasicNameValuePair("serviceDate", service.getDate()));
            params.add(new BasicNameValuePair("startDate", service.getStartDate()));
            params.add(new BasicNameValuePair("startTime", service.getStartTime()));
            params.add(new BasicNameValuePair("state", service.getState()));
            params.add(new BasicNameValuePair("type", service.getType().toString()));
            params.add(new BasicNameValuePair("user", service.getUser()));
            params.add(new BasicNameValuePair("workingTime", service.getWorkingTime()));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httppost.addHeader("email", Config.getMyLogin().trim());
            httppost.addHeader("password", Config.getMyPassword());

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            String data = EntityUtils.toString(entity, "UTF-8");

            if (entity != null) {
                Log.i(TAG, data);
                ServiceAPI service1 = gson.fromJson(data, ServiceAPI.class);
                return service1.getId();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //API
    public static boolean updateService(Service service, Boolean addNextNumber) throws IOException {

        String url = Config.getApiHostname() + "/api/service/update";

            Gson gson = new Gson();
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("addNextNumber", addNextNumber.toString()));
            params.add(new BasicNameValuePair("id", service.getId()));
            params.add(new BasicNameValuePair("comment", service.getComments()));
            params.add(new BasicNameValuePair("customerId", service.getCustomer().getId()));
            params.add(new BasicNameValuePair("daysAtHotel", service.getDaysAtHotel()));
            params.add(new BasicNameValuePair("deviceId", service.getDevice().getId()));
            params.add(new BasicNameValuePair("driveDistance", service.getDriveDistance()));
            params.add(new BasicNameValuePair("endDate", service.getEndDate()));
            params.add(new BasicNameValuePair("endTime", service.getEndTime()));
            params.add(new BasicNameValuePair("gpsLocation", service.getGpsLocation()));
            params.add(new BasicNameValuePair("inspectionReport", gson.toJson(service.getDeviceInspectionReport())));
            params.add(new BasicNameValuePair("materialUsed", service.getMaterialsUsed()));
            params.add(new BasicNameValuePair("number", service.getNumber()));
            params.add(new BasicNameValuePair("paymentType", service.getPaymentType().toString()));
            params.add(new BasicNameValuePair("rangeOfWorks", service.getRangeOfWorks()));
            params.add(new BasicNameValuePair("serviceDate", service.getDate()));
            params.add(new BasicNameValuePair("faultDescription", service.getFaultDescription()));
            params.add(new BasicNameValuePair("startDate", service.getStartDate()));
            params.add(new BasicNameValuePair("startTime", service.getStartTime()));
            params.add(new BasicNameValuePair("state", service.getState()));
            params.add(new BasicNameValuePair("type", service.getType().toString()));
            params.add(new BasicNameValuePair("user", service.getUser().trim()));
            params.add(new BasicNameValuePair("workingTime", service.getWorkingTime()));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httppost.addHeader("email", Config.getMyLogin().trim());
            httppost.addHeader("password", Config.getMyPassword());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String data = EntityUtils.toString(entity);
                Log.i(TAG, data);
                LogRepository.insertLog("updateService - "+service);

                return true;
            }

        return false;
    }

    //API
    public static boolean deleteService(Service service) throws IOException {
        String url = Config.getApiHostname() + "/api/service/delete";
        HttpPost httppost = new HttpPost(url);
        HttpClient httpclient = new DefaultHttpClient();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("id", service.getId()));
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httppost.addHeader("email", Config.getMyLogin().trim());
        httppost.addHeader("password", Config.getMyPassword());
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            LogRepository.insertLog("deleteService - "+service);

            return true;
        }
        return false;
    }

    //API
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Map<String, String>> findAlldeviceServicesForListView(String idDevice) throws IOException {

        String url = Config.getApiHostname() + "/api/service/findByDevice";
        HttpPost httppost = new HttpPost(url);
        HttpClient httpclient = new DefaultHttpClient();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("id", idDevice));
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httppost.addHeader("email", Config.getMyLogin().trim());
        httppost.addHeader("password", Config.getMyPassword());
        HttpResponse response = httpclient.execute(httppost);
        int status = response.getStatusLine().getStatusCode();
        if (status == 200) {
            HttpEntity entity = response.getEntity();
            String data = EntityUtils.toString(entity);
            List<ServiceAPI> devicesFromJSON = Arrays.asList(new GsonBuilder().create().fromJson(data, ServiceAPI[].class));

            List<Map<String, String>> serviceData = new ArrayList<Map<String, String>>();
            for (ServiceAPI service : devicesFromJSON) {
                String number = "";
                if (Integer.parseInt(service.getNumber())>0){
                    number=service.getNumber()+"/"+service.getYear();
                }

                Map<String, String> row = new HashMap<String, String>(2);
                row.put("First Line", "#" + service.getId() + " (" + service.getDate() + ") " + service.getType());
                row.put("Second Line", service.getState() + " " + number + "\n" + service.getUser() + ", \n" + service.getPaymentType());
                serviceData.add(row);
            }
            return serviceData;
        }
        return null;
    }


    //API
    public static Service findServiceAfterId(String idService) {

        String url = Config.getApiHostname() + "/api/service/findById/";
        try {
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id", idService));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httppost.addHeader("email", Config.getMyLogin().trim());
            httppost.addHeader("password", Config.getMyPassword());
            HttpResponse response = httpclient.execute(httppost);
            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);
                Log.i(TAG, data);
                Gson gson = new Gson();
                ServiceAPI serviceApi = gson.fromJson(data, ServiceAPI.class);
                Service service = new Service(
                        serviceApi.getId(),
                        serviceApi.getNumber(),
                        serviceApi.getYear(),
                        serviceApi.getDate(),
                        serviceApi.getCustomer(),
                        serviceApi.getDevice(),
                        null,
                        serviceApi.getPaymentType(),
                        gson.fromJson(serviceApi.getDeviceInspectionReport(), DeviceInspectionReport.class),
                        serviceApi.getFaultDescription(),
                        serviceApi.getRangeOfWorks(),
                        serviceApi.getMaterialsUsed(),
                        serviceApi.getComments(),
                        serviceApi.getStartDate(),
                        serviceApi.getStartTime(),
                        serviceApi.getEndDate(),
                        serviceApi.getEndTime(),
                        serviceApi.getWorkingTime(),
                        serviceApi.getDriveDistance(),
                        serviceApi.getDaysAtHotel(),
                        serviceApi.getUser(),
                        serviceApi.getState(),
                        serviceApi.getGpsLocation()
                );
                List<ServiceType> types = Arrays.asList(new GsonBuilder().create().fromJson(serviceApi.getType(), ServiceType[].class));
                service.setType(types);
                return service;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
