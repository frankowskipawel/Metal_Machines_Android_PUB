package com.pl.metalmachines.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.pl.metalmachines.dao.UtilsDao;
import com.pl.metalmachines.ui.settings.SettingsFragment;

import java.io.IOException;

import static android.content.ContentValues.TAG;


public class ConnectionUtils {


    public static boolean checkLoginAndPassword(Context context, Activity activity, FragmentManager fragmentManager) throws IOException {

    String[] loginAndPassword = FileUtils.readFromFile(context).split(";");
        if (loginAndPassword.length == 2) {
        UtilsDao.getAutenthification(loginAndPassword[0], loginAndPassword[1]);
        if (UtilsDao.getUSER().equals("")) {FragmentUtils.setSubtitleNavigationDrawer(activity, "Jesteś niezalogowany");
            FragmentUtils.replaceFragment(new SettingsFragment(), fragmentManager);
            return false;
        }
        else{
            String accessLevel="";
            if (UtilsDao.getIsAdmin()==true){ accessLevel="(Admin)";}
            FragmentUtils.setSubtitleNavigationDrawer(activity, "Jesteś zalogowany jako:\n"+ UtilsDao.getUSER()+" "+accessLevel); return true;}
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
