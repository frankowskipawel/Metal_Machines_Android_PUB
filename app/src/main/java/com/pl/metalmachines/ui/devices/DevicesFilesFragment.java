package com.pl.metalmachines.ui.devices;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pl.metalmachines.R;
import com.pl.metalmachines.dao.FileDao;
import com.pl.metalmachines.dao.UtilsDao;
import com.pl.metalmachines.model.Customer;
import com.pl.metalmachines.model.Device;
import com.pl.metalmachines.model.FileModel;
import com.pl.metalmachines.model.enums.FileType;
import com.pl.metalmachines.settings.CompanyData;
import com.pl.metalmachines.ui.info.MessageFragment;
import com.pl.metalmachines.utils.DateTimeUtils;
import com.pl.metalmachines.utils.FTPUtils;
import com.pl.metalmachines.utils.FileUtils;
import com.pl.metalmachines.utils.FragmentUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class DevicesFilesFragment extends Fragment {

    private static final int FILE_SELECT_CODE = 0;

    View v;
    private LinearLayout layoutForFiles;
    Device currentDevice;
    ProgressDialog progressDialog;
    Customer currentCustomer;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_devices_files, container, false);
        layoutForFiles = v.findViewById(R.id.device_files_layout);
        setFilesList();

// ADD NAME TO TOOLBAR //
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(currentDevice.getName());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(currentCustomer.getShortName());

// PREVIOUS BUTTON //
        Button previousButton = v.findViewById(R.id.prev_device_files_fragment);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

// ADD NEW FILE BUTTON //
        ImageButton addNewFileButton = v.findViewById(R.id.add_new_file_device_files_fragment);
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
            if (currentDevice != null) {
                final ArrayList<FileModel> filesList = FileDao.findAllFilesByIdDevice(currentDevice.getId());
                Log.e(TAG, filesList.toString() );
                if (filesList.size() > 0) {
                    TextView labelForFiles = new TextView(getContext());
                    labelForFiles.setText(" Załączniki");
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
                                            file[0] = FTPUtils.downloadFileFromFTP(fileModel);
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
                        if (UtilsDao.getIsAdmin()) {//access level
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
                                                        FileDao.deleteDeviceFile(filesList.get(finalI));
                                                        FTPUtils.deleteFileFromFTP(filesList.get(finalI));
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
                        fileNameButtons[i].setText((filesList.get(i).getFilename() + "\n(" + filesList.get(i).getDate() + "/" + filesList.get(i).getFileType() + ")\n" + filesList.get(i).getDescription()).replaceAll("\nnull", ""));
                        fileNameButtons[i].setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onClick(View v) {
                                progressDialog = ProgressDialog.show(getContext(), "Proszę czekać", "Przygotowywanie podglądu.", true);
                                progressDialog.show();
                                progressDialog.setCancelable(false);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        FileModel fileModel = new FileModel();
                                        fileModel.setFilename(filesList.get(finalI).getFilename());
                                        try {
                                            File file = FTPUtils.downloadFileFromFTP(fileModel);
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
                        if (UtilsDao.getIsAdmin()) {//access level
                        layoutRows[i].addView(deleteButtons[i]);}
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
                    alert.setTitle("Wybierz typ pliku");
                    alert.setMessage("");

                    List<String> listFileTypeForSpinner = new ArrayList<>();
                    for (FileType value : FileType.values()) {
                        listFileTypeForSpinner.add(value.getName());
                    }
                    ArrayAdapter<String> adapterForSpinner = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, listFileTypeForSpinner);

                    final Spinner spinner = new Spinner(getContext());
                    adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapterForSpinner);
                    spinner.setPadding(50, 20, 50, 20);

                    LinearLayout linearLayout = new LinearLayout(getContext());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);

                    TextView fileTypeTextView = new TextView(getContext());
                    fileTypeTextView.setTextSize(14);
                    fileTypeTextView.setText("Typ dokumentu");
                    fileTypeTextView.setPadding(50, 20, 50, 20);
                    linearLayout.addView(fileTypeTextView);

                    linearLayout.addView(spinner);

                    final TextView descriptionTextView = new TextView(getContext());
                    descriptionTextView.setTextSize(14);
                    descriptionTextView.setText("Opis");
                    descriptionTextView.setPadding(50, 20, 50, 20);
                    linearLayout.addView(descriptionTextView);

                    final EditText descriptionEditText = new EditText(getContext());
                    descriptionEditText.setGravity(Gravity.CENTER);
                    linearLayout.addView(descriptionEditText);

                    alert.setView(linearLayout);

                    descriptionEditText.setText("");
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
                            final FileModel fileModel = new FileModel(null, currentDevice.getId(), null, DateTimeUtils.now(), FileType.getFileTypeAfterName(spinner.getSelectedItem().toString()), descriptionEditText.getText().toString(), file.getName());

                            final ProgressDialog progressDialog1 = ProgressDialog.show(getContext(), "Proszę czekać", "Przesyłam plik na serwer.", true);
                            progressDialog1.show(); // Display Progress Dialog
                            progressDialog1.setCancelable(false);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        FileDao.insertNewFile(fileModel);
                                        fileModel.setFilename(String.format("%07d", Integer.parseInt(fileModel.getId())) + "~" + currentCustomer.getShortName() + "~" + fileModel.getFileType().getAlias() + "~" + fileModel.getFilename());
                                        FileDao.updateDeviceFile(fileModel);
                                        boolean isUploadedCorrectly = FTPUtils.uploadFileToFTP(file, fileModel);
                                        Log.e(TAG, "--------------------"+isUploadedCorrectly );
                                        if (!isUploadedCorrectly) {
                                            FileDao.deleteDeviceFile(fileModel);
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
}