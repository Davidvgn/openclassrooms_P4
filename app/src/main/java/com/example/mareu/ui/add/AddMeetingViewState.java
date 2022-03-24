package com.example.mareu.ui.add;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class AddMeetingViewState {

    @Nullable
    private final String date;
    @Nullable
    private final String time;

    private final boolean isSaveButtonEnabled;

    public AddMeetingViewState(@Nullable String date, @Nullable String time, boolean isSaveButtonEnabled) {
        this.date = date;
        this.time = time;
        this.isSaveButtonEnabled = isSaveButtonEnabled;
    }

    @Nullable
    public String getDate() {
        return date;
    }

    @Nullable
    public String getTime() {
        return time;
    }

    public boolean isSaveButtonEnabled() {
        return isSaveButtonEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddMeetingViewState that = (AddMeetingViewState) o;
        return isSaveButtonEnabled == that.isSaveButtonEnabled && Objects.equals(date, that.date) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time, isSaveButtonEnabled);
    }

    @NonNull
    @Override
    public String toString() {
        return "AddMeetingViewState{" +
            "date='" + date + '\'' +
            ", time='" + time + '\'' +
            ", isSaveButtonEnabled=" + isSaveButtonEnabled +
            '}';
    }
}
