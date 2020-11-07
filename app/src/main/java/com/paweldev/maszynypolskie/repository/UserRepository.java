package com.paweldev.maszynypolskie.repository;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paweldev.maszynypolskie.config.Config;
import com.paweldev.maszynypolskie.model.Role;
import com.paweldev.maszynypolskie.model.User;
import com.paweldev.maszynypolskie.model.apiModel.UserAPI;

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

public class UserRepository {


    public static User findUserBy(String email, String password) {

        String url = Config.getApiHostname() + "/api/user/auth";
        try {
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            httppost.addHeader("email", email);
            httppost.addHeader("password", password);
            HttpResponse response = httpclient.execute(httppost);
            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);
                Log.i(TAG, data);
                Gson gson = new Gson();
                User user = gson.fromJson(data, User.class);
                return user;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<Map<String, String>> findAllCustomersForListView() {

        String url = Config.getApiHostname() + "/api/user/findAll";
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
                List<User> users = Arrays.asList(new GsonBuilder().create().fromJson(data, User[].class));
                List<Map<String, String>> usersData = new ArrayList<>();
                for (User user : users) {
                    Map<String, String> row = new HashMap<>(2);
                    String roles="";
                    for (Role role : user.getRoles()) {
                        roles += role.getRole()+" ";
                    }
                    String active="";
                    if (user.isActive()==true){
                        active="AKTYWNY";
                    } else {
                        active="NIEAKTYWNY";
                    }
                    row.put("First Line", "#"+user.getId()+" "+user.getFirstName()+" "+user.getLastName()+" ("+user.getUserName()+")");
                    row.put("Second Line", user.getEmail()+"\n"+roles+"\n"+active);
                    usersData.add(row);
                }
                return usersData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean insert(UserAPI user) {

        String url = Config.getApiHostname() + "/api/user/insert";
        try {
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            Gson gson = new Gson();

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("active", gson.toJson(user.isActive())));
            params.add(new BasicNameValuePair("userName", user.getUserName()));
            params.add(new BasicNameValuePair("firstName", user.getFirstName()));
            params.add(new BasicNameValuePair("lastName", user.getLastName()));
            params.add(new BasicNameValuePair("role", user.getRole()));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httppost.addHeader("email", Config.getMyLogin().trim());
            httppost.addHeader("password", Config.getMyPassword());
            httppost.addHeader("emailUser", user.getEmail());
            httppost.addHeader("passwordUser", user.getPassword());
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

    public static boolean update(UserAPI user) {

        String url = Config.getApiHostname() + "/api/user/update";
        try {
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            Gson gson = new Gson();

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id", user.getId()));
            params.add(new BasicNameValuePair("active", gson.toJson(user.isActive())));
            params.add(new BasicNameValuePair("userName", user.getUserName()));
            params.add(new BasicNameValuePair("firstName", user.getFirstName()));
            params.add(new BasicNameValuePair("lastName", user.getLastName()));
            params.add(new BasicNameValuePair("role", user.getRole()));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httppost.addHeader("email", Config.getMyLogin().trim());
            httppost.addHeader("password", Config.getMyPassword());
            httppost.addHeader("emailUser", user.getEmail());
            httppost.addHeader("passwordUser", user.getPassword());
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

    public static User findById(String idUser) {

        String url = Config.getApiHostname() + "/api/user/findById";
        try {
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id", idUser));
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
                User user = gson.fromJson(data, User.class);

                return user;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean delete(User user) throws IOException {

        String url = Config.getApiHostname() + "/api/user/delete";
        HttpPost httpPost = new HttpPost(url);
        HttpClient httpclient = new DefaultHttpClient();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("id", user.getId()+""));
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httpPost.addHeader("email", Config.getMyLogin().trim());
        httpPost.addHeader("password", Config.getMyPassword());
        HttpResponse response = httpclient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            return true;
        }
        return false;
    }
}
