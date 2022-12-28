package com.example.attendance;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarClass extends DialogFragment {


    Calendar cal = Calendar.getInstance();
    private int year = Calendar.YEAR;
    private int month = Calendar.MONTH;
    private int day = Calendar.DAY_OF_MONTH;
    final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public interface OnCalendarClickListener{
        void onClick(int year, int month, int day);
    }

    public OnCalendarClickListener onCalendarClickListener;

    public void setOnCalendarClickListener(OnCalendarClickListener onCalendarClickListener)
    {
        this.onCalendarClickListener = onCalendarClickListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        return new DatePickerDialog(getActivity(), ((view, year, month, dayOfMonth) ->
        {onCalendarClickListener.onClick(year, month, dayOfMonth);}),
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    }

    void setDate(int year, int month, int day)
    {
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
    }

    public String getDate()
    {
        return dateFormat.format(cal.getTime());
    }

    public Date getDateRaw()
    {
        return cal.getTime();
    }
}
