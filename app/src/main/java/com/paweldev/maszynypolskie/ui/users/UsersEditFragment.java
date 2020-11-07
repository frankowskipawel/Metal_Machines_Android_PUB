package com.paweldev.maszynypolskie.ui.users;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.paweldev.maszynypolskie.R;
import com.paweldev.maszynypolskie.model.Role;
import com.paweldev.maszynypolskie.model.User;
import com.paweldev.maszynypolskie.model.apiModel.UserAPI;
import com.paweldev.maszynypolskie.repository.UserRepository;
import com.paweldev.maszynypolskie.ui.info.MessageFragment;
import com.paweldev.maszynypolskie.utils.ConnectionUtils;
import com.paweldev.maszynypolskie.utils.FragmentUtils;
import com.paweldev.maszynypolskie.utils.KeyboardUtils;

import java.io.IOException;


public class UsersEditFragment extends Fragment {

    private User currentUser;


    public UsersEditFragment(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_users_edit, container, false);

        final EditText id = v.findViewById(R.id.idUserEditText);
        final EditText email = v.findViewById(R.id.emailUserEditText);
        final EditText password = v.findViewById(R.id.passwordUserEditText);
        final EditText userName = v.findViewById(R.id.userNameEditText);
        final EditText firstName = v.findViewById(R.id.firstNameEditText);
        final EditText lastName = v.findViewById(R.id.lastNameText);
        final Switch adminSwitch = v.findViewById(R.id.switch_admin_edit_user);
        final Switch isActiveSwitch = v.findViewById(R.id.switch_isActive_edit_user);
        final Button deleteButton = v.findViewById(R.id.deleteUserFragment);

        if (currentUser != null) {
            id.setText(currentUser.getId() + "");
            email.setText(currentUser.getEmail());
            userName.setText(currentUser.getUserName());
            firstName.setText(currentUser.getFirstName());
            lastName.setText(currentUser.getLastName());
            adminSwitch.setChecked(false);
            for (Role role : currentUser.getRoles()) {
                if (role.getRole().equals("ADMIN")) {
                    adminSwitch.setChecked(true);
                }
            }
            if (currentUser.isActive() == false) {
                isActiveSwitch.setChecked(false);
            } else {
                isActiveSwitch.setChecked(true);
            }

        }


        // ADD NAME TO TOOLBAR //
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Dodawanie/Edycja Pracownika");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");
        try {
            if (!ConnectionUtils.isInternetAvailable(getContext())) {
                MessageFragment messageFragment = new MessageFragment();
                FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                messageFragment.setMessage("Brak połączenia z internetem");
            } else {
                if (ConnectionUtils.checkLoginAndPassword(getContext(), getActivity(), getFragmentManager())) {
                    final UsersEditFragment usersListFragment = this;


                }
            }
// SAVE BUTTON //
            Button saveButton = (Button) v.findViewById(R.id.saveEditUserFragment);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    try {
                        if (currentUser == null) {
                            String role = "";
                            if (adminSwitch.isChecked()) {
                                role = "ADMIN";
                            } else {
                                role = "USER";
                            }
                            final UserAPI user = new UserAPI("",
                                    userName.getText().toString(),
                                    password.getText().toString(),
                                    email.getText().toString(),
                                    firstName.getText().toString(),
                                    lastName.getText().toString(),
                                    null,
                                    isActiveSwitch.isChecked(),
                                    role
                            );
                            UserRepository.insert(user);
                            getFragmentManager().popBackStack();
                            KeyboardUtils.hideKeyboard(getContext(), container);
                        } else {
                            String role = "";
                            if (adminSwitch.isChecked()) {
                                role = "ADMIN";
                            } else {
                                role = "USER";
                            }
                            final UserAPI user = new UserAPI(id.getText().toString(),
                                    userName.getText().toString(),
                                    password.getText().toString(),
                                    email.getText().toString(),
                                    firstName.getText().toString(),
                                    lastName.getText().toString(),
                                    null,
                                    isActiveSwitch.isChecked(),
                                    role
                            );
                            UserRepository.update(user);
                            getFragmentManager().popBackStack();
                            KeyboardUtils.hideKeyboard(getContext(), container);
                        }

                    } catch (Exception e) {
                        MessageFragment messageFragment = new MessageFragment();
                        messageFragment.setMessage(e.getMessage());
                        FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                        e.printStackTrace();
                    }
                }
            });

// DELETE BUTTON //
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Potwierdzenie operacji")
                            .setMessage("Czy nepewno chcesz usunąć\n" + currentUser.getFirstName()+" "+currentUser.getLastName() + "?")
                            .setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        UserRepository.delete(currentUser);
                                        getFragmentManager().popBackStack();
                                        Toast.makeText(getActivity(), "Usunięto  : " + currentUser.getFirstName()+" "+currentUser.getLastName(), Toast.LENGTH_SHORT).show();
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


// PREVIEW BUTTON //
            final Button prevButton = (Button) v.findViewById(R.id.prevEditUserFragment);
            prevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KeyboardUtils.hideKeyboard(getContext(), container);

                        getFragmentManager().popBackStack();

                }
            });
        } catch (IOException e) {
            MessageFragment messageFragment = new MessageFragment();
            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
            messageFragment.setMessage(e.getMessage());
            e.printStackTrace();
        }

        return v;
    }
}