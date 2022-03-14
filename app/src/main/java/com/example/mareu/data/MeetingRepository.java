package com.example.mareu.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mareu.config.BuildConfigResolver;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MeetingRepository {


    private final MutableLiveData<List<Meeting>> meetingLiveData = new MutableLiveData<>(new ArrayList<>());

    private long maxId = 0;

    public MeetingRepository(BuildConfigResolver buildConfigResolver) {
        if (buildConfigResolver.isDebug()) {
            generateRandomMeeting();
        }
    }

    public boolean addMeeting(
        LocalDateTime date,
        String meetingRoom,
        String meetingSubject,
        String participants
    ) {
        List<Meeting> meetings = meetingLiveData.getValue();

        if (meetings == null) return false;

        for (Meeting meeting : meetings) {
            if (meeting.getMeetingRoom().equals(meetingRoom) &&
                (meeting.getDate().isAfter(date) && meeting.getDate().isBefore(date.plusHours(1)))
                || (date.isAfter(meeting.getDate()) && date.isBefore(meeting.getDate().plusHours(1)))
            ) {
                return false;
            }
        }

        meetings.add(
            new Meeting(
                maxId++,
                date,
                meetingRoom,
                meetingSubject,
                participants
            )
        );

        meetingLiveData.setValue(meetings);

        return true;
    }

    public void deleteMeeting(long meetingId) {
        List<Meeting> meetings = meetingLiveData.getValue();

        if (meetings == null) return;

        for (Iterator<Meeting> iterator = meetings.iterator(); iterator.hasNext(); ) {
            Meeting meeting = iterator.next();

            if (meeting.getId() == meetingId) {
                iterator.remove();
                break;
            }
        }

        meetingLiveData.setValue(meetings);
    }

    public LiveData<List<Meeting>> getMeetingsLiveData() {
        return meetingLiveData;
    }

    private void generateRandomMeeting() {
        addMeeting(
            LocalDateTime.of(2022, 3, 7, 14, 0),
            "Android",
            "Sujet 1",
            "email@email.com"
        );
        addMeeting(
            LocalDateTime.of(2022, 3, 7, 14, 45),
            "Kotlin",
            "Sujet 2",
            "email@email.com"
        );
    }
}

