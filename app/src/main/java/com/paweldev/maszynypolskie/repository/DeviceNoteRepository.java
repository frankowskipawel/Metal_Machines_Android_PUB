package com.paweldev.maszynypolskie.repository;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.paweldev.maszynypolskie.config.Config;
import com.paweldev.maszynypolskie.model.DeviceNote;

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

public class DeviceNoteRepository {

    private static final String DEVICES_NOTES = "devices_notes";

    //API
    public static boolean insertDeviceNote(DeviceNote deviceNote) {

        String url = Config.getApiHostname() + "/api/deviceNote/insert";
        try {
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("deviceId", deviceNote.getIdDevice()));
            params.add(new BasicNameValuePair("text", deviceNote.getText()));
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
    public static boolean updateDeviceNote(DeviceNote deviceNote) throws SQLException, ClassNotFoundException {

        String url = Config.getApiHostname() + "/api/deviceNote/update";
        try {
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("deviceId", deviceNote.getIdDevice()));
            params.add(new BasicNameValuePair("text", deviceNote.getText()));
            params.add(new BasicNameValuePair("id", deviceNote.getId()));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httppost.addHeader("email", Config.getMyLogin().trim());
            httppost.addHeader("password", Config.getMyPassword());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                LogRepository.insertLog("updateDeviceNote - "+deviceNote);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    //API
    public static boolean deleteDeviceNote(DeviceNote deviceNote) throws SQLException, ClassNotFoundException, IOException {

        String url = Config.getApiHostname() + "/api/deviceNote/delete";
        HttpPost httpPost = new HttpPost(url);
        HttpClient httpclient = new DefaultHttpClient();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("id", deviceNote.getId()));
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httpPost.addHeader("email", Config.getMyLogin().trim());
        httpPost.addHeader("password", Config.getMyPassword());
        HttpResponse response = httpclient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            LogRepository.insertLog("deleteDeviceNote - "+deviceNote);
            return true;
        }
        return false;

    }

    //API
    public static List<Map<String, String>> findAllNotesByIdDevice(String idDevice) throws IOException {

        String url = Config.getApiHostname() + "/api/deviceNote/findByIdDevice";
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
            Log.i(TAG, data);
            List<DeviceNote> devicesNoteFromJSON = Arrays.asList(new GsonBuilder().create().fromJson(data, DeviceNote[].class));

            List<Map<String, String>> deviceData = new ArrayList<Map<String, String>>();
            for (DeviceNote deviceNote : devicesNoteFromJSON) {

                Map<String, String> row = new HashMap<String, String>(2);
                row.put("First Line", deviceNote.getId() + "");
                row.put("Second Line", deviceNote.getText());
                deviceData.add(row);
            }
            return deviceData;
        }
        return null;
    }
}
