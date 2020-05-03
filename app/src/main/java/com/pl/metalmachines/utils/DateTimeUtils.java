package com.pl.metalmachines.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {

    public static void getDate(Activity activity, final Button datePickerButton, String setDate){

        String[] setDateSplit = setDate.split("-");

        DatePickerDialog datePickerDialog;
                final Calendar c = Calendar.getInstance();
                int mYear = Integer.parseInt(setDateSplit[0]); // current year
                int mMonth = Integer.parseInt(setDateSplit[1])-1; // current month
                int mDay = Integer.parseInt(setDateSplit[2]); // current day

                // date picker dialog
                datePickerDialog = new DatePickerDialog(activity,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                String dayOfMonthString = String.format("%02d", dayOfMonth);
                                String monthOfYearString = String.format("%02d", monthOfYear+1);
                                String dateSelected= year + "-"
                                        + monthOfYearString + "-" + dayOfMonthString;
                                datePickerButton.setText(dateSelected);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
    }

    public static String now() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd"); //"dd/MM/yyyy hh:mm:ss.SSS"
        Date currentDate = new Date();

        return formatter.format(currentDate);
    }

    public static void getTime(Activity activity, final Button timePickerButton){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                timePickerButton.setText( selectedHour + ":" + String.format("%02d",selectedMinute));
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
}
