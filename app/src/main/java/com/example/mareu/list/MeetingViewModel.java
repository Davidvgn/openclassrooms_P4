package com.example.mareu.list;

import static android.graphics.Typeface.*;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingRepository;

import java.util.ArrayList;
import java.util.List;

public class MeetingViewModel extends ViewModel {

    private final MeetingRepository meetingRepository;

    public MeetingViewModel(MeetingRepository meetingRepository) {
    this.meetingRepository = meetingRepository;
    }

    public LiveData<List<MeetingViewStateItem>> getMeetingViewStateItemsLiveData() {
        return Transformations.map(meetingRepository.getMeetingsLiveData(), meetings -> {
            List<MeetingViewStateItem> meetingViewStateItem = new ArrayList<>();

            for (Meeting meeting : meetings) {
                SpannableStringBuilder builder = new SpannableStringBuilder();
                builder.append(meeting.getHour())
                    .append(" H ", new StyleSpan(BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    .append(meeting.getMin());

                meetingViewStateItem.add(
                    new MeetingViewStateItem(
                        meeting.getId(),
                        builder,
                        meeting.getMeetingRoom(),
                        meeting.getMeetingSubject(),
                        meeting.getParticipants()
                    )
                );
            }
            return meetingViewStateItem;
        });
    }

    public void onDeleteMeetingClicked(long meetingId) {
        meetingRepository.deleteMeeting(meetingId);
    }
}

