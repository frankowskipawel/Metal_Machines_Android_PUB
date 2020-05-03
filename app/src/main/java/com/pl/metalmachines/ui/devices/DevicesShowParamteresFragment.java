package com.pl.metalmachines.ui.devices;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.pl.metalmachines.R;
import com.pl.metalmachines.dao.ParametersDao;
import com.pl.metalmachines.dao.UtilsDao;
import com.pl.metalmachines.dao.enumDao.columnsNames.ParametersColumnNameDAO;
import com.pl.metalmachines.model.Device;
import com.pl.metalmachines.model.parametersDevice.ParametersDevice;
import com.pl.metalmachines.model.parametersDevice.StringParameters;
import com.pl.metalmachines.model.parametersDevice.ciecie.Ciecie;
import com.pl.metalmachines.model.parametersDevice.przebijanie.Przebijanie;
import com.pl.metalmachines.model.parametersDevice.wpalenie.Wpalenie;
import com.pl.metalmachines.ui.devices.tabfragment.Tab1Fragment;
import com.pl.metalmachines.ui.devices.tabfragment.Tab2Fragment;
import com.pl.metalmachines.ui.devices.tabfragment.Tab3Fragment;
import com.pl.metalmachines.ui.devices.tabfragment.TabAdapter;
import com.pl.metalmachines.ui.info.MessageFragment;
import com.pl.metalmachines.utils.DialogUtils;
import com.pl.metalmachines.utils.FragmentUtils;
import com.pl.metalmachines.utils.ParametersUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import static android.content.ContentValues.TAG;


public class DevicesShowParamteresFragment extends Fragment {

    private Fragment previousFragment;
    private Device currentDevice;
    private StringParameters currentStringParameters;
    private List<String> listMaterial, listgrubosc, listModelDyszy, listGaz;
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button addMaterialButton, addGruboscButton, addModelDyszyButton, addGazButton, editParametersButton, addAndSaveParametersButton, deleteParametersButton, listParametersButton;
    private Spinner spinnerMaterial, spinnerGrubosc, spinnerModelDyszy, spinnerGaz;
    private Tab1Fragment fragment1;
    private Tab2Fragment fragment2;
    private Tab3Fragment fragment3;
    private ArrayList<ParametersDevice> listParameters;
    Mode currentMode = Mode.SHOW_PARAMETERS;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_devices_show_parameters, container, false);

        try {
            addMaterialButton = v.findViewById(R.id.add_meterial_device_details_Fragment);
            addGruboscButton = v.findViewById(R.id.add_grubosc_device_details_Fragment);
            addModelDyszyButton = v.findViewById(R.id.add_model_dyszy_device_details_Fragment);
            addGazButton = v.findViewById(R.id.add_gaz_device_details_Fragment);
            spinnerMaterial = (Spinner) v.findViewById(R.id.material_spinner);
            spinnerGrubosc = (Spinner) v.findViewById(R.id.grubosc_spinner);
            spinnerModelDyszy = (Spinner) v.findViewById(R.id.model_dyszy_spinner);
            spinnerGaz = (Spinner) v.findViewById(R.id.gaz_spinner);
            listParameters = ParametersDao.getAllParameters(currentDevice.id);
            viewPager = (ViewPager) v.findViewById(R.id.viewPager);
            tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);
            adapter = new TabAdapter(getFragmentManager());
            fragment1 = new Tab1Fragment();
            fragment2 = new Tab2Fragment();
            fragment3 = new Tab3Fragment();
            adapter.addFragment(fragment1, "Cięcie");
            adapter.addFragment(fragment2, "Przebijanie");
            adapter.addFragment(fragment3, "Wpalenie");
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
            editParametersButton = (Button) v.findViewById(R.id.edit_parameters_device_show_parameters_Fragment);
            deleteParametersButton = (Button) v.findViewById(R.id.delete_device_show_parameters_Fragment);
            addAndSaveParametersButton = (Button) v.findViewById(R.id.add_and_save_parameters_device_show_parameters_Fragment);
            if (UtilsDao.getIsAdmin() == false) {//access level
                editParametersButton.setVisibility(View.INVISIBLE);
                deleteParametersButton.setVisibility(View.INVISIBLE);
                addAndSaveParametersButton.setVisibility(View.INVISIBLE);
            }
            addMaterialButton.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            addGruboscButton.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            addModelDyszyButton.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            addGazButton.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            loadSpinner();

// SPINNER MATERIAL //
            spinnerMaterial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    String msupplier = spinnerMaterial.getSelectedItem().toString();
                    if (currentMode == Mode.SHOW_PARAMETERS) {
                        loadParametersAfterSpinnerChoice();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
// SPINNER GRUBOSC //
            spinnerGrubosc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    if (currentMode == Mode.SHOW_PARAMETERS) {
                        loadParametersAfterSpinnerChoice();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
// SPINNER MODEL DYSZY //
            spinnerModelDyszy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    String msupplier = spinnerMaterial.getSelectedItem().toString();
                    if (currentMode == Mode.SHOW_PARAMETERS) {
                        loadParametersAfterSpinnerChoice();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
// SPINNER GAZ //
            spinnerGaz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    String msupplier = spinnerMaterial.getSelectedItem().toString();
                    if (currentMode == Mode.SHOW_PARAMETERS) {
                        loadParametersAfterSpinnerChoice();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });

// ADD NAME DEVICE TO TOOLBAR //
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("#" + currentDevice.getId() + " " + currentDevice.getName());
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(currentDevice.getSourcePower() + " (" + currentDevice.getCustomerName() + ")");

// PREVIOUS BUTTON //
            final Button previousButton = v.findViewById(R.id.prev_devices_show_parameters_fragment);
            previousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentMode != Mode.SHOW_PARAMETERS) {
                        setCurrentMode(Mode.SHOW_PARAMETERS);
                    } else {
                        getFragmentManager().popBackStack();
                    }
                }
            });

// LIST PARAMETERS BUTTON //
            listParametersButton = v.findViewById(R.id.list_parameters_button_show_parameters_device_fragment);
            listParametersButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "++++++++++++++++++++++++++++");
                    final AlertDialog alert = new AlertDialog.Builder(getContext()).create();
                    alert.setTitle("LISTA DOSTĘPNYCH PARAMETRÓW");

                    /// CREATE LIST VIEW
                    final ListView listView = new ListView(getContext());
                    List<ParametersDevice> parameters = listParameters;

                    List<Map<String, String>> parametersData = new ArrayList<Map<String, String>>();
                    for (ParametersDevice device : parameters) {
                        Map<String, String> row = new HashMap<String, String>(2);
                        row.put("First Line", device.getMaterial() + "/" + device.getGrubosc() + "/" + device.getModelDyszy() + "/" + device.getGaz());
                        parametersData.add(row);
                        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
                        data = parametersData;
                        List<Map<String, String>> filteredData = new ArrayList<Map<String, String>>();
                        for (Map<String, String> datum : data) {
                            filteredData.add(datum);
                        }
                        SimpleAdapter adapter = new SimpleAdapter(listView.getContext(), filteredData,
                                android.R.layout.simple_list_item_1,
                                new String[]{"First Line"},
                                new int[]{android.R.id.text1});
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Object clickItemObj = parent.getAdapter().getItem(position);
                                HashMap clickItemMap = (HashMap) clickItemObj;
                                String selcetedItem = (String) clickItemMap.get("First Line");
                                Log.i("selected ", selcetedItem);
                                String[] parametersTab = selcetedItem.split("/");

                                ///// SET SPINNERS
                                for (int i = 0; i < spinnerMaterial.getAdapter().getCount(); i++) {
                                    if (spinnerMaterial.getItemAtPosition(i).equals(parametersTab[0])) {
                                        spinnerMaterial.setSelection(i);
                                    }
                                }
                                for (int i = 0; i < spinnerGrubosc.getAdapter().getCount(); i++) {
                                    if (spinnerGrubosc.getItemAtPosition(i).equals(parametersTab[1])) {
                                        spinnerGrubosc.setSelection(i);
                                    }
                                }
                                for (int i = 0; i < spinnerModelDyszy.getAdapter().getCount(); i++) {
                                    if (spinnerModelDyszy.getItemAtPosition(i).equals(parametersTab[2])) {
                                        spinnerModelDyszy.setSelection(i);
                                    }
                                }
                                for (int i = 0; i < spinnerGaz.getAdapter().getCount(); i++) {
                                    if (spinnerGaz.getItemAtPosition(i).equals(parametersTab[3])) {
                                        spinnerGaz.setSelection(i);
                                    }
                                }
                                alert.dismiss();
                            }
                        });

                    }
                    alert.setView(listView);
                    alert.show();
                }
            });

// ADD AND SAVE BUTTON //
            addAndSaveParametersButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //SET ADD NEW PARAMETERS MODE
                    if (currentMode == Mode.SHOW_PARAMETERS) {
                        setCurrentMode(Mode.ADD_NEW_PARAMETERS);
                        ///// SAVE PARAMETERS
                    } else if (currentMode == Mode.ADD_NEW_PARAMETERS) {
                        if (fragment1.isResumed()) {
                            fragment1.saveParametersToCiecie();
                        }
                        if (fragment2.isResumed()) {
                            fragment2.saveParametersToPrzebijanie();
                        }
                        if (fragment3.isResumed()) {
                            fragment3.savedParametersToWpalenie();
                        }
                        StringParameters newStringParameters = new StringParameters(fragment1.getCurrentCiecie(), fragment2.getCurrentPrzebijanie(), fragment3.getCurrentWpalenie());
                        final ParametersDevice newParametersDevice = new ParametersDevice(null, currentDevice.getId(), spinnerMaterial.getSelectedItem().toString(), spinnerGrubosc.getSelectedItem().toString(), spinnerModelDyszy.getSelectedItem().toString(), spinnerGaz.getSelectedItem().toString(), newStringParameters);

                        // CHECK isEXIST NEW PARAMETERS IN DATABASE
                        try {
                            if (ParametersDao.findStringParameters(newParametersDevice.getIdDevice(),
                                    newParametersDevice.getMaterial(),
                                    newParametersDevice.getGrubosc(),
                                    newParametersDevice.getModelDyszy(),
                                    newParametersDevice.getGaz()) != null) {

                                Toast.makeText(getActivity(), "Parametry o podanych kryteriach już istnieją", Toast.LENGTH_SHORT).show();
                            } else {

                                // CHECK IS SPINNER CHOICE IS NOT NULL
                                if (spinnerMaterial.getSelectedItem().toString().equals("<none>") ||
                                        spinnerGrubosc.getSelectedItem().toString().equals("<none>") ||
                                        spinnerModelDyszy.getSelectedItem().toString().equals("<none>") ||
                                        spinnerGaz.getSelectedItem().toString().equals("<none>")) {
                                    Toast.makeText(getActivity(), "Wybierz wszystkie parametry bazowe", Toast.LENGTH_SHORT).show();

                                } else {

                                    // CHECK IS ALL EDIT TEXT IN NOT NULL
                                    // NOT NULL
                                    if (isAllEditTextAreNotEmpty()) {
                                        try {
                                            ParametersDao.insertParameters(newParametersDevice);
                                            Toast.makeText(getActivity(), "Zapisano parametry", Toast.LENGTH_SHORT).show();
                                            listParameters = ParametersDao.getAllParameters(currentDevice.id);
                                            setCurrentMode(Mode.SHOW_PARAMETERS);
                                        } catch (Exception e) {
                                            MessageFragment messageFragment = new MessageFragment();
                                            messageFragment.setMessage(e.getMessage());
                                            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                            e.printStackTrace();
                                        }
                                    } else {  //EXIST NULL

                                        new AlertDialog.Builder(getContext())
                                                .setTitle("Nie podano wszystkich parametrów!")
                                                .setMessage("Czy nepewno chcesz zapisać?\n" + "?")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // continue with OK
                                                        try {
                                                            ParametersDao.insertParameters(newParametersDevice);
                                                            Toast.makeText(getActivity(), "Zapisano", Toast.LENGTH_SHORT).show();
                                                            listParameters = ParametersDao.getAllParameters(currentDevice.id);
                                                            setCurrentMode(Mode.SHOW_PARAMETERS);
                                                        } catch (SQLException e) {
                                                            MessageFragment messageFragment = new MessageFragment();
                                                            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                                            messageFragment.setMessage(getString(R.string.no_connection) + "\n\n" + e.getMessage());
                                                            e.printStackTrace();
                                                        } catch (ClassNotFoundException e) {
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
                                }
                            }
                        } catch (Exception e) {
                            MessageFragment messageFragment = new MessageFragment();
                            messageFragment.setMessage(e.getMessage());
                            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                            e.printStackTrace();
                        }
                    } else if (currentMode == Mode.ADD_NEW_PARAMETERS) {
                        setCurrentMode(Mode.SHOW_PARAMETERS);
                    }
                    // SAVE AFTER EDIT MODE //
                    if (currentMode == Mode.EDIT_PARAMETERS) {
                        if (fragment1.isResumed()) {
                            fragment1.saveParametersToCiecie();
                        }
                        if (fragment2.isResumed()) {
                            fragment2.saveParametersToPrzebijanie();
                        }
                        if (fragment3.isResumed()) {
                            fragment3.savedParametersToWpalenie();
                        }
                        StringParameters newStringParameters = new StringParameters(fragment1.getCurrentCiecie(), fragment2.getCurrentPrzebijanie(), fragment3.getCurrentWpalenie());
                        final ParametersDevice newParametersDevice = new ParametersDevice("", currentDevice.getId(), spinnerMaterial.getSelectedItem().toString(), spinnerGrubosc.getSelectedItem().toString(), spinnerModelDyszy.getSelectedItem().toString(), spinnerGaz.getSelectedItem().toString(), newStringParameters);
                        if (isAllEditTextAreNotEmpty()) {
                            try {
                                ParametersDao.updateParameters(newParametersDevice);
                                setCurrentMode(Mode.SHOW_PARAMETERS);
                                listParameters = ParametersDao.getAllParameters(currentDevice.id);
                            } catch (Exception e) {
                                MessageFragment messageFragment = new MessageFragment();
                                messageFragment.setMessage(e.getMessage());
                                FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                e.printStackTrace();
                            }
                            Toast.makeText(getActivity(), "Zapisano parametry", Toast.LENGTH_SHORT).show();
                        } else {  //EXIST NULL
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Nie podano wszystkich parametrów!")
                                    .setMessage("Czy nepewno chcesz zapisać?\n" + "?")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with OK
                                            try {
                                                ParametersDao.updateParameters(newParametersDevice);
                                                listParameters = ParametersDao.getAllParameters(currentDevice.id);
                                                Toast.makeText(getActivity(), "Zapisano", Toast.LENGTH_SHORT).show();
                                                setCurrentMode(Mode.SHOW_PARAMETERS);
                                            } catch (SQLException e) {
                                                MessageFragment messageFragment = new MessageFragment();
                                                FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                                messageFragment.setMessage(getString(R.string.no_connection) + "\n\n" + e.getMessage());
                                                e.printStackTrace();
                                            } catch (ClassNotFoundException e) {
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
                    }
                }
            });

// EDIT PARAMETERS BUTTON //
            editParametersButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!spinnerMaterial.getSelectedItem().toString().equals("<none>") &&
                            !spinnerGaz.getSelectedItem().toString().equals("<none>") &&
                            !spinnerModelDyszy.getSelectedItem().toString().equals("<none>") &&
                            !spinnerGrubosc.getSelectedItem().toString().equals("<none>")) {
                        StringParameters stringParameters;
                        try {
                            stringParameters = ParametersDao.findStringParameters(currentDevice.id, spinnerMaterial.getSelectedItem().toString(),
                                    spinnerGrubosc.getSelectedItem().toString(),
                                    spinnerModelDyszy.getSelectedItem().toString(),
                                    spinnerGaz.getSelectedItem().toString()
                            );
                            if (stringParameters == null) {
                                Toast.makeText(getActivity(), "Brak parametrów", Toast.LENGTH_SHORT).show();
                                setBlankParameters();
                            } else {
                                setCurrentMode(Mode.EDIT_PARAMETERS);
                            }
                        } catch (Exception e) {
                            MessageFragment messageFragment = new MessageFragment();
                            messageFragment.setMessage(e.getMessage());
                            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                            e.printStackTrace();
                        }
                    }
                }
            });

// DELETE PARAMETERS BUTTON //
            deleteParametersButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isParametersExist()) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Potwierdzenie operacji")
                                .setMessage("Czy nepewno chcesz usunąć wskazane parametry?")
                                .setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                        ParametersDevice deleteParameters = new ParametersDevice("",
                                                currentDevice.getId(),
                                                spinnerMaterial.getSelectedItem().toString(),
                                                spinnerGrubosc.getSelectedItem().toString(),
                                                spinnerModelDyszy.getSelectedItem().toString(),
                                                spinnerGaz.getSelectedItem().toString(),
                                                null
                                        );
                                        try {
                                            ParametersDao.deleteParameters(deleteParameters);
                                            loadSpinner();
                                            setEmptyEditTextinFragments();
                                            listParameters = ParametersDao.getAllParameters(currentDevice.id);
                                            Toast.makeText(getActivity(), "Usunięto  parametry", Toast.LENGTH_SHORT).show();
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
                }
            });

// ADD MATERIAL BUTTON //
            addMaterialButton = (Button) v.findViewById(R.id.add_meterial_device_details_Fragment);
            addMaterialButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        DialogUtils.getTextViewDialog(getContext(), "Dodaj nowy materiał", "", ParametersColumnNameDAO.PARAMETERS.material, listMaterial, spinnerMaterial, false);
                    } catch (Exception e) {
                        MessageFragment messageFragment = new MessageFragment();
                        messageFragment.setMessage(e.getMessage());
                        FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                        e.printStackTrace();
                    }
                }
            });

// ADD GRUBOSC BUTTON //
            addGruboscButton = (Button) v.findViewById(R.id.add_grubosc_device_details_Fragment);
            addGruboscButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        DialogUtils.getTextViewDialog(getContext(), "Dodaj nową grubość", "", ParametersColumnNameDAO.PARAMETERS.grubosc, listgrubosc, spinnerGrubosc, true);
                    } catch (Exception e) {
                        MessageFragment messageFragment = new MessageFragment();
                        messageFragment.setMessage(e.getMessage());
                        FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                        e.printStackTrace();
                    }
                }
            });

// ADD MODEL DYSZY BUTTON //
            addModelDyszyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        DialogUtils.getTextViewDialog(getContext(), "Dodaj nowy model dyszy", "", ParametersColumnNameDAO.PARAMETERS.modelDyszy, listModelDyszy, spinnerModelDyszy, false);
                    } catch (Exception e) {
                        MessageFragment messageFragment = new MessageFragment();
                        messageFragment.setMessage(e.getMessage());
                        FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                        e.printStackTrace();
                    }
                }
            });

// ADD GAZ BUTTON //
            addGazButton = (Button) v.findViewById(R.id.add_gaz_device_details_Fragment);
            addGazButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        DialogUtils.getTextViewDialog(getContext(), "Dodaj nowy gaz", "", ParametersColumnNameDAO.PARAMETERS.gaz, listGaz, spinnerGaz, false);
                    } catch (Exception e) {
                        MessageFragment messageFragment = new MessageFragment();
                        messageFragment.setMessage(e.getMessage());
                        FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            MessageFragment messageFragment = new MessageFragment();
            messageFragment.setMessage(e.getMessage());
            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
            e.printStackTrace();
        }
        return v;
    }

    private void loadSpinner() {
        HashMap<String, TreeSet<String>> mapSpinnerParameters = new HashMap();
        mapSpinnerParameters = getAllSpinnerParameters(listParameters);
        listMaterial = new ArrayList<String>();
        listMaterial.add("<none>");
        listMaterial.addAll(mapSpinnerParameters.get(ParametersColumnNameDAO.PARAMETERS.material));
        listgrubosc = new ArrayList<String>();
        listgrubosc.add("<none>");
        listgrubosc.addAll(mapSpinnerParameters.get(ParametersColumnNameDAO.PARAMETERS.grubosc));
        listModelDyszy = new ArrayList<String>();
        listModelDyszy.add("<none>");
        listModelDyszy.addAll(mapSpinnerParameters.get(ParametersColumnNameDAO.PARAMETERS.modelDyszy));
        listGaz = new ArrayList<String>();
        listGaz.add("<none>");
        listGaz.addAll(mapSpinnerParameters.get(ParametersColumnNameDAO.PARAMETERS.gaz));

        ArrayAdapter<String> materialAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, listMaterial);
        materialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaterial.setAdapter(materialAdapter);

        ArrayAdapter<String> gruboscAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, listgrubosc);
        gruboscAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrubosc.setAdapter(gruboscAdapter);

        ArrayAdapter<String> modelDyszyAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, listModelDyszy);
        modelDyszyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModelDyszy.setAdapter(modelDyszyAdapter);

        ArrayAdapter<String> gazAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, listGaz);
        gazAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGaz.setAdapter(gazAdapter);
    }


    private void loadParametersAfterSpinnerChoice() {
        if (currentMode == Mode.SHOW_PARAMETERS) {
            if (isParametersExist()) {
                Toast.makeText(getActivity(), "Znaleziono parametry", Toast.LENGTH_SHORT).show();
                fragment1.setCurrentCiecie(currentStringParameters.getCiecie());
                if (fragment1.isResumed()) {
                    fragment1.loadParametersToTextView();
                }
                fragment2.setCurrentPrzebijanie(currentStringParameters.getPrzebijanie());
                if (fragment2.isResumed()) {
                    fragment2.loadParametersToTextView();
                }
                fragment3.setCurrentWpalenie(currentStringParameters.getWpalenie());
                if (fragment3.isResumed()) {
                    fragment3.loadParametersToTextView();
                }
            }
        } else {
            setBlankParameters();
        }
    }

    private HashMap<String, TreeSet<String>> getAllSpinnerParameters(ArrayList<ParametersDevice> listParameters) {
        HashMap<String, TreeSet<String>> mapSpinnerParameters = new HashMap();
        TreeSet<String> gruboscTreeSet = new TreeSet<>();
        TreeSet<String> materialTreeSet = new TreeSet<>();
        TreeSet<String> modelDyszyTreeSet = new TreeSet<>();
        TreeSet<String> gazTreeSet = new TreeSet<>();

        for (ParametersDevice listParameter : listParameters) {

            String grubosc = listParameter.getGrubosc().toString();
            String material = listParameter.getMaterial();
            String modelDyszy = listParameter.getModelDyszy();
            String gaz = listParameter.getGaz();

            gruboscTreeSet.add(grubosc);
            materialTreeSet.add(material);
            modelDyszyTreeSet.add(modelDyszy);
            gazTreeSet.add(gaz);
        }
        mapSpinnerParameters.put(ParametersColumnNameDAO.PARAMETERS.grubosc, gruboscTreeSet);
        mapSpinnerParameters.put(ParametersColumnNameDAO.PARAMETERS.material, materialTreeSet);
        mapSpinnerParameters.put(ParametersColumnNameDAO.PARAMETERS.modelDyszy, modelDyszyTreeSet);
        mapSpinnerParameters.put(ParametersColumnNameDAO.PARAMETERS.gaz, gazTreeSet);

        return mapSpinnerParameters;
    }

    public void setEmptyEditTextinFragments() {
        fragment1.setCurrentCiecie(ParametersUtils.getBlankParametersDevice().getCiecie());
        fragment2.setCurrentPrzebijanie(ParametersUtils.getBlankParametersDevice().getPrzebijanie());
        fragment3.setCurrentWpalenie(ParametersUtils.getBlankParametersDevice().getWpalenie());
        if (fragment1.isResumed()) {
            fragment1.loadParametersToTextView();
        }
        if (fragment2.isResumed()) {
            fragment2.loadParametersToTextView();
        }
        if (fragment3.isResumed()) {
            fragment3.loadParametersToTextView();
        }
    }

    public void setCurrentMode(Mode currentMode) {

        switch (currentMode) {
            case SHOW_PARAMETERS:
                this.currentMode = Mode.SHOW_PARAMETERS;
                addMaterialButton.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                addGruboscButton.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                addModelDyszyButton.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                addGazButton.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                spinnerMaterial.setEnabled(true);
                spinnerGrubosc.setEnabled(true);
                spinnerModelDyszy.setEnabled(true);
                spinnerGaz.setEnabled(true);
                fragment1.setCurrentMode(Mode.SHOW_PARAMETERS);
                fragment2.setCurrentMode(Mode.SHOW_PARAMETERS);
                fragment3.setCurrentMode(Mode.SHOW_PARAMETERS);
                if (fragment1.isResumed()) {
                    fragment1.loadParametersToTextView();
                    fragment1.setControlsOnMode();
                }
                if (fragment2.isResumed()) {
                    fragment2.loadParametersToTextView();
                    fragment2.setControlsByMode();
                }
                if (fragment3.isResumed()) {
                    fragment3.loadParametersToTextView();
                    fragment3.setControlsOnMode();
                }
                if (UtilsDao.getIsAdmin() == true) { //access level
                    editParametersButton.setVisibility(View.VISIBLE);
                    deleteParametersButton.setVisibility(View.VISIBLE);
                    listParametersButton.setVisibility(View.VISIBLE);
                }
                addAndSaveParametersButton.setText("dodaj");
                break;

            case EDIT_PARAMETERS:
                this.currentMode = Mode.EDIT_PARAMETERS;
                addMaterialButton.setEnabled(false);
                addGruboscButton.setEnabled(false);
                addModelDyszyButton.setEnabled(false);
                addGazButton.setEnabled(false);
                spinnerMaterial.setEnabled(false);
                spinnerGrubosc.setEnabled(false);
                spinnerModelDyszy.setEnabled(false);
                spinnerGaz.setEnabled(false);

                fragment1.setCurrentMode(Mode.EDIT_PARAMETERS);
                fragment2.setCurrentMode(Mode.EDIT_PARAMETERS);
                fragment3.setCurrentMode(Mode.EDIT_PARAMETERS);
                if (fragment1.isResumed()) {
                    fragment1.loadParametersToTextView();
                    fragment1.setControlsOnMode();
                }
                if (fragment2.isResumed()) {
                    fragment2.loadParametersToTextView();
                    fragment2.setControlsByMode();
                }
                if (fragment3.isResumed()) {
                    fragment3.loadParametersToTextView();
                    fragment3.setControlsOnMode();
                }
                editParametersButton.setVisibility(View.INVISIBLE);
                deleteParametersButton.setVisibility(View.INVISIBLE);
                listParametersButton.setVisibility(View.INVISIBLE);
                addAndSaveParametersButton.setText("zapisz");
                break;

            case ADD_NEW_PARAMETERS:
                this.currentMode = Mode.ADD_NEW_PARAMETERS;
                spinnerMaterial.setSelection(0);
                spinnerGrubosc.setSelection(0);
                spinnerModelDyszy.setSelection(0);
                spinnerGaz.setSelection(0);
                spinnerMaterial.setEnabled(true);
                spinnerGrubosc.setEnabled(true);
                spinnerModelDyszy.setEnabled(true);
                spinnerGaz.setEnabled(true);
                addMaterialButton.setEnabled(true);
                addGruboscButton.setEnabled(true);
                addModelDyszyButton.setEnabled(true);
                addGazButton.setEnabled(true);
                int factor = (int) getContext().getResources().getDisplayMetrics().density;
                addMaterialButton.setLayoutParams(new LinearLayout.LayoutParams(30* factor, 30* factor));
                addGruboscButton.setLayoutParams(new LinearLayout.LayoutParams(30* factor, 30* factor));
                addModelDyszyButton.setLayoutParams(new LinearLayout.LayoutParams(30* factor, 30* factor));
                addGazButton.setLayoutParams(new LinearLayout.LayoutParams(30* factor, 30* factor));

                setBlankParameters();
                fragment1.setCurrentMode(Mode.ADD_NEW_PARAMETERS);
                fragment2.setCurrentMode(Mode.ADD_NEW_PARAMETERS);
                fragment3.setCurrentMode(Mode.ADD_NEW_PARAMETERS);
                if (fragment1.isResumed()) {
                    fragment1.setControlsOnMode();
                }
                if (fragment2.isResumed()) {
                    fragment2.setControlsByMode();
                }
                if (fragment3.isResumed()) {
                    fragment3.setControlsOnMode();
                }
                editParametersButton.setVisibility(View.INVISIBLE);
                deleteParametersButton.setVisibility(View.INVISIBLE);
                listParametersButton.setVisibility(View.INVISIBLE);
                addAndSaveParametersButton.setText("zapisz");
                break;
        }
    }

    public void setBlankParameters() {
        fragment1.setCurrentCiecie(ParametersUtils.getBlankParametersDevice().getCiecie());
        fragment2.setCurrentPrzebijanie(ParametersUtils.getBlankParametersDevice().getPrzebijanie());
        fragment3.setCurrentWpalenie(ParametersUtils.getBlankParametersDevice().getWpalenie());
        if (fragment1.isResumed()) {
            fragment1.loadParametersToTextView();
            fragment1.setControlsOnMode();
        }
        if (fragment2.isResumed()) {
            fragment2.loadParametersToTextView();
            fragment2.setControlsByMode();
        }
        if (fragment3.isResumed()) {
            fragment3.loadParametersToTextView();
            fragment3.setControlsOnMode();
        }
    }

    private boolean isParametersExist() {
        for (ParametersDevice listParameter : listParameters) {
            if (listParameter.getMaterial().equals(spinnerMaterial.getSelectedItem().toString()) &&
                    listParameter.getGrubosc().equals(spinnerGrubosc.getSelectedItem().toString()) &&
                    listParameter.getModelDyszy().equals(spinnerModelDyszy.getSelectedItem().toString()) &&
                    listParameter.getGaz().equals(spinnerGaz.getSelectedItem().toString())) {
                currentStringParameters = listParameter.getStringParameters();
            }
        }
        if (!spinnerMaterial.getSelectedItem().toString().equals("<none>") &&
                !spinnerGaz.getSelectedItem().toString().equals("<none>") &&
                !spinnerModelDyszy.getSelectedItem().toString().equals("<none>") &&
                !spinnerGrubosc.getSelectedItem().toString().equals("<none>")) {
            if (currentStringParameters == null) {
                Toast.makeText(getActivity(), "Brak parametrów", Toast.LENGTH_SHORT).show();
                setBlankParameters();
            } else {
                return true;
            }
        } else {
            setBlankParameters();
        }

        return false;
    }


    public boolean isAllEditTextAreNotEmpty() {
        Ciecie ciecie = fragment1.getCurrentCiecie();
        Przebijanie przebijanie = fragment2.getCurrentPrzebijanie();
        Wpalenie wpalenie = fragment3.getCurrentWpalenie();

        if (ciecie.getParametryGlowne().getPredkoscCiecia().equals("")) {
            return false;
        }
        if (ciecie.getParametryGlowne().getWysokoscPodnoszenia().equals("")) {
            return false;
        }
        if (ciecie.getParametryGlowne().getWysokoscCiecia().equals("")) {
            return false;
        }
        if (ciecie.getParametryGlowne().getGazCiecia().equals("")) {
            return false;
        }
        if (ciecie.getParametryGlowne().getCisnienieCiecia().equals("")) {
            return false;
        }
        if (ciecie.getParametryGlowne().getNatezenieCieciaProcent().equals("")) {
            return false;
        }
        if (ciecie.getParametryGlowne().getMocCiecia().equals("")) {
            return false;
        }
        if (ciecie.getParametryGlowne().getCzestotliwoscPrzebiegu().equals("")) {
            return false;
        }
        if (ciecie.getParametryGlowne().getSzerokoscWiazki().equals("")) {
            return false;
        }
        if (ciecie.getParametryGlowne().getFocus().equals("")) {
            return false;
        }
        if (ciecie.getParametryGlowne().getCzasZwloki().equals("")) {
            return false;
        }
        if (ciecie.getParametryGlowne().getOpoznienieWylaczeniaWiazki().equals("")) {
            return false;
        }
        if (ciecie.getRuszWolno().getDlugosc().equals("")) {
            return false;
        }
        if (ciecie.getRuszWolno().getPredkosc().equals("")) {
            return false;
        }
        if (ciecie.getStopWolno().getPredkosc().equals("")) {
            return false;
        }
        if (przebijanie.getEtap1().getCzasKroku().equals("")) {
            return false;
        }
        if (przebijanie.getEtap1().getWysokoscPrzebijania().equals("")) {
            return false;
        }
        if (przebijanie.getEtap1().getGazPrzebijania().equals("")) {
            return false;
        }
        if (przebijanie.getEtap1().getCisnieniePrzebijania().equals("")) {
            return false;
        }
        if (przebijanie.getEtap1().getNatezeniePrzebijaniaProcent().equals("")) {
            return false;
        }
        if (przebijanie.getEtap1().getMocPrzebijania().equals("")) {
            return false;
        }
        if (przebijanie.getEtap1().getCzestotliwoscPrzebijania().equals("")) {
            return false;
        }
        if (przebijanie.getEtap1().getSzerokoscWiazki().equals("")) {
            return false;
        }
        if (przebijanie.getEtap1().getFocusPrzebijania().equals("")) {
            return false;
        }
        if (przebijanie.getEtap1().getCzasPrzebijania().equals("")) {
            return false;
        }
        if (przebijanie.getEtap1().getPrzedmuch().equals("")) {
            return false;
        }
        if (przebijanie.getEtap2().getCzasKroku().equals("")) {
            return false;
        }
        if (przebijanie.getEtap2().getWysokoscPrzebijania().equals("")) {
            return false;
        }
        if (przebijanie.getEtap2().getGazPrzebijania().equals("")) {
            return false;
        }
        if (przebijanie.getEtap2().getCisnieniePrzebijania().equals("")) {
            return false;
        }
        if (przebijanie.getEtap2().getNatezeniePrzebijaniaProcent().equals("")) {
            return false;
        }
        if (przebijanie.getEtap2().getMocPrzebijania().equals("")) {
            return false;
        }
        if (przebijanie.getEtap2().getCzestotliwoscPrzebijania().equals("")) {
            return false;
        }
        if (przebijanie.getEtap2().getSzerokoscWiazki().equals("")) {
            return false;
        }
        if (przebijanie.getEtap2().getFocusPrzebijania().equals("")) {
            return false;
        }
        if (przebijanie.getEtap2().getCzasPrzebijania().equals("")) {
            return false;
        }
        if (przebijanie.getEtap2().getPrzedmuch().equals("")) {
            return false;
        }
        if (przebijanie.getEtap3().getCzasKroku().equals("")) {
            return false;
        }
        if (przebijanie.getEtap3().getWysokoscPrzebijania().equals("")) {
            return false;
        }
        if (przebijanie.getEtap3().getGazPrzebijania().equals("")) {
            return false;
        }
        if (przebijanie.getEtap3().getCisnieniePrzebijania().equals("")) {
            return false;
        }
        if (przebijanie.getEtap3().getNatezeniePrzebijaniaProcent().equals("")) {
            return false;
        }
        if (przebijanie.getEtap3().getMocPrzebijania().equals("")) {
            return false;
        }
        if (przebijanie.getEtap3().getCzestotliwoscPrzebijania().equals("")) {
            return false;
        }
        if (przebijanie.getEtap3().getSzerokoscWiazki().equals("")) {
            return false;
        }
        if (przebijanie.getEtap3().getFocusPrzebijania().equals("")) {
            return false;
        }
        if (przebijanie.getEtap3().getCzasPrzebijania().equals("")) {
            return false;
        }
        if (przebijanie.getEtap3().getPrzedmuch().equals("")) {
            return false;
        }
        if (wpalenie.getParametryWpalenia().getPredkoscCiecia().equals("")) {
            return false;
        }
        if (wpalenie.getParametryWpalenia().getCyklPracy().equals("")) {
            return false;
        }
        if (wpalenie.getParametryWpalenia().getCzestotliwosc().equals("")) {
            return false;
        }
        if (wpalenie.getWpalajWolno().getWysokoscWpalania().equals("")) {
            return false;
        }
        if (wpalenie.getWpalajWolno().getStabilnaOdleglosc().equals("")) {
            return false;
        }
        if (wpalenie.getWpalajKolem().getPredkosc().equals("")) {
            return false;
        }
        return true;
    }

    public void setPreviousFragment(Fragment previousFragment) {
        this.previousFragment = previousFragment;
    }

    public void setCurrentDevice(Device currentDevice) {
        this.currentDevice = currentDevice;
    }
}