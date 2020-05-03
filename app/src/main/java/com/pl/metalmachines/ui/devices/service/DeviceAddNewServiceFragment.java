package com.pl.metalmachines.ui.devices.service;

import android.Manifest;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pl.metalmachines.R;
import com.pl.metalmachines.dao.CustomerDao;
import com.pl.metalmachines.dao.FileDao;
import com.pl.metalmachines.dao.ServiceDao;
import com.pl.metalmachines.dao.UtilsDao;
import com.pl.metalmachines.model.Customer;
import com.pl.metalmachines.model.Device;
import com.pl.metalmachines.model.DeviceInspectionReport;
import com.pl.metalmachines.model.FileModel;
import com.pl.metalmachines.model.enums.FileType;
import com.pl.metalmachines.model.enums.PaymentType;
import com.pl.metalmachines.model.Service;
import com.pl.metalmachines.model.enums.ServiceType;
import com.pl.metalmachines.settings.CompanyData;
import com.pl.metalmachines.ui.devices.service.enums.EnumServiceState;
import com.pl.metalmachines.ui.info.MessageFragment;
import com.pl.metalmachines.ui.signaturepad.Signatory;
import com.pl.metalmachines.ui.signaturepad.SignaturePadFragment;
import com.pl.metalmachines.utils.ConnectionUtils;
import com.pl.metalmachines.utils.ControlsUtils;
import com.pl.metalmachines.utils.DateTimeUtils;
import com.pl.metalmachines.utils.FTPUtils;
import com.pl.metalmachines.utils.FileUtils;
import com.pl.metalmachines.utils.FragmentUtils;
import com.pl.metalmachines.utils.GPSUtils;
import com.pl.metalmachines.utils.pdfUtils.ProtokolSerwisowyCreatePDF;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.DOWNLOAD_SERVICE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class DeviceAddNewServiceFragment extends Fragment {
    private static final int PERMISSION_STORAGE_CODE = 1000;
    private static final int FILE_SELECT_CODE = 0;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private View v;
    private Device currentDevice;
    private Customer currentCustomer;
    private DeviceInspectionReport currentInspectionReport;
    private Service currentService;
    private TextView idService;
    private TextView customer;
    private TextView device;
    private TextView state;
    private DeviceInspectionReportFragment fragmentInspectionReport;
    private Boolean isInspectionReportCompleted = false;
    private CheckBox isInspectionReportCompletedCheckbox;
    private CheckBox periodicServiceCheckBox;
    private Button inspectionReportButton;
    private Button signButton;
    private Button rejectButton;
    private Button datePickerButton;
    private Button dateButton;
    private CheckBox instalacjaCheckBox;
    private CheckBox szkolenieOperatorowCheckBox;
    private CheckBox przegladOkresowyCheckBox;
    private CheckBox naprawaCheckBox;
    private RadioButton kosztPlatnaRadioButton;
    private RadioButton kosztGwarancyjnaRadioButton;
    private RadioButton kosztDoDecyzjiGwarantaCRadioButton;
    private EditText opisUsterkiEditText;
    private EditText zakresPracEditText;
    private EditText zuzyteMaterialyEditText;
    private EditText uwagiEditText;
    private EditText czasPracyEditText;
    private EditText dojazdEditText;
    private EditText noclegEditText;
    private Button startDatePickerButton;
    private Button startTimePicker;
    private Button endDatePickerButton;
    private Button endTimePicker;
    private ImageButton attachmentsButton;
    private ImageButton pdfShowButton;
    boolean isEdit = true;
    private ScrollView scrollView;
    private Button saveButton;
    private String url;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialog1;
    private boolean showAfterSigning = false;
    private TextView numberService;
    private ImageButton gpsButton;
    private LinearLayout layoutForFiles;
    private boolean isPressedAddScanButton;
    private Button addScanButton;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_device_add_new_service, container, false);

// ADD NAME TO TOOLBAR //
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Zdarzenie serwisowe");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");
//
        GPSUtils gpsUtils = new GPSUtils(getActivity(), getContext());
        gpsUtils.getLastLocation();

        try {
            idService = v.findViewById(R.id.id_textView_add_service);
            numberService = v.findViewById(R.id.number_textView_add_service);
            customer = v.findViewById(R.id.customet_textView_add_service);
            device = v.findViewById(R.id.device_textView_add_service);
            state = v.findViewById(R.id.state_textView_add_new_service_fragment);
            isInspectionReportCompletedCheckbox = v.findViewById(R.id.protokol_przegladu_chceckBox_add_service);
            isInspectionReportCompletedCheckbox.setChecked(isInspectionReportCompleted);
            attachmentsButton = v.findViewById(R.id.attachments_button_add_new_device_fragment);
            pdfShowButton = v.findViewById(R.id.show_pdf_button_add_new_device_fragment);
            gpsButton = v.findViewById(R.id.gps_button_add_new_device_fragment);
            addScanButton = v.findViewById(R.id.add_scan_add_new_service_button);
            if (currentDevice != null) {
                currentCustomer = CustomerDao.findCustomerByShortName(currentDevice.getCustomerName());
                customer.setText(currentCustomer.getFullName() + "\n" + currentCustomer.getStreetAddress() + "\n" + currentCustomer.getZipCode() + " " + currentCustomer.getCity() + "\nNIP: " + currentCustomer.getNip());
                device.setText(currentDevice.getName() + "\nMoc źródła : " + currentDevice.getSourcePower() + "\nS/N : " + currentDevice.getSerialNumber() + "\nKat.: " + currentDevice.getCategoryName() + "\nData przekazania : " + currentDevice.getTransferDate());
            }
            signButton = v.findViewById(R.id.sign_button_add_service);
            rejectButton = v.findViewById(R.id.reject_button_add_service);
            if (UtilsDao.getIsAdmin() == false) {//access level
                signButton.setVisibility(View.INVISIBLE);
                rejectButton.setVisibility(View.INVISIBLE);
            }
            dateButton = v.findViewById(R.id.date_button_add_service);
            instalacjaCheckBox = v.findViewById(R.id.instalacja_chcecBox_add_service);
            szkolenieOperatorowCheckBox = v.findViewById(R.id.szkolenie_operatorow_chcecBox_add_service);
            przegladOkresowyCheckBox = v.findViewById(R.id.przeglad_okresowy_checkBox_add_service);
            naprawaCheckBox = v.findViewById(R.id.naprawa_chceckbox_add_service);
            kosztPlatnaRadioButton = v.findViewById(R.id.koszt_platna_radioButton_add_service);
            kosztGwarancyjnaRadioButton = v.findViewById(R.id.koszt_gwarancyjna_radioButton_add_service);
            kosztDoDecyzjiGwarantaCRadioButton = v.findViewById(R.id.koszt_dodecyzjigwaranta_radioButton_add_service);
            opisUsterkiEditText = v.findViewById(R.id.opis_usterki_editText_add_service);
            zakresPracEditText = v.findViewById(R.id.zakres_prac_editText_add_service);
            zuzyteMaterialyEditText = v.findViewById(R.id.zuzyte_materialy_edittext_add_service);
            uwagiEditText = v.findViewById(R.id.uwagi_editText_add_service);
            czasPracyEditText = v.findViewById(R.id.czas_pracy_editText_add_service);
            dojazdEditText = v.findViewById(R.id.dojazd_editText_add_service);
            noclegEditText = v.findViewById(R.id.nocleg_editText_add_service);
            scrollView = v.findViewById(R.id.scroll_view_add_new_service);

// PREVIOUS BUTTON //
            Button prevButton = (Button) v.findViewById(R.id.previev_button_add_new_service_fragment);
            prevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            });

// INSPECTION REPORT BUTTON //
            inspectionReportButton = (Button) v.findViewById(R.id.protokol_przegladu_button_add_service);
            inspectionReportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragmentInspectionReport == null) {
                        DeviceInspectionReportFragment deviceInspectionReportFragment = new DeviceInspectionReportFragment();
                        setFragmentInspectionReport(deviceInspectionReportFragment);
                        deviceInspectionReportFragment.setCurrentDevice(currentDevice);
                        deviceInspectionReportFragment.setPreviousFragment(DeviceAddNewServiceFragment.this);
                        if (currentService != null) {
                            fragmentInspectionReport.setCurrentInspectionReport(currentService.getDeviceInspectionReport());
                        }
                        deviceInspectionReportFragment.setCurrentService(currentService);
                        FragmentUtils.replaceFragment(deviceInspectionReportFragment, getFragmentManager());
                        fragmentInspectionReport.setEdit(isEdit);
                    } else {
                        setFragmentInspectionReport(fragmentInspectionReport);
                        fragmentInspectionReport.setCurrentDevice(currentDevice);
                        fragmentInspectionReport.setPreviousFragment(DeviceAddNewServiceFragment.this);
                        fragmentInspectionReport.setCurrentInspectionReport(currentInspectionReport);
                        fragmentInspectionReport.setCurrentService(currentService);
                        FragmentUtils.replaceFragment(fragmentInspectionReport, getFragmentManager());
                        fragmentInspectionReport.setEdit(isEdit);
                    }
                }
            });

// DATE PICKER BUTTON //
            datePickerButton = v.findViewById(R.id.date_button_add_service);
            datePickerButton.setText(DateTimeUtils.now());
            datePickerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateTimeUtils.getDate(getActivity(), datePickerButton, datePickerButton.getText().toString());
                }
            });
            startDatePickerButton = v.findViewById(R.id.start_date_add_service);
            startDatePickerButton.setText(DateTimeUtils.now());
            startDatePickerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateTimeUtils.getDate(getActivity(), startDatePickerButton, startDatePickerButton.getText().toString());
                }
            });
            startTimePicker = v.findViewById(R.id.start_time_add_service);
            startTimePicker.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
            startTimePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateTimeUtils.getTime(getActivity(), startTimePicker);
                }
            });
            endDatePickerButton = v.findViewById(R.id.end_date_add_service);
            endDatePickerButton.setText(DateTimeUtils.now());
            endDatePickerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateTimeUtils.getDate(getActivity(), endDatePickerButton, endDatePickerButton.getText().toString());
                }
            });
            endTimePicker = v.findViewById(R.id.end_time_add_service);
            endTimePicker.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
            endTimePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateTimeUtils.getTime(getActivity(), endTimePicker);
                }
            });

// SAVE/EDIT BUTTON //
            saveButton = (Button) v.findViewById(R.id.save_button_add_service);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (saveButton.getText().equals("Zapisz")) {
                            boolean validation = false;
                            String message = "Uzupełnij dane na formularzu";
                            List<ServiceType> listService = new ArrayList<>();
                            if (instalacjaCheckBox.isChecked()) {
                                listService.add(ServiceType.INSTALACJA);
                                validation = true;
                            }
                            if (szkolenieOperatorowCheckBox.isChecked()) {
                                listService.add(ServiceType.SZKOLENIE_OPERATOROW);
                                validation = true;
                            }
                            if (naprawaCheckBox.isChecked()) {
                                listService.add(ServiceType.NAPRAWA);
                                validation = true;
                            }
                            if (przegladOkresowyCheckBox.isChecked() && isInspectionReportCompletedCheckbox.isChecked()) {
                                listService.add(ServiceType.PRZEGLAD_OKRESOWY);
                                validation = true;
                            } else if (przegladOkresowyCheckBox.isChecked() && !isInspectionReportCompletedCheckbox.isChecked()) {
                                message = "Wypełnij protokół serwisowy";
                                validation = false;
                            }
                            PaymentType paymentType = null;
                            if (kosztPlatnaRadioButton.isChecked()) {
                                paymentType = PaymentType.PLATNA;
                            }
                            if (kosztGwarancyjnaRadioButton.isChecked()) {
                                paymentType = PaymentType.GWARANCYJNA;
                            }
                            if (kosztDoDecyzjiGwarantaCRadioButton.isChecked()) {
                                paymentType = PaymentType.DO_DECYZJI_GWARANTA;
                            }
                            if (validation) {
                                if (!przegladOkresowyCheckBox.isChecked()) {
                                    currentInspectionReport = null;
                                }
                                String id = idService.getText().toString();
                                String number = numberService.getText().toString();
                                if (id.equals("(auto)")) {
                                    id = null;
                                    number = null;
                                }
                                Service service = null;
                                service = new Service(id,
                                        numberService.getText().toString(),
                                        dateButton.getText().toString(),
                                        CustomerDao.findIdCustomerByShortName(currentDevice.getCustomerName()),
                                        currentDevice.getId(),
                                        listService,
                                        paymentType,
                                        currentInspectionReport,
                                        opisUsterkiEditText.getText().toString(),
                                        zakresPracEditText.getText().toString(),
                                        zuzyteMaterialyEditText.getText().toString(),
                                        uwagiEditText.getText().toString(),
                                        startDatePickerButton.getText().toString(),
                                        startTimePicker.getText().toString(),
                                        endDatePickerButton.getText().toString(),
                                        endTimePicker.getText().toString(),
                                        czasPracyEditText.getText().toString(),
                                        dojazdEditText.getText().toString(),
                                        noclegEditText.getText().toString(),
                                        UtilsDao.getUSER(),
                                        EnumServiceState.UTWORZONY.toString()
                                );
                                String[] idAndNumber = new String[2];
                                if (currentService == null) {
                                    currentService = service;
                                    idAndNumber = ServiceDao.insertService(service, false);
                                    currentService.setId(idAndNumber[0] + "");
                                    service.setId(idAndNumber[0] + "");
                                } else {
                                    currentService = service;
                                    ServiceDao.updateService(service, false);
                                    idAndNumber[0] = currentService.getId();
                                    isEdit = false;
                                }
                                idService.setText(idAndNumber[0] + "");
                                state.setText(service.getState());
                                isEdit = false;
                                Toast.makeText(getActivity(), "Zapisano", Toast.LENGTH_SHORT).show();
                                saveButton.setText("Edytuj");
                                signButton.setVisibility(View.VISIBLE);
                                rejectButton.setVisibility(View.VISIBLE);
                                ControlsUtils.disableEnableControls(false, scrollView);
                                pdfShowButton.setClickable(true);
                                attachmentsButton.setClickable(true);
                                gpsButton.setClickable(true);
                                attachmentsButton.setVisibility(View.VISIBLE);
                                pdfShowButton.setVisibility(View.VISIBLE);
                                gpsButton.setVisibility(View.VISIBLE);
                                inspectionReportButton.setClickable(true);
                                if (!UtilsDao.getIsAdmin()) {
                                    gpsButton.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            isEdit = true;
                            currentInspectionReport = currentService.getDeviceInspectionReport();
                            ControlsUtils.disableEnableControls(true, scrollView);
                            attachmentsButton.setVisibility(View.INVISIBLE);
                            pdfShowButton.setVisibility(View.INVISIBLE);
                            gpsButton.setVisibility(View.INVISIBLE);
                            signButton.setVisibility(View.INVISIBLE);
                            rejectButton.setVisibility(View.INVISIBLE);
                            saveButton.setText("Zapisz");
                        }
                    } catch (Exception e) {
                        MessageFragment messageFragment = new MessageFragment();
                        messageFragment.setMessage(e.getMessage());
                        FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                        e.printStackTrace();
                    }
                }

            });

// REJECTED BUTTON //
            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Potwierdzenie")
                            .setMessage("Czy napewno chcesz odrzucić zdarzenie serwisowe? Nie będzie już można cofnąć tej operacji")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        currentService.setState(EnumServiceState.ODRZUCONY.toString());
                                        if (ServiceDao.updateService(currentService, false)) {
                                            state.setText(EnumServiceState.ODRZUCONY.toString());
                                        }
                                        Toast.makeText(getActivity(), "Odrzucono", Toast.LENGTH_SHORT).show();
                                        if (UtilsDao.getIsAdmin() == false) { //access level
                                            saveButton.setVisibility(View.INVISIBLE);
                                            rejectButton.setVisibility(View.INVISIBLE);
                                            signButton.setVisibility(View.INVISIBLE);
                                        }
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

// SIGN BUTTON //
            signButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GPSUtils gpsUtils = new GPSUtils(getActivity(), getContext());
                    gpsUtils.getLastLocation();
                    new AlertDialog.Builder(getContext())
                            .setTitle("Wybierz wersje podpisania dokumentu")
                            .setPositiveButton("Elektroniczna", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (currentService.getState().equals(EnumServiceState.PODPISANY.toString())) {
                                        new AlertDialog.Builder(getContext())
                                                .setTitle("OSTRZEŻENIE!!!!!!")
                                                .setMessage("Ponowne podpisanie spowoduje usunięcie jego pierwotnej wersji. Czy napewno chcesz dokonać takiej operacji?")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        FragmentUtils.replaceFragment(new SignaturePadFragment(currentService, DeviceAddNewServiceFragment.this, Signatory.USER), getFragmentManager());
                                                    }
                                                })
                                                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // do nothing
                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    } else {
                                        FragmentUtils.replaceFragment(new SignaturePadFragment(currentService, DeviceAddNewServiceFragment.this, Signatory.USER), getFragmentManager());
                                    }
                                }
                            })
                            .setNegativeButton("Papierowa", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        if (ConnectionUtils.isInternetAvailable(getContext())) {
                                            currentService.setState(EnumServiceState.DODAJ_SKAN.toString());
                                            state.setText(EnumServiceState.DODAJ_SKAN.toString());
                                            addScanButton.setVisibility(View.VISIBLE);
                                            addScanButton.setClickable(true);
                                            progressDialog1 = ProgressDialog.show(getContext(), "Proszę czekać", "Tworzę pdf-a i wysyłam go e-mailem", true);
                                            progressDialog1.show(); // Display Progress Dialog
                                            progressDialog1.setCancelable(false);
                                            new Thread(new Runnable() {
                                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                                @Override
                                                public void run() {
                                                    try {
                                                        ServiceDao.updateService(currentService, true);
                                                        File file = ProtokolSerwisowyCreatePDF.createPdfServiceProtocol(getActivity(), currentService, null, null, null);
                                                        Uri path = Uri.fromFile(file);
                                                        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                                        emailIntent.setType("plain/text");
                                                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{currentCustomer.getEmail()});
                                                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Protokół przeglądu nr " + currentService.getId());
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
                                                }
                                            }).start();
                                        } else {
                                            MessageFragment messageFragment = new MessageFragment();
                                            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                            messageFragment.setMessage("Brak połączenia z internetem");
                                        }

                                    } catch (Exception e) {
                                        MessageFragment messageFragment = new MessageFragment();
                                        messageFragment.setMessage(e.getMessage());
                                        FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNeutralButton("Anuluj", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

// CHECKBOX PERIODIC SERVICE //
            periodicServiceCheckBox = v.findViewById(R.id.przeglad_okresowy_checkBox_add_service);
            periodicServiceCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setVisibleInspectionReport(periodicServiceCheckBox, inspectionReportButton);
                }
            });
// SET VISIBLE INSPECTION REPORT //
            setVisibleInspectionReport(periodicServiceCheckBox, inspectionReportButton);
            if (currentService != null) {
                loadDataToAllFields();
            }

// ATTACHMENTS BUTTON //
            attachmentsButton.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    isPressedAddScanButton = false;
                    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseFile.setType("*/*");
                    chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                    startActivityForResult(chooseFile, FILE_SELECT_CODE);
                }
            });

// PDF SHOW BUTTON //
            pdfShowButton.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    if (currentService != null && isEdit == false) {
                        progressDialog = ProgressDialog.show(getContext(), "Proszę czekać", "Przygotowywanie podglądu.", true);
                        progressDialog.show(); // Display Progress Dialog
                        progressDialog.setCancelable(false);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    File file = null;
                                    if (currentService.getState().equals(EnumServiceState.PODPISANY.toString())) {
                                        ArrayList<FileModel> filesList = new ArrayList<>();
                                        filesList = FileDao.findAllFilesByIdService(currentService.getId());
                                        if (filesList.size() == 1) {
                                            Log.e(TAG, "filesList: " + filesList.toString());
                                            FileModel fileModel = new FileModel();
                                            fileModel.setFilename(filesList.get(0).getFilename());
                                            file = FTPUtils.downloadFileFromFTP(fileModel);
                                        }
                                    } else {
                                        file = ProtokolSerwisowyCreatePDF.createPdfServiceProtocol(getActivity(), currentService, null, null, null);
                                    }
                                    Intent intent = new Intent("com.adobe.reader");
                                    intent.setType("application/pdf");
                                    intent.setAction(Intent.ACTION_VIEW);
                                    Uri uri1 = Uri.fromFile(file);
                                    intent.setDataAndType(uri1, "application/pdf");
                                    startActivity(intent);
                                } catch (Exception e) {
                                    MessageFragment messageFragment = new MessageFragment();
                                    FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                    messageFragment.setMessage(getString(R.string.no_connection) + "\n\n" + e.getMessage());
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } else {
                        Toast.makeText(getActivity(), "Żeby wyświetlić protokół musisz go najpierw zapisać", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            MessageFragment messageFragment = new MessageFragment();
            messageFragment.setMessage(e.getMessage());
            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
            e.printStackTrace();
        }

/////// GPS BUTTON //////////////////////////////////////////////////////////////////////////////////
        if (UtilsDao.getIsAdmin() == false) { //access level
            gpsButton.setVisibility(View.INVISIBLE);
        }
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Location location = ServiceDao.findLocationGPSofService(currentService.getId());
                    FragmentUtils.replaceFragment(new MapFragment(location, currentDevice), getFragmentManager());
                } catch (Exception e) {
                    MessageFragment messageFragment = new MessageFragment();
                    messageFragment.setMessage(e.getMessage());
                    FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                    e.printStackTrace();
                }
            }
        });

// LAYOUT FOR FILES //
        layoutForFiles = v.findViewById(R.id.layout_for_files);
        buildFileList();

// ADD SCAN BUTTON //
        addScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPressedAddScanButton = true;
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, FILE_SELECT_CODE);
            }
        });
//
        if (state.getText().toString().equals(EnumServiceState.DODAJ_SKAN.toString())) {
            addScanButton.setVisibility(View.VISIBLE);
            addScanButton.setClickable(true);
        } else {
            addScanButton.setVisibility(View.INVISIBLE);
            addScanButton.setClickable(false);
        }

        return v;
    }

    public void buildFileList() {
        try {
            layoutForFiles.removeAllViews();
            if (currentService != null) {
                final ArrayList<FileModel> filesList = FileDao.findAllFilesByIdService(currentService.getId());
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
                                if (currentService != null) {
                                    if (currentService.getState().equals(EnumServiceState.PODPISANY.toString())) {
                                        progressDialog1 = ProgressDialog.show(getContext(), "Proszę czekać", "Pobieram pdf-a z serwera.", true);
                                        progressDialog1.show(); // Display Progress Dialog
                                        progressDialog1.setCancelable(false);
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
                                                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Protokół przeglądu nr " + currentService.getId());
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
                                            }
                                        }).start();
                                    } else {
                                        Toast.makeText(getActivity(), "Żeby wysłać protokół najpierw należy go podpisać.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Żeby wysłać protokół trzeba go najpierw podpisac.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        if (UtilsDao.getIsAdmin()) { //access level
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
                                                        buildFileList();
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
                            public void onClick(View v) {////////
                                progressDialog = ProgressDialog.show(getContext(), "Proszę czekać", "Przygotowywanie podglądu.", true);
                                progressDialog.show(); // Display Progress Dialog
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
                                                intent.setType("image/*,video/*");
                                                Uri uri1 = Uri.fromFile(file);
                                                startActivity(intent);
                                            }
                                        } catch (Exception e) {
                                            MessageFragment messageFragment = new MessageFragment();
                                            messageFragment.setMessage(e.getMessage());
                                            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                            e.printStackTrace();
                                        }
                                    }

                                }).start();
                            }/////
                        });
                        layoutRows[i].addView(fileNameButtons[i], params1);
                        if (UtilsDao.getIsAdmin()) {//access level
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

    public void setSingnedState() {

        state.setText(EnumServiceState.PODPISANY.toString());
        if (UtilsDao.getIsAdmin() == false) {//access level
            saveButton.setVisibility(View.INVISIBLE);
            rejectButton.setVisibility(View.INVISIBLE);
            signButton.setVisibility(View.INVISIBLE);
        }
        if (state.getText().toString().equals(EnumServiceState.DODAJ_SKAN.toString())) {
            addScanButton.setVisibility(View.VISIBLE);
            addScanButton.setClickable(true);
        }
        pdfShowButton.setVisibility(View.INVISIBLE);
        attachmentsButton.setVisibility(View.VISIBLE);
        attachmentsButton.setClickable(true);
        gpsButton.setClickable(true);
        gpsButton.setVisibility(View.VISIBLE);
        inspectionReportButton.setClickable(true);
        isEdit = false;
    }

    private void loadDataToAllFields() {
        idService.setText(currentService.getId());
        numberService.setText(currentService.getNumber());
        dateButton.setText(currentService.getDate());
        state.setText(currentService.getState());
        if (currentService.getServiceType().contains(ServiceType.INSTALACJA)) {
            instalacjaCheckBox.setChecked(true);
        }
        if (currentService.getServiceType().contains(ServiceType.SZKOLENIE_OPERATOROW)) {
            szkolenieOperatorowCheckBox.setChecked(true);
        }
        if (currentService.getServiceType().contains(ServiceType.PRZEGLAD_OKRESOWY)) {
            przegladOkresowyCheckBox.setChecked(true);
        }
        if (currentService.getServiceType().contains(ServiceType.NAPRAWA)) {
            naprawaCheckBox.setChecked(true);
        }
        if (currentService.getPaymentType().toString().equals(PaymentType.PLATNA.toString())) {
            kosztPlatnaRadioButton.setChecked(true);
            Log.i(TAG, "loadToAllFields: platna");
        }
        if (currentService.getPaymentType().toString().equals(PaymentType.GWARANCYJNA.toString())) {
            kosztGwarancyjnaRadioButton.setChecked(true);
            Log.i(TAG, "loadToAllFields: gwarancyjna");
        }
        if (currentService.getPaymentType().toString().equals(PaymentType.DO_DECYZJI_GWARANTA.toString())) {
            kosztDoDecyzjiGwarantaCRadioButton.setChecked(true);
            Log.i(TAG, "loadToAllFields: do decyzji gwaranta");
        }
        opisUsterkiEditText.setText(currentService.getFaultDescription());
        zakresPracEditText.setText(currentService.getRangeOfWorks());
        zuzyteMaterialyEditText.setText(currentService.getMaterialsUsed());
        uwagiEditText.setText(currentService.getComments());
        startDatePickerButton.setText(currentService.getStartDate());
        startTimePicker.setText(currentService.getStartTime());
        endDatePickerButton.setText(currentService.getEndDate());
        endTimePicker.setText(currentService.getEndTime());
        czasPracyEditText.setText(currentService.getWorkingTime());
        dojazdEditText.setText(currentService.getDriveDistance());
        noclegEditText.setText(currentService.getDaysAtHotel());
        ControlsUtils.disableEnableControls(false, scrollView);
        attachmentsButton.setClickable(true);
        pdfShowButton.setClickable(true);
        gpsButton.setClickable(true);
        inspectionReportButton.setClickable(true);
        if (UtilsDao.getIsAdmin() == false) {//access level
            saveButton.setVisibility(View.INVISIBLE);
        }
        saveButton.setText("Edytuj");
        if (currentService.getState().equals(EnumServiceState.PODPISANY.toString())) {
            attachmentsButton.setVisibility(View.VISIBLE);
            gpsButton.setVisibility(View.VISIBLE);
            pdfShowButton.setVisibility(View.INVISIBLE);
        }
        if (currentService.getState().equals(EnumServiceState.UTWORZONY.toString())) {
            saveButton.setText("Edytuj");
            signButton.setVisibility(View.VISIBLE);
            rejectButton.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);
        }
        if (currentService.getDeviceInspectionReport() == null) {
            isInspectionReportCompletedCheckbox.setVisibility(View.INVISIBLE);
            inspectionReportButton.setVisibility(View.INVISIBLE);
        } else {
            isInspectionReportCompleted = true;
            isInspectionReportCompletedCheckbox.setChecked(isInspectionReportCompleted);
        }
        if (state.getText().toString().equals(EnumServiceState.DODAJ_SKAN.toString())) {
            addScanButton.setVisibility(View.VISIBLE);
            addScanButton.setClickable(true);
        } else {
            addScanButton.setVisibility(View.INVISIBLE);
            addScanButton.setClickable(false);
        }
    }

    public void setVisibleInspectionReport(CheckBox periodicServiceCheckBox, Button inspectionReportButton) {
        if (periodicServiceCheckBox.isChecked()) {
            isInspectionReportCompletedCheckbox.setVisibility(View.VISIBLE);
            inspectionReportButton.setVisibility(View.VISIBLE);
        } else {
            isInspectionReportCompletedCheckbox.setVisibility(View.INVISIBLE);
            inspectionReportButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "------pause-------" + isEdit);
        idService.setText(idService.getText());
        numberService.setText(numberService.getText());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (progressDialog1 != null) {
            progressDialog1.dismiss();
        }
        Log.i(TAG, "---------resume------" + isEdit);
        isInspectionReportCompletedCheckbox.setChecked(isInspectionReportCompleted);
        setVisibleInspectionReport(periodicServiceCheckBox, inspectionReportButton);
        if (currentService != null) {
            idService.setText(currentService.getId() + "");
            numberService.setText(currentService.getNumber() + "");
        }
        if (isEdit) {
            signButton.setVisibility(View.INVISIBLE);
            rejectButton.setVisibility(View.INVISIBLE);
            pdfShowButton.setVisibility(View.INVISIBLE);
            attachmentsButton.setVisibility(View.INVISIBLE);
            gpsButton.setVisibility(View.INVISIBLE);
            saveButton.setText("Zapisz");
            ControlsUtils.disableEnableControls(true, scrollView);
        }
        if (currentService != null) {
            idService.setText(currentService.getId());
            numberService.setText(currentService.getNumber());
        }

        if (showAfterSigning == true) {
            showAfterSigning = false;
            //initiate the button
            pdfShowButton.performClick();
            pdfShowButton.setPressed(true);
            pdfShowButton.invalidate();
            // delay completion till animation completes
            pdfShowButton.postDelayed(new Runnable() {  //delay button
                public void run() {
                    pdfShowButton.setPressed(false);
                    pdfShowButton.invalidate();
                    //any other associated action
                }
            }, 800);  // .8secs delay time
        }
    }


    public void setCurrentDevice(Device currentDevice) {
        this.currentDevice = currentDevice;
    }

    public void setFragmentInspectionReport(DeviceInspectionReportFragment fragmentInspectionReport) {
        this.fragmentInspectionReport = fragmentInspectionReport;
    }

    public void setInspectionReportCompleted(Boolean inspectionReportCompleted) {
        isInspectionReportCompleted = inspectionReportCompleted;
    }

    public Service getCurrentService() {
        return currentService;
    }

    public void setCurrentService(Service currentService) {
        this.currentService = currentService;
    }

    public void setCurrentInspectionReport(DeviceInspectionReport currentInspectionReport) {
        this.currentInspectionReport = currentInspectionReport;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    startDownloading();
                } else {
                    Toast.makeText(getContext(), "Permision denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void startDownloading() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Download");
        request.setDescription("Downloading file...");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "" + System.currentTimeMillis());
        DownloadManager manager = (DownloadManager) getContext().getSystemService(DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    public void setShowAfterSigning(boolean showAfterSigning) {
        this.showAfterSigning = showAfterSigning;
    }


//After choice file //
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {


        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
//                     Get the Uri of the selected file
                    final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    if (!isPressedAddScanButton) {
                        alert.setTitle("Wybierz typ pliku");
                        alert.setMessage("");
                    } else {
                        alert.setTitle("Zatwierdzenie serwisu");
                        alert.setMessage("Dodajesz skan protokołu serwisowego. Po dodaniu nie będzie już możliwości edycji!");

                    }
                    List<String> listFileTypeForSpinner = new ArrayList<>();

                    if (!isPressedAddScanButton) {
                        for (FileType value : FileType.values()) {
                            listFileTypeForSpinner.add(value.getName());
                        }
                    } else {
                        listFileTypeForSpinner.add(FileType.PROTOKOL_SERWISOWY.getName());
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
                            File file = null;
                            FileModel fileModel = null;
                            try {
                                path = FileUtils.getPath(getContext(), uri);
                                file = new File(path);
                                fileModel = new FileModel(null, null, currentService.getId(), currentService.getDate(), FileType.getFileTypeAfterName(spinner.getSelectedItem().toString()), descriptionEditText.getText().toString(), file.getName());
                                if (isPressedAddScanButton) {
                                    currentService.setState(EnumServiceState.PODPISANY.toString());
                                    try {
                                        ServiceDao.updateService(currentService, false);
                                    } catch (Exception e) {
                                        MessageFragment messageFragment = new MessageFragment();
                                        messageFragment.setMessage(e.getMessage());
                                        FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                        e.printStackTrace();
                                    }
                                    state.setText(EnumServiceState.PODPISANY.toString());
                                    addScanButton.setVisibility(View.INVISIBLE);
                                    setSingnedState();
                                    if (!UtilsDao.getIsAdmin()) {
                                        gpsButton.setVisibility(View.INVISIBLE);
                                    }
                                }
                            } catch (Exception e) {
                                MessageFragment messageFragment = new MessageFragment();
                                messageFragment.setMessage(e.getMessage());
                                FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                e.printStackTrace();
                            }

                            Log.e(ContentValues.TAG, "File Path: " + path);
                            final ProgressDialog progressDialog1 = ProgressDialog.show(getContext(), "Proszę czekać", "Przesyłam plik na serwer.", true);
                            progressDialog1.show(); // Display Progress Dialog
                            progressDialog1.setCancelable(false);


                            final File finalFile = file;
                            final FileModel finalFileModel = fileModel;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        FileDao.insertNewFile(finalFileModel);
                                        finalFileModel.setFilename(String.format("%07d", Integer.parseInt(finalFileModel.getId())) + "~" + currentCustomer.getShortName() + "~" + finalFileModel.getFileType().getAlias() + "~" + finalFileModel.getFilename());
                                        FileDao.updateDeviceFile(finalFileModel);
                                        boolean isUploadedCorrectly = FTPUtils.uploadFileToFTP(finalFile, finalFileModel);
                                        if (!isUploadedCorrectly) {
                                            FileDao.deleteDeviceFile(finalFileModel);
                                            Toast.makeText(getActivity(), "Przesyłanie zakończone niepowodzeniem! Sprawdź połaczenie lub przyznany dla aplikacji dostęp do plików.", Toast.LENGTH_LONG).show();
                                            currentService.setState(EnumServiceState.DODAJ_SKAN.toString());
                                            FileDao.deleteDeviceFile(finalFileModel);

                                            currentService.setState(EnumServiceState.DODAJ_SKAN.toString());
                                            state.setText(EnumServiceState.DODAJ_SKAN.toString());
                                            addScanButton.setVisibility(View.VISIBLE);
                                        }
                                        progressDialog1.dismiss();
                                    } catch (Exception e) {
                                        progressDialog1.dismiss();
                                        MessageFragment messageFragment = new MessageFragment();
                                        messageFragment.setMessage("Błąd pobierania path do pliku. Spróbuj wybrać plik bezpośrednio przeglądając katalogi od głównego np. /downloads (nie używać androidowego skrótu POBRANE itp.) lub sprawdź w androidowych ustawieniach aplikacji czy jest przyznany dostęp do plików! \n\n"+e.getMessage());
                                        FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                        e.printStackTrace();
                                    }
                                    //Run on primary Thread
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            buildFileList();
                                        }
                                    });
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
}
