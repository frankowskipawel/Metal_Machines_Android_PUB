package com.paweldev.maszynypolskie.ui.devices;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.paweldev.maszynypolskie.R;
import com.paweldev.maszynypolskie.model.Device;
import com.paweldev.maszynypolskie.model.Part;
import com.paweldev.maszynypolskie.repository.DeviceRepository;
import com.paweldev.maszynypolskie.ui.info.MessageFragment;
import com.paweldev.maszynypolskie.utils.FragmentUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevicesEquipmentFragment extends Fragment {

    private ProgressBar progressBar;
    private Device currentDevice;
    private ListView listView;
    private List<Map<String, String>> data;
    private View v;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_devices_equipment, container, false);

        progressBar = v.findViewById(R.id.progressBar_Equipmen_fragment);
        listView = v.findViewById(R.id.listViewEquipmenFragment);

        // ADD NAME TO TOOLBAR //
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Wyposażenie maszyny");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(currentDevice.getName()+" ("+currentDevice.getCustomerName()+")");

        // PREVIOUS BUTTON //
        Button previousButton = v.findViewById(R.id.prevEquipmentFragment);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        // ADD BUTTON //
        Button addButton = v.findViewById(R.id.addNewEquipmenFragment);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Dodawanie wyposażenia maszyny");
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setPadding(40, 20, 40, 0);

                TextView nameLabel = new TextView(getContext());
                nameLabel.setText("Nazwa");
                linearLayout.addView(nameLabel);
                final EditText nameEditText = new EditText(getContext());
                nameEditText.setHint("Wpisz nazwę...");
                linearLayout.addView(nameEditText);

                TextView serialNumberLabel = new TextView(getContext());
                serialNumberLabel.setText("Numer seryjny");
                linearLayout.addView(serialNumberLabel);
                final EditText serialNumberEditText = new EditText(getContext());
                serialNumberEditText.setHint("Wpisz numer seryjny...");
                linearLayout.addView(serialNumberEditText);

                TextView producerLabel = new TextView(getContext());
                producerLabel.setText("Producent");
                linearLayout.addView(producerLabel);
                final EditText producerEditText = new EditText(getContext());
                producerEditText.setHint("Wpisz nazwę producenta...");
                linearLayout.addView(producerEditText);

                TextView descriptionLabel = new TextView(getContext());
                descriptionLabel.setText("Opis");
                linearLayout.addView(descriptionLabel);
                final EditText descriptionEditText = new EditText(getContext());
                descriptionEditText.setHint("Wpisz opis...");
                linearLayout.addView(descriptionEditText);

                alert.setView(linearLayout);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Part part = new Part();
                        part.setName(nameEditText.getText().toString());
                        part.setSerialNumber(serialNumberEditText.getText().toString());
                        part.setProducer(producerEditText.getText().toString());
                        part.setDecsription(descriptionEditText.getText().toString());
                        Boolean isTransactionOk = false;
                        try {
                            isTransactionOk = DeviceRepository.insertPart(part, currentDevice.getId());
                            if (isTransactionOk) {
                             startAsyncTask(v);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();

            }
        });

//////LISTVIEW ONCLICK
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {

                    Object clickItemObj = adapterView.getAdapter().getItem(index);
                    HashMap clickItemMap = (HashMap) clickItemObj;
                    final String id = (String) clickItemMap.get("Third Line"); //id
                    List<Part> parts = currentDevice.getParts();
                    Part currentPart=null;
                for (Part part : parts) {
                    if ((part.getId()+"").equals(id)){currentPart=part;}
                }

                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Edycja");
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setPadding(40, 20, 40, 0);

                TextView nameLabel = new TextView(getContext());
                nameLabel.setText("Nazwa");
                linearLayout.addView(nameLabel);
                final EditText nameEditText = new EditText(getContext());
                nameEditText.setHint("Wpisz nazwę...");
                nameEditText.setText(currentPart.getName());
                linearLayout.addView(nameEditText);

                TextView serialNumberLabel = new TextView(getContext());
                serialNumberLabel.setText("Numer seryjny");
                linearLayout.addView(serialNumberLabel);
                final EditText serialNumberEditText = new EditText(getContext());
                serialNumberEditText.setHint("Wpisz numer seryjny...");
                serialNumberEditText.setText(currentPart.getSerialNumber());
                linearLayout.addView(serialNumberEditText);

                TextView producerLabel = new TextView(getContext());
                producerLabel.setText("Producent");
                linearLayout.addView(producerLabel);
                final EditText producerEditText = new EditText(getContext());
                producerEditText.setHint("Wpisz nazwę producenta...");
                producerEditText.setText(currentPart.getProducer());
                linearLayout.addView(producerEditText);

                TextView descriptionLabel = new TextView(getContext());
                descriptionLabel.setText("Opis");
                linearLayout.addView(descriptionLabel);
                final EditText descriptionEditText = new EditText(getContext());
                descriptionEditText.setHint("Wpisz opis...");
                descriptionEditText.setText(currentPart.getDecsription());
                linearLayout.addView(descriptionEditText);


                alert.setView(linearLayout);
                final Part finalCurrentPart1 = currentPart;
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Part part = new Part();
                            part.setId(finalCurrentPart1.getId());
                            part.setName(nameEditText.getText().toString());
                            part.setSerialNumber(serialNumberEditText.getText().toString());
                            part.setProducer(producerEditText.getText().toString());
                            part.setDecsription(descriptionEditText.getText().toString());
                            Boolean isTransactionOk = false;
                            try {
                                isTransactionOk = DeviceRepository.updatePart(part);
                                if (isTransactionOk) {
                                    startAsyncTask(v);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                final Part finalCurrentPart = currentPart;
                alert.setNeutralButton("Usuń", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Boolean isTransactionOk;
                            try {
                                isTransactionOk = DeviceRepository.deletePart(currentDevice.getId(), finalCurrentPart.getId()+"");
                                if (isTransactionOk) {
                                    startAsyncTask(v);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });
                    alert.show();
                }

        });




        startAsyncTask(v);
        return v;
    }


    public void startAsyncTask(View v) {
        DevicesEquipmentFragment.GetDataFromApiAsyncTask task = new DevicesEquipmentFragment.GetDataFromApiAsyncTask(this);
        task.execute();
    }

    private class GetDataFromApiAsyncTask extends AsyncTask<Integer, Integer, String> {

        private WeakReference<DevicesEquipmentFragment> activityWeakReference;
        private SimpleAdapter adapter;

        GetDataFromApiAsyncTask(DevicesEquipmentFragment activity) {
            activityWeakReference = new WeakReference<DevicesEquipmentFragment>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... integers) {

//            progressBar.setVisibility(View.VISIBLE);
//            progressBar.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

            try {

                currentDevice = DeviceRepository.findDeviceById(currentDevice.getId());
                Collections.sort(currentDevice.getParts(), new PartComparator());

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
            loadDataToListView();

    }



    }

    public Device getCurrentDevice() {
        return currentDevice;
    }

    public void setCurrentDevice(Device currentDevice) {
        this.currentDevice = currentDevice;
    }

    private void loadDataToListView(){
        progressBar.setVisibility(View.GONE);
        progressBar.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        DevicesEquipmentFragment activity = activityWeakReference.get();
        List<Map<String, String>> deviceData = new ArrayList<Map<String, String>>();
        for (Part part : currentDevice.getParts()) {
            Map<String, String> row = new HashMap<String, String>(2);
            row.put("First Line", part.getName());
            row.put("Second Line",
                    "S/N: " + part.getSerialNumber() + "\n" +
                            "Producent: " + part.getProducer() + "\n" +
                            "Opis: " + part.getDecsription()
            );
            row.put("Third Line", part.getId() + "");
            deviceData.add(row);
        }

        data = deviceData;

        if (v.getContext() != null) {
            SimpleAdapter adapter = new SimpleAdapter(v.getContext(), data,
                    R.layout.list_item_for_parts,
                    new String[]{"First Line", "Second Line", "Third Line"},
                    new int[]{R.id.text1, R.id.text2, R.id.text3});
            listView.setAdapter(adapter);
        }
    }
}

class PartComparator implements Comparator<Part> {
    @Override
    public int compare(Part a, Part b) {
        return a.getName().compareToIgnoreCase(b.getName());
    }
}