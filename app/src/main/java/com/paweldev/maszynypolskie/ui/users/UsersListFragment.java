package com.paweldev.maszynypolskie.ui.users;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.paweldev.maszynypolskie.R;
import com.paweldev.maszynypolskie.config.Config;
import com.paweldev.maszynypolskie.model.User;
import com.paweldev.maszynypolskie.repository.UserRepository;
import com.paweldev.maszynypolskie.ui.info.MessageFragment;
import com.paweldev.maszynypolskie.utils.ConnectionUtils;
import com.paweldev.maszynypolskie.utils.FragmentUtils;
import com.paweldev.maszynypolskie.utils.KeyboardUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UsersListFragment extends Fragment {


    private UsersListFragment usersListFragment;
    private ListView listView;
    private EditText editTextSearch;
    private ProgressBar progressBar;
    private List<Map<String, String>> users;

    public UsersListFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_users_list, container, false);

        progressBar = v.findViewById(R.id.progressBar_usersList_fragment);

        // ADD NAME TO TOOLBAR //
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Pracownicy");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");

        usersListFragment = this;
        listView = (ListView) v.findViewById(R.id.listViewUsersFragment);
        editTextSearch = v.findViewById(R.id.editTextSearchUsers);
        final ImageButton buttonSearch = v.findViewById(R.id.searchButtonUsers);


            if (!ConnectionUtils.isInternetAvailable(getContext())) {
                MessageFragment messageFragment = new MessageFragment();
                FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                messageFragment.setMessage("Brak połączenia z internetem");
            } else {
                if (Config.getUSER()!="") {

                    startAsyncTask(v);
//                    loadDataToListView(editTextSearch, usersListFragment, listView);


/// LIST VIEW ON CLICK //
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {

                            Object clickItemObj = adapterView.getAdapter().getItem(index);
                            HashMap clickItemMap = (HashMap) clickItemObj;
                            String header = (String) clickItemMap.get("First Line");
                            String content = (String) clickItemMap.get("Second Line");
                            User user = UserRepository.findById(getIdFromString(header));
                            UsersEditFragment usersEditFragment = new UsersEditFragment(user);
                            FragmentUtils.replaceFragment(usersEditFragment, getFragmentManager());
                        }
                    });
                }
            }
// ADD BUTTON //
            Button addButton = (Button) v.findViewById(R.id.add_new_user_list_fragment);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    UsersEditFragment usersEditFragment = new UsersEditFragment(null);
                    FragmentUtils.replaceFragment(usersEditFragment, getFragmentManager());
                }
            });

// PREVIEW BUTTON //
            final Button prevButton = (Button) v.findViewById(R.id.prevUsersFragment);
            prevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KeyboardUtils.hideKeyboard(getContext(), container);

                    getFragmentManager().popBackStack();

                }
            });

// SEARCH BUTTON //
            buttonSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadDataToListView(editTextSearch, usersListFragment, listView);
                    //Hide keyboard
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(container.getWindowToken(), 0);
                }
            });


        return v;
    }

    private void loadDataToListView(EditText editTextSearch, UsersListFragment usersListFragment, ListView listView) {

            List<Map<String, String>> filteredData = new ArrayList<Map<String, String>>();
            for (Map<String, String> datum : users) {
                if (replaceCustomChar(datum.toString().toUpperCase()).contains(replaceCustomChar(editTextSearch.getText().toString().toUpperCase()))) {
                    filteredData.add(datum);
                }
            }
            SimpleAdapter adapter = new SimpleAdapter(usersListFragment.getContext(), filteredData,
                    R.layout.simple_list_item_2_bold_header,
                    new String[]{"First Line", "Second Line"},
                    new int[]{R.id.text1, R.id.text2});
            listView.setAdapter(adapter);

    }

    public String replaceCustomChar(String string) {
        string = string.replace("Ę", "E");
        string = string.replace("Ą", "A");
        string = string.replace("Ć", "C");
        string = string.replace("Ń", "N");
        string = string.replace("Ł", "L");
        string = string.replace("Ó", "O");
        string = string.replace("Ż", "Z");
        string = string.replace("Ź", "Z");
        string = string.replace("Ś", "S");
        string = string.replace(",", ".");
        return string;
    }

    private String getIdFromString(String header) {

        Pattern pattern = Pattern.compile("^#\\d+ ");
        Matcher matcher = pattern.matcher(header);

        while (matcher.find()) {
            String id = matcher.group().substring(1, matcher.group().length() - 1);
            return id;
        }
        return null;
    }


    /// Async task ///
    public void startAsyncTask(View v) {
        UsersListFragment.GetDataFromApiAsyncTask task = new UsersListFragment.GetDataFromApiAsyncTask(this);
        task.execute();
    }

    private class GetDataFromApiAsyncTask extends AsyncTask<Integer, Integer, String> {

        private WeakReference<UsersListFragment> activityWeakReference;
        private SimpleAdapter adapter;

        GetDataFromApiAsyncTask(UsersListFragment activity) {
            activityWeakReference = new WeakReference<UsersListFragment>(activity);
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

                users = UserRepository.findAllCustomersForListView();
                System.out.println(users.toString());
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
            UsersListFragment activity = activityWeakReference.get();

            List<Map<String, String>> filteredData = new ArrayList<Map<String, String>>();
            for (Map<String, String> datum : users) {
                if (replaceCustomChar(datum.toString().toUpperCase()).contains(replaceCustomChar(editTextSearch.getText().toString().toUpperCase()))) {
                    filteredData.add(datum);
                }
            }
            if (usersListFragment.getContext()!=null){
            SimpleAdapter adapter = new SimpleAdapter(usersListFragment.getContext(), filteredData,
                    R.layout.simple_list_item_2_bold_header,
                    new String[]{"First Line", "Second Line"},
                    new int[]{R.id.text1, R.id.text2});
            listView.setAdapter(adapter);}
        }
    }
}