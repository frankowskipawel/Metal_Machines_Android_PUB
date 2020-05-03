package com.pl.metalmachines.ui.devices.tabfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.pl.metalmachines.R;
import com.pl.metalmachines.model.parametersDevice.wpalenie.ParametryWpalenia;
import com.pl.metalmachines.model.parametersDevice.wpalenie.WpalajKolem;
import com.pl.metalmachines.model.parametersDevice.wpalenie.WpalajWolno;
import com.pl.metalmachines.model.parametersDevice.wpalenie.Wpalenie;
import com.pl.metalmachines.ui.devices.Mode;
import com.pl.metalmachines.utils.ParametersUtils;

import java.util.ArrayList;
import java.util.List;

public class Tab3Fragment extends Fragment {

    private View v;

    private List<EditText> listEditText = new ArrayList<EditText>();
    private Wpalenie currentWpalenie = ParametersUtils.getBlankParametersDevice().getWpalenie();
    private Mode currentMode =Mode.SHOW_PARAMETERS;

    private EditText predkoscCiecia;
    private EditText cyklPracy;
    private EditText czestotliwosc;
    private EditText wysokoscWpalania;
    private EditText stabilnaOdleglosc;
    private EditText predkosc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_devices_show_parameters_tab_3, container, false);

        listEditText.add(predkoscCiecia = (EditText) v.findViewById(R.id.wpalenie_predkosc_ciecia));
        listEditText.add(cyklPracy = (EditText) v.findViewById(R.id.wpalenie_cykl_pracy));
        listEditText.add(czestotliwosc = (EditText) v.findViewById(R.id.wpalenie_czestotliwosc));
        listEditText.add(wysokoscWpalania = (EditText) v.findViewById(R.id.wpalenie_wysokosc_wpalania));
        listEditText.add(stabilnaOdleglosc = (EditText) v.findViewById(R.id.wpalenie_stabilna_odleglosc));
        listEditText.add(predkosc = (EditText) v.findViewById(R.id.wpalenie_predkosc));

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
        savedParametersToWpalenie();
    }

    public void loadParametersToTextView() {

        if (currentMode == Mode.SHOW_PARAMETERS || currentMode==Mode.ADD_NEW_PARAMETERS) {
            predkoscCiecia.setText(String.valueOf(currentWpalenie.getParametryWpalenia().getPredkoscCiecia()));
            cyklPracy.setText(String.valueOf(currentWpalenie.getParametryWpalenia().getCyklPracy()));
            czestotliwosc.setText(String.valueOf(currentWpalenie.getParametryWpalenia().getCzestotliwosc()));
            wysokoscWpalania.setText(String.valueOf(currentWpalenie.getWpalajWolno().getWysokoscWpalania()));
            stabilnaOdleglosc.setText(String.valueOf(currentWpalenie.getWpalajWolno().getStabilnaOdleglosc()));
            predkosc.setText(String.valueOf(currentWpalenie.getWpalajKolem().getPredkosc()));
        }
    }

    public void setControlsOnMode() {
        if (currentMode == Mode.ADD_NEW_PARAMETERS) {
            for (EditText edittext : listEditText) {
                edittext.setEnabled(true);
            }
        } else if (currentMode == Mode.SHOW_PARAMETERS) {
            for (EditText edittext : listEditText) {
                edittext.setEnabled(false);
            }
        } else if (currentMode == Mode.EDIT_PARAMETERS) {
            for (EditText edittext : listEditText) {
                edittext.setEnabled(true);
            }
        }
    }

    public void savedParametersToWpalenie() {
        currentWpalenie = new Wpalenie(new ParametryWpalenia(predkoscCiecia.getText().toString(),
                cyklPracy.getText().toString(),
                czestotliwosc.getText().toString()),
                new WpalajWolno(wysokoscWpalania.getText().toString(),
                        stabilnaOdleglosc.getText().toString()),
                new WpalajKolem(predkosc.getText().toString()));
    }

    public Wpalenie getCurrentWpalenie() {
        return currentWpalenie;
    }

    public void setCurrentWpalenie(Wpalenie wpalenie) {
        this.currentWpalenie = wpalenie;
    }

    public void setCurrentMode(Mode mode) {
        this.currentMode = mode;
    }
}