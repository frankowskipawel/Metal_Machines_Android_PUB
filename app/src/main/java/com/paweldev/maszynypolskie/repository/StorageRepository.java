package com.paweldev.maszynypolskie.repository;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;

import androidx.annotation.RequiresApi;

import com.paweldev.maszynypolskie.config.Config;
import com.paweldev.maszynypolskie.model.FileModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;


public class StorageRepository {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static File downloadFile(FileModel fileModel, Context context) throws IOException {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        String url = Config.getApiHostname() + "/api/storage/downloadFile/";

        HttpPost httppost = new HttpPost(url);
        HttpClient httpclient = new DefaultHttpClient();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("fileName", fileModel.getFilename()));

        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httppost.addHeader("email", Config.getMyLogin());
        httppost.addHeader("password", Config.getMyPassword());
        HttpResponse response = httpclient.execute(httppost);
        int status = response.getStatusLine().getStatusCode();
        if (status == 200) {
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            String directory_path = Environment.getExternalStorageDirectory().toString() + "/"; //// new - with package path

            File file = new File(directory_path + fileModel.getFilename());
            if (entity != null) {
                try (FileOutputStream outstream = new FileOutputStream(file)) {
                    entity.writeTo(outstream);
                }
            }
            return file;
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean uploadFile(File file, FileModel fileModel) throws IOException {
        StrictMode.VmPolicy.Builder builderPolicy = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builderPolicy.build());
        String url = Config.getApiHostname() + "/api/storage/uploadFile/";

        String directory_path = Environment.getExternalStorageDirectory().toString() + "/"; //// new - with package path
        File fileAfterRename = new File(directory_path + fileModel.getFilename());
        if (!fileAfterRename.exists()) {
            fileAfterRename.createNewFile();
        }
        FileChannel origin = null;
        FileChannel destination = null;
        try {
            origin = new FileInputStream(file).getChannel();
            destination = new FileOutputStream(fileAfterRename).getChannel();

            long count = 0;
            long size = origin.size();
            while ((count += destination.transferFrom(origin, count, size - count)) < size) ;
        } finally {
            if (origin != null) {
                origin.close();
            }
            if (destination != null) {
                destination.close();
            }
        }

        HttpClient client = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("email", Config.getMyLogin());
        httppost.addHeader("password", Config.getMyPassword());
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("file", new FileBody(fileAfterRename));
        httppost.setEntity(builder.build());
        HttpResponse response = client.execute(httppost);

        return true;
    }

    public static void deleteFile(FileModel fileModel) throws IOException {
        StrictMode.VmPolicy.Builder builderPolicy = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builderPolicy.build());
        String url = Config.getApiHostname() + "/api/storage/deleteFile/";

        HttpPost httppost = new HttpPost(url);
        HttpClient httpclient = new DefaultHttpClient();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("fileName", fileModel.getFilename()));

        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httppost.addHeader("email", Config.getMyLogin());
        httppost.addHeader("password", Config.getMyPassword());
        HttpResponse response = httpclient.execute(httppost);
    }
}
