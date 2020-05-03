package com.pl.metalmachines.ui.devices;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.pl.metalmachines.R;
import com.pl.metalmachines.dao.DeviceDao;
import com.pl.metalmachines.model.Device;
import com.pl.metalmachines.ui.home.HomeFragment;
import com.pl.metalmachines.ui.info.MessageFragment;
import com.pl.metalmachines.utils.DeviceUtils;
import com.pl.metalmachines.utils.FragmentUtils;
import com.pl.metalmachines.utils.ConnectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DevicesListFragment extends Fragment {

    private TreeSet<Device> devices;
    private Fragment previousFragment;

    public DevicesListFragment(Fragment previousFragment) {
        this.previousFragment = previousFragment;
    }

    public DevicesListFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_devices, container, false);

        try {
            if (!ConnectionUtils.isInternetAvailable(getContext())) {
                MessageFragment messageFragment = new MessageFragment();
                FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                messageFragment.setMessage("Brak połączenia z internetem");
            } else {
                if (ConnectionUtils.checkLoginAndPassword(getContext(), getActivity(), getFragmentManager())) {
                    final DevicesListFragment devicesListFragment = this;
                    final ListView listView = (ListView) v.findViewById(R.id.listViewCustomersFragment);
                    final EditText editTextSearch = v.findViewById(R.id.editTextSearch);
                    ImageButton buttonSearch = (ImageButton) v.findViewById(R.id.searchButton);

// SEARCH BUTTON //
                    buttonSearch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadDataToListView(editTextSearch, devicesListFragment, listView);
                            //Hide keyboard
                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(container.getWindowToken(), 0);
                        }
                    });
// ADD NAME TO TOOLBAR //
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Lista maszyn");
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");

// CLICK ITEM FROM LISTVIEW //
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                            Object clickItemObj = adapterView.getAdapter().getItem(index);
                            HashMap clickItemMap = (HashMap) clickItemObj;
                            String header = (String) clickItemMap.get("First Line");
                            String content = (String) clickItemMap.get("Second Line");
                            DevicesDetailsFragment devicesDetailsFragment = new DevicesDetailsFragment();
                            devicesDetailsFragment.setPreviousFragment(DevicesListFragment.this);
                            devicesDetailsFragment.setCurrentDevice(getDeviceByHeader(header));
                            FragmentUtils.replaceFragment(devicesDetailsFragment, getFragmentManager());
                        }
                    });

// ADD BUTTON //
                    Button addButton = (Button) v.findViewById(R.id.add_new_devices_list_fragment);
                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DevicesEditFragment devicesEditFragment = new DevicesEditFragment();
                            devicesEditFragment.setPreviousFragment(DevicesListFragment.this);
                            FragmentUtils.replaceFragment(devicesEditFragment, getFragmentManager());
                        }
                    });

// PREVIOUS BUTTON //
                    Button prevButton = (Button) v.findViewById(R.id.prevDevicesFragment);
                    prevButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (previousFragment == null) {
                                previousFragment = new HomeFragment();
                            }
                            getFragmentManager().popBackStack();
                        }
                    });
                    loadDataToListView(editTextSearch, devicesListFragment, listView);
                }
            }
        } catch (IOException e) {
            MessageFragment messageFragment = new MessageFragment();
            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
            messageFragment.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return v;
    }

    private void loadDataToListView(EditText editTextSearch, DevicesListFragment devicesListFragment, ListView listView) {
        try {
            devices = DeviceDao.findAllDevicesForListView();
            List<Map<String, String>> deviceData = new ArrayList<Map<String, String>>();
            for (Device device : devices) {
                Map<String, String> row = new HashMap<String, String>(2);
                row.put("First Line", "#" + device.getId() + " " + device.getName());
                row.put("Second Line", DeviceUtils.getDeviceContentAtString(device));
                deviceData.add(row);
            }
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();
            data = deviceData;
            List<Map<String, String>> filteredData = new ArrayList<Map<String, String>>();
            for (Map<String, String> datum : data) {
                if (replaceCustomChar(datum.toString().toUpperCase()).contains(replaceCustomChar(editTextSearch.getText().toString().toUpperCase()))) {
                    filteredData.add(datum);
                }
            }
            SimpleAdapter adapter = new SimpleAdapter(devicesListFragment.getContext(), filteredData,
                    R.layout.simple_list_item_2_bold_header,
                    new String[]{"First Line", "Second Line"},
                    new int[]{R.id.text1, R.id.text2});
            listView.setAdapter(adapter);
        } catch (Exception e) {
            MessageFragment messageFragment = new MessageFragment();
            messageFragment.setMessage(e.getMessage());
            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
            e.printStackTrace();
        }
    }

    public String replaceCustomChar(String string) {
        string = string.replace("Ę", "E");
        string = string.replace("Ą", "A");
        string = string.replace("Ć", "C");
        string = string.replace("Ń", "N");
        string = string.replace("Ł", "L");
        string = string.replace("Ó", "O");
        string = string.replace("Ż", "Z");
        string = string.replace("Ź", "Z");
        string = string.replace("Ś", "S");
        string = string.replace(",", ".");

        return string;
    }

    private Device getDeviceByHeader(String header) {

        Pattern pattern = Pattern.compile("^#\\d+ ");
        Matcher matcher = pattern.matcher(header);

        while (matcher.find()) {
            String id = matcher.group().substring(1, matcher.group().length() - 1);
            for (Device device : devices) {
                if (device.id.equals(id)) {
                    return device;
                }
            }
            break;
        }
        return null;
    }

    public void setPreviousFragment(Fragment previousFragment) {
        this.previousFragment = previousFragment;
    }
}