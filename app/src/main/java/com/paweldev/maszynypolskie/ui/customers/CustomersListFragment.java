package com.paweldev.maszynypolskie.ui.customers;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.paweldev.maszynypolskie.R;
import com.paweldev.maszynypolskie.config.Config;
import com.paweldev.maszynypolskie.repository.CustomerRepository;
import com.paweldev.maszynypolskie.ui.devices.DevicesEditFragment;
import com.paweldev.maszynypolskie.ui.home.HomeFragment;
import com.paweldev.maszynypolskie.ui.info.MessageFragment;
import com.paweldev.maszynypolskie.utils.ConnectionUtils;
import com.paweldev.maszynypolskie.utils.FragmentUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomersListFragment extends Fragment {

    private CustomersViewModel customersViewModel;
    private Fragment previousFragment;
    private ProgressBar progressBar;
    private List<Map<String, String>> customers;
    private EditText editTextSearch;
    private ListView listView;
    private CustomersListFragment customersListFragment;

    public CustomersListFragment(Fragment previousFragment) {
        this.previousFragment = previousFragment;
    }

    public CustomersListFragment() {
    }

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_customers, container, false);

        progressBar = v.findViewById(R.id.progressBar_customersList_fragment);


            if (!ConnectionUtils.isInternetAvailable(getContext())) {
                MessageFragment messageFragment = new MessageFragment();
                FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                messageFragment.setMessage("Brak połączenia z internetem");
            } else {
                if (Config.getUSER()!="") {
                    customersListFragment = this;
                    listView = (ListView) v.findViewById(R.id.listViewCustomersFragment);
                    editTextSearch = v.findViewById(R.id.editTextSearch);

// ADD NAME TO TOOLBAR //
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Lista kontrahentów");
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");


/// LIST VIEW ON CLICK //
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {

                            Object clickItemObj = adapterView.getAdapter().getItem(index);
                            HashMap clickItemMap = (HashMap) clickItemObj;
                            String header = (String) clickItemMap.get("First Line");
                            String content = (String) clickItemMap.get("Second Line");

                            if (previousFragment instanceof DevicesEditFragment) {
                                getFragmentManager().popBackStack();
                                ((DevicesEditFragment) previousFragment).getCurrentDevice().setCustomerName(header.replaceFirst("^#\\d+ ", ""));
                            } else {
                                try {
                                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                    StrictMode.setThreadPolicy(policy);
                                    CustomersDetailsFragment customersDetailsFragment = new CustomersDetailsFragment();
                                    customersDetailsFragment.setPreviousFragment(CustomersListFragment.this);
                                    customersDetailsFragment.setCurrentCustomer(CustomerRepository.findCustomerById(getIdFromString(header)));
                                    FragmentUtils.replaceFragment(customersDetailsFragment, getFragmentManager());
                                } catch (Exception e) {
                                    MessageFragment messageFragment = new MessageFragment();
                                    messageFragment.setMessage(e.getMessage());
                                    FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

// SEARCH BUTTON //
                    Button buttonSearch = (Button) v.findViewById(R.id.searchButton);
                    buttonSearch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            loadDataToListView(editTextSearch, customersListFragment, listView);
                        }
                    });
// ADD BUTTON //
                    Button addButton = (Button) v.findViewById(R.id.addNewCustomerFrame);
                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            FragmentUtils.replaceFragment(new CustomersEditFragment(CustomersListFragment.this), getFragmentManager());
                        }
                    });

// PREVIEW BUTTON //
                    final Button prevButton = (Button) v.findViewById(R.id.prevCustomerFragment);
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
            }



        return v;
    }


    private void loadDataToListView(EditText editTextSearch, CustomersListFragment customersListFragment, ListView listView) {

            List<Map<String, String>> filteredData = new ArrayList<Map<String, String>>();
            for (Map<String, String> datum : customers) {
                if (replaceCustomChar(datum.toString().toUpperCase()).contains(replaceCustomChar(editTextSearch.getText().toString().toUpperCase()))) {
                    filteredData.add(datum);
                }
            }
            SimpleAdapter adapter = new SimpleAdapter(customersListFragment.getContext(), filteredData,
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

    private String getIdFromString(String header) {

        Pattern pattern = Pattern.compile("^#\\d+ ");
        Matcher matcher = pattern.matcher(header);

        while (matcher.find()) {
            String id = matcher.group().substring(1, matcher.group().length() - 1);
            return id;
        }
        return null;
    }

    public void setPreviousFragment(Fragment previousFragment) {
        this.previousFragment = previousFragment;
    }

    /// Async task ///
    public void startAsyncTask(View v) {
        CustomersListFragment.GetDataFromApiAsyncTask task = new CustomersListFragment.GetDataFromApiAsyncTask(this);
        task.execute();
    }

    private class GetDataFromApiAsyncTask extends AsyncTask<Integer, Integer, String> {

        private WeakReference<CustomersListFragment> activityWeakReference;
        private SimpleAdapter adapter;

        GetDataFromApiAsyncTask(CustomersListFragment activity) {
            activityWeakReference = new WeakReference<CustomersListFragment>(activity);
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

                customers = CustomerRepository.findAllCustomersForListView();
                System.out.println(customers.toString());
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
            CustomersListFragment activity = activityWeakReference.get();

            List<Map<String, String>> data = new ArrayList<Map<String, String>>();

                List<Map<String, String>> filteredData = new ArrayList<Map<String, String>>();
                for (Map<String, String> datum : customers) {
                    if (replaceCustomChar(datum.toString().toUpperCase()).contains(replaceCustomChar(editTextSearch.getText().toString().toUpperCase()))) {
                        filteredData.add(datum);
                    }
                }
            if (customersListFragment.getContext() != null) {

                SimpleAdapter adapter = new SimpleAdapter(customersListFragment.getContext(), filteredData,
                        R.layout.simple_list_item_2_bold_header,
                        new String[]{"First Line", "Second Line"},
                        new int[]{R.id.text1, R.id.text2});
                listView.setAdapter(adapter);  }
        }
    }
}
