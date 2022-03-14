package com.example.mareu.ui.add;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.app.TimePickerDialog;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mareu.R;
import com.example.mareu.ui.ViewModelFactory;
import com.example.mareu.ui.list.MeetingViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class AddMeetingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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

        TextInputEditText subjectEditText = findViewById(R.id.add_meeting_subject_tie_subject);
        TextInputEditText participantEditText = findViewById(R.id.add_meeting_tie_participants);
        Button addMeetingButton = findViewById(R.id.add_meeting_button);

//        TextInputLayout roomSpinner = findViewById(R.id.add_meeting_til_room);
        AutoCompleteTextView roomAutoCompleteTextView = findViewById(R.id.add_meeting_act_room);
        roomArrayAdapter = ArrayAdapter.createFromResource(this, R.array.room, R.layout.support_simple_spinner_dropdown_item);
        roomAutoCompleteTextView.setAdapter(roomArrayAdapter);
        roomAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> viewModel.onRoomSelected(roomArrayAdapter.getItem(position)));

        TextInputEditText date = findViewById(R.id.add_meeting_tie_day);

        date.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            datePickerDialog = new DatePickerDialog(AddMeetingActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> date.setText(dayOfMonth + "/" + checkDigit(monthOfYear + 1) + "/" + year), mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

            datePickerDialog.show();
        });

        TextInputEditText time_editText = findViewById(R.id.add_meeting_tie_time);
        time_editText.setInputType(InputType.TYPE_NULL);
        time_editText.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            picker = new TimePickerDialog(AddMeetingActivity.this,
                    (tp, sHour, sMinute) -> time_editText.setText("" + checkDigit(sHour) + ":" + checkDigit(sMinute)), hour, minutes, true);

            picker.show();
        });

        bindTime(viewModel, date, time_editText, roomAutoCompleteTextView);
        bindAddButton(viewModel, date, time_editText, subjectEditText, participantEditText, addMeetingButton);

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


    private void bindTime(AddMeetingViewModel viewModel, TextInputEditText date, TextInputEditText time, AutoCompleteTextView room) {
        date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.onCalendarValueChanged(s.toString());
            }
        });

        time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.onTimeValueChanged(s.toString());

            }
        });
        room.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.onRoomValueChanged(s.toString());

            }
        });
    }


    private void bindAddButton(AddMeetingViewModel viewModel, TextInputEditText date, TextInputEditText time_editText, TextInputEditText subjectEditText, TextInputEditText participantsEditText, Button addMeetingButton) {
        addMeetingButton.setOnClickListener(v -> viewModel.onAddButtonClicked(
                date.getText().toString(),
                time_editText.getText().toString(),
                subjectEditText.getText().toString(),
                participantsEditText.getText().toString()
        ));
        viewModel.getIsSaveButtonEnabledLiveData().observe(this, addMeetingButton::setEnabled);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

}


