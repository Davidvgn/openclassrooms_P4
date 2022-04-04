package com.example.mareu.ui.add;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.mareu.data.MeetingRepository;
import com.example.mareu.utils.SingleLiveEvent;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class AddMeetingViewModel extends ViewModel {

    private final MeetingRepository meetingRepository;

    private final MutableLiveData<LocalDate> dateMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<LocalTime> timeMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> roomMutableLiveData = new MutableLiveData<>();

    private final MediatorLiveData<AddMeetingViewState> addMeetingViewStateMediatorLiveData = new MediatorLiveData<>();

    private final SingleLiveEvent<Void> closeActivitySingleLiveEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<String> showToastSingleLiveEvent = new SingleLiveEvent<>();

    public AddMeetingViewModel(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;

        addMeetingViewStateMediatorLiveData.addSource(dateMutableLiveData, new Observer<LocalDate>() {
            @Override
            public void onChanged(LocalDate date) {
                combine(date, timeMutableLiveData.getValue(), roomMutableLiveData.getValue());
            }
        });
        addMeetingViewStateMediatorLiveData.addSource(timeMutableLiveData, new Observer<LocalTime>() {
            @Override
            public void onChanged(LocalTime time) {
                combine(dateMutableLiveData.getValue(), time, roomMutableLiveData.getValue());
            }
        });
        addMeetingViewStateMediatorLiveData.addSource(roomMutableLiveData, new Observer<String>() {
            @Override
            public void onChanged(String roomOnChanged) {
                combine(dateMutableLiveData.getValue(), timeMutableLiveData.getValue(), roomOnChanged);
            }
        });
    }

    private void combine(@Nullable LocalDate date, @Nullable LocalTime time, @Nullable String room) {
        String formattedDate = null;
        String formattedTime = null;

        if (date != null) {
            formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        if (time != null) {
            formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"));
        }

        addMeetingViewStateMediatorLiveData.setValue(
            new AddMeetingViewState(
                formattedDate,
                formattedTime,
                date != null && time != null && room != null
            )
        );
    }

    public LiveData<AddMeetingViewState> getAddMeetingViewStateLiveData() {
        return addMeetingViewStateMediatorLiveData;
    }

    public SingleLiveEvent<Void> getCloseActivitySingleLiveEvent() {
        return closeActivitySingleLiveEvent;
    }

    public SingleLiveEvent<String> getShowToastSingleLiveEvent() {
        return showToastSingleLiveEvent;
    }

    public void onDateChanged(int selectedDayOfMonth, int selectedMonthOfYear, int selectedYear) {
        dateMutableLiveData.setValue(LocalDate.of(selectedYear, selectedMonthOfYear + 1, selectedDayOfMonth));
    }

    public void onTimeChanged(int selectedHour, int selectedMinutes) {
        timeMutableLiveData.setValue(LocalTime.of(selectedHour, selectedMinutes));
    }

    public void onRoomSelected(CharSequence room) {
        roomMutableLiveData.setValue(room.toString());
    }

    public void onAddButtonClicked(
        @NonNull String date,
        @NonNull String time,
        @NonNull String meetingSubject,
        @NonNull String participants
    ) {
        boolean emailValidation = false;

        if (participants.isEmpty()) {
            emailValidation = true;
        } else {
            String[] participantsSplit = participants.replaceAll(" ", ",").split(",");
            String emailRegex = "^[\\s]?+[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z{2,5}]+$";
            for (int i = 0; i < participantsSplit.length; i++) {
                if (!participantsSplit[i].matches(emailRegex)) {
                    emailValidation = false;
                    showToastSingleLiveEvent.setValue("Email non-valide");
                    break;
                } else {
                    emailValidation = true;
                    participants = Arrays.toString(participantsSplit);
                }
            }
        }


        LocalDate localDate = null;
        LocalTime localTime = null;

        String[] splitDayMonthYear = date.split("/");
        if (splitDayMonthYear.length != 3) {
            showToastSingleLiveEvent.setValue("Format de date non-valide (format : DD/MM/YYYY)");
        } else {
            String day = splitDayMonthYear[0];
            String month = splitDayMonthYear[1];
            String year = splitDayMonthYear[2];

            localDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        }

        String[] splitHourMinute = time.split(":");
        if (splitHourMinute.length != 2) {
            showToastSingleLiveEvent.setValue("Format heure non-valide (format : HH:MM)");
        } else {
            String hour = splitHourMinute[0];
            String minute = splitHourMinute[1];
            String timeRegex = "[0-9]+";

            if (hour.matches(timeRegex) && minute.matches(timeRegex)) {
                localTime = LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute));
            } else {
                showToastSingleLiveEvent.setValue("Heure : caractère(s) non-valide(s)");

            }
        }

        String selectedRoom = roomMutableLiveData.getValue();

        if (emailValidation && localDate != null && localTime != null && selectedRoom != null) {
            boolean success = meetingRepository.addMeeting(LocalDateTime.of(localDate, localTime), selectedRoom, meetingSubject, participants);

            if (success) {
                closeActivitySingleLiveEvent.call();
            } else {
                showToastSingleLiveEvent.setValue("La salle est déjà occupée à cet horaire");

            }
        }
    }
}