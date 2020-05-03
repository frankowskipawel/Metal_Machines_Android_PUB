package com.pl.metalmachines.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.pl.metalmachines.R;
import com.pl.metalmachines.dao.UtilsDao;
import com.pl.metalmachines.ui.categories.CategoriesListFragment;
import com.pl.metalmachines.ui.customers.CustomersListFragment;
import com.pl.metalmachines.ui.devices.DevicesListFragment;
import com.pl.metalmachines.ui.info.MessageFragment;
import com.pl.metalmachines.ui.logs.LogsFragment;
import com.pl.metalmachines.utils.FragmentUtils;
import com.pl.metalmachines.utils.ConnectionUtils;
import com.pl.metalmachines.utils.GPSUtils;

import java.io.IOException;

public class HomeFragment extends Fragment {


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        GPSUtils gpsUtils = new GPSUtils(getActivity(), getContext());
        gpsUtils.getLastLocation();

        try {
            if (!ConnectionUtils.isInternetAvailable(getContext())) {
                MessageFragment messageFragment = new MessageFragment();
                FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                messageFragment.setMessage("Brak połączenia z internetem");
            } else {
                ConnectionUtils.checkLoginAndPassword(getContext(), getActivity(), getFragmentManager());

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

// LOGS BUTTON //
                Button logs = (Button) v.findViewById(R.id.LogsButton);
                logs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FragmentUtils.replaceFragment(new LogsFragment(HomeFragment.this), getFragmentManager());
                    }
                });
                if (UtilsDao.getIsAdmin() == false) {//access level
                    logs.setVisibility(View.INVISIBLE);
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
        } catch (IOException e) {
            MessageFragment messageFragment = new MessageFragment();
            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
            messageFragment.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return v;
    }
}