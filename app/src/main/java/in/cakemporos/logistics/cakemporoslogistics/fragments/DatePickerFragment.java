package in.cakemporos.logistics.cakemporoslogistics.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

import in.cakemporos.logistics.cakemporoslogistics.events.OnDateTimePickedEventListner;

/**
 * Created by roger on 10/8/16.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    OnDateTimePickedEventListner onDateTimePickedEventListner;

    public void setOnDateTimePickedEventListner(OnDateTimePickedEventListner onDateTimePickedEventListner) {
        this.onDateTimePickedEventListner = onDateTimePickedEventListner;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        Calendar c = Calendar.getInstance();
        Bundle bundle = getArguments();
        int year = bundle.getInt("y");
        int month = bundle.getInt("m");
        int day = bundle.getInt("d");

        final Calendar nextWeek = Calendar.getInstance();
        nextWeek.add(Calendar.WEEK_OF_MONTH, 1);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(nextWeek.getTimeInMillis());
        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, day);

        onDateTimePickedEventListner.onDateTimePicked(calendar.getTime());
    }
}