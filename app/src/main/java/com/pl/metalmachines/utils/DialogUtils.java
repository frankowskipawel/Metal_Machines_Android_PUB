package com.pl.metalmachines.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.pl.metalmachines.dao.ParametersDao;
import com.pl.metalmachines.dao.UtilsDao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DialogUtils {

    public static String getTextViewDialog(final Context context, String title, String message, String baseParameter, final List<String> listSpinner, final Spinner spinner, boolean decimalInputType) throws SQLException, ClassNotFoundException {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        final EditText editTextDialog = new EditText(context);
        editTextDialog.setGravity(Gravity.CENTER);
        final Spinner spinnerDialog = new Spinner(context);
        ArrayList list = new ArrayList<String>();
        list.add("");
        list.addAll(ParametersDao.findAllBaseParameters().get(baseParameter));
        ArrayAdapter<String> materialAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list);
        materialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDialog.setAdapter(materialAdapter);

        linearLayout.addView(spinnerDialog);
        linearLayout.addView(editTextDialog);

        spinnerDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                editTextDialog.setText(spinnerDialog.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                editTextDialog.setText("");
            }
        });

        if (decimalInputType) {
            editTextDialog.setInputType(InputType.TYPE_CLASS_PHONE);
        }
        alert.setView(linearLayout);

        editTextDialog.setText("");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String newValue = editTextDialog.getText().toString().toUpperCase().replace(",", ".").trim();
                boolean isExist = false;
                for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
                    if (spinner.getItemAtPosition(i).equals(newValue)) {
                        spinner.setSelection(i);
                        isExist = true;
                    }
                }
                if (!isExist) {
                    listSpinner.add(editTextDialog.getText().toString().toUpperCase().replace(",", ".").trim());
                    spinner.setSelection(listSpinner.size() - 1);
                }
            }
        });

        alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();

        return editTextDialog.getText().toString();
    }

    public static void getLoginDialog(final Context context, final Activity activity, final TextView message, final Button button) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Wprowadź login i hasło");
        alert.setMessage("");

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView loginTextView = new TextView(context);
        loginTextView.setTextSize(14);
        loginTextView.setText("Login");
        loginTextView.setPadding(50,20,50,20);
        linearLayout.addView(loginTextView);

        final EditText loginEditText = new EditText(context);
        loginEditText.setGravity(Gravity.CENTER);
        linearLayout.addView(loginEditText);

        TextView passwordTextView = new TextView(context);
        passwordTextView.setTextSize(14);
        passwordTextView.setText("Hasło");
        passwordTextView.setPadding(50,20,50,20);
        linearLayout.addView(passwordTextView);

        final EditText passwordEditText = new EditText(context);
        passwordEditText.setGravity(Gravity.CENTER);
        passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        linearLayout.addView(passwordEditText);

        alert.setView(linearLayout);

        loginEditText.setText("");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    UtilsDao.getAutenthification(loginEditText.getText().toString(),passwordEditText.getText().toString());
                if (UtilsDao.getUSER().equals("")) {
                    message.setText("Błędny login lub hasło");
                    FileUtils.writeToFile(UtilsDao.getMyLogin()+";"+ UtilsDao.getMyPassword(), context);
                    FragmentUtils.setSubtitleNavigationDrawer(activity, "Nie jesteś zalogowany.");
                } else {
                    FileUtils.writeToFile(UtilsDao.getMyLogin()+";"+ UtilsDao.getMyPassword(), context);
                    String accessLevel="";
                    if (UtilsDao.getIsAdmin()==true){ accessLevel="(Administrator)";}
                    message.setText("Jesteś zalogowany jako :\n" + UtilsDao.getUSER()+"\n"+accessLevel);
                    FragmentUtils.setSubtitleNavigationDrawer(activity, "Jesteś zalogowany jako:\n"+ UtilsDao.getUSER());
                    button.setText("Wyloguj");
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

    public static void getCustomerNameDialog(final Context context, final TextView nameOfTheSignatory) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Wprowadź");
        alert.setMessage("Imie i nazwisko odbierającego serwis.");

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText loginEditText = new EditText(context);
        loginEditText.setGravity(Gravity.CENTER);
        linearLayout.addView(loginEditText);

        alert.setView(linearLayout);

        loginEditText.setText("");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                nameOfTheSignatory.setText(loginEditText.getText());
            }
        });
        alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
}
