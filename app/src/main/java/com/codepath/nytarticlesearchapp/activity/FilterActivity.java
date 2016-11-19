package com.codepath.nytarticlesearchapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.codepath.nytarticlesearchapp.R;
import com.codepath.nytarticlesearchapp.fragment.SelectDateFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mallikaa on 11/17/16.
 */

public class FilterActivity extends AppCompatActivity{
    EditText datePickerText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
    }

    public void selectDate(View view) {
        DialogFragment fragment = new SelectDateFragment();
        fragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onFilterSubmit(View view) {
        String dateQuery = getFormattedDateQuery();
        String sortOrder = getSortOrder();
        String categories = getCategories();
        Intent data = new Intent();
        data.putExtra("query", (dateQuery + sortOrder + categories));
        setResult(RESULT_OK, data);
        finish();
    }

    private String getFormattedDateQuery() {
        TextView dateTextView = (TextView) findViewById(R.id.tvFilterDate);
        String dateText = dateTextView.getText().toString();
        if (dateText.equals("")) {
            return dateText;
        }
        final String beginDateQP = "&begin_date=";
        String tempDate = dateText.substring(0, 4) + dateText.substring(5);
        tempDate = tempDate.substring(0, 6) + tempDate.substring(7);
        return (beginDateQP + tempDate);
    }

    private String getSortOrder() {
        Spinner spinner = (Spinner) findViewById(R.id.spinnerOrderFilter);
        String spinnerText = spinner.getSelectedItem().toString();
        final String spinnerQuery = "&sort=";
        final String newest = "newest";
        final String oldest = "oldest";
        switch (spinnerText) {
            case "Newest First":
                return (spinnerQuery + newest);
            case "Oldest First":
                return (spinnerQuery + oldest);
            default:
                return (spinnerQuery + newest);
        }
    }

    private String getCategories() {
        Map<CheckBox, String> map = new HashMap<>();
        map.put((CheckBox) findViewById(R.id.cbSports), "sports,");
        map.put((CheckBox) findViewById(R.id.cbArts), "arts,");
        map.put((CheckBox) findViewById(R.id.cbFashion), "fashion,");

        final String categoryQuery = "&q=";
        String fQuery = categoryQuery;
        for (CheckBox checkBox : map.keySet()) {
            if (checkBox.isChecked()) {
                fQuery += map.get(checkBox);
            }
        }
        fQuery = fQuery.substring(0, fQuery.length() - 1);
        return fQuery;
    }
}

