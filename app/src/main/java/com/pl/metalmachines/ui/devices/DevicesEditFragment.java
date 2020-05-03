package com.pl.metalmachines.ui.devices;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.pl.metalmachines.R;
import com.pl.metalmachines.dao.CategoryDao;
import com.pl.metalmachines.dao.CustomerDao;
import com.pl.metalmachines.dao.DeviceDao;
import com.pl.metalmachines.model.Device;
import com.pl.metalmachines.ui.categories.CategoriesListFragment;
import com.pl.metalmachines.ui.customers.CustomersListFragment;
import com.pl.metalmachines.ui.info.MessageFragment;
import com.pl.metalmachines.utils.DateTimeUtils;
import com.pl.metalmachines.utils.DeviceUtils;
import com.pl.metalmachines.utils.FragmentUtils;


public class DevicesEditFragment extends Fragment {

    private Fragment previousFragment;
    private Device currentDevice = DeviceUtils.getBalnkDevice();
    private Button categoryDevice;
    private Button customerDevice;
    private Button transferDate;
    private TextView idDevice;
    private TextView nameDevice;
    private TextView serialNumber;
    private TextView sourcePower;

    public DevicesEditFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_devices_edit, container, false);

        idDevice = v.findViewById(R.id.id_device_editText);
        nameDevice = v.findViewById(R.id.name_device_edit_text);
        serialNumber = v.findViewById(R.id.serial_number_device_edit_text);
        sourcePower = v.findViewById(R.id.source_power_device_edit_text);
        customerDevice = v.findViewById(R.id.customer_device_button);
        categoryDevice = v.findViewById(R.id.category_device_button);
        transferDate = v.findViewById(R.id.date_device_button);

        loadDataToAllControls();

// ADD NAME TO TOOLBAR //
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Dodaj/Edycja maszyny");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");

// SAVE BUTTON //
        final Button saveButton = (Button) v.findViewById(R.id.save_add_new_devices_fragment);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Device newDevice = null;
                try {
                    newDevice = new Device(null,
                            nameDevice.getText().toString(),
                            serialNumber.getText().toString(),
                            sourcePower.getText().toString(),
                            CustomerDao.findIdCustomerByShortName(customerDevice.getText().toString()),
                            CategoryDao.findIdCategoryByName(categoryDevice.getText().toString()),
                            transferDate.getText().toString()
                    );
                    if (idDevice.getText().toString().equals("")) {
                        Log.i("newDevice", newDevice.toString());
                        DeviceDao.insertDevice(newDevice);
                    } else {
                        DeviceDao.updateDevice(newDevice);
                    }
                    //Hide keyboard
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(container.getWindowToken(), 0);
                    getFragmentManager().popBackStack();
                } catch (Exception e) {
                    MessageFragment messageFragment = new MessageFragment();
                    messageFragment.setMessage(e.getMessage());
                    FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                    e.printStackTrace();
                }
            }
        });

// CUSTOMER CHOICE BUTTON //
        final Button customerChoice = (Button) v.findViewById(R.id.customer_device_button);
        customerChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomersListFragment customersListFragment = new CustomersListFragment();
                customersListFragment.setPreviousFragment(DevicesEditFragment.this);
                FragmentUtils.replaceFragment(customersListFragment, getFragmentManager());
            }
        });

        final Button categoryChoice = (Button) v.findViewById(R.id.category_device_button);

// CATEGORY CHOICE BUTTON //
        categoryChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CategoriesListFragment categoriesListFragment = new CategoriesListFragment();
                categoriesListFragment.setPreviousFragment(DevicesEditFragment.this);
                FragmentUtils.replaceFragment(categoriesListFragment, getFragmentManager());
            }
        });

// DATE PICKER BUTTON //
        final Button datePickerButton = (Button) v.findViewById(R.id.date_device_button);
        datePickerButton.setText(DateTimeUtils.now());
        if (currentDevice != null) {
            datePickerButton.setText(currentDevice.getTransferDate());
        } else {
            datePickerButton.setText(DateTimeUtils.now());
        }
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimeUtils.getDate(getActivity(), datePickerButton, datePickerButton.getText().toString());
            }
        });

// PREVIOUS BUTTON //
        final Button prevButton = (Button) v.findViewById(R.id.prevAddNewDevicesFragment);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return v;
    }

    private void loadDataToAllControls() {
        if (currentDevice != null) {
            idDevice.setText(currentDevice.getId());
            nameDevice.setText(currentDevice.getName());
            serialNumber.setText(currentDevice.getSerialNumber());
            sourcePower.setText(currentDevice.getSourcePower());
            customerDevice.setText(currentDevice.getCustomerName());
            categoryDevice.setText(currentDevice.getCategoryName());
            transferDate.setText(currentDevice.getTransferDate());
        }
    }

    public Object getPreviousFragment() {
        return previousFragment;
    }

    public void setPreviousFragment(Fragment previousFragment) {
        this.previousFragment = previousFragment;
    }

    public Device getCurrentDevice() {
        return currentDevice;
    }

    public void setCurrentDevice(Device currentDevice) {
        this.currentDevice = currentDevice;
    }


}
