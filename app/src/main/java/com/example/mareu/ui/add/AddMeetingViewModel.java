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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
            String[] participantsSplit = participants.split(",");
            String emailRegex = "^[\\s]?+[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z{2,5}]+$";
            for (String addresses : participantsSplit) {
                if (!addresses.matches(emailRegex)) {
                    emailValidation = false;
                    showToastSingleLiveEvent.setValue("Email non-valide");
                } else {
                    emailValidation = true;
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

            localDate = LocalDate.of(LocalDate.now().getYear(), Integer.parseInt(month), Integer.parseInt(day));
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

//todo david Base
//package com.example.mareu.ui.add;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MediatorLiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModel;
//
//import com.example.mareu.data.Meeting;
//import com.example.mareu.data.MeetingRepository;
//import com.example.mareu.utils.SingleLiveEvent;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//
//public class AddMeetingViewModel extends ViewModel {
//
//    private final MeetingRepository meetingRepository;
//    private final MutableLiveData<Boolean> calendarMutableLiveData = new MutableLiveData<>(false);
//    private final MutableLiveData<Boolean> hourMutableLiveData = new MutableLiveData<>(false);
//    private final MutableLiveData<Boolean> roomMutableLiveData = new MutableLiveData<>(false);
//    private final MutableLiveData<Boolean> isRoomAlreadyUsed = new MutableLiveData<>(false);
//
//    private final MediatorLiveData<Boolean> isSaveButtonEnabledMediatorLiveData = new MediatorLiveData<>();
//
//    private final SingleLiveEvent<Void> closeActivitySingleLiveEvent = new SingleLiveEvent<>();
//    private final SingleLiveEvent<String> showToastSingleLiveEvent = new SingleLiveEvent<>();
//
//    @NonNull
//    public String roomSelected = "";
//
//    public AddMeetingViewModel(MeetingRepository meetingRepository) {
//        this.meetingRepository = meetingRepository;
//
//        isSaveButtonEnabledMediatorLiveData.addSource(calendarMutableLiveData, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean calendarOnChanged) {
//                combine(calendarOnChanged, hourMutableLiveData.getValue(), roomMutableLiveData.getValue(), isRoomAlreadyUsed.getValue());
//            }
//        });
//        isSaveButtonEnabledMediatorLiveData.addSource(hourMutableLiveData, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean hourOnchanged) {
//                combine(calendarMutableLiveData.getValue(), hourOnchanged, roomMutableLiveData.getValue(), isRoomAlreadyUsed.getValue());
//            }
//        });
//        isSaveButtonEnabledMediatorLiveData.addSource(roomMutableLiveData, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean roomOnChanged) {
//                combine(calendarMutableLiveData.getValue(), hourMutableLiveData.getValue(), roomOnChanged, isRoomAlreadyUsed.getValue());
//            }
//        });
//        isSaveButtonEnabledMediatorLiveData.addSource(isRoomAlreadyUsed, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean roomAlreadyUsed) {
//                combine(calendarMutableLiveData.getValue(), hourMutableLiveData.getValue(), roomMutableLiveData.getValue(), roomAlreadyUsed);
//            }
//        });
//    }
//
//    private void combine(Boolean calendar, Boolean hour, Boolean room, Boolean roomAlreadyUsed) {
//        if (calendar && hour && room && !roomAlreadyUsed) {
//            isSaveButtonEnabledMediatorLiveData.setValue(true);
//        }
//    }
//
//    public LiveData<Boolean> getIsSaveButtonEnabledLiveData() {
//        return isSaveButtonEnabledMediatorLiveData;
//    }
//
//    public SingleLiveEvent<Void> getCloseActivitySingleLiveEvent() {
//        return closeActivitySingleLiveEvent;
//    }
//
//    public SingleLiveEvent<String> getShowToastSingleLiveEvent() {
//        return showToastSingleLiveEvent;
//    }
//
//    public void onCalendarValueChanged(String calendar) {
//        if ((!calendar.isEmpty())) {
//            calendarMutableLiveData.setValue(true);
//        }
//    }
//
//    public void onTimeValueChanged(String calendar) {
//        if ((!calendar.isEmpty())) {
//            hourMutableLiveData.setValue(true);
//        }
//    }
//
//    public void onRoomValueChanged(String calendar) {
//        if ((!calendar.isEmpty())) {
//            roomMutableLiveData.setValue(true);
//        }
//    }
//
//    public void onAddButtonClicked(
//        @NonNull String date,
//        @NonNull String time,
//        @NonNull String meetingSubject,
//        @NonNull String participants
//    ) {
//        boolean emailValidation = false;
//        String[] participantsSplitted = participants.split(",");
//        String emailRegex = "^[\\s]?+[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z{2,5}]+$";
//        for (String addresses : participantsSplitted) {
//            if (!addresses.matches(emailRegex)) {
//                emailValidation = false;
//                showToastSingleLiveEvent.setValue("Email non-valide");
//            } else {
//                emailValidation = true;
//            }
//        }
//
//        LocalDate localDate = null;
//        LocalTime localTime = null;
//
//        String[] splitDayMonthYear = date.split("/");
//        if (splitDayMonthYear.length != 3) {
//            showToastSingleLiveEvent.setValue("Format de date non-valide (format : DD/MM/YYYY)");
//        } else {
//            String day = splitDayMonthYear[0];
//            String month = splitDayMonthYear[1];
//
//            localDate = LocalDate.of(LocalDate.now().getYear(), Integer.parseInt(month), Integer.parseInt(day));
//        }
//
//        String[] splitHourMinute = time.split(":");
//        if (splitHourMinute.length != 2) {
//            showToastSingleLiveEvent.setValue("Format heure non-valide (format : HH:MM)");
//        } else {
//            String hour = splitHourMinute[0];
//            String minute = splitHourMinute[1];
//            String timeRegex = "[0-9]+";
//
//            if (hour.matches(timeRegex) && minute.matches(timeRegex)) {
//                localTime = LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute));
//            } else {
//                showToastSingleLiveEvent.setValue("Heure : caractère(s) non-valide(s)");
//
//            }
//        }
//
//
//        if (emailValidation && localDate != null && localTime != null) {
//            boolean success = meetingRepository.addMeeting(LocalDateTime.of(localDate, localTime), roomSelected, meetingSubject, participants);
//
//            if (success) {
//                closeActivitySingleLiveEvent.call();
//            } else {
//                showToastSingleLiveEvent.setValue("La salle est déjà occupée à cet horaire");
//
//            }
//        }
//    }
//
//    public void onRoomSelected(CharSequence room) {
//        roomSelected = room.toString();
//    }
//}