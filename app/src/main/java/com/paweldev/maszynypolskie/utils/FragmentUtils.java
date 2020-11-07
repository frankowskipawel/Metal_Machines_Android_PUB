package com.paweldev.maszynypolskie.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.paweldev.maszynypolskie.R;

public class FragmentUtils {

    public static void replaceFragment(Fragment newFragment, FragmentManager manager) {
        manager.beginTransaction().replace(R.id.nav_host_fragment, newFragment, newFragment.getTag()).addToBackStack("tag").commit(); //addToBackStack is back after back key pressed
    }

    public static void setSubtitleNavigationDrawer(Activity activity, String text){
        NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.textView_nav_header);
        navUsername.setText(text);
    }
}
