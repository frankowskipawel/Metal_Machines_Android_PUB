package com.pl.metalmachines.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.pl.metalmachines.R;
import com.pl.metalmachines.dao.UtilsDao;
import com.pl.metalmachines.ui.info.MessageFragment;
import com.pl.metalmachines.utils.DialogUtils;
import com.pl.metalmachines.utils.FileUtils;
import com.pl.metalmachines.utils.FragmentUtils;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private View v;
    private TextView message;
    private Button loginButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        v = inflater.inflate(R.layout.fragment_settings, container, false);

// ADD NAME TO TOOLBAR //
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Logowanie");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");

// LOGIN BUTTON //
        loginButton = v.findViewById(R.id.login_button_settings);
        setField();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginButton.getText().equals("Zaloguj")) {
                    DialogUtils.getLoginDialog(getContext(), getActivity(), message, loginButton);
                    setField();
                } else {
                    try {
                        FileUtils.writeToFile(";", getContext());
                        message.setText("niezalogowany");
                        FragmentUtils.setSubtitleNavigationDrawer(getActivity(), "Nie jesteś zalogowany.");
                        loginButton.setText("Zaloguj");
                        UtilsDao.getAutenthification("","");
                    } catch (Exception e) {
                        MessageFragment messageFragment = new MessageFragment();
                        messageFragment.setMessage(e.getMessage());
                        FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                        e.printStackTrace();
                    }
                }
            }
        });

// PREVIOUS BUTTON //
        Button previousButton = v.findViewById(R.id.previous_button_settings_fragment);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();}

        });

        return v;
    }

    public void setField() {
        message = v.findViewById(R.id.message_text_view_settings);
        if (UtilsDao.getUSER().equals("")) {
            message.setText("Nie jesteś zalogowany");
            loginButton.setText("Zaloguj");
        } else {
            String accessLevel="";
            if (UtilsDao.getIsAdmin()==true){ accessLevel="(Administrator)";}

            message.setText("Jesteś zalogowany jako :\n" + UtilsDao.getUSER()+"\n"+accessLevel);
            loginButton.setText("Wyloguj");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setField();
    }

    @Override
    public void onPause() {
        super.onPause();
        setField();
    }
}