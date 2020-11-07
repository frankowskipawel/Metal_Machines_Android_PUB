package com.paweldev.maszynypolskie.ui.categories;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.paweldev.maszynypolskie.R;
import com.paweldev.maszynypolskie.repository.CategoryRepository;
import com.paweldev.maszynypolskie.repository.DeviceRepository;
import com.paweldev.maszynypolskie.config.Config;
import com.paweldev.maszynypolskie.model.Category;
import com.paweldev.maszynypolskie.model.Device;
import com.paweldev.maszynypolskie.ui.devices.DevicesDetailsFragment;
import com.paweldev.maszynypolskie.ui.info.MessageFragment;
import com.paweldev.maszynypolskie.utils.FragmentUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CategoriesDetailsFragment extends Fragment {
    private String header;
    private String content;
    private Fragment previousFragment;
    private Category currentCategory;

    public CategoriesDetailsFragment(String header, String content, Fragment previousFragment) {
        this.header = header;
        this.content = content;
        this.previousFragment = previousFragment;
    }

    public CategoriesDetailsFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category_details, container, false);
        TextView headerTextView = v.findViewById(R.id.headerTextView);
        TextView contentTextview = v.findViewById(R.id.contentTextView);
        Button editButton = (Button) v.findViewById(R.id.saveShowCategoriesFragment);
        Button deleteButton = (Button) v.findViewById(R.id.deleteShowCategoriesFragment);
        if (Config.getIsAdmin() == false) {
            editButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
        }
        try {
            currentCategory = CategoryRepository.findCategoryById(content);
            headerTextView.setText(currentCategory.getName());
            contentTextview.setText("id: " + content);
            final ListView listView = (ListView) v.findViewById(R.id.listView_category_details_fragment);
            loadDataToListView(CategoriesDetailsFragment.this, listView);

//CLICK ITEM OF LISTVIEW //
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                    Object clickItemObj = adapterView.getAdapter().getItem(index);
                    HashMap clickItemMap = (HashMap) clickItemObj;
                    String header = (String) clickItemMap.get("First Line");
                    String content = (String) clickItemMap.get("Second Line");

                    DevicesDetailsFragment devicesDetailsFragment = new DevicesDetailsFragment();
                    devicesDetailsFragment.setPreviousFragment(CategoriesDetailsFragment.this);
                    devicesDetailsFragment.setCurrentDevice(getDeviceByHeader(header));
                    FragmentUtils.replaceFragment(devicesDetailsFragment, getFragmentManager());
                }
            });

// ADD NAME TO TOOLBAR //
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Szczegóły kategorii");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");

// PREVIOUS BUTTON //
            Button prevButton = (Button) v.findViewById(R.id.prevShowCategoriesFragment);
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
                    CategoriesEditFragment categoriesEditFragment = new CategoriesEditFragment(CategoriesDetailsFragment.this, true, content, header);
                   categoriesEditFragment.setCurrentCategory(currentCategory);
                    FragmentUtils.replaceFragment(categoriesEditFragment, getFragmentManager());
                }
            });

// DELETE BUTTON //
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Potwierdzenie operacji")
                            .setMessage("Czy nepewno chcesz usunąć\n" + header + "?")
                            .setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        CategoryRepository.deleteCategoryByName(header);
                                    } catch (Exception e) {
                                        MessageFragment messageFragment = new MessageFragment();
                                        messageFragment.setMessage(e.getMessage());
                                        FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                        e.printStackTrace();
                                    }
                                    getFragmentManager().popBackStack();
                                    Toast.makeText(getActivity(), "Usunięto  : " + header, Toast.LENGTH_SHORT).show();
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
        } catch (Exception e) {
            MessageFragment messageFragment = new MessageFragment();
            messageFragment.setMessage(e.getMessage());
            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
            e.printStackTrace();
        }

// SERVICE OPERATIONS BUTTON //
        Button serviceOperationsButton = v.findViewById(R.id.protokol_przegladu_serwisowego_button_categroy_details);
        serviceOperationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.replaceFragment(new ServiceOperationsFragment(currentCategory), getFragmentManager());
            }
        });

        return v;
    }

    private void loadDataToListView(CategoriesDetailsFragment categoriesDetailsFragment, ListView listView) {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        try {
            data = DeviceRepository.findByCategoryForListview(content);
            List<Map<String, String>> filteredData = new ArrayList<Map<String, String>>();
            for (Map<String, String> datum : data) {
                filteredData.add(datum);
            }
            SimpleAdapter adapter = new SimpleAdapter(categoriesDetailsFragment.getContext(), filteredData,
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
}