package com.paweldev.maszynypolskie.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paweldev.maszynypolskie.config.Config;

import java.io.IOException;

public class DialogUtils {


    public static void getLoginDialog(final Context context, final Activity activity, final TextView message, final Button button) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Wprowadź login i hasło");
        alert.setMessage("");

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView loginTextView = new TextView(context);
        loginTextView.setTextSize(14);
        loginTextView.setText("Email");
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
                    Config.getAutenthification(loginEditText.getText().toString(),passwordEditText.getText().toString());
                if (Config.getUSER().equals("")) {
                    message.setText("Błędny login lub hasło");
                    FileUtils.writeToFile(Config.getMyLogin()+";"+ Config.getMyPassword(), context);
                    FragmentUtils.setSubtitleNavigationDrawer(activity, "Nie jesteś zalogowany.");
                } else {
                    FileUtils.writeToFile(Config.getMyLogin()+";"+ Config.getMyPassword(), context);
                    String accessLevel="";
                    if (Config.getIsAdmin()==true){ accessLevel="(Administrator)";}
                    message.setText("Jesteś zalogowany jako :\n" + Config.getUSER()+"\n"+accessLevel);
                    FragmentUtils.setSubtitleNavigationDrawer(activity, "Jesteś zalogowany jako:\n"+ Config.getUSER());
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
