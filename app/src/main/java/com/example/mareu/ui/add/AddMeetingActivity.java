package com.example.mareu.ui.add;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mareu.R;
import com.example.mareu.ui.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class AddMeetingActivity extends AppCompatActivity {

    ArrayAdapter<CharSequence> roomArrayAdapter;
    TimePickerDialog picker;
    DatePickerDialog datePickerDialog;


    public static Intent navigate(Context context) {
        return new Intent(context, AddMeetingActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_meeting_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AddMeetingViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(AddMeetingViewModel.class);

        TextInputEditText subjectEditText = findViewById(R.id.add_meeting_subject_tiet_subject);
        TextInputEditText participantEditText = findViewById(R.id.add_meeting_tiet_participants);
        Button addMeetingButton = findViewById(R.id.add_meeting_button);

        AutoCompleteTextView roomAutoCompleteTextView = findViewById(R.id.add_meeting_act_room);
        roomArrayAdapter = ArrayAdapter.createFromResource(this, R.array.room, R.layout.support_simple_spinner_dropdown_item);
        roomAutoCompleteTextView.setAdapter(roomArrayAdapter);
        roomAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> viewModel.onRoomSelected(roomArrayAdapter.getItem(position)));

        TextInputEditText dateEditText = findViewById(R.id.add_meeting_tiet_day);

        dateEditText.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR); // current year
            int month = c.get(Calendar.MONTH); // current month
            int day = c.get(Calendar.DAY_OF_MONTH); // current day
            datePickerDialog = new DatePickerDialog(
                AddMeetingActivity.this,
                (view, selectedYear, selectedMonthOfYear, selectedDayOfMonth) -> viewModel.onDateChanged(selectedDayOfMonth, selectedMonthOfYear, selectedYear),
                year,
                month,
                day
            );
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

            datePickerDialog.show();
        });

        TextInputEditText timeEditText = findViewById(R.id.add_meeting_tiet_time);
        timeEditText.setInputType(InputType.TYPE_NULL);
        timeEditText.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minutes = c.get(Calendar.MINUTE);
            picker = new TimePickerDialog(
                AddMeetingActivity.this,
                (tp, selectedHour, selectedMinutes) -> viewModel.onTimeChanged(selectedHour, selectedMinutes),
                hour,
                minutes,
                true
            );

            picker.show();
        });

        addMeetingButton.setOnClickListener(v -> viewModel.onAddButtonClicked(
            dateEditText.getText().toString(),
            timeEditText.getText().toString(),
            subjectEditText.getText().toString(),
            participantEditText.getText().toString()
        ));
        viewModel.getAddMeetingViewStateLiveData().observe(this, new Observer<AddMeetingViewState>() {
            @Override
            public void onChanged(AddMeetingViewState addMeetingViewState) {
                timeEditText.setText(addMeetingViewState.getTime());
                dateEditText.setText(addMeetingViewState.getDate());
                addMeetingButton.setEnabled(addMeetingViewState.isSaveButtonEnabled());
            }
        });

        viewModel.getCloseActivitySingleLiveEvent().observe(this, aVoid -> finish());
        viewModel.getShowToastSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                Toast.makeText(AddMeetingActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}


