package com.example.mareu.data;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.Objects;

public class Meeting {

    private final long id;
    @NonNull
    private final LocalDateTime date;
    @NonNull
    private final String meetingRoom;
    @NonNull
    private final String meetingSubject;
    @NonNull
    private final String participants;

    public Meeting(long id,
                   @NonNull LocalDateTime date,
                   @NonNull String meetingRoom,
                   @NonNull String meetingSubject,
                   @NonNull String participants
    ) {
        this.id = id;
        this.date = date;
        this.meetingRoom = meetingRoom;
        this.meetingSubject = meetingSubject;
        this.participants = participants;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public LocalDateTime getDate() {
        return date;
    }

    @NonNull
    public String getMeetingRoom() {
        return meetingRoom;
    }
    @NonNull
    public String getMeetingSubject() {
        return meetingSubject;
    }
    @NonNull
    public String getParticipants() {
        return participants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return id == meeting.id && date.equals(meeting.date) && meetingRoom.equals(meeting.meetingRoom) && meetingSubject.equals(meeting.meetingSubject) && participants.equals(meeting.participants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, meetingRoom, meetingSubject, participants);
    }

    @Override
    public String toString() {
        return "Meeting{" +
            "id=" + id +
            ", date=" + date +
            ", meetingRoom='" + meetingRoom + '\'' +
            ", meetingSubject='" + meetingSubject + '\'' +
            ", participants='" + participants + '\'' +
            '}';
    }
}

