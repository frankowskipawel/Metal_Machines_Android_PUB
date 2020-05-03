package com.pl.metalmachines.ui.info;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pl.metalmachines.R;
import com.pl.metalmachines.dao.LogDao;
import com.pl.metalmachines.utils.FragmentUtils;

public class MessageFragment extends Fragment {

    View v;
    private Button prevButton;
    private String message;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_message, container, false);
        TextView messageTextView = v.findViewById(R.id.message_text_view_info_fragment);
        messageTextView.setText(message);
        try {
            LogDao.insertLog(message);
        } catch (Exception e) {
            MessageFragment messageFragment = new MessageFragment();
            messageFragment.setMessage(e.getMessage());
            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
            e.printStackTrace();
        }

// PREVIOUS BUTTON //
        prevButton = v.findViewById(R.id.prev_info_fragment);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return v;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}