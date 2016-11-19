package com.codepath.nytarticlesearchapp.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

/**
 * Created by mallikaa on 11/17/16.
 */
public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public SelectDateFragment() {}

    public interface EditDatePickerDialogListener {
        void onFinishEditDatePicker(String inputText);
    }


    @Override
    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        mm += 1;
        String date = yy + "/" + mm + "/" + dd;
        sendBackResult(date);
        //View parView  = manager.getView();
        //TextView textView = (TextView) parView.findViewById(R.id.tvFilterDate);
        //textView.setText(yy + "/" + mm + "/" + dd);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final java.util.Calendar calendar = java.util.Calendar.getInstance();
        int yy = calendar.get(java.util.Calendar.YEAR);
        int mm = calendar.get(java.util.Calendar.MONTH);
        int dd = calendar.get(java.util.Calendar.DATE);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);

    }

    public void sendBackResult(String text) {
        EditDatePickerDialogListener listener = (EditDatePickerDialogListener) getTargetFragment();
        listener.onFinishEditDatePicker(text);
        dismiss();
    }

    }