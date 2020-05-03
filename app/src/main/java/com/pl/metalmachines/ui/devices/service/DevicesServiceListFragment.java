package com.pl.metalmachines.ui.devices.service;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.pl.metalmachines.R;
import com.pl.metalmachines.dao.ServiceDao;
import com.pl.metalmachines.model.Device;
import com.pl.metalmachines.model.Service;
import com.pl.metalmachines.ui.info.MessageFragment;
import com.pl.metalmachines.utils.ConnectionUtils;
import com.pl.metalmachines.utils.FragmentUtils;
import com.pl.metalmachines.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevicesServiceListFragment extends Fragment {

    private View v;
    private Device currentDevice;
    private Fragment previousFragment;
    private ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_device_service_list, container, false);

        if (!ConnectionUtils.isInternetAvailable(getContext())) {
            MessageFragment messageFragment = new MessageFragment();
            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
            messageFragment.setMessage("Brak połączenia z internetem");
        } else {

// ADD NAME TO TOOLBAR //
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Lista zdarzeń serwisowych");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");
//
            listView = v.findViewById(R.id.service_listView);
            loadDataToListView(DevicesServiceListFragment.this, listView);

// PREVIOUS BUTTON //
            Button prevButton = (Button) v.findViewById(R.id.previous_button_service_fragment);
            prevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();

                }
            });
// ADD BUTTON //
            Button addButton = (Button) v.findViewById(R.id.devices_add_new_service_button);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeviceAddNewServiceFragment deviceAddNewServiceFragment = new DeviceAddNewServiceFragment();
                    deviceAddNewServiceFragment.setCurrentDevice(currentDevice);
                    deviceAddNewServiceFragment.setEdit(true);
                    FragmentUtils.replaceFragment(deviceAddNewServiceFragment, getFragmentManager());
                }
            });

// CLICK ITEM FROM LISTVIEW //
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                    try {
                        Object clickItemObj = adapterView.getAdapter().getItem(index);
                        HashMap clickItemMap = (HashMap) clickItemObj;
                        String header = (String) clickItemMap.get("First Line");
                        String content = (String) clickItemMap.get("Second Line");
                        Service service = ServiceDao.findServiceAfterId(StringUtils.getIdFromString(header));
                        DeviceAddNewServiceFragment deviceAddNewServiceFragment = new DeviceAddNewServiceFragment();
                        deviceAddNewServiceFragment.setCurrentService(service);
                        deviceAddNewServiceFragment.setCurrentDevice(currentDevice);
                        deviceAddNewServiceFragment.setEdit(false);
                        FragmentUtils.replaceFragment(deviceAddNewServiceFragment, getFragmentManager());
                    } catch (Exception e) {
                        MessageFragment messageFragment = new MessageFragment();
                        messageFragment.setMessage(e.getMessage());
                        FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                        e.printStackTrace();
                    }
                }
            });
        }
        return v;
    }

    private void loadDataToListView(DevicesServiceListFragment devicesServiceListFragment, ListView listView) {
        try {
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();
            data = ServiceDao.findAlldeviceServicesForListView(currentDevice.getId());
            List<Map<String, String>> filteredData = new ArrayList<Map<String, String>>();
            for (Map<String, String> datum : data) {
                filteredData.add(datum);
            }
            SimpleAdapter adapter = new SimpleAdapter(devicesServiceListFragment.getContext(), filteredData,
                    android.R.layout.simple_list_item_2,
                    new String[]{"First Line", "Second Line"},
                    new int[]{android.R.id.text1, android.R.id.text2});
            listView.setAdapter(adapter);
        } catch (Exception e) {
            MessageFragment messageFragment = new MessageFragment();
            messageFragment.setMessage(e.getMessage());
            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
            e.printStackTrace();
        }
    }

    public void setCurrentDevice(Device currentDevice) {
        this.currentDevice = currentDevice;
    }

    public Fragment getPreviousFragment() {
        return previousFragment;
    }

    public void setPreviousFragment(Fragment previousFragment) {
        this.previousFragment = previousFragment;
    }
}
