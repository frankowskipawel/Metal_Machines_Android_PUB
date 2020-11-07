package com.paweldev.maszynypolskie.ui.categories;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;

import com.paweldev.maszynypolskie.R;
import com.paweldev.maszynypolskie.repository.CategoryRepository;
import com.paweldev.maszynypolskie.config.Config;
import com.paweldev.maszynypolskie.model.Category;
import com.paweldev.maszynypolskie.ui.info.MessageFragment;
import com.paweldev.maszynypolskie.utils.FragmentUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ServiceOperationsFragment extends Fragment {

    private final int MAX_NUMBER_SERVICE_OPERATIONS = 33;

    View v;
    ObservableArrayList<String> listOperations = new ObservableArrayList<>();
    private ListView listView;
    private Category currentCategory;

    public ServiceOperationsFragment(Category currentCategory) {
        this.currentCategory = currentCategory;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_service_operations, container, false);
        listView = v.findViewById(R.id.listView_service_operations_fragment);
        loadDataToListView(ServiceOperationsFragment.this, listView);

// PREVIOUS BUTTON //
        Button previousButton = v.findViewById(R.id.previous_button_service_operations_fragment);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

// ADD NEW BUTTON //
        Button addNewServiceOperation = v.findViewById(R.id.add_new_service_operations_fragment);

        if (Config.getIsAdmin() == false) {
            addNewServiceOperation.setVisibility(View.INVISIBLE);
        } //access level

        addNewServiceOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listOperations.size() < MAX_NUMBER_SERVICE_OPERATIONS) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Dodaj");
                    alert.setMessage("Nową czynność serwisową");
                    LinearLayout linearLayout = new LinearLayout(getContext());
                    linearLayout.setPadding(40, 0, 40, 0);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    final TextView text1 = new TextView(getContext());
                    text1.setText("Nazwa");
                    linearLayout.addView(text1);
                    final EditText newServiceOperations = new EditText(getContext());
                    linearLayout.addView(newServiceOperations);
                    final TextView text2 = new TextView(getContext());
                    text2.setText("Dodatkowe pola wpisz nazwa+jednostka;nazwa+jednostka ... np. L1+V;L2+V;L3+V");
                    linearLayout.addView(text2);
                    final EditText extendedFields = new EditText(getContext());
                    linearLayout.addView(extendedFields);
                    alert.setView(linearLayout);
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            listOperations.add(newServiceOperations.getText().toString() + " \n@dodatkowePola{" + extendedFields.getText().toString() + "}");
                            currentCategory.setServiceReviewOperationsList(listOperations);
                            try {
                                CategoryRepository.updateCategory(currentCategory);
                                loadDataToListView(ServiceOperationsFragment.this, listView);
                            } catch (Exception e) {
                                MessageFragment messageFragment = new MessageFragment();
                                messageFragment.setMessage(e.getMessage());
                                FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                e.printStackTrace();
                            }
                        }
                    });
                    alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });
                    alert.show();
                } else {
                    Toast.makeText(getActivity(), "Maksymalna liczba czynności serwisowych to " + MAX_NUMBER_SERVICE_OPERATIONS, Toast.LENGTH_SHORT).show();
                }
            }
        });

// LISTVIEW ON CLICK //
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                if (Config.getIsAdmin() == true) {   //access level
                    Object clickItemObj = adapterView.getAdapter().getItem(index);
                    HashMap clickItemMap = (HashMap) clickItemObj;
                    final String header = (String) clickItemMap.get("First Line"); //id
                    String content = (String) clickItemMap.get("Second Line"); //content
                    final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Edycja");
                    alert.setMessage("id : " + header);
                    LinearLayout linearLayout = new LinearLayout(getContext());
                    linearLayout.setPadding(40, 0, 40, 0);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    final TextView text1 = new TextView(getContext());
                    text1.setText("Nazwa");
                    linearLayout.addView(text1);
                    final EditText serviceOperationName = new EditText(getContext());
                    serviceOperationName.setText(content.replaceAll("\n@dodatkowePola.*", ""));
                    linearLayout.addView(serviceOperationName);
                    final TextView text2 = new TextView(getContext());
                    text2.setText("Dodatkowe pola wpisz nazwa+jednostka;nazwa+jednostka ... np. L1+V;L2+V;L3+V");
                    linearLayout.addView(text2);
                    final EditText extendedFields = new EditText(getContext());
                    String filteredContent = content.replaceAll(".*\n@dodatkowePola", "");
                    extendedFields.setText(filteredContent.substring(1, filteredContent.length() - 1));
                    linearLayout.addView(extendedFields);
                    alert.setView(linearLayout);
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            listOperations.set(Integer.parseInt(header) - 1, serviceOperationName.getText().toString() + " \n@dodatkowePola{" + extendedFields.getText().toString() + "}");
                            currentCategory.setServiceReviewOperationsList(listOperations);
                            try {
                                CategoryRepository.updateCategory(currentCategory);
                                loadDataToListView(ServiceOperationsFragment.this, listView);
                            } catch (Exception e) {
                                MessageFragment messageFragment = new MessageFragment();
                                messageFragment.setMessage(e.getMessage());
                                FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                e.printStackTrace();
                            }
                        }
                    });

                    alert.setNeutralButton("Usuń", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            listOperations.remove(Integer.parseInt(header) - 1);
                            currentCategory.setServiceReviewOperationsList(listOperations);
                            try {
                                CategoryRepository.updateCategory(currentCategory);
                                loadDataToListView(ServiceOperationsFragment.this, listView);
                            } catch (Exception e) {
                                MessageFragment messageFragment = new MessageFragment();
                                messageFragment.setMessage(e.getMessage());
                                FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                e.printStackTrace();
                            }
                        }
                    });
                    alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });
                    alert.show();
                }
            }
        });

        return v;
    }

    private void loadDataToListView(ServiceOperationsFragment serviceOperationsFragment, ListView listView) {
        try {
//            ArrayList<String> list = CategoryDao.findAllServiceOperationsForListView(currentCategory);
            Category category = CategoryRepository.findCategoryById(currentCategory.getId());
            ArrayList<String> list = category.getServiceReviewOperationsList();


            if (list != null) {
                listOperations.clear();
                listOperations.addAll(list);
                List<Map<String, String>> data = new ArrayList<Map<String, String>>();
                List<Map<String, String>> operationsData = new ArrayList<Map<String, String>>();
                for (String listOperation : listOperations) {
                    Map<String, String> row = new HashMap<String, String>(2);
                    row.put("First Line", (listOperations.indexOf(listOperation) + 1) + "");
                    row.put("Second Line", listOperation);
                    operationsData.add(row);
                }
                List<Map<String, String>> filteredData = new ArrayList<Map<String, String>>();
                for (Map<String, String> datum : operationsData) {
                    filteredData.add(datum);
                }
                SimpleAdapter adapter = new SimpleAdapter(serviceOperationsFragment.getContext(), filteredData,
                        android.R.layout.simple_list_item_2,
                        new String[]{"First Line", "Second Line"},
                        new int[]{android.R.id.text1, android.R.id.text2});
                listView.setAdapter(adapter);
            }
        } catch (Exception e) {
            MessageFragment messageFragment = new MessageFragment();
            messageFragment.setMessage(e.getMessage());
            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
            e.printStackTrace();
        }
    }
}
