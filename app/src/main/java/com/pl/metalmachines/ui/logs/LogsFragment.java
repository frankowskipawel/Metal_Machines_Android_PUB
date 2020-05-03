package com.pl.metalmachines.ui.logs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.pl.metalmachines.R;
import com.pl.metalmachines.dao.LogDao;
import com.pl.metalmachines.ui.info.MessageFragment;
import com.pl.metalmachines.utils.ConnectionUtils;
import com.pl.metalmachines.utils.FragmentUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class LogsFragment extends Fragment {

    private View v;
    private Fragment previousFragment;
    ListView listView;

    public LogsFragment(Fragment previousFragment) {
        this.previousFragment = previousFragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_logs, container, false);
        try {
            if (!ConnectionUtils.isInternetAvailable(getContext())) {

                MessageFragment messageFragment = new MessageFragment();
                FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                messageFragment.setMessage("Brak połączenia z internetem");

            } else {
                if (ConnectionUtils.checkLoginAndPassword(getContext(), getActivity(), getFragmentManager())) {
                    listView = (ListView) v.findViewById(R.id.list_view_logs);
                    loadDataToListView(LogsFragment.this, listView);

// PREVIOUS BUTTON //
                    final Button prevButton = (Button) v.findViewById(R.id.prev_logs_button);
                    prevButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getFragmentManager().popBackStack();
                        }
                    });
                }
            }
        } catch (Exception e) {
            MessageFragment messageFragment = new MessageFragment();
            messageFragment.setMessage(e.getMessage());
            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
            e.printStackTrace();
        }
        return v;
    }

    private void loadDataToListView(LogsFragment logsFragment, ListView listView) throws SQLException, ClassNotFoundException {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        data = LogDao.findAllLogsForListView();
        List<Map<String, String>> filteredData = new ArrayList<Map<String, String>>();
        for (Map<String, String> datum : data) {
            filteredData.add(datum);
        }
        SimpleAdapter adapter = new SimpleAdapter(logsFragment.getContext(), filteredData,
                android.R.layout.simple_list_item_2,
                new String[]{"First Line", "Second Line"},
                new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);
    }
}
