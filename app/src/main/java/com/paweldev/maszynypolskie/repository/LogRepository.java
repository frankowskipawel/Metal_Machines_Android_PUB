package com.paweldev.maszynypolskie.repository;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.paweldev.maszynypolskie.config.Config;
import com.paweldev.maszynypolskie.model.LogModel;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class LogRepository {


    public static boolean insertLog(String logString) {

        String url = Config.getApiHostname() + "/api/log/insert";
        try {
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("user", Config.getMyLogin()));
            params.add(new BasicNameValuePair("val", logString));
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

    public static List<Map<String, String>> findAllLogsForListView() {

        String url = Config.getApiHostname() + "/api/log/findAll";
        try {
            HttpPost httpPost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            httpPost.addHeader("email", Config.getMyLogin().trim());
            httpPost.addHeader("password", Config.getMyPassword());

            HttpResponse response = httpclient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);
                Log.i(TAG, data);
                List<LogModel> logs = Arrays.asList(new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(data, LogModel[].class));

                List<Map<String, String>> categoriesData = new ArrayList<>();
                for (LogModel logModel : logs) {
                    Map<String, String> row = new HashMap<>(2);
                    row.put("First Line", "#"+logModel.getId()+" "+logModel.getDate()+" - "+logModel.getUser());
                    row.put("Second Line", logModel.getValue());
                    categoriesData.add(row);
                }
                return categoriesData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
