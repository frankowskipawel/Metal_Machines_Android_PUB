package com.pl.metalmachines.ui.devices.tabfragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.pl.metalmachines.R;
import com.pl.metalmachines.model.parametersDevice.Gaz;
import com.pl.metalmachines.model.parametersDevice.ciecie.Ciecie;
import com.pl.metalmachines.model.parametersDevice.ciecie.ParametryGlowne;
import com.pl.metalmachines.model.parametersDevice.ciecie.RuszWolno;
import com.pl.metalmachines.model.parametersDevice.ciecie.StopWolno;
import com.pl.metalmachines.ui.devices.Mode;
import com.pl.metalmachines.utils.ParametersUtils;

import java.util.ArrayList;
import java.util.List;

public class Tab1Fragment extends Fragment {

    private View v;
    private List<EditText> listEditText = new ArrayList<EditText>();
    private Ciecie currentCiecie = ParametersUtils.getBlankParametersDevice().getCiecie();
    private Mode currentMode = Mode.SHOW_PARAMETERS;

    private EditText predkoscCiecia;
    private EditText wysokoscPodnoszenia;
    private EditText wysokoscCiecia;
    private Spinner gazCiecia;
    private EditText cisnienieCiecia;
    private EditText natezenieCieciaProcent;
    private EditText mocciecia;
    private EditText czestotliwoscPrzebiegu;
    private EditText szerokoscWiazki;
    private EditText focus;
    private EditText czasZwloki;
    private EditText opoznienieWylaczania;
    private EditText ruszWolnoDlugosc;
    private EditText ruszWolnoPredkosc;
    private EditText stopWolnoDlugosc;
    private EditText stopWolnoPredkosc;
    private List listGazCieciaForSpinner = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_devices_show_parameters_tab_1, container, false);

        listEditText.add(predkoscCiecia = (EditText) v.findViewById(R.id.predkosc_ciecia_edit_text_ciecie));
        listEditText.add(wysokoscPodnoszenia = (EditText) v.findViewById(R.id.wysokosc_podnoszenia_edit_text_ciecie));
        listEditText.add(wysokoscCiecia = (EditText) v.findViewById(R.id.wysokosc_ciecia_edit_text_ciecie));
        gazCiecia = (Spinner) v.findViewById(R.id.gaz_ciecia_spinner_ciecie);
        listEditText.add(cisnienieCiecia = (EditText) v.findViewById(R.id.cisnienie_ciecia_edit_text_ciecie));
        listEditText.add(natezenieCieciaProcent = (EditText) v.findViewById(R.id.natezenie_ciecia_procent_edit_text_ciecie));
        listEditText.add(mocciecia = (EditText) v.findViewById(R.id.moc_ciecia_edit_text_ciecie));
        listEditText.add(czestotliwoscPrzebiegu = (EditText) v.findViewById(R.id.czestotliwosc_przebiegu_edit_text_ciecie));
        listEditText.add(szerokoscWiazki = (EditText) v.findViewById(R.id.szerokosc_wiazki_edit_text_ciecie));
        listEditText.add(focus = (EditText) v.findViewById(R.id.focus_edit_text_ciecie));
        listEditText.add(czasZwloki = (EditText) v.findViewById(R.id.czas_zwloki_edit_text_ciecie));
        listEditText.add(opoznienieWylaczania = (EditText) v.findViewById(R.id.opoznienie_wylaczenia_wiazki_edit_text_ciecie));
        listEditText.add(ruszWolnoDlugosc = (EditText) v.findViewById(R.id.rusz_wolno_dlugosc_edit_text_ciecie));
        listEditText.add(ruszWolnoPredkosc = (EditText) v.findViewById(R.id.rusz_wolno_predkosc_edit_text_ciecie));
        listEditText.add(stopWolnoDlugosc = (EditText) v.findViewById(R.id.stop_wolno_dlugosc_edit_text_ciecie));
        listEditText.add(stopWolnoPredkosc = (EditText) v.findViewById(R.id.stop_wolno_predkosc_edit_text_ciecie));

        ParametersUtils.setGazListOnSpinner(getContext(),gazCiecia);
        loadParametersToTextView();
        setControlsOnMode();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadParametersToTextView();
        setControlsOnMode();
    }

    @Override
    public void onPause() {
        super.onPause();

        saveParametersToCiecie();
    }

    public void loadParametersToTextView() {

        if (currentMode == Mode.SHOW_PARAMETERS || currentMode==Mode.ADD_NEW_PARAMETERS) {
            predkoscCiecia.setText(String.valueOf(currentCiecie.getParametryGlowne().getPredkoscCiecia()));
            wysokoscPodnoszenia.setText(String.valueOf(currentCiecie.getParametryGlowne().getWysokoscPodnoszenia()));
            wysokoscCiecia.setText(String.valueOf(currentCiecie.getParametryGlowne().getWysokoscCiecia()));
            ParametersUtils.setGazListOnSpinner(getContext(),gazCiecia);
            for (int i=0; i<gazCiecia.getAdapter().getCount(); i++){ if (gazCiecia.getItemAtPosition(i).toString().equals(currentCiecie.getParametryGlowne().getGazCiecia())){
                    gazCiecia.setSelection(i);
                }
            }
            cisnienieCiecia.setText(String.valueOf(currentCiecie.getParametryGlowne().getCisnienieCiecia().toString()));
            natezenieCieciaProcent.setText(String.valueOf(currentCiecie.getParametryGlowne().getNatezenieCieciaProcent()));
            mocciecia.setText(String.valueOf(currentCiecie.getParametryGlowne().getMocCiecia()));
            czestotliwoscPrzebiegu.setText(String.valueOf(currentCiecie.getParametryGlowne().getCzestotliwoscPrzebiegu()));
            szerokoscWiazki.setText(String.valueOf(currentCiecie.getParametryGlowne().getSzerokoscWiazki()));
            focus.setText(String.valueOf(currentCiecie.getParametryGlowne().getFocus()));
            czasZwloki.setText(String.valueOf(currentCiecie.getParametryGlowne().getCzasZwloki()));
            opoznienieWylaczania.setText(String.valueOf(currentCiecie.getParametryGlowne().getOpoznienieWylaczeniaWiazki()));
            ruszWolnoDlugosc.setText(String.valueOf(currentCiecie.getRuszWolno().getDlugosc()));
            ruszWolnoPredkosc.setText(String.valueOf(currentCiecie.getRuszWolno().getPredkosc().toString()));
            stopWolnoDlugosc.setText(String.valueOf(currentCiecie.getStopWolno().getDlugosc()));
            stopWolnoPredkosc.setText(String.valueOf(currentCiecie.getStopWolno().getPredkosc().toString()));
        }

    }

    public void setControlsOnMode() {

        if (currentMode == Mode.ADD_NEW_PARAMETERS) {
            for (EditText edittext : listEditText) {
                edittext.setEnabled(true);
            }
            gazCiecia.setEnabled(true);

            for (Gaz value : Gaz.values()) {
                listGazCieciaForSpinner.add(value.name());
            }

        } else if (currentMode == Mode.SHOW_PARAMETERS) {
            for (EditText edittext : listEditText) {
                edittext.setEnabled(false);
            }
            gazCiecia.setEnabled(false);
        } else if (currentMode == Mode.EDIT_PARAMETERS) {
            for (EditText edittext : listEditText) {
                edittext.setEnabled(true);
            }
            gazCiecia.setEnabled(true);
        }
    }

    public void saveParametersToCiecie() {

        currentCiecie = new Ciecie(
                new ParametryGlowne(
                        predkoscCiecia.getText().toString(),
                        wysokoscPodnoszenia.getText().toString(),
                        wysokoscCiecia.getText().toString(),
                        gazCiecia.getSelectedItem().toString(),
                        cisnienieCiecia.getText().toString(),
                        natezenieCieciaProcent.getText().toString(),
                        mocciecia.getText().toString(),
                        czestotliwoscPrzebiegu.getText().toString(),
                        szerokoscWiazki.getText().toString(),
                        focus.getText().toString(),
                        czasZwloki.getText().toString(),
                        opoznienieWylaczania.getText().toString()),
                new RuszWolno(
                        ruszWolnoDlugosc.getText().toString(),
                        ruszWolnoPredkosc.getText().toString()
                ),
                new StopWolno(
                        stopWolnoDlugosc.getText().toString(),
                        stopWolnoPredkosc.getText().toString())
        );
    }

    public Ciecie getCurrentCiecie() {
        return currentCiecie;
    }

    public void setCurrentCiecie(Ciecie currentCiecie) {
        this.currentCiecie = currentCiecie;
    }

    public void setCurrentMode(Mode currentMode) {
        this.currentMode = currentMode;
    }
}