package com.droidsoul.nytimessearch.fragments;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by bear&bear on 9/19/2017.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    DateListener dateListener;

    public interface DateListener {
        void onDateSet(Calendar c, String tag);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String beginDate = getArguments().getString("begin_date");
        String endDate = getArguments().getString("end_Date");
        Calendar c = Calendar.getInstance();

        int year = beginDate != null ? Integer.valueOf(beginDate.substring(0, 4)) : (endDate != null ? Integer.valueOf(endDate.substring(0, 4)) : c.get(Calendar.YEAR));
        int month = beginDate != null ? Integer.valueOf(beginDate.substring(5, 7)) - 1 : (endDate != null ? Integer.valueOf(endDate.substring(5, 7)) - 1 : c.get(Calendar.MONTH));
        int day = beginDate != null ? Integer.valueOf(beginDate.substring(8)) : (endDate != null ? Integer.valueOf(endDate.substring(8)) : c.get(Calendar.DAY_OF_MONTH));

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        dateListener = (DateListener) getTargetFragment();
        dateListener.onDateSet(c, getTag());
    }
}
