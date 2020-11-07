package com.paweldev.maszynypolskie.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.paweldev.maszynypolskie.R;
import com.paweldev.maszynypolskie.config.Config;
import com.paweldev.maszynypolskie.ui.categories.CategoriesListFragment;
import com.paweldev.maszynypolskie.ui.customers.CustomersListFragment;
import com.paweldev.maszynypolskie.ui.devices.DevicesListFragment;
import com.paweldev.maszynypolskie.ui.info.MessageFragment;
import com.paweldev.maszynypolskie.ui.logs.LogsFragment;
import com.paweldev.maszynypolskie.ui.settings.SettingsFragment;
import com.paweldev.maszynypolskie.ui.users.UsersListFragment;
import com.paweldev.maszynypolskie.utils.ConnectionUtils;
import com.paweldev.maszynypolskie.utils.FileUtils;
import com.paweldev.maszynypolskie.utils.FragmentUtils;
import com.paweldev.maszynypolskie.utils.GPSUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class HomeFragment extends Fragment {


    private ProgressBar progressBar;
    private Context context;
    private Activity activity;
    private FragmentManager fragmentManager;
    private Button logs;
    private Button users;
    private Button deviceButton;
    private Button customerButton;
    private Button categoryButton;
    private Button userButton;
    private Button logsButton;
    private String[] loginAndPassword;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

//        StorageRepository.downloadFile("test1.pdf");
//        File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + Config.getPackagePath()+"test1.pdf");

//        try {
//            StorageRepository.uploadFile(file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        context = getContext();
        activity = getActivity();
        fragmentManager = getFragmentManager();
        progressBar = v.findViewById(R.id.progressBar_home_fragment);

        deviceButton = v.findViewById(R.id.devicesHomeButton);
        customerButton = v.findViewById(R.id.customersHomeButton);
        categoryButton = v.findViewById(R.id.categoriesHomeButton);
        userButton = v.findViewById(R.id.usersButton);
        logsButton = v.findViewById(R.id.LogsButton);

        GPSUtils gpsUtils = new GPSUtils(getActivity(), getContext());
        gpsUtils.getLastLocation();

        try {
            loginAndPassword = FileUtils.readConfigFromFile(context).split(";");
       if (loginAndPassword.length!=2){
           FragmentUtils.replaceFragment(new SettingsFragment(), fragmentManager);
       }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;
            TextView versionTextView = v.findViewById(R.id.version_textView_home_fragment);
            versionTextView.setText("ver. "+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        if (!ConnectionUtils.isInternetAvailable(getContext())) {
            MessageFragment messageFragment = new MessageFragment();
            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
            messageFragment.setMessage("Brak połączenia z internetem");
        } else {
            progressBar.setVisibility(View.GONE);
            if (Config.getUSER() == "") {
                startAsyncTask(v);
            }
// ADD NAME TO TOOLBAR //
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Strona główna");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");

// DEVICES LIST BUTTON //
            Button buttonHome = (Button) v.findViewById(R.id.devicesHomeButton);
            buttonHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentUtils.replaceFragment(new DevicesListFragment(HomeFragment.this), getFragmentManager());
                }
            });

// LOGS BUTTON ///
            logs = (Button) v.findViewById(R.id.LogsButton);
            logs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentUtils.replaceFragment(new LogsFragment(HomeFragment.this), getFragmentManager());
                }
            });
            if (Config.getIsAdmin() == false) {//access level
                logs.setVisibility(View.INVISIBLE);
            }

// USERS BUTTON //
            users = (Button) v.findViewById(R.id.usersButton);
            users.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentUtils.replaceFragment(new UsersListFragment(), getFragmentManager());
                }
            });
            if (Config.getIsAdmin() == false) {//access level
                users.setVisibility(View.INVISIBLE);
            }

// CUSTOMERS BUTTON //
            Button buttonCustomers = (Button) v.findViewById(R.id.customersHomeButton);
            buttonCustomers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentUtils.replaceFragment(new CustomersListFragment(HomeFragment.this), getFragmentManager());
                }
            });

// CATEGORIES BUTTON //
            Button buttonCategories = (Button) v.findViewById(R.id.categoriesHomeButton);
            buttonCategories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentUtils.replaceFragment(new CategoriesListFragment(HomeFragment.this), getFragmentManager());
                }
            });

        }

        return v;
    }

    /// Async task ///
    public void startAsyncTask(View v) {
        HomeFragment.GetDataFromApiAsyncTask task = new HomeFragment.GetDataFromApiAsyncTask(this);
        task.execute();
    }

    private class GetDataFromApiAsyncTask extends AsyncTask<Integer, Integer, String> {

        private WeakReference<HomeFragment> activityWeakReference;
        private SimpleAdapter adapter;

        GetDataFromApiAsyncTask(HomeFragment activity) {
            activityWeakReference = new WeakReference<HomeFragment>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... integers) {

            progressBar.setVisibility(View.VISIBLE);
//            progressBar.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            deviceButton.setVisibility(View.INVISIBLE);
            customerButton.setVisibility(View.INVISIBLE);
            categoryButton.setVisibility(View.INVISIBLE);
            userButton.setVisibility(View.INVISIBLE);
            logsButton.setVisibility(View.INVISIBLE);


            try {

//                if (Config.getUSER() == "") {


                if (loginAndPassword.length == 2) {


                    Config.getAutenthification(loginAndPassword[0], loginAndPassword[1]);

                } else {
                    FragmentUtils.replaceFragment(new SettingsFragment(), fragmentManager);

                }


            } catch (Exception e) {
                MessageFragment messageFragment = new MessageFragment();
                messageFragment.setMessage(e.getMessage());
                FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                e.printStackTrace();
            }
            return "OK";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            return;

        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
//            progressBar.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

            if (Config.getUSER().equals("")) {
                FragmentUtils.setSubtitleNavigationDrawer(activity, "Jesteś niezalogowany");
                FragmentUtils.replaceFragment(new SettingsFragment(), fragmentManager);

            } else {
                String accessLevel = "";
                if (Config.getIsAdmin() == true) {
                    accessLevel = "(Admin)";
                }
                FragmentUtils.setSubtitleNavigationDrawer(activity, "Jesteś zalogowany jako:\n" + Config.getUSER() + " " + accessLevel);

            }
            deviceButton.setVisibility(View.VISIBLE);
            customerButton.setVisibility(View.VISIBLE);
            categoryButton.setVisibility(View.VISIBLE);


            if (Config.getIsAdmin() == false) {//access level
                logs.setVisibility(View.INVISIBLE);
            } else {
                logs.setVisibility(View.VISIBLE);
            }
            if (Config.getIsAdmin() == false) {//access level
                users.setVisibility(View.INVISIBLE);
            } else {
                users.setVisibility(View.VISIBLE);
            }

        }
    }
}