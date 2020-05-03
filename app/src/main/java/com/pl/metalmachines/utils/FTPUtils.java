package com.pl.metalmachines.utils;

import android.os.Build;
import android.os.Environment;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.pl.metalmachines.dao.LogDao;
import com.pl.metalmachines.dao.UtilsDao;
import com.pl.metalmachines.model.FileModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import static android.content.ContentValues.TAG;

public class FTPUtils {

    public static FTPClient ftp = null;

    public static Boolean uploadFileToFTP(File inputFile, FileModel fileModel) throws SQLException, ClassNotFoundException, IOException {
        FTPClient client = new FTPClient();
        FileInputStream fis = null;
            client.connect(UtilsDao.getFtpHostname());
            client.login(UtilsDao.getFtpLogin(), UtilsDao.getFtpPassword());
            client.changeWorkingDirectory("/devices_files");
            client.setFileType(FTP.LOCAL_FILE_TYPE);
            Log.i(TAG, "upload file : "+ fileModel.getFilename());
            fis = new FileInputStream(inputFile.getAbsolutePath());
            Boolean result = client.storeFile(fileModel.getFilename(), fis);
            client.logout();
            Log.i(TAG, "addNewPDFtoFTP: "+result);
            LogDao.insertLog("Upload file to FTP: "+ fileModel.getFilename() +" ("+result+")"); ///Log to database
            return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static File downloadFileFromFTP(FileModel fileModel) throws Exception {
        FTPUtils ftpUtils =
                new FTPUtils(UtilsDao.getFtpHostname(), UtilsDao.getFtpLogin(), UtilsDao.getFtpPassword());
        String directory_path = Environment.getExternalStorageDirectory().toString() + "/";
        String remoteFilePath = "/devices_files/"+fileModel.getFilename();
        String localFilePath = directory_path + fileModel.getFilename();
        ftpUtils.downloadFile(remoteFilePath, localFilePath);
        System.out.println("FTP File downloaded successfully");
        ftpUtils.disconnect();
        return new File(directory_path + fileModel.getFilename());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void deleteFileFromFTP(FileModel fileModel) throws Exception {
        FTPUtils ftpUtils =
                new FTPUtils(UtilsDao.getFtpHostname(), UtilsDao.getFtpLogin(), UtilsDao.getFtpPassword());
        String directory_path = Environment.getExternalStorageDirectory().toString() + "/";
        String remoteFilePath = "/devices_files/"+fileModel.getFilename();
        ftpUtils.deleteFile(remoteFilePath);
        ftpUtils.disconnect();
    }


    public FTPUtils(String host, String user, String pwd) throws Exception {
        ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        int reply;
        ftp.connect(host);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new Exception("Exception in connecting to FTP Server");
        }
        ftp.login(user, pwd);
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void downloadFile(String remoteFilePath, String localFilePath) {
        try (FileOutputStream fos = new FileOutputStream(localFilePath)) {
            this.ftp.retrieveFile(remoteFilePath, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void deleteFile(String remoteFilePath) throws IOException {
            this.ftp.deleteFile(remoteFilePath);
    }

    public void disconnect() {
        if (this.ftp.isConnected()) {
            try {
                this.ftp.logout();
                this.ftp.disconnect();
            } catch (IOException f) {
                // do nothing as file is already downloaded from FTP server
            }
        }
    }
}
