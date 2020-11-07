package com.paweldev.maszynypolskie.repository;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paweldev.maszynypolskie.config.Config;
import com.paweldev.maszynypolskie.model.Device;
import com.paweldev.maszynypolskie.model.Part;
import com.paweldev.maszynypolskie.model.apiModel.DeviceAPI;

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
import java.util.TreeSet;

import static android.content.ContentValues.TAG;

public class DeviceRepository {

    //API
    public static boolean insertDevice(Device device) {

        String url = Config.getApiHostname() + "/api/device/insert";
        try {
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("categoryId", device.getCategoryId()));
            params.add(new BasicNameValuePair("customerId", device.getCustomerId()));
            params.add(new BasicNameValuePair("name", device.getName()));
            params.add(new BasicNameValuePair("serialNumber", device.getSerialNumber()));
            params.add(new BasicNameValuePair("sourcePower", device.getSourcePower()));
            params.add(new BasicNameValuePair("transferDate", device.getTransferDate()));
            params.add(new BasicNameValuePair("streetAddress", device.getStreetAddress()));
            params.add(new BasicNameValuePair("zipCode", device.getZipCode()));
            params.add(new BasicNameValuePair("city", device.getCity()));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httppost.addHeader("email", Config.getMyLogin().trim());
            httppost.addHeader("password", Config.getMyPassword());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //API
    public static boolean updateDevice(Device device) throws SQLException, ClassNotFoundException {

        String url = Config.getApiHostname() + "/api/device/update";
        try {
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id", device.getId()));
            params.add(new BasicNameValuePair("categoryId", device.getCategoryId()));
            params.add(new BasicNameValuePair("customerId", device.getCustomerId()));
            params.add(new BasicNameValuePair("name", device.getName()));
            params.add(new BasicNameValuePair("serialNumber", device.getSerialNumber()));
            params.add(new BasicNameValuePair("sourcePower", device.getSourcePower()));
            params.add(new BasicNameValuePair("transferDate", device.getTransferDate()));
            params.add(new BasicNameValuePair("gpsLocation", device.getGpsLocation()));
            params.add(new BasicNameValuePair("streetAddress", device.getStreetAddress()));
            params.add(new BasicNameValuePair("zipCode", device.getZipCode()));
            params.add(new BasicNameValuePair("city", device.getCity()));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httppost.addHeader("email", Config.getMyLogin().trim());
            httppost.addHeader("password", Config.getMyPassword());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                LogRepository.insertLog("updateDevice - "+device);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    //API
    public static boolean deleteDevice(Device device) throws SQLException, ClassNotFoundException, IOException {

        String url = Config.getApiHostname() + "/api/device/delete";
        HttpPost httpPost = new HttpPost(url);
        HttpClient httpclient = new DefaultHttpClient();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("id", device.getId()));
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httpPost.addHeader("email", Config.getMyLogin().trim());
        httpPost.addHeader("password", Config.getMyPassword());
        HttpResponse response = httpclient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            LogRepository.insertLog("deleteDevice - "+device);

            return true;
        }
        return false;
    }

    //API auth
    public static TreeSet<Device> findAllDevicesForListView() throws IOException {

        String url = Config.getApiHostname() + "/api/device/findAll";
        HttpPost httppost = new HttpPost(url);
        HttpClient httpclient = new DefaultHttpClient();
        httppost.addHeader("email", Config.getMyLogin().trim());
        httppost.addHeader("password", Config.getMyPassword());
        HttpResponse response = httpclient.execute(httppost);
        int status = response.getStatusLine().getStatusCode();
        if (status == 200) {
            HttpEntity entity = response.getEntity();
            String data = EntityUtils.toString(entity);
            Log.i(TAG, data);
            TreeSet<Device> devices = new TreeSet<>();
            List<DeviceAPI> devicesFromJSON = Arrays.asList(new GsonBuilder().create().fromJson(data, DeviceAPI[].class));
            for (DeviceAPI deviceAPI : devicesFromJSON) {
                Device device = new Device(deviceAPI.getId(),
                        deviceAPI.getName(),
                        deviceAPI.getSerialNumber(),
                        deviceAPI.getSourcePower(),
                        deviceAPI.getCustomer().getId(),
                        deviceAPI.getCategory().getId(),
                        deviceAPI.getTransferDate(),
                        deviceAPI.getStreetAddress(),
                        deviceAPI.getZipCode(),
                        deviceAPI.getCity(),
                        deviceAPI.getParts()
                        );
                device.setCategoryName(deviceAPI.getCategory().getName());
                device.setCustomerName(deviceAPI.getCustomer().getShortName());
                devices.add(device);
            }
            return devices;
        }
        return null;
    }


    //API auth
    public static Device findDeviceById(String inputId) {

        String url = Config.getApiHostname() + "/api/device/findById/" + inputId;
        try {
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id", inputId));
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
                DeviceAPI deviceAPI = gson.fromJson(data, DeviceAPI.class);
                Device device = new Device(deviceAPI.getId(),
                        deviceAPI.getName(),
                        deviceAPI.getSerialNumber(),
                        deviceAPI.getSourcePower(),
                        deviceAPI.getCustomer().getId(),
                        deviceAPI.getCategory().getId(),
                        deviceAPI.getTransferDate(),
                        deviceAPI.getGpsLocation(),
                        deviceAPI.getStreetAddress(),
                        deviceAPI.getZipCode(),
                        deviceAPI.getCity(),
                deviceAPI.getParts());
                device.setCategoryName(deviceAPI.getCategory().getName());
                device.setCustomerName(deviceAPI.getCustomer().getShortName());

                return device;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //API
    public static List<Map<String, String>> findByCustomerForListView(String customerId) throws IOException {


        String url = Config.getApiHostname() + "/api/device/findByCustomer";
        HttpPost httppost = new HttpPost(url);
        HttpClient httpclient = new DefaultHttpClient();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("id", customerId));
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httppost.addHeader("email", Config.getMyLogin().trim());
        httppost.addHeader("password", Config.getMyPassword());
        HttpResponse response = httpclient.execute(httppost);
        int status = response.getStatusLine().getStatusCode();
        if (status == 200) {
            HttpEntity entity = response.getEntity();
            String data = EntityUtils.toString(entity);
            Log.i(TAG, data);
            List<DeviceAPI> devicesFromJSON = Arrays.asList(new GsonBuilder().create().fromJson(data, DeviceAPI[].class));

            List<Map<String, String>> deviceData = new ArrayList<Map<String, String>>();
            for (DeviceAPI deviceAPI : devicesFromJSON) {

                String content = "";
                String id = deviceAPI.getId();
                String name = deviceAPI.getName();
                String serialNumber = deviceAPI.getSerialNumber();
                content += "Nr. ser. : " + serialNumber + "\n";
                String surcePower = deviceAPI.getSourcePower();
                content += "Moc źródła : " + surcePower + "\n";
                String customerShortName = deviceAPI.getCustomer().getShortName();
                content += "Kontrahent : " + customerShortName + "\n";
                String categoryName = deviceAPI.getCategory().getName();
                content += "Kategoria : " + categoryName + "\n";
                String transferDate = deviceAPI.getTransferDate();
                content += "Data odbioru : " + transferDate;
                Map<String, String> row = new HashMap<String, String>(2);
                row.put("First Line", "#" + id + " " + name);
                row.put("Second Line", content);
                deviceData.add(row);
            }
            return deviceData;
        }
        return null;
    }

    //API
    public static List<Map<String, String>> findByCategoryForListview(String categoryId) throws IOException {
        String url = Config.getApiHostname() + "/api/device/findByCategory";
        HttpPost httppost = new HttpPost(url);
        HttpClient httpclient = new DefaultHttpClient();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("id", categoryId));
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httppost.addHeader("email", Config.getMyLogin().trim());
        httppost.addHeader("password", Config.getMyPassword());
        HttpResponse response = httpclient.execute(httppost);
        int status = response.getStatusLine().getStatusCode();
        if (status == 200) {
            HttpEntity entity = response.getEntity();
            String data = EntityUtils.toString(entity);
            Log.i(TAG, data);
            List<DeviceAPI> devicesFromJSON = Arrays.asList(new GsonBuilder().create().fromJson(data, DeviceAPI[].class));

            List<Map<String, String>> deviceData = new ArrayList<Map<String, String>>();
            for (DeviceAPI deviceAPI : devicesFromJSON) {

                String content = "";
                String id = deviceAPI.getId();
                String name = deviceAPI.getName();
                String serialNumber = deviceAPI.getSerialNumber();
                content += "Nr. ser. : " + serialNumber + "\n";
                String surcePower = deviceAPI.getSourcePower();
                content += "Moc źródła : " + surcePower + "\n";
                String customerShortName = deviceAPI.getCustomer().getShortName();
                content += "Kontrahent : " + customerShortName + "\n";
                String categoryName = deviceAPI.getCategory().getName();
                content += "Kategoria : " + categoryName + "\n";
                String transferDate = deviceAPI.getTransferDate();
                content += "Data odbioru : " + transferDate;
                Map<String, String> row = new HashMap<String, String>(2);
                row.put("First Line", "#" + id + " " + name);
                row.put("Second Line", content);
                deviceData.add(row);
            }
            return deviceData;
        }
        return null;
    }

    public static boolean insertPart(Part part, String idDevice) throws IOException {
        String url = Config.getApiHostname() + "/api/device/insertPart";
        try {
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("idDevice", idDevice));
            params.add(new BasicNameValuePair("name", part.getName()));
            params.add(new BasicNameValuePair("serialNumber", part.getSerialNumber()));
            params.add(new BasicNameValuePair("producer", part.getProducer()));
            params.add(new BasicNameValuePair("decsription", part.getDecsription()));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httppost.addHeader("email", Config.getMyLogin().trim());
            httppost.addHeader("password", Config.getMyPassword());
            HttpResponse response = httpclient.execute(httppost);

            if (response.getStatusLine().getStatusCode() == 200) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updatePart(Part part) throws IOException {
        String url = Config.getApiHostname() + "/api/device/updatePart";
        try {
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("idPart", part.getId()+""));
            params.add(new BasicNameValuePair("name", part.getName()));
            params.add(new BasicNameValuePair("serialNumber", part.getSerialNumber()));
            params.add(new BasicNameValuePair("producer", part.getProducer()));
            params.add(new BasicNameValuePair("description", part.getDecsription()));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httppost.addHeader("email", Config.getMyLogin().trim());
            httppost.addHeader("password", Config.getMyPassword());
            HttpResponse response = httpclient.execute(httppost);

            if (response.getStatusLine().getStatusCode() == 200) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deletePart(String idDevice, String idPart) throws IOException {
        String url = Config.getApiHostname() + "/api/device/deletePart";
        try {
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("idDevice", idDevice));
            params.add(new BasicNameValuePair("idPart", idPart));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httppost.addHeader("email", Config.getMyLogin().trim());
            httppost.addHeader("password", Config.getMyPassword());
            HttpResponse response = httpclient.execute(httppost);

            if (response.getStatusLine().getStatusCode() == 200) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
