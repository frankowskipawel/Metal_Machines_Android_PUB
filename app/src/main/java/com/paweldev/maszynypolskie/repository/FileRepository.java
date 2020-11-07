package com.paweldev.maszynypolskie.repository;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paweldev.maszynypolskie.config.Config;
import com.paweldev.maszynypolskie.model.FileModel;
import com.paweldev.maszynypolskie.model.apiModel.FileModelAPI;
import com.paweldev.maszynypolskie.model.enums.FileType;

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
import java.util.List;

import static android.content.ContentValues.TAG;

public class FileRepository {


    //APi
    public static boolean insertNewFile(FileModel fileModel){

        String url = Config.getApiHostname() + "/api/file/insert";
        try {
            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("date", fileModel.getDate()));
            params.add(new BasicNameValuePair("description", fileModel.getDescription()));
            params.add(new BasicNameValuePair("filename", fileModel.getFilename()));
            params.add(new BasicNameValuePair("idDevice", fileModel.getIdDevice()));
            params.add(new BasicNameValuePair("idService", fileModel.getIdService()));
            params.add(new BasicNameValuePair("type", fileModel.getFileType().toString()));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httppost.addHeader("email", Config.getMyLogin().trim());
            httppost.addHeader("password", Config.getMyPassword());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String data = EntityUtils.toString(entity);
                Gson gson = new Gson();
                FileModelAPI fileFromJSON = gson.fromJson(data,FileModelAPI.class);
                fileModel.setId(fileFromJSON.getId());
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    //API
    public static boolean updateDeviceFile(FileModel fileModel) throws IOException {
        String url = Config.getApiHostname() + "/api/file/update";

            HttpPost httppost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id", fileModel.getId()));
            params.add(new BasicNameValuePair("date", fileModel.getDate()));
            params.add(new BasicNameValuePair("description", fileModel.getDescription()));
            params.add(new BasicNameValuePair("filename", fileModel.getFilename()));
            params.add(new BasicNameValuePair("idDevice", fileModel.getIdDevice()));
            params.add(new BasicNameValuePair("idService", fileModel.getIdService()));
            params.add(new BasicNameValuePair("type", fileModel.getFileType().toString()));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httppost.addHeader("email", Config.getMyLogin().trim());
            httppost.addHeader("password", Config.getMyPassword());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                LogRepository.insertLog("updateDeviceFile - "+fileModel);
                return true;
            }

        return false;
    }

    //API
    public static boolean deleteDeviceFile(FileModel fileModel) throws IOException, SQLException, ClassNotFoundException {

        String url = Config.getApiHostname() + "/api/file/delete";
        HttpPost httpPost = new HttpPost(url);
        HttpClient httpclient = new DefaultHttpClient();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("id", fileModel.getId()));
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httpPost.addHeader("email", Config.getMyLogin().trim());
        httpPost.addHeader("password", Config.getMyPassword());
        HttpResponse response = httpclient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            LogRepository.insertLog("deleteDeviceFile - "+fileModel);
            return true;
        }
        return false;
    }


    public static List<FileModel> findAllFilesByIdDevice(String idDevice) throws IOException {

        String url = Config.getApiHostname() + "/api/file/findByDevice";
        HttpPost httppost = new HttpPost(url);
        HttpClient httpclient = new DefaultHttpClient();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("idDevice", idDevice));
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httppost.addHeader("email", Config.getMyLogin().trim());
        httppost.addHeader("password", Config.getMyPassword());
        HttpResponse response = httpclient.execute(httppost);
        int status = response.getStatusLine().getStatusCode();
        List<FileModel> fileModels = new ArrayList<>();
        if (status == 200) {
            HttpEntity entity = response.getEntity();
            String data = EntityUtils.toString(entity);
            Log.i(TAG, data);
            List<FileModelAPI> filesFromJSON = Arrays.asList(new GsonBuilder().create().fromJson(data, FileModelAPI[].class));

            for (FileModelAPI fileModelAPI : filesFromJSON) {
                if (!fileModelAPI.getType().equals("PARAMETERS")){
                FileModel fileModel = new FileModel();
                fileModel.setId(fileModelAPI.getId());
                if (fileModelAPI.getDevice()!=null){
                    fileModel.setIdDevice(fileModelAPI.getDevice().getId());}
                if (fileModelAPI.getService()!=null){
                    fileModel.setIdService(fileModelAPI.getService().getId());}

                fileModel.setDate(fileModelAPI.getDate());
                fileModel.setDescription(fileModelAPI.getDescription());
                fileModel.setFilename(fileModelAPI.getFilename());
                fileModel.setFileType(FileType.valueOf(fileModelAPI.getType()));
                fileModels.add(fileModel);}
            }
        }
        return fileModels;
    }

    public static List<FileModel> findByDeviceAndType(String idDevice, String type) throws IOException {

        String url = Config.getApiHostname() + "/api/file/findByDeviceAndType";
        HttpPost httppost = new HttpPost(url);
        HttpClient httpclient = new DefaultHttpClient();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("idDevice", idDevice));
        params.add(new BasicNameValuePair("type", type));
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httppost.addHeader("email", Config.getMyLogin().trim());
        httppost.addHeader("password", Config.getMyPassword());
        HttpResponse response = httpclient.execute(httppost);
        int status = response.getStatusLine().getStatusCode();
        List<FileModel> fileModels = new ArrayList<>();
        if (status == 200) {
            HttpEntity entity = response.getEntity();
            String data = EntityUtils.toString(entity);
            Log.i(TAG, data);
            List<FileModelAPI> filesFromJSON = Arrays.asList(new GsonBuilder().create().fromJson(data, FileModelAPI[].class));

            for (FileModelAPI fileModelAPI : filesFromJSON) {
                FileModel fileModel = new FileModel();
                fileModel.setId(fileModelAPI.getId());
                if (fileModelAPI.getDevice()!=null){
                    fileModel.setIdDevice(fileModelAPI.getDevice().getId());}
                if (fileModelAPI.getService()!=null){
                    fileModel.setIdService(fileModelAPI.getService().getId());}
                fileModel.setDate(fileModelAPI.getDate());
                fileModel.setDescription(fileModelAPI.getDescription());
                fileModel.setFilename(fileModelAPI.getFilename());
                fileModel.setFileType(FileType.valueOf(fileModelAPI.getType()));
                fileModels.add(fileModel);
            }
        }
        return fileModels;
    }

    public static List<FileModel> findAllFilesByIdService(String idService) throws IOException {

        String url = Config.getApiHostname() + "/api/file/findByService";
        HttpPost httppost = new HttpPost(url);
        HttpClient httpclient = new DefaultHttpClient();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("idService", idService));
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httppost.addHeader("email", Config.getMyLogin().trim());
        httppost.addHeader("password", Config.getMyPassword());
        HttpResponse response = httpclient.execute(httppost);
        int status = response.getStatusLine().getStatusCode();
        if (status == 200) {
            HttpEntity entity = response.getEntity();
            String data = EntityUtils.toString(entity);
            Log.i(TAG, data);
            List<FileModelAPI> filesFromJSON = Arrays.asList(new GsonBuilder().create().fromJson(data, FileModelAPI[].class));
            List<FileModel> fileModels = new ArrayList<>();
            for (FileModelAPI fileModelAPI : filesFromJSON) {
                FileModel fileModel = new FileModel();
                fileModel.setId(fileModelAPI.getId());
                if (fileModelAPI.getDevice()!=null){
                    fileModel.setIdDevice(fileModelAPI.getDevice().getId());}
                if (fileModelAPI.getService()!=null){
                    fileModel.setIdService(fileModelAPI.getService().getId());}
                fileModel.setDate(fileModelAPI.getDate());
                if (fileModelAPI.getDescription() != null) {
                    fileModel.setDescription(fileModelAPI.getDescription());
                }
                fileModel.setFilename(fileModelAPI.getFilename());
                fileModel.setFileType(FileType.valueOf(fileModelAPI.getType()));
                fileModels.add(fileModel);
            }
            return fileModels;
        }
        return null;
    }
}
