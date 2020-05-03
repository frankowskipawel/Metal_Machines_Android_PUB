package com.pl.metalmachines.ui.customers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.pl.metalmachines.R;
import com.pl.metalmachines.dao.CustomerDao;
import com.pl.metalmachines.model.Customer;
import com.pl.metalmachines.ui.info.MessageFragment;
import com.pl.metalmachines.utils.FragmentUtils;
import com.pl.metalmachines.utils.KeyboardUtils;

public class CustomersEditFragment extends Fragment {
    private CustomersViewModel customersViewModel;
    private Fragment previousFragment;
    Boolean isEdit = false;
    String shortName;
    String fullName;
    String adress;
    String zipCode;
    String city;
    String nip;
    String regon;
    String phoneNumber;
    String email;

    public CustomersEditFragment(Fragment previousFragment) {
        this.previousFragment = previousFragment;
    }

    public CustomersEditFragment(Fragment previousFragment, Boolean isEdit, String shortName, String fullName, String adress, String zipCode, String city, String nip, String regon, String phoneNumber, String email) {
        this.previousFragment = previousFragment;
        this.isEdit = isEdit;
        this.shortName = shortName;
        this.fullName = fullName;
        this.adress = adress;
        this.zipCode = zipCode;
        this.city = city;
        this.nip = nip;
        this.regon = regon;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        customersViewModel =
                ViewModelProviders.of(this).get(CustomersViewModel.class);
        View v = inflater.inflate(R.layout.fragment_customer_edit, container, false);

        final EditText shortName = v.findViewById(R.id.shortNameEditText);
        if (isEdit) {
            shortName.setEnabled(false);
        }
        final EditText fullName = v.findViewById(R.id.fullNameEditText);
        final EditText adress = v.findViewById(R.id.adressEditText);
        final EditText zipCode = v.findViewById(R.id.zipCodeEditText);
        final EditText city = v.findViewById(R.id.cityEditText);
        final EditText nip = v.findViewById(R.id.nipEditText);
        final EditText regon = v.findViewById(R.id.regonEditText);
        final EditText phoneNumber = v.findViewById(R.id.phoneNumberEditText);
        final EditText email = v.findViewById(R.id.emailEditText);
        shortName.setText(this.shortName);
        fullName.setText(this.fullName);
        adress.setText(this.adress);
        zipCode.setText(this.zipCode);
        city.setText(this.city);
        nip.setText(this.nip);
        regon.setText(this.regon);
        phoneNumber.setText(this.phoneNumber);
        email.setText(this.email);

        boolean isEdit = false;
        if (!shortName.getText().toString().equals("")) {
            isEdit = true;
        }

// ADD NAME TO TOOLBAR //
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Dodaj/Edycja kontrahenta");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");

// SAVE BUTTON //
        Button saveButton = (Button) v.findViewById(R.id.saveShowCustomerFragment);
        final boolean finalIsEdit = isEdit;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Customer customer = new Customer("",

                            shortName.getText().toString().toUpperCase(),
                            fullName.getText().toString(),
                            adress.getText().toString(),
                            zipCode.getText().toString(),
                            city.getText().toString(),
                            nip.getText().toString(),
                            regon.getText().toString(),
                            phoneNumber.getText().toString(),
                            email.getText().toString());
                    Log.i("SHORTNAME - ", shortName.getText().toString().toUpperCase());
                    if (finalIsEdit) {
                        CustomerDao.updateCustomer(customer);
                        customer.setId(CustomerDao.findIdCustomerByShortName(shortName.getText().toString()));
                        CustomersDetailsFragment prev = (CustomersDetailsFragment) previousFragment;
                        prev.setCurrentCustomer(customer);
                        getFragmentManager().popBackStack();
                    } else {
                        CustomerDao.insertCustomer(customer);
                        getFragmentManager().popBackStack();
                    }
                    KeyboardUtils.hideKeyboard(getContext(), container);
                } catch (Exception e) {
                    MessageFragment messageFragment = new MessageFragment();
                    messageFragment.setMessage(e.getMessage());
                    FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                    e.printStackTrace();
                }
            }
        });

// PREVIEW BUTTON //
        final Button prevButton = (Button) v.findViewById(R.id.prevAddNewCustomerFragment);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideKeyboard(getContext(), container);
                if (previousFragment instanceof CustomersListFragment) {
                    getFragmentManager().popBackStack();
                }
                if (previousFragment instanceof CustomersDetailsFragment) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        return v;
    }

    public void setPreviousFragment(Fragment previousFragment) {
        this.previousFragment = previousFragment;
    }
}
