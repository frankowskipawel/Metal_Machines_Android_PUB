package com.paweldev.maszynypolskie.ui.settings;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.paweldev.maszynypolskie.R;
import com.paweldev.maszynypolskie.config.Config;
import com.paweldev.maszynypolskie.ui.info.MessageFragment;
import com.paweldev.maszynypolskie.utils.DialogUtils;
import com.paweldev.maszynypolskie.utils.FileUtils;
import com.paweldev.maszynypolskie.utils.FragmentUtils;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private View v;
    private TextView message;
    private Button loginButton;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
//        settingsViewModel =
//                ViewModelProviders.of(this).get(SettingsViewModel.class);
        v = inflater.inflate(R.layout.fragment_settings, container, false);
//
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
                        Config.getAutenthification("","");
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
        try {
//            ConnectionUtils.checkLoginAndPassword(getContext(), getActivity(), getFragmentManager());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Config.getUSER().equals("")) {
            message.setText("Nie jesteś zalogowany");
            loginButton.setText("Zaloguj");
        } else {
            String accessLevel="";
            if (Config.getIsAdmin()==true){ accessLevel="(Administrator)";}

            message.setText("Jesteś zalogowany jako :\n" + Config.getUSER()+"\n"+accessLevel);
            loginButton.setText("Wyloguj");
        }
    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        setField();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        setField();
//    }
}