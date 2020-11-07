package com.paweldev.maszynypolskie.ui.devices;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.paweldev.maszynypolskie.R;
import com.paweldev.maszynypolskie.repository.CategoryRepository;
import com.paweldev.maszynypolskie.repository.CustomerRepository;
import com.paweldev.maszynypolskie.repository.DeviceRepository;
import com.paweldev.maszynypolskie.model.Device;
import com.paweldev.maszynypolskie.ui.categories.CategoriesListFragment;
import com.paweldev.maszynypolskie.ui.customers.CustomersListFragment;
import com.paweldev.maszynypolskie.ui.info.MessageFragment;
import com.paweldev.maszynypolskie.utils.DateTimeUtils;
import com.paweldev.maszynypolskie.utils.DeviceUtils;
import com.paweldev.maszynypolskie.utils.FragmentUtils;


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
    private TextView streetAddress;
    private TextView zipCode;
    private TextView city;

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
        streetAddress = v.findViewById(R.id.streetAddress_device_edit_text);
        zipCode = v.findViewById(R.id.zipCode_device_edit_text);
        city = v.findViewById(R.id.city_device_edit_text);
        loadDataToAllControls();

// ADD NAME TO TOOLBAR //
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Dodaj/Edycja maszyny");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");

// SAVE BUTTON //
        final Button saveButton = (Button) v.findViewById(R.id.save_add_new_devices_fragment);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!customerDevice.getText().toString().equals("") &&
                !categoryDevice.getText().toString().equals("")
                ){
                Device newDevice = null;
                try {
                    newDevice = new Device(null,
                            nameDevice.getText().toString(),
                            serialNumber.getText().toString(),
                            sourcePower.getText().toString(),
                            CustomerRepository.findCustomerByShortName(customerDevice.getText().toString()).getId(),
//                            CategoryDao.findIdCategoryByName(categoryDevice.getText().toString()),
                            CategoryRepository.findCategoryByName(categoryDevice.getText().toString()).getId(),
                            transferDate.getText().toString(),
                            streetAddress.getText().toString(),
                            zipCode.getText().toString(),
                            city.getText().toString()
                    );
                    if (idDevice.getText().toString().equals("")) {
                        Log.i("newDevice", newDevice.toString());
                        DeviceRepository.insertDevice(newDevice);
                    } else {
                        newDevice.setId(currentDevice.getId());
                        Log.i("newDevice", newDevice.toString());
                        DeviceRepository.updateDevice(newDevice);
                        ((DevicesDetailsFragment) previousFragment).setCurrentDevice(newDevice);
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
            }else {
                    Toast.makeText(getContext(), "Uzupełnij dane!", Toast.LENGTH_SHORT).show();
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
            streetAddress.setText(currentDevice.getStreetAddress());
            zipCode.setText(currentDevice.getZipCode());
            city.setText(currentDevice.getCity());
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
