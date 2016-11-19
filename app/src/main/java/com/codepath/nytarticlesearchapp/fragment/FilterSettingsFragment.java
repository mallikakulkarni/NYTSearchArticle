package com.codepath.nytarticlesearchapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.codepath.nytarticlesearchapp.R;
import com.codepath.nytarticlesearchapp.model.Settings;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by mallikaa on 11/18/16.
 */

public class FilterSettingsFragment extends DialogFragment implements SelectDateFragment.EditDatePickerDialogListener {
    EditText datePickerText;
    View view;
    @BindView(R.id.cbArts) CheckBox cbArts;
    @BindView(R.id.cbSports) CheckBox cbSports;
    @BindView(R.id.cbFashion) CheckBox cbFashion;
    @BindView(R.id.tvFilterDate) TextView datepicker;
    @BindView(R.id.spinnerOrderFilter) Spinner spinner;
    @BindView(R.id.imgBtnDatePicker) ImageButton datePickerBtn;
    @BindView(R.id.btnFilterSubmit) Button submitButton;
    private Unbinder unbinder;
    private Settings settings;

    public FilterSettingsFragment() {}

    private OnItemSelectedListener listener;

    @Override
    public void onFinishEditDatePicker(String date) {
        datepicker.setText(date);
    }

    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {
        // This can be any number of events to be sent to the activity
        public void onRssItemSelected(String link);
    }

    public static FilterSettingsFragment newInstance() {
        FilterSettingsFragment fragment = new FilterSettingsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_filter, parent);
        unbinder = ButterKnife.bind(this, view);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View newView) {
                populateSettingsObject();
                String dateQuery = getFormattedDateQuery(view);
                String sortOrder = getSortOrder(view);
                String categories = getCategories(view);
                listener.onRssItemSelected((dateQuery + sortOrder + categories));
                dismiss();
            }
        });

        datePickerBtn.setOnClickListener(newView -> {
            FragmentManager fm = getFragmentManager();
            DialogFragment fragment = new SelectDateFragment();
            fragment.setTargetFragment(FilterSettingsFragment.this, 300);
            fragment.show(fm, "datePicker");
        });

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            settings = (Settings) Parcels.unwrap(bundle.getParcelable("settings"));
            populateSettings();
        }
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
//        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
//        // Fetch arguments from bundle and set title
//        String title = getArguments().getString("title", "Enter Name");
//        getDialog().setTitle(title);
//        // Show soft keyboard automatically and request focus to field
//        mEditText.requestFocus();
//        getDialog().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void selectDate(View view) {
    }

    public void onFilterSubmit(View view) {
        String dateQuery = getFormattedDateQuery(view);
        String sortOrder = getSortOrder(view);
        String categories = getCategories(view);
        listener.onRssItemSelected((dateQuery + sortOrder + categories));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }

    private String getFormattedDateQuery(View view) {
        String dateText = datepicker.getText().toString();
        if (dateText.equals("")) {
            return dateText;
        }
        final String beginDateQP = "&begin_date=";
        String tempDate = dateText.substring(0, 4) + dateText.substring(5);
        tempDate = tempDate.substring(0, 6) + tempDate.substring(7);
        return (beginDateQP + tempDate);
    }

    private String getSortOrder(View view) {
        String spinnerText = spinner.getSelectedItem().toString();
        final String spinnerQuery = "&sort=";
        return spinnerQuery + getOrderString(spinnerText);
    }

    private String getOrderString(String spinnerText) {
        final String newest = "newest";
        final String oldest = "oldest";
        switch (spinnerText) {
            case "Newest First":
                return (newest);
            case "Oldest First":
                return (oldest);
            default:
                return (newest);
        }
    }

    private String getCategories(View view) {
        Map<CheckBox, String> map = new HashMap<>();
        map.put(cbSports, "sports,");
        map.put(cbArts, "arts,");
        map.put(cbFashion, "fashion,");

        final String categoryQuery = "&q=";
        String fQuery = categoryQuery;
        for (CheckBox checkBox : map.keySet()) {
            if (checkBox.isChecked()) {
                fQuery += map.get(checkBox);
            }
        }
        if (fQuery.equals(categoryQuery)) {
            return "";
        }
        fQuery = fQuery.substring(0, fQuery.length() - 1);
        return fQuery;
    }

    private void populateSettings() {
        if (settings.isCbArts()) {
            cbArts.setChecked(true);
        }
        if (settings.isCbFashion()) {
            cbFashion.setChecked(true);
        }
        if (settings.isCbSports()) {
            cbSports.setChecked(true);
        }
        if (!settings.getDate().equals("")) {
            datepicker.setText(settings.getDate());
        }
        if (settings.getOrder().equals("newest")) {
            spinner.setSelection(0);
        } else {
            spinner.setSelection(1);
        }
    }

    private void populateSettingsObject() {
        Settings settings = Settings.getSettingsInstance();
        settings.setDate(datepicker.getText().toString());
        settings.setCbArts(cbArts.isChecked());
        settings.setCbFashion(cbFashion.isChecked());
        settings.setCbSports(cbSports.isChecked());
        settings.setOrder(getOrderString(spinner.getSelectedItem().toString()));
    }
}