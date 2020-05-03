package com.pl.metalmachines.dao;


import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import static android.content.ContentValues.TAG;

public class UtilsDao {
    private static double MAX_DURATION_OF_CONNECTION_IN_SECONDS=60;
    private static long timeStamp;
    // User Acount
    private static String USER = "";
    private static String MY_LOGIN;
    private static String MY_PASSWORD;
    private static Boolean IS_ADMIN = false;
    // MySQL Connection
    private static Connection connection;
    private static String HOSTNAME;
    private static String PORT;
    private static String DATABASENAME;
    private static String LOGIN;
    private static String PASSWORD;
    // FTP Connection
    private static String FTP_HOSTNAME;
    private static String FTP_LOGIN;
    private static String FTP_PASSWORD;

    public static boolean getAutenthification(String login, String password) throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        MY_LOGIN = login;
        MY_PASSWORD = password;
        String url = "http://www.wegiel-torun.pl/metal_machines_authentification.php?login=" + login + "&password=" + password;
        URL website = new URL(url);
        URLConnection connection = null;
        connection = website.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String[] responseTab = response.toString().split(";");
        if (responseTab.length == 10) {
            USER = responseTab[0];
            if (responseTab[1].equals("0")) {
                IS_ADMIN = false;
            } else if (responseTab[1].equals("1")) {
                IS_ADMIN = true;
            }
            HOSTNAME = responseTab[2];
            PORT = responseTab[3];
            DATABASENAME = responseTab[4];
            LOGIN = responseTab[5];
            PASSWORD = responseTab[6];
            FTP_HOSTNAME = responseTab[7];
            FTP_LOGIN = responseTab[8];
            FTP_PASSWORD = responseTab[9];
            Log.i(TAG, "getAutenthification: IS ADMIN? " + IS_ADMIN);
            return true;
        }
        USER = "";
        HOSTNAME = "";
        PORT = "";
        DATABASENAME = "";
        LOGIN = "";
        PASSWORD = "";
        return false;
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        if (connection == null || connection.isClosed() || timeStamp < System.nanoTime() - MAX_DURATION_OF_CONNECTION_IN_SECONDS *1000000000) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Log.i(TAG, "getConnection: "+">>> Connecting to database <<<");
            Class.forName("com.mysql.jdbc.Driver");
            timeStamp = System.nanoTime();
            connection = DriverManager.getConnection("jdbc:mysql://" + HOSTNAME + ":" + PORT + "/" + DATABASENAME + "?useSSL=false", LOGIN, PASSWORD);
        }
        return connection;
    }

    public static String getUSER() {
        return USER;
    }

    public static String getMyLogin() {
        return MY_LOGIN;
    }

    public static String getMyPassword() {
        return MY_PASSWORD;
    }

    public static String getFtpHostname() {
        return FTP_HOSTNAME;
    }

    public static String getFtpLogin() {
        return FTP_LOGIN;
    }

    public static String getFtpPassword() {
        return FTP_PASSWORD;
    }

    public static Boolean getIsAdmin() {
        return IS_ADMIN;
    }


}
