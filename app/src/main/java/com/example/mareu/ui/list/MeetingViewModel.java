package com.example.mareu.ui.list;


import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MeetingViewModel extends ViewModel {

    private final MeetingRepository meetingRepository;

    private final MediatorLiveData<List<MeetingViewStateItem>> mediatorLiveData = new MediatorLiveData<>();

    private final MutableLiveData<Boolean> isSortingByRoomMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSortingByDateMutableLiveData = new MutableLiveData<>();

    public MeetingViewModel(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;

        LiveData<List<Meeting>> meetingsLiveData = meetingRepository.getMeetingsLiveData();

        mediatorLiveData.addSource(meetingsLiveData, new Observer<List<Meeting>>() {
            @Override
            public void onChanged(List<Meeting> meetings) {
                combine(meetings, isSortingByRoomMutableLiveData.getValue(), isSortingByDateMutableLiveData.getValue());
            }
        });

        mediatorLiveData.addSource(isSortingByRoomMutableLiveData, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSortingByRoom) {
                combine(meetingsLiveData.getValue(), isSortingByRoom, isSortingByDateMutableLiveData.getValue());
            }
        });

        mediatorLiveData.addSource(isSortingByDateMutableLiveData, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSortingByDate) {
                combine(meetingsLiveData.getValue(), isSortingByRoomMutableLiveData.getValue(), isSortingByDate);
            }
        });
    }

    public LiveData<List<MeetingViewStateItem>> getMeetingViewStateItemsLiveData() {
        return mediatorLiveData;
    }

    private void combine(@Nullable List<Meeting> meetings, @Nullable Boolean isSortingByRoom, @Nullable Boolean isSortingByDate) {
        if (meetings == null) {
            return;
        }

        List<MeetingViewStateItem> meetingViewStateItems = new ArrayList<>();

        for (Meeting meeting : meetings) {
            meetingViewStateItems.add(
                    new MeetingViewStateItem(
                            meeting.getId(),
                            meeting.getDate().toLocalDate().toString(),
                            meeting.getDate().toLocalTime().toString(),
                            meeting.getMeetingRoom(),
                            meeting.getMeetingSubject(),
                            meeting.getParticipants()
                    )
            );
        }

        if (isSortingByRoom != null) {
            if (isSortingByRoom) {
                Collections.sort(meetingViewStateItems, new Comparator<MeetingViewStateItem>() {
                    @Override
                    public int compare(MeetingViewStateItem o1, MeetingViewStateItem o2) {
                        return o2.getMeetingRoom().compareTo(o1.getMeetingRoom());
                    }
                });
            } else {
                Collections.sort(meetingViewStateItems, new Comparator<MeetingViewStateItem>() {
                    @Override
                    public int compare(MeetingViewStateItem o1, MeetingViewStateItem o2) {
                        return o1.getMeetingRoom().compareTo(o2.getMeetingRoom());
                    }
                });
            }
        }

        if (isSortingByDate != null) {
            if (isSortingByDate) {
                Collections.sort(meetingViewStateItems, new Comparator<MeetingViewStateItem>() {
                    @Override
                    public int compare(MeetingViewStateItem o1, MeetingViewStateItem o2) {
                        int firstComparison = o1.getDay().compareTo(o2.getDay());
                        if (firstComparison == 0) {
                            return o1.getTime().compareTo(o2.getTime());
                        }
                        return firstComparison;
                    }
                });
            } else {
                Collections.sort(meetingViewStateItems, new Comparator<MeetingViewStateItem>() {
                    @Override
                    public int compare(MeetingViewStateItem o1, MeetingViewStateItem o2) {
                        int firstComparison = o2.getDay().compareTo(o1.getDay());
                        if (firstComparison == 0) {
                            return o2.getTime().compareTo(o1.getTime());
                        }
                        return firstComparison;
                    }
                });
            }

        }
        mediatorLiveData.setValue(meetingViewStateItems);
    }

    public void onDeleteMeetingClicked(long meetingId) {
        meetingRepository.deleteMeeting(meetingId);
    }

    public void onDateSortButtonClicked() {
        Boolean previous = isSortingByDateMutableLiveData.getValue();
        if (previous == null) {
            previous = false;
        }
        isSortingByDateMutableLiveData.setValue(!previous);
    }

    public void onRoomSortButtonClicked() {
        Boolean previousRoom = isSortingByRoomMutableLiveData.getValue();
        if (previousRoom == null) {
            previousRoom = false;
        }

        isSortingByDateMutableLiveData.setValue(null);
        isSortingByRoomMutableLiveData.setValue(!previousRoom);
    }
}

