package com.paweldev.maszynypolskie.ui.devices;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.paweldev.maszynypolskie.R;
import com.paweldev.maszynypolskie.config.CompanyData;
import com.paweldev.maszynypolskie.config.Config;
import com.paweldev.maszynypolskie.model.Customer;
import com.paweldev.maszynypolskie.model.Device;
import com.paweldev.maszynypolskie.model.FileModel;
import com.paweldev.maszynypolskie.model.enums.FileType;
import com.paweldev.maszynypolskie.repository.CustomerRepository;
import com.paweldev.maszynypolskie.repository.FileRepository;
import com.paweldev.maszynypolskie.repository.StorageRepository;
import com.paweldev.maszynypolskie.ui.info.MessageFragment;
import com.paweldev.maszynypolskie.utils.DateTimeUtils;
import com.paweldev.maszynypolskie.utils.FileUtils;
import com.paweldev.maszynypolskie.utils.FragmentUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class DevicesParametersFragment extends Fragment {

    private static final int FILE_SELECT_CODE = 0;

    View v;
    private LinearLayout layoutForFiles;
    Device currentDevice;
    ProgressDialog progressDialog;
    Customer currentCustomer;
    private List<FileModel> filesList;
    private ProgressBar progressBar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_devices_parameters, container, false);
        layoutForFiles = v.findViewById(R.id.device_parameters_layout);
        progressBar = v.findViewById(R.id.progressBar_parameters_fragment);
        startAsyncTask(v);

// ADD NAME TO TOOLBAR //
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Parametry");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(currentDevice.getName());

// PREVIOUS BUTTON //
        Button previousButton = v.findViewById(R.id.prev_device_parameters_fragment);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

// ADD NEW FILE BUTTON //
        ImageButton addNewFileButton = v.findViewById(R.id.add_new_file_device_parameters_fragment);
        addNewFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, FILE_SELECT_CODE);
            }
        });

        return v;
    }

    public void setFilesList() {
        try {
            layoutForFiles.removeAllViews();
            currentCustomer = CustomerRepository.findCustomerById(currentDevice.getCustomerId());
            if (currentDevice != null) {
                final List<FileModel> filesList = FileRepository.findByDeviceAndType(currentDevice.getId(), FileType.PARAMETERS.toString());
                if (filesList.size() > 0) {
                    TextView labelForFiles = new TextView(getContext());
                    labelForFiles.setText(" Lista parametrów");
                    layoutForFiles.addView(labelForFiles);

                    View viewDivider = new View(getContext());
                    viewDivider.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    viewDivider.setBackgroundColor(Color.LTGRAY);
                    viewDivider.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5));
                    layoutForFiles.addView(viewDivider);

                    final Button[] fileNameButtons = new Button[filesList.size()];
                    ImageButton[] sendButtons = new ImageButton[filesList.size()];
                    ImageButton[] deleteButtons = new ImageButton[filesList.size()];
                    final LinearLayout[] layoutRows = new LinearLayout[filesList.size()];

                    int i;
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params1.setMargins(0, 0, 0, 5);
                    params1.weight = 10.0f;
                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    params2.setMargins(0, 15, 0, 0);
                    params1.weight = 1.0f;

                    for (i = 0; i < filesList.size(); i++) {
                        final int finalI = i;
                        layoutRows[i] = new LinearLayout(getContext());
                        layoutRows[i].setOrientation(LinearLayout.HORIZONTAL);

                        sendButtons[i] = new ImageButton(getContext());
                        sendButtons[i].setBackgroundColor(Color.TRANSPARENT);
                        Drawable iconEmail = getResources().getDrawable(R.mipmap.ic_email);
                        sendButtons[i].setBackgroundDrawable(iconEmail);
                        sendButtons[i].setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onClick(View v) {////////
                                final File[] file = {null};
                                progressDialog = ProgressDialog.show(getContext(), "Proszę czekać", "Pobieram pdf-a z serwera.", true);
                                progressDialog.show(); // Display Progress Dialog
                                progressDialog.setCancelable(false);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {

                                            FileModel fileModel = new FileModel();
                                            fileModel.setFilename(filesList.get(finalI).getFilename());
                                            file[0] = StorageRepository.downloadFile(fileModel, getContext());
                                            Uri path = Uri.fromFile(file[0]);
                                            final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                            emailIntent.setType("plain/text");
                                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{currentCustomer.getEmail()});
                                            if (path != null) {
                                                emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                                            }
                                            emailIntent.putExtra(Intent.EXTRA_TEXT, "\n____________________________________\n" + CompanyData.getFullCompanyData());
                                            getActivity().startActivity(Intent.createChooser(emailIntent, "Wysyłam email..."));
                                        } catch (Exception e) {
                                            MessageFragment messageFragment = new MessageFragment();
                                            messageFragment.setMessage(e.getMessage());
                                            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                            e.printStackTrace();
                                        }
                                        progressDialog.dismiss();
                                    }
                                }).start();
                            }
                        });
                        if (Config.getIsAdmin()) {//access level
                            deleteButtons[i] = new ImageButton(getContext());
                            deleteButtons[i].setBackgroundColor(Color.TRANSPARENT);
                            Drawable iconDelete = getResources().getDrawable(R.mipmap.ic_delete_basket);
                            deleteButtons[i].setBackgroundDrawable(iconDelete);
                            deleteButtons[i].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new AlertDialog.Builder(getContext())
                                            .setTitle("Potwierdzenie operacji")
                                            .setMessage("Czy nepewno chcesz usunąć\n" + filesList.get(finalI).getFilename() + "?")
                                            .setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
                                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                                public void onClick(DialogInterface dialog, int which) {
                                                    try {
                                                        FileRepository.deleteDeviceFile(filesList.get(finalI));
                                                        StorageRepository.deleteFile(filesList.get(finalI));
                                                        Toast.makeText(getActivity(), "Usunięto  : " + filesList.get(finalI).getFilename(), Toast.LENGTH_SHORT).show();
                                                        setFilesList();
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
                        }
                        fileNameButtons[i] = new Button(getContext());
                        fileNameButtons[i].setBackgroundColor(Color.parseColor("#FFBEF8F3"));
                        fileNameButtons[i].setBackgroundColor(Color.TRANSPARENT);
                        fileNameButtons[i].setText((filesList.get(i).getDescription()).replaceAll("\nnull", ""));
                        final int finalI1 = i;
                        fileNameButtons[i].setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onClick(View v) {
                                for (int m = 0; m < filesList.size(); m++) {
                                    fileNameButtons[m].setTypeface(null, Typeface.NORMAL);
                                    fileNameButtons[m].setTextSize(14);
                                }
                                fileNameButtons[finalI1].setTypeface(null, Typeface.BOLD);
                                fileNameButtons[finalI1].setTextSize(16);
                                progressDialog = ProgressDialog.show(getContext(), "Proszę czekać", "Przygotowywanie podglądu.", true);
                                progressDialog.show();
                                progressDialog.setCancelable(false);

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        FileModel fileModel = new FileModel();
                                        fileModel.setFilename(filesList.get(finalI).getFilename());
                                        try {
                                            File file = StorageRepository.downloadFile(fileModel, getContext());
                                            String fileExtension = fileModel.getFilename().substring(fileModel.getFilename().lastIndexOf("."));
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            if (fileExtension.equals(".pdf")) {
                                                intent.setType("application/pdf");
                                                intent.setAction(Intent.ACTION_VIEW);
                                                Uri uri1 = Uri.fromFile(file);
                                                intent.setDataAndType(uri1, "application/pdf");
                                                startActivity(intent);
                                            } else if (fileExtension.equals(".jpg") || fileExtension.equals(".jpeg")) {
                                                intent.setType("image/jpeg");
                                                Uri uri1 = Uri.fromFile(file);
                                                intent.setDataAndType(uri1, "image/jpeg");
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(getContext(), "Nie można otworzyć pliku. Nieobsługiwany format.", Toast.LENGTH_LONG);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        progressDialog.dismiss();
                                    }

                                }).start();
                            }/////
                        });
                        layoutRows[i].addView(fileNameButtons[i], params1);
                        if (Config.getIsAdmin()) {//access level
                            layoutRows[i].addView(deleteButtons[i]);
                        }
                        layoutRows[i].addView(sendButtons[i]);
                        layoutRows[i].setGravity(Gravity.CENTER_VERTICAL);
                        viewDivider = new View(getContext());
                        viewDivider.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        viewDivider.setBackgroundColor(Color.LTGRAY);
                        viewDivider.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5));
                        layoutForFiles.addView(layoutRows[i]);
                        layoutForFiles.addView(viewDivider);
                    }
                }
            }
        } catch (Exception e) {
            MessageFragment messageFragment = new MessageFragment();
            messageFragment.setMessage(e.getMessage());
            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
            e.printStackTrace();
        }
    }

    //After choice file //////////////////////
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {

        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
//                     Get the Uri of the selected file
                    final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Dodaj opis do pliku");
                    alert.setMessage("");

                    List<String> listFileTypeForSpinner = new ArrayList<>();
                    for (FileType value : FileType.values()) {
                        listFileTypeForSpinner.add(value.getName());
                    }
                    ArrayAdapter<String> adapterForSpinner = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, listFileTypeForSpinner);

                    LinearLayout linearLayout = new LinearLayout(getContext());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);


                    final TextView descriptionTextView = new TextView(getContext());
                    descriptionTextView.setTextSize(14);
                    descriptionTextView.setText("Opis");
                    descriptionTextView.setPadding(50, 20, 50, 20);
                    linearLayout.addView(descriptionTextView);

                    final EditText descriptionEditText = new EditText(getContext());
                    descriptionEditText.setGravity(Gravity.CENTER);
                    Uri uri = data.getData();
                    File file = null;
                    try {
                        String path = FileUtils.getPath(getContext(), uri);
                        file = new File(path);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    descriptionEditText.setText(file.getName().replaceFirst("[.].*", ""));
                    linearLayout.addView(descriptionEditText);

                    alert.setView(linearLayout);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            Uri uri = data.getData();
                            Log.e(ContentValues.TAG, "File Uri: " + uri.toString());
                            // Get the path
                            String path = null;
                            try {
                                path = FileUtils.getPath(getContext(), uri);
                            } catch (Exception e) {
                                MessageFragment messageFragment = new MessageFragment();
                                messageFragment.setMessage(e.getMessage());
                                FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                e.printStackTrace();
                            }
                            final File file = new File(path);
                            Log.e(ContentValues.TAG, "File Path: " + path);
                            final FileModel fileModel = new FileModel(null, currentDevice.getId(), null, DateTimeUtils.now(), FileType.PARAMETERS, descriptionEditText.getText().toString(), file.getName());

                            final ProgressDialog progressDialog1 = ProgressDialog.show(getContext(), "Proszę czekać", "Przesyłam plik na serwer.", true);
                            progressDialog1.show(); // Display Progress Dialog
                            progressDialog1.setCancelable(false);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        FileRepository.insertNewFile(fileModel);
                                        fileModel.setFilename(String.format("%07d", Integer.parseInt(fileModel.getId())) + "~DEV_" + currentDevice.getId() + "~" + fileModel.getFileType().getAlias() + "~" + fileModel.getFilename());
                                        FileRepository.updateDeviceFile(fileModel);
                                        boolean isUploadedCorrectly = StorageRepository.uploadFile(file, fileModel);
                                        Log.e(TAG, "--------------------" + isUploadedCorrectly);
                                        if (!isUploadedCorrectly) {
                                            FileRepository.deleteDeviceFile(fileModel);
                                        }
                                        progressDialog1.dismiss();

                                        //Run on primary Thread
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                setFilesList();
                                            }
                                        });
                                    } catch (Exception e) {
                                        progressDialog1.dismiss();
                                        MessageFragment messageFragment = new MessageFragment();
                                        messageFragment.setMessage(e.getMessage());
                                        FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    });

                    alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });
                    alert.show();
                    break;
                }
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setCurrentDevice(Device currentDevice) {
        this.currentDevice = currentDevice;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public void startAsyncTask(View v) {
        DevicesParametersFragment.GetDataFromApiAsyncTask task = new DevicesParametersFragment.GetDataFromApiAsyncTask(this);
        task.execute();
    }

    private class GetDataFromApiAsyncTask extends AsyncTask<Integer, Integer, String> {

        private WeakReference<DevicesParametersFragment> activityWeakReference;
        private SimpleAdapter adapter;

        GetDataFromApiAsyncTask(DevicesParametersFragment activity) {
            activityWeakReference = new WeakReference<DevicesParametersFragment>(activity);
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
                currentCustomer = CustomerRepository.findCustomerById(currentDevice.getCustomerId());
                filesList = FileRepository.findByDeviceAndType(currentDevice.getId(), FileType.PARAMETERS.toString());

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

            if (getContext()!= null && filesList.size() > 0) {
                TextView labelForFiles = new TextView(getContext());
                labelForFiles.setText(" Lista parametrów");
                layoutForFiles.addView(labelForFiles);

                View viewDivider = new View(getContext());
                viewDivider.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                viewDivider.setBackgroundColor(Color.LTGRAY);
                viewDivider.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5));
                layoutForFiles.addView(viewDivider);

                final Button[] fileNameButtons = new Button[filesList.size()];
                ImageButton[] sendButtons = new ImageButton[filesList.size()];
                ImageButton[] deleteButtons = new ImageButton[filesList.size()];
                final LinearLayout[] layoutRows = new LinearLayout[filesList.size()];

                int i;
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params1.setMargins(0, 0, 0, 5);
                params1.weight = 10.0f;
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params2.setMargins(0, 15, 0, 0);
                params1.weight = 1.0f;

                for (i = 0; i < filesList.size(); i++) {
                    final int finalI = i;
                    layoutRows[i] = new LinearLayout(getContext());
                    layoutRows[i].setOrientation(LinearLayout.HORIZONTAL);

                    sendButtons[i] = new ImageButton(getContext());
                    sendButtons[i].setBackgroundColor(Color.TRANSPARENT);
                    Drawable iconEmail = getResources().getDrawable(R.mipmap.ic_email);
                    sendButtons[i].setBackgroundDrawable(iconEmail);
                    sendButtons[i].setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onClick(View v) {////////
                            final File[] file = {null};
                            progressDialog = ProgressDialog.show(getContext(), "Proszę czekać", "Pobieram pdf-a z serwera.", true);
                            progressDialog.show(); // Display Progress Dialog
                            progressDialog.setCancelable(false);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        FileModel fileModel = new FileModel();
                                        fileModel.setFilename(filesList.get(finalI).getFilename());
                                        file[0] = StorageRepository.downloadFile(fileModel, getContext());
                                        Uri path = Uri.fromFile(file[0]);
                                        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                        emailIntent.setType("plain/text");
                                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{currentCustomer.getEmail()});
                                        if (path != null) {
                                            emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                                        }
                                        emailIntent.putExtra(Intent.EXTRA_TEXT, "\n____________________________________\n" + CompanyData.getFullCompanyData());
                                        getActivity().startActivity(Intent.createChooser(emailIntent, "Wysyłam email..."));
                                    } catch (Exception e) {
                                        MessageFragment messageFragment = new MessageFragment();
                                        messageFragment.setMessage(e.getMessage());
                                        FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                }
                            }).start();
                        }
                    });
                    if (Config.getIsAdmin()) {//access level
                        deleteButtons[i] = new ImageButton(getContext());
                        deleteButtons[i].setBackgroundColor(Color.TRANSPARENT);
                        Drawable iconDelete = getResources().getDrawable(R.mipmap.ic_delete_basket);
                        deleteButtons[i].setBackgroundDrawable(iconDelete);
                        deleteButtons[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Potwierdzenie operacji")
                                        .setMessage("Czy nepewno chcesz usunąć\n" + filesList.get(finalI).getFilename() + "?")
                                        .setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
                                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                            public void onClick(DialogInterface dialog, int which) {
                                                try {
                                                    FileRepository.deleteDeviceFile(filesList.get(finalI));
                                                    StorageRepository.deleteFile(filesList.get(finalI));
                                                    Toast.makeText(getActivity(), "Usunięto  : " + filesList.get(finalI).getFilename(), Toast.LENGTH_SHORT).show();
                                                    setFilesList();
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
                    }
                    fileNameButtons[i] = new Button(getContext());
                    fileNameButtons[i].setBackgroundColor(Color.parseColor("#FFBEF8F3"));
                    fileNameButtons[i].setBackgroundColor(Color.TRANSPARENT);
                    fileNameButtons[i].setText((filesList.get(i).getDescription()).replaceAll("\nnull", ""));
                    final int finalI1 = i;
                    fileNameButtons[i].setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onClick(View v) {
                            for (int m = 0; m < filesList.size(); m++) {
                                fileNameButtons[m].setTypeface(null, Typeface.NORMAL);
                                fileNameButtons[m].setTextSize(14);
                            }
                            fileNameButtons[finalI1].setTypeface(null, Typeface.BOLD);
                            fileNameButtons[finalI1].setTextSize(16);
                            progressDialog = ProgressDialog.show(getContext(), "Proszę czekać", "Przygotowywanie podglądu.", true);
                            progressDialog.show();
                            progressDialog.setCancelable(false);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    FileModel fileModel = new FileModel();
                                    fileModel.setFilename(filesList.get(finalI).getFilename());
                                    try {
                                        File file = StorageRepository.downloadFile(fileModel, getContext());
                                        String fileExtension = fileModel.getFilename().substring(fileModel.getFilename().lastIndexOf("."));
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        if (fileExtension.equals(".pdf")) {
                                            intent.setType("application/pdf");
                                            intent.setAction(Intent.ACTION_VIEW);
                                            Uri uri1 = Uri.fromFile(file);
                                            intent.setDataAndType(uri1, "application/pdf");
                                            startActivity(intent);
                                        } else if (fileExtension.equals(".jpg") || fileExtension.equals(".jpeg")) {
                                            intent.setType("image/jpeg");
                                            Uri uri1 = Uri.fromFile(file);
                                            intent.setDataAndType(uri1, "image/jpeg");
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(getContext(), "Nie można otworzyć pliku. Nieobsługiwany format.", Toast.LENGTH_LONG);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                }

                            }).start();
                        }/////
                    });
                    layoutRows[i].addView(fileNameButtons[i], params1);
                    if (Config.getIsAdmin()) {//access level
                        layoutRows[i].addView(deleteButtons[i]);
                    }
                    layoutRows[i].addView(sendButtons[i]);
                    layoutRows[i].setGravity(Gravity.CENTER_VERTICAL);
                    viewDivider = new View(getContext());
                    viewDivider.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    viewDivider.setBackgroundColor(Color.LTGRAY);
                    viewDivider.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5));
                    layoutForFiles.addView(layoutRows[i]);
                    layoutForFiles.addView(viewDivider);
                }
            }
        }
    }
}