package com.pl.metalmachines.utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.pl.metalmachines.R;
import com.pl.metalmachines.model.parametersDevice.Gaz;
import com.pl.metalmachines.model.parametersDevice.StringParameters;
import com.pl.metalmachines.model.parametersDevice.ciecie.Ciecie;
import com.pl.metalmachines.model.parametersDevice.ciecie.ParametryGlowne;
import com.pl.metalmachines.model.parametersDevice.ciecie.RuszWolno;
import com.pl.metalmachines.model.parametersDevice.ciecie.StopWolno;
import com.pl.metalmachines.model.parametersDevice.przebijanie.Etap;
import com.pl.metalmachines.model.parametersDevice.przebijanie.Przebijanie;
import com.pl.metalmachines.model.parametersDevice.wpalenie.ParametryWpalenia;
import com.pl.metalmachines.model.parametersDevice.wpalenie.WpalajKolem;
import com.pl.metalmachines.model.parametersDevice.wpalenie.WpalajWolno;
import com.pl.metalmachines.model.parametersDevice.wpalenie.Wpalenie;
import java.util.ArrayList;
import java.util.List;

public class ParametersUtils {

    public static void setGazListOnSpinner(Context context, Spinner spinner){
        List listGaz = new ArrayList();
        listGaz.add("");
        for (Gaz value : Gaz.values()) {
            listGaz.add(value.name());
        }
        ArrayAdapter<String> materialAdapter = new ArrayAdapter<String>(context,
                R.layout.spinner_item, listGaz);
        materialAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(materialAdapter);
    }

    public static StringParameters getBlankParametersDevice(){
                return new StringParameters(
                new Ciecie(
                        new ParametryGlowne(
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                ""
                                ),
                        new RuszWolno(
                                "",
                                ""),
                        new StopWolno(
                                "",
                                "")
                ),
                new Przebijanie(
                        new Etap(
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                ""
                                ),
                        new Etap(
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                ""
                                ),
                        new Etap(
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                ""
                                )
                ),
                new Wpalenie(
                        new ParametryWpalenia(
                                "",
                                "",
                                ""),
                        new WpalajWolno(
                                "",
                                ""),
                        new WpalajKolem("")
                )
        );
    }
}
