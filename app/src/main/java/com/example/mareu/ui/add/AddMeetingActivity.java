package com.example.mareu.ui.add;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mareu.R;
import com.example.mareu.data.MeetingRepository;
import com.example.mareu.databinding.AddMeetingActivityBinding;
import com.example.mareu.ui.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

public class AddMeetingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static Intent navigate(Context context) {
        return new Intent(context, AddMeetingActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_meeting_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AddMeetingViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(AddMeetingViewModel.class);

//        TextInputEditText room = findViewById(R.id.add_meeting_room_edit);
//        TextInputEditText hourEditText = findViewById(R.id.add_meeting_hour_edit);
//        TextInputEditText minEditText = findViewById(R.id.add_meeting_min_edit);
        TextInputEditText subjectEditText = findViewById(R.id.add_meeting_subject_edit);
        TextInputEditText participantEditText = findViewById(R.id.add_meeting_participants_edit);
        Button addMeetingButton = findViewById(R.id.add_meeting_button);


//        bindHour(viewModel, hourEditText);
//        bindAddButton(viewModel, room, hourEditText,minEditText, subjectEditText, addMeetingButton);


//        viewModel.getCloseActivitySingleLiveEvent().observe(this, aVoid -> finish());


        Spinner hourSpinner = findViewById(R.id.add_meeting_hourSpinner);
        ArrayAdapter<CharSequence> hourAdapter = ArrayAdapter.createFromResource(this, R.array.heures, android.R.layout.simple_spinner_item);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hourSpinner.setAdapter(hourAdapter);
        hourSpinner.setOnItemSelectedListener(this);


        Spinner minSpinner = findViewById(R.id.add_meeting_minSpinner);
        ArrayAdapter<CharSequence> minAdapter = ArrayAdapter.createFromResource(this, R.array.minutes, android.R.layout.simple_spinner_item);
        minAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minSpinner.setAdapter(minAdapter);
        minSpinner.setOnItemSelectedListener(this);

        Spinner roomSpinner = findViewById(R.id.add_meeting_room);
        ArrayAdapter<CharSequence> roomAdapter = ArrayAdapter.createFromResource(this, R.array.room, android.R.layout.simple_spinner_item);
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomSpinner.setAdapter(roomAdapter);
        roomSpinner.setOnItemSelectedListener(this);

        bindAddButton(viewModel, hourSpinner, minSpinner, roomSpinner, subjectEditText, participantEditText, addMeetingButton);

        viewModel.getCloseActivitySingleLiveEvent().observe(this, aVoid -> finish());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void bindHour(AddMeetingViewModel viewModel, TextInputEditText nameEditText) {
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.onNameChanged(s.toString());
            }
        });
    }

    //    private void bindAddButton(AddMeetingViewModel viewModel, TextInputEditText room, TextInputEditText hourEditText,TextInputEditText minEditText, TextInputEditText subjectEditText, Button addMeetingButton) {
//        addMeetingButton.setOnClickListener(v -> viewModel.onAddButtonClicked(
//                hourEditText.getText().toString(),
//                minEditText.getText().toString(),
//                subjectEditText.getText().toString(),
//                room.getText().toString()
//        ));
//        viewModel.getIsSaveButtonEnabledLiveData().observe(this, isSaveButtonEnabled -> addMeetingButton.setEnabled(isSaveButtonEnabled));
//    }
    private void bindAddButton(AddMeetingViewModel viewModel, Spinner hourSpinner, Spinner minSpinner, Spinner roomSpinner, TextInputEditText subjectEditText,TextInputEditText participantsEditText, Button addMeetingButton) {
        addMeetingButton.setOnClickListener(v -> viewModel.onAddButtonClicked(
                hourSpinner.getSelectedItem().toString(),
                minSpinner.getSelectedItem().toString(),
                roomSpinner.getSelectedItem().toString(),
                subjectEditText.getText().toString(),
                participantsEditText.getText().toString()
        ));
        viewModel.getIsSaveButtonEnabledLiveData().observe(this, isSaveButtonEnabled -> addMeetingButton.setEnabled(isSaveButtonEnabled));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}


