package com.first.michaelclarke.qudue.JavaProcessing;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import com.first.michaelclarke.qudue.UserInterface.AddNewEssay;

/**
 * Created by michaelclarke on 19/08/2016.
 */
public class DateSettings implements DatePickerDialog.OnDateSetListener{

    Context context;
    String type = "";

    public DateSettings(Context context, String text){
    this.context = context;
       type = text;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        monthOfYear++;

        AddNewEssay.date = dayOfMonth + " - " +monthOfYear + " - " + year;

        if (type.equals("start")){
            AddNewEssay.startDateButton.getText().clear();
            AddNewEssay.startDateButton.setText(AddNewEssay.date);
        }

        if (type.equals("end")){
            AddNewEssay.dueDateButton.getText().clear();
            AddNewEssay.dueDateButton.setText(AddNewEssay.date);
        }

    }
}
