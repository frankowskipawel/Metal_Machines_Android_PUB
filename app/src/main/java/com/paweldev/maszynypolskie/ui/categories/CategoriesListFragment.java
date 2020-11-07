package com.paweldev.maszynypolskie.ui.categories;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.paweldev.maszynypolskie.R;
import com.paweldev.maszynypolskie.config.Config;
import com.paweldev.maszynypolskie.repository.CategoryRepository;
import com.paweldev.maszynypolskie.ui.devices.DevicesEditFragment;
import com.paweldev.maszynypolskie.ui.home.HomeFragment;
import com.paweldev.maszynypolskie.ui.info.MessageFragment;
import com.paweldev.maszynypolskie.utils.ConnectionUtils;
import com.paweldev.maszynypolskie.utils.FragmentUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriesListFragment extends Fragment {

    private Fragment previousFragment;
    private CategoriesListFragment categoriesListFragment;
    private ListView listView;
    private EditText editTextSearch;
    private ProgressBar progressBar;
    private List<Map<String, String>> categories;

    public CategoriesListFragment(Fragment previousFragment) {
        this.previousFragment = previousFragment;
    }

    public CategoriesListFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_categories, container, false);

        progressBar = v.findViewById(R.id.progressBar_categoriesList_fragment);

            if (!ConnectionUtils.isInternetAvailable(getContext())) {
                MessageFragment messageFragment = new MessageFragment();
                FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                messageFragment.setMessage("Brak połączenia z internetem");
            } else {
                if (Config.getUSER()!="") {
                    categoriesListFragment = this;
                    listView = (ListView) v.findViewById(R.id.listViewCategoriesFragment);
                    editTextSearch = v.findViewById(R.id.editTextSearch);

// ADD NAME TO TOOLBAR //
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Lista kategorii");
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");

// LIST VIEW ON CLICK //
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                            Object clickItemObj = adapterView.getAdapter().getItem(index);
                            HashMap clickItemMap = (HashMap) clickItemObj;
                            String header = (String) clickItemMap.get("First Line");
                            String content = (String) clickItemMap.get("Second Line");
                            if (previousFragment instanceof DevicesEditFragment) {
                                getFragmentManager().popBackStack();
                                ((DevicesEditFragment) previousFragment).getCurrentDevice().setCategoryName(header);
                            } else {
                                FragmentUtils.replaceFragment(new CategoriesDetailsFragment(header, content.replaceFirst("id: ", ""), CategoriesListFragment.this), getFragmentManager());
                            }
                        }
                    });

// BUTTON SEARCH //
                    Button buttonSearch = (Button) v.findViewById(R.id.searchButton);
                    buttonSearch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadDataToListView(editTextSearch, categoriesListFragment, listView);
                        }
                    });

// ADD BUTTON //
                    Button addButton = (Button) v.findViewById(R.id.addNewCategoriesFragment);
                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentUtils.replaceFragment(new CategoriesEditFragment(CategoriesListFragment.this, false, null, null), getFragmentManager());
                        }
                    });

// PREVIOUS BUTTON //
                    final Button prevButton = (Button) v.findViewById(R.id.prevCategriesFragment);
                    prevButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (previousFragment == null) {
                                FragmentUtils.replaceFragment(new HomeFragment(), getFragmentManager());
                            } else {
                                getFragmentManager().popBackStack();
                            }
                        }
                    });
                    startAsyncTask(v);
                }
            }

        return v;
    }

    private void loadDataToListView(EditText editTextSearch, CategoriesListFragment categoriesListFragment, ListView listView) {

            List<Map<String, String>> filteredData = new ArrayList<Map<String, String>>();
            for (Map<String, String> datum : categories) {
                if (replaceCustomChar(datum.toString().toUpperCase()).contains(replaceCustomChar(editTextSearch.getText().toString().toUpperCase()))) {
                    filteredData.add(datum);
                }
            }
            SimpleAdapter adapter = new SimpleAdapter(categoriesListFragment.getContext(), filteredData,
                    R.layout.simple_list_item_2_small_header,
                    new String[]{"First Line", "Second Line"},
                    new int[]{R.id.text2, R.id.text1});
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

    public Fragment getPreviousFragment() {
        return previousFragment;
    }

    public void setPreviousFragment(Fragment previousFragment) {
        this.previousFragment = previousFragment;
    }

    /// Async task ///
    public void startAsyncTask(View v) {
        CategoriesListFragment.GetDataFromApiAsyncTask task = new CategoriesListFragment.GetDataFromApiAsyncTask(this);
        task.execute();
    }

    private class GetDataFromApiAsyncTask extends AsyncTask<Integer, Integer, String> {

        private WeakReference<CategoriesListFragment> activityWeakReference;
        private SimpleAdapter adapter;

        GetDataFromApiAsyncTask(CategoriesListFragment activity) {
            activityWeakReference = new WeakReference<CategoriesListFragment>(activity);
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

                categories = CategoryRepository.findAllCategoryForListView();
                System.out.println(categories.toString());
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
            CategoriesListFragment activity = activityWeakReference.get();

            List<Map<String, String>> filteredData = new ArrayList<Map<String, String>>();
            for (Map<String, String> datum : categories) {
                if (replaceCustomChar(datum.toString().toUpperCase()).contains(replaceCustomChar(editTextSearch.getText().toString().toUpperCase()))) {
                    filteredData.add(datum);
                }
            }
            if (categoriesListFragment.getContext()!=null){
            SimpleAdapter adapter = new SimpleAdapter(categoriesListFragment.getContext(), filteredData,
                    R.layout.simple_list_item_2_small_header,
                    new String[]{"First Line", "Second Line"},
                    new int[]{R.id.text2, R.id.text1});
            listView.setAdapter(adapter);}
        }
    }
}