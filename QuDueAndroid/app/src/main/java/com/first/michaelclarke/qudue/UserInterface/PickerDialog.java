package com.first.michaelclarke.qudue.UserInterface;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.first.michaelclarke.qudue.JavaProcessing.DateSettings;

import java.util.Calendar;


/**
 * Created by michaelclarke on 19/08/2016.
 */
public class PickerDialog extends DialogFragment {

    public PickerDialog(){

    }


    public Dialog  onCreateDialog(Bundle savedInstanceState){

        String text = getArguments().getString("From Activity");

        DateSettings dateSettings = new DateSettings(getActivity(), text);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(getActivity(), dateSettings, year, month, day);

        return datePickerDialog;

    }

}
