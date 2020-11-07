package com.paweldev.maszynypolskie.repository;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paweldev.maszynypolskie.config.Config;
import com.paweldev.maszynypolskie.model.Customer;

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


public class CustomerRepository {

    private static final String TABLE_NAME = "customers";

    // API auth
    public static boolean insertCustomer(Customer customer) {

        String url = Config.getApiHostname() + "/api/customer/insert";
        try {
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("shortName", customer.getShortName()));
            params.add(new BasicNameValuePair("city", customer.getCity()));
            params.add(new BasicNameValuePair("emailCustomer", customer.getEmail()));
            params.add(new BasicNameValuePair("fullName", customer.getFullName()));
            params.add(new BasicNameValuePair("nip", customer.getNip()));
            params.add(new BasicNameValuePair("phone", customer.getPhoneNumber()));
            params.add(new BasicNameValuePair("regon", customer.getRegon()));
            params.add(new BasicNameValuePair("street", customer.getStreetAddress()));
            params.add(new BasicNameValuePair("zipCode", customer.getZipCode()));
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

    // API auth
    public static boolean updateCustomer(Customer customer) throws SQLException, ClassNotFoundException {

        String url = Config.getApiHostname() + "/api/customer/update";
        try {
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            Gson gson = new Gson();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id", customer.getId()));
            params.add(new BasicNameValuePair("shortName", customer.getShortName()));
            params.add(new BasicNameValuePair("city", customer.getCity()));
            params.add(new BasicNameValuePair("emailCustomer", customer.getEmail()));
            params.add(new BasicNameValuePair("fullName", customer.getFullName()));
            params.add(new BasicNameValuePair("nip", customer.getNip()));
            params.add(new BasicNameValuePair("phone", customer.getPhoneNumber()));
            params.add(new BasicNameValuePair("regon", customer.getRegon()));
            params.add(new BasicNameValuePair("zipCode", customer.getZipCode()));
            params.add(new BasicNameValuePair("street", customer.getStreetAddress()));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httppost.addHeader("email", Config.getMyLogin().trim());
            httppost.addHeader("password", Config.getMyPassword());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                LogRepository.insertLog("updateCustomer - "+customer);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // API auth
    public static Customer findCustomerById(String idCustomer) {

        String url = Config.getApiHostname() + "/api/customer/findById/" + idCustomer;
        try {
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
                Gson gson = new Gson();
                Customer customer = gson.fromJson(data, Customer.class);
                return customer;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // API
    public static String findIdCustomerByShortName(String shortName)  {

        return findCustomerByShortName(shortName).getId();
    }

    // API auth
    public static Customer findCustomerByShortName(String shortName) {

        String url = Config.getApiHostname() + "/api/customer/findByShortName";
        try {
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            Gson gson = new Gson();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("shortName", shortName));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httppost.addHeader("email", Config.getMyLogin().trim());
            httppost.addHeader("password", Config.getMyPassword());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String data = EntityUtils.toString(entity);
                Customer customer = gson.fromJson(data, Customer.class);
                return customer;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // API auth
    public static List<Map<String, String>> findAllCustomersForListView() {

        String url = Config.getApiHostname() + "/api/customer/findAll";
        try {
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
                List<Customer> customers = Arrays.asList(new GsonBuilder().create().fromJson(data, Customer[].class));
                List<Map<String, String>> customersData = new ArrayList<>();

                for (Customer customer : customers) {
                    String dateRow = "";
                    if (!customer.getFullName().equals("")) {
                        dateRow += customer.getFullName() + "\n";
                    }
                    if (!customer.getStreetAddress().equals("")) {
                        dateRow += customer.getStreetAddress() + ", ";
                    }
                    if (!customer.getZipCode().equals("")) {
                        dateRow += customer.getZipCode() + " ";
                    }
                    if (!customer.getCity().equals("")) {
                        dateRow += customer.getCity() + "\n";
                    }
                    if (!customer.getNip().equals("")) {
                        dateRow += "NIP : " + customer.getNip() + "\n";
                    }
                    if (!customer.getRegon().equals("")) {
                        dateRow += "REGON : " + customer.getRegon() + "\n";
                    }
                    if (!customer.getPhoneNumber().equals("")) {
                        dateRow += "tel. " + customer.getPhoneNumber() + "\n";
                    }
                    if (!customer.getEmail().equals("")) {
                        dateRow += "email. " + customer.getEmail() + "\n";
                    }
                    Map<String, String> row = new HashMap<String, String>(2);
                    row.put("First Line", "#" + customer.getId() + " " + customer.getShortName());
                    row.put("Second Line", dateRow);
                    customersData.add(row);
                }
                return customersData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // API auth
    public static boolean deleteCustomer(Customer customer) throws IOException, SQLException, ClassNotFoundException {

        String url = Config.getApiHostname() + "/api/customer/delete";
        HttpPost httpPost = new HttpPost(url);
        HttpClient httpclient = new DefaultHttpClient();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("id", customer.getId()));
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httpPost.addHeader("email", Config.getMyLogin().trim());
        httpPost.addHeader("password", Config.getMyPassword());
        HttpResponse response = httpclient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            LogRepository.insertLog("updateCustomer - "+customer);
            return true;
        }
        return false;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }
}
