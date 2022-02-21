package com.example.mareu.list;

import androidx.annotation.NonNull;

import java.util.Objects;

public class MeetingViewStateItem {

    private final long id;
    private final CharSequence time;
    private final String meetingRoom;
    private final String meetingSubject;
    private final String participants;

    public MeetingViewStateItem(long id, CharSequence time, String meetingRoom, String meetingSubject, String participants) {
        this.id = id;
        this.time = time;
        this.meetingRoom = meetingRoom;
        this.meetingSubject = meetingSubject;
        this.participants = participants;
    }

    public long getId() {
        return id;
    }

    public CharSequence getTime() {
        return time;
    }

    public String getMeetingRoom() {
        return meetingRoom;
    }

    public String getMeetingSubject() {
        return meetingSubject;
    }

    public String getParticipants() {
        return participants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeetingViewStateItem that = (MeetingViewStateItem) o;
        return id == that.id && time.equals(that.time) && meetingRoom.equals(that
        .meetingRoom) && meetingSubject.equals(that.meetingSubject) && participants.equals((that.participants));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, time, meetingRoom, meetingSubject, participants);
    }

    @NonNull
    @Override
    public String toString() {
        return "NeighboursViewStateItem{" +
                "id=" + id +
                ", hour='" + time + '\'' +
                ", meetingRoom='" + meetingRoom + '\'' +
                ", meetingSubject='" + meetingSubject + '\'' +
                ", participants='" + participants + '\'' +
                '}';
    }
}

