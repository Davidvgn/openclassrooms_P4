package com.example.mareu.ui.add;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mareu.data.Meeting;

import java.util.ArrayList;
import java.util.List;

public class AddMeetingViewState {

    @NonNull
    private final String day;
    @NonNull
    private final String time;
    @NonNull
    private final String meetingRoom;

    public AddMeetingViewState(@NonNull String day, @NonNull String time, @NonNull String meetingRoom) {
        this.day = day;
        this.time = time;
        this.meetingRoom = meetingRoom;
    }

    @NonNull
    public String getDay() {
        return day;
    }

    @NonNull
    public String getTime() {
        return time;
    }

    @NonNull
    public String getMeetingRoom() {
        return meetingRoom;
    }
}
