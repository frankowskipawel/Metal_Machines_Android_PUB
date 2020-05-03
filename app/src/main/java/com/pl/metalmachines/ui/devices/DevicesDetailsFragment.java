package com.pl.metalmachines.ui.devices;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.pl.metalmachines.R;
import com.pl.metalmachines.dao.CustomerDao;
import com.pl.metalmachines.dao.DeviceDao;
import com.pl.metalmachines.dao.DeviceNoteDao;
import com.pl.metalmachines.dao.UtilsDao;
import com.pl.metalmachines.model.Device;
import com.pl.metalmachines.model.DeviceNote;
import com.pl.metalmachines.ui.devices.service.DevicesServiceListFragment;
import com.pl.metalmachines.ui.info.MessageFragment;
import com.pl.metalmachines.utils.DeviceUtils;
import com.pl.metalmachines.utils.FragmentUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class DevicesDetailsFragment extends Fragment {
    private static final int FILE_SELECT_CODE = 0;
    private Fragment previousFragment;
    private Device currentDevice;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_devices_details, container, false);

        final TextView headerTextView = v.findViewById(R.id.header_device_deatils_textView);
        final TextView contentTextView = v.findViewById(R.id.content_device_details_textView);

        final Button deleteButton = v.findViewById(R.id.delete_button_device_details_fragment);
        Button editButton = (Button) v.findViewById(R.id.edit_button_device_details_fragment);
        if (UtilsDao.getIsAdmin() == false) {
            deleteButton.setVisibility(View.INVISIBLE);
            editButton.setVisibility(View.INVISIBLE);
        }

        headerTextView.setText("#" + currentDevice.getId() + " " + currentDevice.getName());
        contentTextView.setText(DeviceUtils.getDeviceContentAtString(currentDevice));

        final ListView noteListView = v.findViewById(R.id.notes_list_device_details_fragment);
        try {
            loadDataToListView(DevicesDetailsFragment.this, noteListView);

            if (UtilsDao.getIsAdmin() == true) { //access level
                noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Object clickItemObj = parent.getAdapter().getItem(position);
                        HashMap clickItemMap = (HashMap) clickItemObj;
                        final String header = (String) clickItemMap.get("First Line");
                        final String content = (String) clickItemMap.get("Second Line");

                        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle("Nowa notatka");
                        LinearLayout linearLayout = new LinearLayout(getContext());
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        linearLayout.setPadding(10, 0, 10, 0);
                        final TextView textView = new TextView(getContext());
                        textView.setText(header);
                        final EditText editText = new EditText(getContext());
                        editText.setText(content);
                        linearLayout.addView(textView);
                        linearLayout.addView(editText);
                        alert.setView(linearLayout);
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                DeviceNote deviceNote = new DeviceNote(header, currentDevice.getId(), editText.getText().toString());
                                try {
                                    DeviceNoteDao.updateDeviceNote(deviceNote);
                                    loadDataToListView(DevicesDetailsFragment.this, noteListView);
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

                        alert.setNeutralButton("Usuń", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    DeviceNoteDao.deleteDeviceNote(new DeviceNote(header, currentDevice.getId(), null));
                                    loadDataToListView(DevicesDetailsFragment.this, noteListView);
                                } catch (Exception e) {
                                    MessageFragment messageFragment = new MessageFragment();
                                    messageFragment.setMessage(e.getMessage());
                                    FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                    e.printStackTrace();
                                }
                            }
                        });
                        alert.show();
                    }
                });
            }
        } catch (Exception e) {
            MessageFragment messageFragment = new MessageFragment();
            messageFragment.setMessage(e.getMessage());
            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
            e.printStackTrace();
        }
// ADD NAME TO TOOLBAR //
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Szczegóły maszyny");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");

// PREVIOUS BUTTON //
        final Button previousButton = v.findViewById(R.id.preview_device_details_fragment);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

// DELETE BUTTON //
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Potwierdzenie operacji")
                        .setMessage("Czy nepewno chcesz usunąć\n" + currentDevice.getName() + "?")
                        .setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                try {
                                    DeviceDao.deleteDevice(currentDevice);
                                    getFragmentManager().popBackStack();
                                    Toast.makeText(getActivity(), "Usunięto  : " + currentDevice.getName(), Toast.LENGTH_SHORT).show();
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

// EDIT BUTTON //
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DevicesEditFragment devicesEditFragment = new DevicesEditFragment();
                devicesEditFragment.setCurrentDevice(currentDevice);
                devicesEditFragment.setPreviousFragment(DevicesDetailsFragment.this);
                FragmentUtils.replaceFragment(devicesEditFragment, getFragmentManager());
            }
        });

// PARAMETERS BUTTON //
        Button parametersButton = (Button) v.findViewById(R.id.parameters_button_device_details_fragment);
        parametersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "currentDevice: "+currentDevice );
                DevicesShowParamteresFragment devicesShowParamteresFragment = new DevicesShowParamteresFragment();
                devicesShowParamteresFragment.setCurrentDevice(currentDevice);
                devicesShowParamteresFragment.setPreviousFragment(DevicesDetailsFragment.this);
                FragmentUtils.replaceFragment(devicesShowParamteresFragment, getFragmentManager());
            }
        });
// SERVICE BUTTON //
        Button serviceButton = (Button) v.findViewById(R.id.service_button_device_details_fragment);
        serviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DevicesServiceListFragment devicesServiceListFragment = new DevicesServiceListFragment();
                devicesServiceListFragment.setCurrentDevice(currentDevice);
                devicesServiceListFragment.setPreviousFragment(DevicesDetailsFragment.this);
                FragmentUtils.replaceFragment(devicesServiceListFragment, getFragmentManager());
            }
        });

// ADD NEW NOTE BUTTON //
        ImageButton addNewNoteButton = v.findViewById(R.id.add_note_device_details_fragment);
        addNewNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Nowa notatka");
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setPadding(10, 0, 10, 0);
                final EditText editText = new EditText(getContext());
                linearLayout.addView(editText);
                alert.setView(linearLayout);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        DeviceNote deviceNote = new DeviceNote(null, currentDevice.getId(), editText.getText().toString());
                        try {
                            DeviceNoteDao.insertDeviceNote(deviceNote);
                            loadDataToListView(DevicesDetailsFragment.this, noteListView);
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
                    }
                });
                alert.show();
            }
        });

// FILES BUTTON //
        Button filesButton = v.findViewById(R.id.files_button_device_details_fragment);
        filesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DevicesFilesFragment devicesFilesFragment = new DevicesFilesFragment();
                devicesFilesFragment.setCurrentDevice(currentDevice);
                try {
                    devicesFilesFragment.setCurrentCustomer(CustomerDao.findCustomerByShortName(currentDevice.customerName));
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                FragmentUtils.replaceFragment(devicesFilesFragment, getFragmentManager());
            }
        });

        return v;
    }

    private void loadDataToListView(Fragment fragment, ListView listView) throws SQLException, ClassNotFoundException {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        data = DeviceNoteDao.findAllNotesByIdDevice(currentDevice.getId());
        List<Map<String, String>> filteredData = new ArrayList<Map<String, String>>();
        for (Map<String, String> datum : data) {
            filteredData.add(datum);
        }
        SimpleAdapter adapter = new SimpleAdapter(fragment.getContext(), filteredData,

                R.layout.simple_list_item_2_small_header,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.text1, R.id.text2});
        listView.setAdapter(adapter);
    }

    public Fragment getPreviousFragment() {
        return previousFragment;
    }

    public void setPreviousFragment(Fragment previousFragment) {
        this.previousFragment = previousFragment;
    }

    public void setCurrentDevice(Device currentDevice) {
        this.currentDevice = currentDevice;
    }


}
