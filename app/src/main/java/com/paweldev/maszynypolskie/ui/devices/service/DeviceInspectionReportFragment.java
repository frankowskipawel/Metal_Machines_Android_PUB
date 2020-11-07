package com.paweldev.maszynypolskie.ui.devices.service;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.paweldev.maszynypolskie.R;
import com.paweldev.maszynypolskie.repository.CategoryRepository;
import com.paweldev.maszynypolskie.model.Device;
import com.paweldev.maszynypolskie.model.DeviceInspectionReport;
import com.paweldev.maszynypolskie.model.Service;
import com.paweldev.maszynypolskie.ui.devices.service.enums.EnumServiceState;
import com.paweldev.maszynypolskie.ui.devices.service.enums.EnumValueServiceOperations;
import com.paweldev.maszynypolskie.utils.ControlsUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class DeviceInspectionReportFragment extends Fragment {

    private View v;
    private Device currentDevice;
    private Service currentService;
    private DeviceInspectionReport currentInspectionReport;
    private TextView[] textViewGroup;
    private RadioGroup[] radioGroup;
    private EditText diffrentPeriod;
    private int numberOfServiceCheckItems;
    private RadioGroup periodServiceGroup;
    private TextView deviceNameTextview;
    private Fragment previousFragment;
    private ScrollView scrollView;
    private Button okButton;
    private EditText descriptionEditText;
    private Boolean isEdit;
    private ArrayList serviceOperationsList;
    private EditText[][] extendedFieldsEditText;
    private TextView[][][] extendedFieldsTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_device_inspection_report, container, false);

// ADD NAME TO TOOLBAR //
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Protokół przeglądu");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");

        serviceOperationsList = CategoryRepository.findCategoryByName(currentDevice.getCategoryName()).getServiceReviewOperationsList();

        periodServiceGroup = v.findViewById(R.id.period_service_group);
        descriptionEditText = v.findViewById(R.id.description_device_inspection_report);
        deviceNameTextview = v.findViewById(R.id.device_name_text_view_inspection_report);
        if (serviceOperationsList != null) {
            numberOfServiceCheckItems = serviceOperationsList.size(); /// ilosć pól
        }
        textViewGroup = new TextView[numberOfServiceCheckItems];
        radioGroup = new RadioGroup[numberOfServiceCheckItems];
        diffrentPeriod = v.findViewById(R.id.difrent_period_inspection_report);
        okButton = v.findViewById(R.id.ok_button_device_inspection_report);
        scrollView = v.findViewById(R.id.scroll_view_device_inspection_report);

        if (currentInspectionReport == null) {
            createNewFields();
        } else {
            setFieldsFromServiceObject(currentInspectionReport);
            if (!isEdit) {
                ControlsUtils.disableEnableControls(false, scrollView);
            }
            if (currentService != null) {
                if (currentService.getState().equals(EnumServiceState.PODPISANY.toString()) || currentService.getState().equals(EnumServiceState.ODRZUCONY.toString())) {
                    okButton.setVisibility(View.INVISIBLE);
                }
            }
        }

// PREVIOUS BUTTON //
        final Button previousButton = v.findViewById(R.id.preview_button_device_inspection_report);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceAddNewServiceFragment prev = (DeviceAddNewServiceFragment) previousFragment;
                prev.setFragmentInspectionReport(DeviceInspectionReportFragment.this);
                prev.setCurrentInspectionReport(currentInspectionReport);
                getFragmentManager().popBackStack();

                DeviceAddNewServiceFragment prevFrg = (DeviceAddNewServiceFragment) previousFragment;
                if (isAllChecked()) {
                    prevFrg.setInspectionReportCompleted(true);
                } else {
                    prevFrg.setInspectionReportCompleted(false);
                }
            }
        });

// OK BUTTON //
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedPeriodService = null;
                if (isAllChecked()) {
                    int radioButtonID = periodServiceGroup.getCheckedRadioButtonId();
                    View radioButton = periodServiceGroup.findViewById(radioButtonID);
                    int idx = periodServiceGroup.indexOfChild(radioButton);
                    if (idx == 6) {
                        RadioButton r = (RadioButton) periodServiceGroup.getChildAt(idx);
                        selectedPeriodService = r.getText().toString() + " : " + diffrentPeriod.getText().toString();
                    } else {
                        RadioButton r = (RadioButton) periodServiceGroup.getChildAt(idx);
                        selectedPeriodService = r.getText().toString();
                    }
                    LinkedHashMap<String, String> serviceOperationsMap = new LinkedHashMap<>();
                    String extendedFieldsPasteToString = "";
                    for (int i = 0; i < numberOfServiceCheckItems; i++) {
                        int idRadio = radioGroup[i].getCheckedRadioButtonId();
                        if (extendedFieldsEditText[i] != null) {
                            extendedFieldsPasteToString = " @dodatkowePola{";
                            for (int e = 0; e < extendedFieldsEditText[i].length; e++) {
                                extendedFieldsPasteToString += extendedFieldsTextView[i][e][0].getText() + "+";
                                extendedFieldsPasteToString += extendedFieldsEditText[i][e].getText() + "+";
                                extendedFieldsPasteToString += extendedFieldsTextView[i][e][1].getText() + ";";
                            }
                            extendedFieldsPasteToString = extendedFieldsPasteToString.substring(0, extendedFieldsPasteToString.length() - 1) + "}";
                        }
                        View radio = radioGroup[i].findViewById(idRadio);
                        int id = radioGroup[i].indexOfChild(radio);
                        RadioButton rd = (RadioButton) radioGroup[i].getChildAt(id);
                        String selectedValue;
                        if (id >= 0) {
                            selectedValue = rd.getText().toString();
                        } else {
                            selectedValue = "";
                        }
                        serviceOperationsMap.put(textViewGroup[i].getText().toString(), selectedValue + extendedFieldsPasteToString);
                        extendedFieldsPasteToString = "";
                    }
                    DeviceInspectionReport deviceInspectionReport = new DeviceInspectionReport(
                            currentDevice.getId(),
                            selectedPeriodService,
                            descriptionEditText.getText().toString(),
                            serviceOperationsMap
                    );
                    DeviceAddNewServiceFragment prev = (DeviceAddNewServiceFragment) previousFragment;
                    prev.setCurrentInspectionReport(deviceInspectionReport);
                    if (prev.getCurrentService() != null) {
                        prev.getCurrentService().setDeviceInspectionReport(deviceInspectionReport);
                    }
                    prev.setFragmentInspectionReport(DeviceInspectionReportFragment.this);
                    prev.setInspectionReportCompleted(true);
                    getFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getActivity(), "Uzupełnij wszystkie pola", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    public void createNewFields() {
        deviceNameTextview.setText(currentDevice.getName() + "\nMoc źródła : " + currentDevice.getSourcePower() + "\nS/N : " + currentDevice.getSerialNumber() + "\nKat.: " + currentDevice.getCategoryName() + "\nData przekazania :" + currentDevice.getTransferDate());
        if (serviceOperationsList != null) {
            extendedFieldsEditText = new EditText[serviceOperationsList.size()][];
            extendedFieldsTextView = new TextView[serviceOperationsList.size()][][];
            for (int i = 0; i < serviceOperationsList.size(); i++) {
                LinearLayout linearLayoutForCheckbox = new LinearLayout(v.getContext());
                linearLayoutForCheckbox.setOrientation(LinearLayout.VERTICAL);
                textViewGroup[i] = new TextView(v.getContext());
                Log.i(TAG, "current device ++++++++" + currentDevice);
                textViewGroup[i].setText(serviceOperationsList.get(i).toString().replaceAll("\n@dodatkowePola.*", ""));
                textViewGroup[i].setBackgroundColor(Color.parseColor("#66808080"));
                textViewGroup[i].setTextColor(Color.parseColor("#000000"));
                textViewGroup[i].setTextSize(16);
                textViewGroup[i].setPadding(10, 5, 10, 0);
                linearLayoutForCheckbox.addView(textViewGroup[i]);
                radioGroup[i] = new RadioGroup(v.getContext());
                radioGroup[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                radioGroup[i].setOrientation(LinearLayout.HORIZONTAL);
                RadioButton radioButton1 = new RadioButton(v.getContext());
                radioButton1.setId(i);
                radioButton1.setText(EnumValueServiceOperations.NIEPOPRAWNE.toString());
                RadioButton radioButton2 = new RadioButton(v.getContext());
                radioButton2.setId(i + 100);
                radioButton2.setText(EnumValueServiceOperations.POPRAWNE.toString());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.weight = 1.0f;
                radioButton1.setLayoutParams(params);
                radioButton2.setLayoutParams(params);

                radioGroup[i].addView(radioButton1);
                radioGroup[i].addView(radioButton2);
                radioGroup[i].setGravity(Gravity.CENTER);

                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout linearlayout = (LinearLayout) v.findViewById(R.id.linear_layout_in_scrollView_service);
                LinearLayout linearLayoutForExtendedFields = new LinearLayout(getContext());
                linearLayoutForExtendedFields.setOrientation(LinearLayout.VERTICAL);
                linearLayoutForExtendedFields.setPadding(10, 0, 10, 0);
                String filteredContent = serviceOperationsList.get(i).toString().replaceAll(".*\n@dodatkowePola", "");
                String extendedFieldString = filteredContent.substring(1, filteredContent.length() - 1);
                String[] extendedFieldSplit = extendedFieldString.split(";");
                if (extendedFieldSplit[0].equals("")) {
                    extendedFieldSplit = null;
                }
                if (extendedFieldSplit != null && serviceOperationsList != null) {
                    extendedFieldsTextView[i] = new TextView[extendedFieldSplit.length][2];
                    extendedFieldsEditText[i] = new EditText[extendedFieldSplit.length];
                    for (int k = 0; k < extendedFieldSplit.length; k++) {
                        LinearLayout rowLinearLayout = new LinearLayout(getContext());
                        rowLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        rowLinearLayout.setPadding(150, 0, 150, 0);
                        Log.e(TAG, "\nlength split " + extendedFieldSplit.length + " =" + k + "= " + extendedFieldSplit[k]);
                        extendedFieldsTextView[i][k][0] = new TextView(getContext());
                        extendedFieldsTextView[i][k][0].setText(extendedFieldSplit[k].replaceFirst("([+]).*", ""));
                        rowLinearLayout.addView(extendedFieldsTextView[i][k][0]);
                        extendedFieldsEditText[i][k] = new EditText(getContext());
                        extendedFieldsEditText[i][k].setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                        extendedFieldsEditText[i][k].setGravity(Gravity.CENTER);
                        extendedFieldsEditText[i][k].setWidth(500);
                        rowLinearLayout.addView(extendedFieldsEditText[i][k]);
                        extendedFieldsTextView[i][k][1] = new TextView(getContext());
                        extendedFieldsTextView[i][k][1].setText(extendedFieldSplit[k].replaceFirst(".*([+])", ""));
                        rowLinearLayout.addView(extendedFieldsTextView[i][k][1]);
                        linearLayoutForExtendedFields.addView(rowLinearLayout);
                    }
                }
                linearLayoutForCheckbox.addView(linearLayoutForExtendedFields);
                linearLayoutForCheckbox.addView(radioGroup[i]);
                linearlayout.addView(linearLayoutForCheckbox, buttonParams);
            }
        }

    }

    public void setFieldsFromServiceObject(DeviceInspectionReport deviceInspectionReport) {

        deviceNameTextview.setText(currentDevice.getName() +
                "\nMoc źródła : " +
                currentDevice.getSourcePower() +
                "\nS/N : " + currentDevice.getSerialNumber() +
                "\nKat.: " + currentDevice.getCategoryName() +
                "\nData przekazania :" +
                currentDevice.getTransferDate());
        RadioButton radioButton6mcy = v.findViewById(R.id.radioButton6mcy);
        RadioButton radioButton12mcy = v.findViewById(R.id.radioButton12mcy);
        RadioButton radioButton18mcy = v.findViewById(R.id.radioButton18mcy);
        RadioButton radioButton24mcy = v.findViewById(R.id.radioButton24mcy);
        RadioButton radioButton30mcy = v.findViewById(R.id.radioButton30mcy);
        RadioButton radioButton36mcy = v.findViewById(R.id.radioButton36mcy);
        RadioButton radioButtonInny = v.findViewById(R.id.radioButtonInny);

        if (deviceInspectionReport.getPeriodicService().equals("6 m-cy")) {
            radioButton6mcy.setChecked(true);
        }
        if (deviceInspectionReport.getPeriodicService().equals("12 m-cy")) {
            radioButton12mcy.setChecked(true);
        }
        if (deviceInspectionReport.getPeriodicService().equals("18 m-cy")) {
            radioButton18mcy.setChecked(true);
        }
        if (deviceInspectionReport.getPeriodicService().equals("24 m-ce")) {
            radioButton24mcy.setChecked(true);
        }
        if (deviceInspectionReport.getPeriodicService().equals("30 m-cy")) {
            radioButton30mcy.setChecked(true);
        }
        if (deviceInspectionReport.getPeriodicService().equals("36 m-cy")) {
            radioButton36mcy.setChecked(true);
        }
        if (deviceInspectionReport.getPeriodicService().contains("inny")) {
            diffrentPeriod.setText(deviceInspectionReport.getPeriodicService().substring(7));
            radioButtonInny.setChecked(true);
        }
        extendedFieldsEditText = new EditText[500][];
        extendedFieldsTextView = new TextView[500][][];
        descriptionEditText.setText(deviceInspectionReport.getDescription());
        int i = 0;
        for (String key : deviceInspectionReport.getServiceOperations().keySet()) {
            String value = deviceInspectionReport.getServiceOperations().get(key);
            LinearLayout linearLayoutForCheckbox = new LinearLayout(v.getContext());
            linearLayoutForCheckbox.setOrientation(LinearLayout.VERTICAL);
            textViewGroup[i] = new TextView(v.getContext());
            textViewGroup[i].setText(key);
            textViewGroup[i].setBackgroundColor(Color.parseColor("#66808080"));
            textViewGroup[i].setTextColor(Color.parseColor("#000000"));
            textViewGroup[i].setTextSize(16);
            textViewGroup[i].setPadding(10, 5, 10, 0);
            linearLayoutForCheckbox.addView(textViewGroup[i]);
            radioGroup[i] = new RadioGroup(v.getContext());
            radioGroup[i].setGravity(Gravity.CENTER);
            radioGroup[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            radioGroup[i].setOrientation(LinearLayout.HORIZONTAL);
            RadioButton radioButton1 = new RadioButton(v.getContext());
            radioButton1.setId(i);
            radioButton1.setText(EnumValueServiceOperations.NIEPOPRAWNE.toString());
            if (value.replaceAll(" @dodatkowePola.*", "").equals(EnumValueServiceOperations.NIEPOPRAWNE.toString())) {
                radioButton1.setChecked(true);
            }
            RadioButton radioButton2 = new RadioButton(v.getContext());
            radioButton2.setId(i + 100);
            radioButton2.setText(EnumValueServiceOperations.POPRAWNE.toString());
            if (value.replaceAll(" @dodatkowePola.*", "").equals(EnumValueServiceOperations.POPRAWNE.toString())) {
                radioButton2.setChecked(true);
            }
            // ładowanie i tworzenie pól rozszeżonych
            String extendedFieldString = value.replaceAll(".*@dodatkowePola", "");
            if (value.contains("@dodatkowePola")) {
                String[] extendedFieldSplit = extendedFieldString.substring(1, extendedFieldString.length() - 1).split(";");
                extendedFieldsEditText[i] = new EditText[extendedFieldSplit.length];
                extendedFieldsTextView[i] = new TextView[extendedFieldSplit.length][2];
                for (int j = 0; j < extendedFieldSplit.length; j++) {
                    extendedFieldsEditText[i][j] = new EditText(getContext());
                    extendedFieldsTextView[i][j][0] = new TextView(getContext());
                    String[] extendedSplitValue = extendedFieldSplit[j].split("\\+");
                    Log.i(TAG, "extendedSplitValue = " + extendedSplitValue.length);
                    extendedFieldsTextView[i][j][0].setText(extendedSplitValue[0]);
                    if (extendedFieldSplit.length > 0) {
                        LinearLayout linearLayoutForExtendedFields = new LinearLayout(getContext());
                        linearLayoutForExtendedFields.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayoutForExtendedFields.setPadding(150, 0, 150, 0);
                        extendedFieldsEditText[i][j].setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                        extendedFieldsEditText[i][j].setGravity(Gravity.CENTER);
                        extendedFieldsEditText[i][j].setText(extendedSplitValue[1]);
                        Log.i(TAG, "setFieldsFromServiceObject: 222222222222222 " + extendedFieldSplit[j]);
                        extendedFieldsTextView[i][j][1] = new TextView(getContext());
                        extendedFieldsTextView[i][j][1].setText(extendedSplitValue[2]);
                        linearLayoutForExtendedFields.addView(extendedFieldsTextView[i][j][0]);
                        linearLayoutForExtendedFields.addView(extendedFieldsEditText[i][j]);
                        linearLayoutForExtendedFields.addView(extendedFieldsTextView[i][j][1]);
                        linearLayoutForCheckbox.addView(linearLayoutForExtendedFields);
                    }
                }

            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1.0f;
            radioButton1.setLayoutParams(params);
            radioButton2.setLayoutParams(params);
            radioGroup[i].addView(radioButton1);
            radioGroup[i].addView(radioButton2);
            linearLayoutForCheckbox.addView(radioGroup[i]);
            LinearLayout linearlayout = (LinearLayout) v.findViewById(R.id.linear_layout_in_scrollView_service);
            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            linearlayout.addView(linearLayoutForCheckbox, buttonParams);
            i++;
        }
    }

    private boolean isAllChecked() {
        for (int i = 0; i < numberOfServiceCheckItems; i++) {
            int radioButtonID = periodServiceGroup.getCheckedRadioButtonId();
            View radioButton = periodServiceGroup.findViewById(radioButtonID);
            if (periodServiceGroup.indexOfChild(radioButton) == -1) {
                return false;
            }
        }
        boolean isExistIncorrect = false;
        for (int j = 0; j < numberOfServiceCheckItems; j++) {
            int idRadio = radioGroup[j].getCheckedRadioButtonId();
            View radio = radioGroup[j].findViewById(idRadio);
            if (radioGroup[j].indexOfChild(radio) == 0) {
                isExistIncorrect = true;
            }
            if (radioGroup[j].indexOfChild(radio) == -1) {
                return false;
            }
            if (isExistIncorrect && descriptionEditText.getText().length() < 3) {
                Toast.makeText(getActivity(), "Jedna z czynności jest oznaczona jako nieprawidłowa musisz opisać ją w uwagach!", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isEdit) {
            ControlsUtils.disableEnableControls(true, scrollView);
            okButton.setVisibility(View.VISIBLE);
        } else {
            ControlsUtils.disableEnableControls(false, scrollView);
            okButton.setVisibility(View.INVISIBLE);
        }
    }

    public void setCurrentDevice(Device currentDevice) {
        this.currentDevice = currentDevice;
    }

    public Fragment getPreviousFragment() {
        return previousFragment;
    }

    public void setPreviousFragment(Fragment previousFragment) {
        this.previousFragment = previousFragment;
    }

    public void setCurrentService(Service currentService) {
        this.currentService = currentService;
    }

    public void setEdit(Boolean edit) {
        isEdit = edit;
    }

    public void setCurrentInspectionReport(DeviceInspectionReport currentInspectionReport) {
        this.currentInspectionReport = currentInspectionReport;
    }
}
