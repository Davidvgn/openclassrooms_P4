package com.example.mareu.ui.add;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

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

public class AddMeetingActivity extends AppCompatActivity {

    public static Intent navigate(Context context) {
        return new Intent(context, AddMeetingActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_meeting_activity);

   //     getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AddMeetingViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(AddMeetingViewModel.class);

        TextInputEditText room = findViewById(R.id.add_meeting_room_edit);
        TextInputEditText timeEditText = findViewById(R.id.add_meeting_hour_edit);
        TextInputEditText subjectEditText = findViewById(R.id.add_meeting_subject_edit);
        Button addMeetingButton = findViewById(R.id.add_meeting_button);


        bindHour(viewModel, timeEditText);
        bindAddButton(viewModel, room, timeEditText, subjectEditText, addMeetingButton);


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

    private void bindAddButton(AddMeetingViewModel viewModel, TextInputEditText room, TextInputEditText timeEditText, TextInputEditText subjectEditText, Button addMeetingButton) {
        addMeetingButton.setOnClickListener(v -> viewModel.onAddButtonClicked(
                timeEditText.getText().toString(),
                subjectEditText.getText().toString(),
                room.getText().toString()
        ));
        viewModel.getIsSaveButtonEnabledLiveData().observe(this, isSaveButtonEnabled -> addMeetingButton.setEnabled(isSaveButtonEnabled));
    }
}


