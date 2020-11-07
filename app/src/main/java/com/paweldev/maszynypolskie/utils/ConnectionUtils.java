package com.paweldev.maszynypolskie.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.paweldev.maszynypolskie.config.Config;
import com.paweldev.maszynypolskie.ui.settings.SettingsFragment;

import java.io.IOException;

import static android.content.ContentValues.TAG;


public class ConnectionUtils {


    public static boolean checkLoginAndPassword(Context context, Activity activity, FragmentManager fragmentManager) throws IOException {

    String[] loginAndPassword = FileUtils.readConfigFromFile(context).split(";");
        if (loginAndPassword.length == 2) {



        Config.getAutenthification(loginAndPassword[0], loginAndPassword[1]);
        if (Config.getUSER().equals("")) {FragmentUtils.setSubtitleNavigationDrawer(activity, "Jesteś niezalogowany");
            FragmentUtils.replaceFragment(new SettingsFragment(), fragmentManager);
            return false;
        }
        else{
            String accessLevel="";
            if (Config.getIsAdmin()==true){ accessLevel="(Admin)";}
            FragmentUtils.setSubtitleNavigationDrawer(activity, "Jesteś zalogowany jako:\n"+ Config.getUSER()+" "+accessLevel);
            return true;
        }
    } else { FragmentUtils.replaceFragment(new SettingsFragment(), fragmentManager);
        return false;}
    }



    public static boolean isInternetAvailable(Context context)
    {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null)
        {
            Log.d(TAG,"no internet connection");
            return false;
        }
        else
        {
            if(info.isConnected())
            {
                Log.d(TAG," internet connection available...");
                return true;
            }
            else
            {
                Log.d(TAG," internet connection");
                return true;
            }

        }
    }
}
