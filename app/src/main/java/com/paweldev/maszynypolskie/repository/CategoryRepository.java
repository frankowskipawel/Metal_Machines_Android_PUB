package com.paweldev.maszynypolskie.repository;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paweldev.maszynypolskie.config.Config;
import com.paweldev.maszynypolskie.model.Category;
import com.paweldev.maszynypolskie.model.apiModel.CategoryAPI;

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

public class CategoryRepository {

    private static final String TABLE_NAME = "categories";

    // API auth
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean insertCategory(Category category) throws SQLException, ClassNotFoundException {

        String url = Config.getApiHostname() + "/api/category/insert";
        try {
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            Gson gson = new Gson();

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("name", category.getName()));
            params.add(new BasicNameValuePair("serviceReviewOperationsList", gson.toJson(category.getServiceReviewOperationsList())));
            params.add(new BasicNameValuePair("operatorTrainingList", gson.toJson(category.getOperatorTrainingList2())));
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
    public static Category findCategoryById(String idCategory) {

        String url = Config.getApiHostname() + "/api/category/findById/" + idCategory;
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
                CategoryAPI categoryAPI = gson.fromJson(data, CategoryAPI.class);

                Category category = new Category(categoryAPI.getId(),
                        categoryAPI.getName(),
                        gson.fromJson(categoryAPI.getServiceReviewOperationsList(), ArrayList.class),
                        gson.fromJson(categoryAPI.getOperatorTrainingList(), HashMap.class));
                return category;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


    // API auth
    public static Category findCategoryByName(String name) {

        String url = Config.getApiHostname() + "/api/category/findByName";
        try {
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            Gson gson = new Gson();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("name", name.trim()));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httppost.addHeader("email", Config.getMyLogin().trim());
            httppost.addHeader("password", Config.getMyPassword());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String data = EntityUtils.toString(entity);
                CategoryAPI categoryAPI = gson.fromJson(data, CategoryAPI.class);
                Category category = new Category(categoryAPI.getId(),
                        categoryAPI.getName(),
                        gson.fromJson(categoryAPI.getServiceReviewOperationsList(), ArrayList.class),
                        gson.fromJson(categoryAPI.getOperatorTrainingList(), HashMap.class));
                return category;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //API auth
    public static List<Map<String, String>> findAllCategoryForListView() {

        String url = Config.getApiHostname() + "/api/category/findAll";
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
                List<CategoryAPI> categories_temp = Arrays.asList(new GsonBuilder().create().fromJson(data, CategoryAPI[].class));
                List<Category> categories = new ArrayList<>();
                for (CategoryAPI categoryAPI : categories_temp) {
                    Category category = new Category(categoryAPI.getId(), categoryAPI.getName(), null, null);
                    categories.add(category);
                }
                List<Map<String, String>> categoriesData = new ArrayList<>();
                for (Category category : categories) {
                    Map<String, String> row = new HashMap<>(2);
                    row.put("First Line", category.getName());
                    row.put("Second Line", "id: " + category.getId());
                    categoriesData.add(row);
                }
                return categoriesData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // API auth
    public static boolean updateCategory(Category category) throws SQLException, ClassNotFoundException {

        String url = Config.getApiHostname() + "/api/category/update";
        try {
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            Gson gson = new Gson();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id", category.getId()));
            params.add(new BasicNameValuePair("name", category.getName()));
            params.add(new BasicNameValuePair("serviceReviewOperationsList", gson.toJson(category.getServiceReviewOperationsList())));
            params.add(new BasicNameValuePair("operatorTrainingList", gson.toJson(category.getOperatorTrainingList2())));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httppost.addHeader("email", Config.getMyLogin().trim());
            httppost.addHeader("password", Config.getMyPassword());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                LogRepository.insertLog("updateCategory - "+category);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // API auth
    public static boolean deleteCategoryByName(String name) throws IOException, SQLException, ClassNotFoundException {

        String url = Config.getApiHostname() + "/api/category/deleteByName";
        HttpPost httppost = new HttpPost(url);
        HttpClient httpclient = new DefaultHttpClient();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("name", name));
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httppost.addHeader("email", Config.getMyLogin().trim());
        httppost.addHeader("password", Config.getMyPassword());
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            LogRepository.insertLog("deleteCategoryByName - "+name);
            return true;
        }
        return false;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }
}

