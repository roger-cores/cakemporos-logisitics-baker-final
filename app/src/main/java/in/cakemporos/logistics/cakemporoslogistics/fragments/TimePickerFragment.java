package in.cakemporos.logistics.cakemporoslogistics.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

import in.cakemporos.logistics.cakemporoslogistics.custom_views.CustomTimePickerDialog;
import in.cakemporos.logistics.cakemporoslogistics.events.OnDateTimePickedEventListner;

/**
 * Created by roger on 10/8/16.
 */
public class TimePickerFragment  extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    boolean today = false;

    OnDateTimePickedEventListner onDateTimePickedEventListner;

    public void setOnDateTimePickedEventListner(OnDateTimePickedEventListner onDateTimePickedEventListner) {
        this.onDateTimePickedEventListner = onDateTimePickedEventListner;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

        today = getArguments().getBoolean("today", false);

        if(today){
            Calendar hourLater = Calendar.getInstance();
            hourLater.add(Calendar.HOUR_OF_DAY, 1);

            timePickerDialog.setMin(hourLater.get(Calendar.HOUR_OF_DAY), -1);
        }

        // Create a new instance of TimePickerDialog and return it
        return timePickerDialog;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        onDateTimePickedEventListner.onDateTimePicked(calendar.getTime());
    }
}
