package com.pl.metalmachines.ui.customers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.pl.metalmachines.R;
import com.pl.metalmachines.dao.CustomerDao;
import com.pl.metalmachines.ui.devices.DevicesEditFragment;
import com.pl.metalmachines.ui.home.HomeFragment;
import com.pl.metalmachines.ui.info.MessageFragment;
import com.pl.metalmachines.utils.FragmentUtils;
import com.pl.metalmachines.utils.ConnectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomersListFragment extends Fragment {

    private CustomersViewModel customersViewModel;
    private Fragment previousFragment;

    public CustomersListFragment(Fragment previousFragment) {
        this.previousFragment = previousFragment;
    }

    public CustomersListFragment() {
    }

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_customers, container, false);

        try {
            if (!ConnectionUtils.isInternetAvailable(getContext())) {
                MessageFragment messageFragment = new MessageFragment();
                FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                messageFragment.setMessage("Brak połączenia z internetem");
            } else {
                if (ConnectionUtils.checkLoginAndPassword(getContext(), getActivity(), getFragmentManager())) {
                    final CustomersListFragment customersListFragment = this;
                    final ListView listView = (ListView) v.findViewById(R.id.listViewCustomersFragment);
                    final EditText editTextSearch = v.findViewById(R.id.editTextSearch);

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
                                    FragmentUtils.replaceFragment(new CustomersDetailsFragment(CustomersListFragment.this, CustomerDao.findCustomerById(getIdFromString(header))), getFragmentManager());
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
                    loadDataToListView(editTextSearch, customersListFragment, listView);

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


    private void loadDataToListView(EditText editTextSearch, CustomersListFragment customersListFragment, ListView listView) {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        try {
            data = CustomerDao.findAllCustomersForListView();

            List<Map<String, String>> filteredData = new ArrayList<Map<String, String>>();
            for (Map<String, String> datum : data) {
                if (replaceCustomChar(datum.toString().toUpperCase()).contains(replaceCustomChar(editTextSearch.getText().toString().toUpperCase()))) {
                    filteredData.add(datum);
                }
            }
            SimpleAdapter adapter = new SimpleAdapter(customersListFragment.getContext(), filteredData,
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
}
