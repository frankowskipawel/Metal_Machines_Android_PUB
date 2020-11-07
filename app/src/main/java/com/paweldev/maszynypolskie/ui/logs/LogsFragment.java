package com.paweldev.maszynypolskie.ui.logs;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.paweldev.maszynypolskie.R;
import com.paweldev.maszynypolskie.config.Config;
import com.paweldev.maszynypolskie.repository.LogRepository;
import com.paweldev.maszynypolskie.ui.info.MessageFragment;
import com.paweldev.maszynypolskie.utils.ConnectionUtils;
import com.paweldev.maszynypolskie.utils.FragmentUtils;

import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class LogsFragment extends Fragment {

    private View v;
    private Fragment previousFragment;
    private ListView listView;
    private ProgressBar progressBar;
    private LogsFragment logsFragment;
    private List<Map<String, String>> logs;

    public LogsFragment(Fragment previousFragment) {
        this.previousFragment = previousFragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_logs, container, false);

        progressBar = v.findViewById(R.id.progressBar_logs_fragment);
        logsFragment = this;

        if (!ConnectionUtils.isInternetAvailable(getContext())) {

            MessageFragment messageFragment = new MessageFragment();
            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
            messageFragment.setMessage("Brak połączenia z internetem");

        } else {
            if (Config.getUSER() != "") {
                listView = (ListView) v.findViewById(R.id.list_view_logs);
//                    loadDataToListView(LogsFragment.this, listView);
                startAsyncTask(v);

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

        return v;
    }

    private void loadDataToListView(LogsFragment logsFragment, ListView listView) throws SQLException, ClassNotFoundException {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        data = LogRepository.findAllLogsForListView();
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

    /// Async task ///
    public void startAsyncTask(View v) {
        LogsFragment.GetDataFromApiAsyncTask task = new LogsFragment.GetDataFromApiAsyncTask(this);
        task.execute();
    }

    private class GetDataFromApiAsyncTask extends AsyncTask<Integer, Integer, String> {

        private WeakReference<LogsFragment> activityWeakReference;
        private SimpleAdapter adapter;

        GetDataFromApiAsyncTask(LogsFragment activity) {
            activityWeakReference = new WeakReference<LogsFragment>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... integers) {

            progressBar.setVisibility(View.VISIBLE);
            progressBar.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

            try {

                logs = LogRepository.findAllLogsForListView();
                System.out.println(logs.toString());
            } catch (Exception e) {
                MessageFragment messageFragment = new MessageFragment();
                messageFragment.setMessage(e.getMessage());
                FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                e.printStackTrace();
            }
            return "OK";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            return;

        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            progressBar.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            LogsFragment activity = activityWeakReference.get();

            List<Map<String, String>> filteredData = new ArrayList<Map<String, String>>();
            for (Map<String, String> datum : logs) {
                filteredData.add(datum);
            }
            if (logsFragment.getContext() != null) {
                SimpleAdapter adapter = new SimpleAdapter(logsFragment.getContext(), filteredData,
                        android.R.layout.simple_list_item_2,
                        new String[]{"First Line", "Second Line"},
                        new int[]{android.R.id.text1, android.R.id.text2});
                listView.setAdapter(adapter);
            }
        }
    }
}
