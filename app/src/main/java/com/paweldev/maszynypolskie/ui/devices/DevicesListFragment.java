package com.paweldev.maszynypolskie.ui.devices;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.paweldev.maszynypolskie.R;
import com.paweldev.maszynypolskie.model.Device;
import com.paweldev.maszynypolskie.repository.DeviceRepository;
import com.paweldev.maszynypolskie.ui.home.HomeFragment;
import com.paweldev.maszynypolskie.ui.info.MessageFragment;
import com.paweldev.maszynypolskie.utils.ConnectionUtils;
import com.paweldev.maszynypolskie.utils.DeviceUtils;
import com.paweldev.maszynypolskie.utils.FragmentUtils;

import java.lang.ref.WeakReference;
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
    private DevicesListFragment devicesListFragment;
    private ListView listView;
    private EditText editTextSearch;
    private ProgressBar progressBar;

    public DevicesListFragment(Fragment previousFragment) {
        this.previousFragment = previousFragment;
    }

    public DevicesListFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_devices, container, false);


        if (!ConnectionUtils.isInternetAvailable(getContext())) {
            MessageFragment messageFragment = new MessageFragment();
            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
            messageFragment.setMessage("Brak połączenia z internetem");
        } else {

            devicesListFragment = this;
            listView = (ListView) v.findViewById(R.id.listViewCustomersFragment);
            editTextSearch = v.findViewById(R.id.editTextSearch);
            ImageButton buttonSearch = (ImageButton) v.findViewById(R.id.searchButton);
            progressBar = v.findViewById(R.id.progressBar_devicesList_fragment);

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
            startAsyncTask(v);
        }


        return v;
    }

    private void loadDataToListView(EditText editTextSearch, DevicesListFragment devicesListFragment, ListView listView) {

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
                if (device.getId().equals(id)) {
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


    public void startAsyncTask(View v) {
        DevicesListFragment.GetDataFromApiAsyncTask task = new DevicesListFragment.GetDataFromApiAsyncTask(this);
        task.execute();
    }

    private class GetDataFromApiAsyncTask extends AsyncTask<Integer, Integer, String> {

        private WeakReference<DevicesListFragment> activityWeakReference;
        private SimpleAdapter adapter;

        GetDataFromApiAsyncTask(DevicesListFragment activity) {
            activityWeakReference = new WeakReference<DevicesListFragment>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... integers) {

            progressBar.setVisibility(View.VISIBLE);
            progressBar.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

            try {

                devices = DeviceRepository.findAllDevicesForListView();
                System.out.println(devices.toString());
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
            progressBar.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            DevicesListFragment activity = activityWeakReference.get();
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
            if (devicesListFragment.getContext()!=null){
            SimpleAdapter adapter = new SimpleAdapter(devicesListFragment.getContext(), filteredData,
                    R.layout.simple_list_item_2_bold_header,
                    new String[]{"First Line", "Second Line"},
                    new int[]{R.id.text1, R.id.text2});
            listView.setAdapter(adapter);}
        }
    }
}