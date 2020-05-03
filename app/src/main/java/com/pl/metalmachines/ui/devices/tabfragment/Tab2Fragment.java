package com.pl.metalmachines.ui.devices.tabfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.pl.metalmachines.R;
import com.pl.metalmachines.model.parametersDevice.przebijanie.Etap;
import com.pl.metalmachines.model.parametersDevice.przebijanie.Przebijanie;
import com.pl.metalmachines.ui.devices.Mode;
import com.pl.metalmachines.utils.ParametersUtils;

import java.util.ArrayList;
import java.util.List;

public class Tab2Fragment extends Fragment {

    private View v;

    private List<EditText> listEditText = new ArrayList<EditText>();
    private Przebijanie currentPrzebijanie = ParametersUtils.getBlankParametersDevice().getPrzebijanie();
    private Mode currentMode = Mode.SHOW_PARAMETERS;

    private EditText etap1CzasKroku;
    private EditText etap1WysokoscPrzeb;
    private Spinner etap1GazPrzeb;
    private EditText etap1CisnieniePrzeb;
    private EditText etap1NatezeniePrzebProcent;
    private EditText etap1MocPrzeb;
    private EditText etap1CzestotliwoscPrzeb;
    private EditText etap1SzerokoscWiazki;
    private EditText etap1FocusPrzeb;
    private EditText etap1CzasPrzeb;
    private EditText etap1Przedmuch;

    private EditText etap2CzasKroku;
    private EditText etap2WysokoscPrzeb;
    private Spinner etap2GazPrzeb;
    private EditText etap2CisnieniePrzeb;
    private EditText etap2NatezeniePrzebProcent;
    private EditText etap2MocPrzeb;
    private EditText etap2CzestotliwoscPrzeb;
    private EditText etap2SzerokoscWiazki;
    private EditText etap2FocusPrzeb;
    private EditText etap2CzasPrzeb;
    private EditText etap2Przedmuch;

    private EditText etap3CzasKroku;
    private EditText etap3WysokoscPrzeb;
    private Spinner etap3GazPrzeb;
    private EditText etap3CisnieniePrzeb;
    private EditText etap3NatezeniePrzebProcent;
    private EditText etap3MocPrzeb;
    private EditText etap3CzestotliwoscPrzeb;
    private EditText etap3SzerokoscWiazki;
    private EditText etap3FocusPrzeb;
    private EditText etap3CzasPrzeb;
    private EditText etap3Przedmuch;

    private List listGazPrzebijanieForSpinners = new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_devices_show_parameters_tab_2, container, false);

        listEditText.add(etap1CzasKroku = (EditText) v.findViewById(R.id.etap1_czas_kroku));
        listEditText.add(etap1WysokoscPrzeb = (EditText) v.findViewById(R.id.etap1_wysokosc_przeb));
        etap1GazPrzeb = (Spinner) v.findViewById(R.id.etap1_gaz_przeb);
        listEditText.add(etap1CisnieniePrzeb = (EditText) v.findViewById(R.id.etap1_cisnienie_przeb));
        listEditText.add(etap1NatezeniePrzebProcent = (EditText) v.findViewById(R.id.etap1_natezenie_przeb_procent));
        listEditText.add(etap1MocPrzeb = (EditText) v.findViewById(R.id.etap1_moc_przeb));
        listEditText.add(etap1CzestotliwoscPrzeb = (EditText) v.findViewById(R.id.etap1_czestotliwosc_przeb));
        listEditText.add(etap1SzerokoscWiazki = (EditText) v.findViewById(R.id.etap1_szerokosc_wiazki));
        listEditText.add(etap1FocusPrzeb = (EditText) v.findViewById(R.id.etap1_focus_przeb));
        listEditText.add(etap1CzasPrzeb = (EditText) v.findViewById(R.id.etap1_czas_przeb));
        listEditText.add(etap1Przedmuch = (EditText) v.findViewById(R.id.etap1_przedmuch));
        listEditText.add(etap2CzasKroku = (EditText) v.findViewById(R.id.etap2_czas_kroku));
        listEditText.add(etap2WysokoscPrzeb = (EditText) v.findViewById(R.id.etap2_wysokosc_przeb));
        etap2GazPrzeb = (Spinner) v.findViewById(R.id.etap2_gaz_przeb);
        listEditText.add(etap2CisnieniePrzeb = (EditText) v.findViewById(R.id.etap2_cisnienie_przeb));
        listEditText.add(etap2NatezeniePrzebProcent = (EditText) v.findViewById(R.id.etap2_natezenie_przeb_procent));
        listEditText.add(etap2MocPrzeb = (EditText) v.findViewById(R.id.etap2_moc_przeb));
        listEditText.add(etap2CzestotliwoscPrzeb = (EditText) v.findViewById(R.id.etap2_czestotliwosc_przeb));
        listEditText.add(etap2SzerokoscWiazki = (EditText) v.findViewById(R.id.etap2_szerokosc_wiazki));
        listEditText.add(etap2FocusPrzeb = (EditText) v.findViewById(R.id.etap2_focus_przeb));
        listEditText.add(etap2CzasPrzeb = (EditText) v.findViewById(R.id.etap2_czas_przeb));
        listEditText.add(etap2Przedmuch = (EditText) v.findViewById(R.id.etap2_przedmuch));
        listEditText.add(etap3CzasKroku = (EditText) v.findViewById(R.id.etap3_czas_kroku));
        listEditText.add(etap3WysokoscPrzeb = (EditText) v.findViewById(R.id.etap3_wysokosc_przeb));
        etap3GazPrzeb = (Spinner) v.findViewById(R.id.etap3_gaz_przeb);
        listEditText.add(etap3CisnieniePrzeb = (EditText) v.findViewById(R.id.etap3_cisnienie_przeb));
        listEditText.add(etap3NatezeniePrzebProcent = (EditText) v.findViewById(R.id.etap3_natezenie_przeb_procent));
        listEditText.add(etap3MocPrzeb = (EditText) v.findViewById(R.id.etap3_moc_przeb));
        listEditText.add(etap3CzestotliwoscPrzeb = (EditText) v.findViewById(R.id.etap3_czestotliwosc_przeb));
        listEditText.add(etap3SzerokoscWiazki = (EditText) v.findViewById(R.id.etap3_szerokosc_wiazki));
        listEditText.add(etap3FocusPrzeb = (EditText) v.findViewById(R.id.etap3_focus_przeb));
        listEditText.add(etap3CzasPrzeb = (EditText) v.findViewById(R.id.etap3_czas_przeb));
        listEditText.add(etap3Przedmuch = (EditText) v.findViewById(R.id.etap3_przedmuch));

        ParametersUtils.setGazListOnSpinner(getContext(), etap1GazPrzeb);
        ParametersUtils.setGazListOnSpinner(getContext(), etap2GazPrzeb);
        ParametersUtils.setGazListOnSpinner(getContext(), etap3GazPrzeb);

        loadParametersToTextView();
        setControlsByMode();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadParametersToTextView();
        setControlsByMode();
    }

    @Override
    public void onPause() {
        super.onPause();

        saveParametersToPrzebijanie();
    }

    public void loadParametersToTextView() {
        if (currentMode == Mode.SHOW_PARAMETERS || currentMode == Mode.ADD_NEW_PARAMETERS) {
            etap1CzasKroku.setText(String.valueOf(currentPrzebijanie.getEtap1().getCzasKroku()));
            etap1WysokoscPrzeb.setText(String.valueOf(currentPrzebijanie.getEtap1().getWysokoscPrzebijania()));
            ParametersUtils.setGazListOnSpinner(getContext(),etap1GazPrzeb);
            for (int i = 0; i < etap1GazPrzeb.getAdapter().getCount(); i++) {
                if (etap1GazPrzeb.getItemAtPosition(i).toString().equals(currentPrzebijanie.getEtap1().getGazPrzebijania())) {
                    etap1GazPrzeb.setSelection(i);
                }
            }
            etap1CisnieniePrzeb.setText(String.valueOf(currentPrzebijanie.getEtap1().getCisnieniePrzebijania()));
            etap1NatezeniePrzebProcent.setText(String.valueOf(currentPrzebijanie.getEtap1().getNatezeniePrzebijaniaProcent()));
            etap1MocPrzeb.setText(String.valueOf(currentPrzebijanie.getEtap1().getMocPrzebijania()));
            etap1CzestotliwoscPrzeb.setText(String.valueOf(currentPrzebijanie.getEtap1().getCzestotliwoscPrzebijania()));
            etap1SzerokoscWiazki.setText(String.valueOf(currentPrzebijanie.getEtap1().getSzerokoscWiazki()));
            etap1FocusPrzeb.setText(String.valueOf(currentPrzebijanie.getEtap1().getFocusPrzebijania()));
            etap1CzasPrzeb.setText(String.valueOf(currentPrzebijanie.getEtap1().getCzasPrzebijania()));
            etap1Przedmuch.setText(String.valueOf(currentPrzebijanie.getEtap1().getPrzedmuch()));
            etap2CzasKroku.setText(String.valueOf(currentPrzebijanie.getEtap2().getCzasKroku()));
            etap2WysokoscPrzeb.setText(String.valueOf(currentPrzebijanie.getEtap2().getWysokoscPrzebijania()));
            ParametersUtils.setGazListOnSpinner(getContext(),etap2GazPrzeb);
            for (int i = 0; i < etap2GazPrzeb.getAdapter().getCount(); i++) {
                if (etap2GazPrzeb.getItemAtPosition(i).toString().equals(currentPrzebijanie.getEtap2().getGazPrzebijania())) {
                    etap2GazPrzeb.setSelection(i);
                }
            }
            etap2CisnieniePrzeb.setText(String.valueOf(currentPrzebijanie.getEtap2().getCisnieniePrzebijania()));
            etap2NatezeniePrzebProcent.setText(String.valueOf(currentPrzebijanie.getEtap2().getNatezeniePrzebijaniaProcent()));
            etap2MocPrzeb.setText(String.valueOf(currentPrzebijanie.getEtap2().getMocPrzebijania()));
            etap2CzestotliwoscPrzeb.setText(String.valueOf(currentPrzebijanie.getEtap2().getCzestotliwoscPrzebijania()));
            etap2SzerokoscWiazki.setText(String.valueOf(currentPrzebijanie.getEtap2().getSzerokoscWiazki()));
            etap2FocusPrzeb.setText(String.valueOf(currentPrzebijanie.getEtap2().getFocusPrzebijania()));
            etap2CzasPrzeb.setText(String.valueOf(currentPrzebijanie.getEtap2().getCzasPrzebijania()));
            etap2Przedmuch.setText(String.valueOf(currentPrzebijanie.getEtap2().getPrzedmuch()));
            etap3CzasKroku.setText(String.valueOf(currentPrzebijanie.getEtap3().getCzasKroku()));
            etap3WysokoscPrzeb.setText(String.valueOf(currentPrzebijanie.getEtap3().getWysokoscPrzebijania()));
            ParametersUtils.setGazListOnSpinner(getContext(),etap3GazPrzeb);
            for (int i = 0; i < etap3GazPrzeb.getAdapter().getCount(); i++) {
                if (etap3GazPrzeb.getItemAtPosition(i).toString().equals(currentPrzebijanie.getEtap3().getGazPrzebijania())) {
                    etap3GazPrzeb.setSelection(i);
                }
            }
            etap3CisnieniePrzeb.setText(String.valueOf(currentPrzebijanie.getEtap3().getCisnieniePrzebijania()));
            etap3NatezeniePrzebProcent.setText(String.valueOf(currentPrzebijanie.getEtap3().getNatezeniePrzebijaniaProcent()));
            etap3MocPrzeb.setText(String.valueOf(currentPrzebijanie.getEtap3().getMocPrzebijania()));
            etap3CzestotliwoscPrzeb.setText(String.valueOf(currentPrzebijanie.getEtap3().getCzestotliwoscPrzebijania()));
            etap3SzerokoscWiazki.setText(String.valueOf(currentPrzebijanie.getEtap3().getSzerokoscWiazki()));
            etap3FocusPrzeb.setText(String.valueOf(currentPrzebijanie.getEtap3().getFocusPrzebijania()));
            etap3CzasPrzeb.setText(String.valueOf(currentPrzebijanie.getEtap3().getCzasPrzebijania()));
            etap3Przedmuch.setText(String.valueOf(currentPrzebijanie.getEtap3().getPrzedmuch()));
        }
    }

    public void setControlsByMode() {
        if (currentMode == Mode.ADD_NEW_PARAMETERS) {
            for (EditText edittext : listEditText) {
                edittext.setEnabled(true);
            }
            etap1GazPrzeb.setEnabled(true);
            etap2GazPrzeb.setEnabled(true);
            etap3GazPrzeb.setEnabled(true);
        } else if (currentMode == Mode.SHOW_PARAMETERS) {
            for (EditText edittext : listEditText) {
                edittext.setEnabled(false);
            }
            etap1GazPrzeb.setEnabled(false);
            etap2GazPrzeb.setEnabled(false);
            etap3GazPrzeb.setEnabled(false);
        } else if (currentMode == Mode.EDIT_PARAMETERS) {
            for (EditText edittext : listEditText) {
                edittext.setEnabled(true);
            }
            etap1GazPrzeb.setEnabled(true);
            etap2GazPrzeb.setEnabled(true);
            etap3GazPrzeb.setEnabled(true);
        }
    }

    public void saveParametersToPrzebijanie() {
        currentPrzebijanie = new Przebijanie(
                new Etap(etap1CzasKroku.getText().toString(),
                        etap1WysokoscPrzeb.getText().toString(),
                        etap1GazPrzeb.getSelectedItem().toString(),
                        etap1CisnieniePrzeb.getText().toString(),
                        etap1NatezeniePrzebProcent.getText().toString(),
                        etap1MocPrzeb.getText().toString(),
                        etap1CzestotliwoscPrzeb.getText().toString(),
                        etap1SzerokoscWiazki.getText().toString(),
                        etap1FocusPrzeb.getText().toString(),
                        etap1CzasPrzeb.getText().toString(),
                        etap1Przedmuch.getText().toString()),
                new Etap(etap2CzasKroku.getText().toString(),
                        etap2WysokoscPrzeb.getText().toString(),
                        etap2GazPrzeb.getSelectedItem().toString(),
                        etap2CisnieniePrzeb.getText().toString(),
                        etap2NatezeniePrzebProcent.getText().toString(),
                        etap2MocPrzeb.getText().toString(),
                        etap2CzestotliwoscPrzeb.getText().toString(),
                        etap2SzerokoscWiazki.getText().toString(),
                        etap2FocusPrzeb.getText().toString(),
                        etap2CzasPrzeb.getText().toString(),
                        etap2Przedmuch.getText().toString()),
                new Etap(etap3CzasKroku.getText().toString(),
                        etap3WysokoscPrzeb.getText().toString(),
                        etap3GazPrzeb.getSelectedItem().toString(),
                        etap3CisnieniePrzeb.getText().toString(),
                        etap3NatezeniePrzebProcent.getText().toString(),
                        etap3MocPrzeb.getText().toString(),
                        etap3CzestotliwoscPrzeb.getText().toString(),
                        etap3SzerokoscWiazki.getText().toString(),
                        etap3FocusPrzeb.getText().toString(),
                        etap3CzasPrzeb.getText().toString(),
                        etap3Przedmuch.getText().toString()));
    }

    public Przebijanie getCurrentPrzebijanie() {
        return currentPrzebijanie;
    }

    public void setCurrentPrzebijanie(Przebijanie przebijanie) {
        this.currentPrzebijanie = przebijanie;
    }

    public void setCurrentMode(Mode mode) {
        this.currentMode = mode;
    }
}

