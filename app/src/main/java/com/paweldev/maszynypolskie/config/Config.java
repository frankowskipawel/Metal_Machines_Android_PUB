package com.paweldev.maszynypolskie.config;


import android.os.StrictMode;
import android.util.Log;

import com.paweldev.maszynypolskie.model.Role;
import com.paweldev.maszynypolskie.model.User;
import com.paweldev.maszynypolskie.repository.UserRepository;

import static android.content.ContentValues.TAG;

public class Config {
    // User Acount
    private static String USER = "";
    private static String MY_LOGIN;
    private static String MY_PASSWORD;
    private static Boolean IS_ADMIN = false;
    // API
    private static String API_HOSTNAME = "http://46.41.148.86:8000"; //demo


    public static boolean getAutenthification(String login, String password) {
        login = "demo@demo.pl"; // FOR DEMO ONLY
        password = "password";  // FOR DEMO ONLY
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        User user = UserRepository.findUserBy(login, password);
        if (user != null) {
            MY_LOGIN = login;
            MY_PASSWORD = password;
            USER = user.getFirstName()+" "+user.getLastName();
            for (Role role : user.getRoles()) {
                if (role.getRole().equals("ADMIN")) {
                    IS_ADMIN = true;
                } else {
                    IS_ADMIN = false;
                }
            }
            return true;
        }
        Log.i(TAG, "getAutenthification: IS ADMIN? " + IS_ADMIN);
        MY_LOGIN = "";
        MY_PASSWORD = "";
        USER = "";
        IS_ADMIN = false;
        return false;
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

    public static Boolean getIsAdmin() {
        return IS_ADMIN;
    }

    public static String getApiHostname() {
        return API_HOSTNAME;
    }

}
