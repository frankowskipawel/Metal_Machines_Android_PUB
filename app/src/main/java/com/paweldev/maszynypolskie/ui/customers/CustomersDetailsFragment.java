package com.paweldev.maszynypolskie.ui.customers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.paweldev.maszynypolskie.R;
import com.paweldev.maszynypolskie.repository.CustomerRepository;
import com.paweldev.maszynypolskie.repository.DeviceRepository;
import com.paweldev.maszynypolskie.config.Config;
import com.paweldev.maszynypolskie.model.Customer;
import com.paweldev.maszynypolskie.model.Device;
import com.paweldev.maszynypolskie.config.CompanyData;
import com.paweldev.maszynypolskie.ui.devices.DevicesDetailsFragment;
import com.paweldev.maszynypolskie.ui.info.MessageFragment;
import com.paweldev.maszynypolskie.utils.FragmentUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CustomersDetailsFragment extends Fragment {

    private Fragment previousFragment;
    private Customer currentCustomer;

    public CustomersDetailsFragment(Fragment previousFragment, Customer currentCustomer) {
        this.previousFragment = previousFragment;
        this.currentCustomer = currentCustomer;
    }

    public CustomersDetailsFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        Log.i("Message", "On create run");
        View v = inflater.inflate(R.layout.fragment_customer_details, container, false);
        TextView headerTextView = v.findViewById(R.id.headerTextView);
        TextView contentTextview = v.findViewById(R.id.contentTextView);
        headerTextView.setText("#" + currentCustomer.getId() + " " + currentCustomer.getShortName());
        final ListView listView = (ListView) v.findViewById(R.id.listView_customer_details_fragment);
        Button editButton = (Button) v.findViewById(R.id.saveShowCustomerFragment);
        Button deleteButton = (Button) v.findViewById(R.id.deleteShowCustomerFragment);
        if (Config.getIsAdmin() == false) {
            editButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
        }
        String contentFromDataBase = "";
        if (currentCustomer.getFullName()!=null) {
            contentFromDataBase += currentCustomer.getFullName() + "\n";
        }
        if (currentCustomer.getStreetAddress()!=null) {
            contentFromDataBase += currentCustomer.getStreetAddress() + ", ";
        }
        if (currentCustomer.getZipCode()!=null) {
            contentFromDataBase += currentCustomer.getZipCode() + " ";
        }
        if (currentCustomer.getCity()!=null) {
            contentFromDataBase += currentCustomer.getCity() + "\n";
        }
        if (currentCustomer.getNip()!=null) {
            contentFromDataBase += "NIP : " + currentCustomer.getNip() + "\n";
        }
        if (currentCustomer.getRegon()!=null) {
            contentFromDataBase += "REGON : " + currentCustomer.getRegon() + "\n";
        }
        if (currentCustomer.getPhoneNumber()!=null) {
            contentFromDataBase += "tel. " + currentCustomer.getPhoneNumber() + "\n";
        }
        if (currentCustomer.getEmail()!=null) {
            contentFromDataBase += currentCustomer.getEmail() + "\n";
        }
        loadDataToListView(CustomersDetailsFragment.this, listView);
        contentTextview.setText(contentFromDataBase);

// ADD NAME TO TOOLBAR //
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Szczegóły kontrahenta");
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
                devicesDetailsFragment.setPreviousFragment(CustomersDetailsFragment.this);
                devicesDetailsFragment.setCurrentDevice(getDeviceByHeader(header));
                FragmentUtils.replaceFragment(devicesDetailsFragment, getFragmentManager());
            }
        });

// PREVIEW BUTTON //
        Button prevButton = (Button) v.findViewById(R.id.prevShowCustomerFragment);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

// EDIT BUTTON //
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.replaceFragment(new CustomersEditFragment(
                        CustomersDetailsFragment.this,
                        true,
                        currentCustomer.getId(),
                        currentCustomer.getShortName(),
                        currentCustomer.getFullName(),
                        currentCustomer.getStreetAddress(),
                        currentCustomer.getZipCode(),
                        currentCustomer.getCity(),
                        currentCustomer.getNip(),
                        currentCustomer.getRegon(),
                        currentCustomer.getPhoneNumber(),
                        currentCustomer.getEmail()), getFragmentManager());
            }
        });

// DELETE BUTTON //
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Potwierdzenie operacji")
                        .setMessage("Czy nepewno chcesz usunąć\n" + currentCustomer.getShortName() + "?")
                        .setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    CustomerRepository.deleteCustomer(currentCustomer);
                                    getFragmentManager().popBackStack();
                                    Toast.makeText(getActivity(), "Usunięto  : " + currentCustomer.getShortName(), Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    MessageFragment messageFragment = new MessageFragment();
                                    messageFragment.setMessage(e.getMessage());
                                    FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

// NAVIGATION TO CUSTOMER BUTTON //
        ImageButton navigationToCustomerButton = v.findViewById(R.id.navigaton_to_customer_details_fragment);
        navigationToCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=" + currentCustomer.getStreetAddress() + "+" + currentCustomer.getZipCode() + " " + currentCustomer.getCity()));
                startActivity(intent);
            }
        });

// SEND EMAIL BUTTON //
        ImageButton sendEmailButton = v.findViewById(R.id.send_email_customer_details_fragment);
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{currentCustomer.getEmail()});
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "\n____________________________________\n" + CompanyData.getFullCompanyData());
                getActivity().startActivity(Intent.createChooser(emailIntent, "Wysyłam email..."));
            }
        });
        
// CALL TO CUSTOMER BUTTON //
        ImageButton callToCustomer = v.findViewById(R.id.call_to_customer_details_fragment);
        callToCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", currentCustomer.getPhoneNumber(), null)));
            }
        });

// SEND SMS BUTTON //
        ImageButton sendSms = v.findViewById(R.id.send_sms_customer_details_fragment);
        sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("smsto:"));
                i.setType("vnd.android-dir/mms-sms");
                i.putExtra("address", currentCustomer.getPhoneNumber());
                startActivity(Intent.createChooser(i, "Send sms via:"));
            }
        });

        return v;
    }

    private void loadDataToListView(CustomersDetailsFragment customersDetailsFragment, ListView listView) {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        try {
            data = DeviceRepository.findByCustomerForListView(currentCustomer.getId());
            List<Map<String, String>> filteredData = new ArrayList<Map<String, String>>();
            for (Map<String, String> datum : data) {
                filteredData.add(datum);
            }
            SimpleAdapter adapter = new SimpleAdapter(customersDetailsFragment.getContext(), filteredData,
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

    private Device getDeviceByHeader(String header) {

        Pattern pattern = Pattern.compile("^#\\d+ ");
        Matcher matcher = pattern.matcher(header);

        while (matcher.find()) {
            String id = matcher.group().substring(1, matcher.group().length() - 1);
            try {
                return DeviceRepository.findDeviceById(id);
            } catch (Exception e) {
                MessageFragment messageFragment = new MessageFragment();
                messageFragment.setMessage(e.getMessage());
                FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                e.printStackTrace();
            }
        }
        return null;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public void setPreviousFragment(Fragment previousFragment) {
        this.previousFragment = previousFragment;
    }
}