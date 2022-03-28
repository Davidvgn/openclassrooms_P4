package com.example.mareu.meeting;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mareu.LiveDataTestUtils;
import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingRepository;
import com.example.mareu.ui.list.MeetingViewModel;
import com.example.mareu.ui.list.MeetingViewStateItem;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MeetingViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private MeetingRepository meetingRepository;

    private MutableLiveData<List<Meeting>> meetingsMutableLiveData;
    private MeetingViewModel viewModel;

    private MutableLiveData<Boolean> isSortingByRoomMutableLiveData;
    private MutableLiveData<Boolean> isSortingByDateMutableLiveData;

    @Before
    public void setUp() {
        meetingsMutableLiveData = new MutableLiveData<>();

        given(meetingRepository.getMeetingsLiveData()).willReturn(meetingsMutableLiveData);

        List<Meeting> meetings = getMeetings();
        meetingsMutableLiveData.setValue(meetings);

        isSortingByRoomMutableLiveData = new MutableLiveData<>();
        isSortingByDateMutableLiveData = new MutableLiveData<>();

        isSortingByDateMutableLiveData.setValue(false);
        isSortingByRoomMutableLiveData.setValue(false);

        viewModel = new MeetingViewModel(meetingRepository);

        verify(meetingRepository).getMeetingsLiveData();
    }

    @Test
    public void initialCase() {
        meetingsMutableLiveData.setValue(new ArrayList<>());

        LiveDataTestUtils.observeForTesting(viewModel.getMeetingViewStateItemsLiveData(), value -> {
            assertEquals(0, value.size());
        });
    }

    @Test
    public void nominalCase() {
        // When
        LiveDataTestUtils.observeForTesting(viewModel.getMeetingViewStateItemsLiveData(), value -> {
            // Then
            assertEquals(getExpectedMeetingViewStateItems(), value);
        });
    }

    @Test
    public void given_sorting_by_date_when_getting_state_then_list_should_be_in_alphabetical_order() {
        // Given
        viewModel.onDateSortButtonClicked();

        // When
        LiveDataTestUtils.observeForTesting(viewModel.getMeetingViewStateItemsLiveData(), value -> {
            // Then
            List<MeetingViewStateItem> expected = new ArrayList<>();
            expected.add(new MeetingViewStateItem(0, "2022-03-15", "14:00", "Java", "SUJET 1", "particpant1@email.com, participant2@email.com"));
            expected.add(new MeetingViewStateItem(2, "2022-03-22", "14:00", "Dart", "SUJET 3", "particpant1@email.com, participant2@email.com"));
            expected.add(new MeetingViewStateItem(1, "2022-03-23", "14:00", "Swift", "SUJET 2", "particpant1@email.com, participant2@email.com"));
            expected.add(new MeetingViewStateItem(3, "2022-03-28", "14:00", "Kotlin", "SUJET 4", "particpant1@email.com, participant2@email.com"));

            assertEquals(expected, value);
        });
    }

    @Test
    public void given_sorting_by_date_with_2_meetings_with_same_day_when_getting_state_then_list_should_be_in_alphabetical_order() {
        List<Meeting> meetings = Arrays.asList(
            new Meeting(
                0, LocalDateTime.of(2022, 3, 15, 14, 0),
                "Java", "SUJET 1",
                "particpant1@email.com, participant2@email.com"
            ),
            new Meeting(
                1, LocalDateTime.of(2022, 3, 15, 15, 0),
                "Swift", "SUJET 2",
                "particpant1@email.com, participant2@email.com"
            ),
            new Meeting(
                2, LocalDateTime.of(2022, 3, 15, 6, 0),
                "Dart", "SUJET 3",
                "particpant1@email.com, participant2@email.com"
            ),
            new Meeting(
                3, LocalDateTime.of(2022, 3, 15, 10, 0),
                "Kotlin", "SUJET 4",
                "particpant1@email.com, participant2@email.com"
            )
        );
        meetingsMutableLiveData.setValue(meetings);

        // Given
        viewModel.onDateSortButtonClicked();

        // When
        LiveDataTestUtils.observeForTesting(viewModel.getMeetingViewStateItemsLiveData(), value -> {
            // Then
            List<MeetingViewStateItem> expected = new ArrayList<>();
            expected.add(new MeetingViewStateItem(2, "2022-03-15", "06:00", "Dart", "SUJET 3", "particpant1@email.com, participant2@email.com"));
            expected.add(new MeetingViewStateItem(3, "2022-03-15", "10:00", "Kotlin", "SUJET 4", "particpant1@email.com, participant2@email.com"));
            expected.add(new MeetingViewStateItem(0, "2022-03-15", "14:00", "Java", "SUJET 1", "particpant1@email.com, participant2@email.com"));
            expected.add(new MeetingViewStateItem(1, "2022-03-15", "15:00", "Swift", "SUJET 2", "particpant1@email.com, participant2@email.com"));

            assertEquals(expected, value);
        });
    }


    @Test
    public void given_sorting_by_date_desc_with_2_meetings_with_same_day_when_getting_state_then_list_should_be_in_alphabetical_order() {
        List<Meeting> meetings = Arrays.asList(
            new Meeting(
                0, LocalDateTime.of(2022, 3, 15, 14, 0),
                "Java", "SUJET 1",
                "particpant1@email.com, participant2@email.com"
            ),
            new Meeting(
                1, LocalDateTime.of(2022, 3, 15, 15, 0),
                "Swift", "SUJET 2",
                "particpant1@email.com, participant2@email.com"
            ),
            new Meeting(
                2, LocalDateTime.of(2022, 3, 15, 6, 0),
                "Dart", "SUJET 3",
                "particpant1@email.com, participant2@email.com"
            ),
            new Meeting(
                3, LocalDateTime.of(2022, 3, 15, 10, 0),
                "Kotlin", "SUJET 4",
                "particpant1@email.com, participant2@email.com"
            )
        );
        meetingsMutableLiveData.setValue(meetings);

        // Given
        viewModel.onDateSortButtonClicked();
        viewModel.onDateSortButtonClicked();

        // When
        LiveDataTestUtils.observeForTesting(viewModel.getMeetingViewStateItemsLiveData(), value -> {
            // Then
            List<MeetingViewStateItem> expected = new ArrayList<>();
            expected.add(new MeetingViewStateItem(1, "2022-03-15", "15:00", "Swift", "SUJET 2", "particpant1@email.com, participant2@email.com"));
            expected.add(new MeetingViewStateItem(0, "2022-03-15", "14:00", "Java", "SUJET 1", "particpant1@email.com, participant2@email.com"));
            expected.add(new MeetingViewStateItem(3, "2022-03-15", "10:00", "Kotlin", "SUJET 4", "particpant1@email.com, participant2@email.com"));
            expected.add(new MeetingViewStateItem(2, "2022-03-15", "06:00", "Dart", "SUJET 3", "particpant1@email.com, participant2@email.com"));

            assertEquals(expected, value);
        });
    }

    @Test
    public void setIsSortingByDateMutableLiveData() {

    }


    @Test
    public void onDeleteMeetingTest() {
        // When
        viewModel.onDeleteMeetingClicked(89);

        // Then
        Mockito.verify(meetingRepository).deleteMeeting(89);
        Mockito.verifyNoMoreInteractions(meetingRepository);
        //todo Nino : utilité de verifyNoInteractions/ verifyNoMoreInteractions ?

    }


    // region IN
    private List<Meeting> getMeetings() {
        List<Meeting> meetings = new ArrayList<>();

        meetings.add(
            new Meeting(
                0, LocalDateTime.of(2022, 3, 15, 14, 0),
                "Java", "SUJET 1",
                "particpant1@email.com, participant2@email.com"
            )
        );

        meetings.add(
            new Meeting(
                1, LocalDateTime.of(2022, 3, 23, 14, 0),
                "Swift", "SUJET 2",
                "particpant1@email.com, participant2@email.com"
            )
        );

        meetings.add(
            new Meeting(
                2, LocalDateTime.of(2022, 3, 22, 14, 0),
                "Dart", "SUJET 3",
                "particpant1@email.com, participant2@email.com"
            )
        );

        meetings.add(
            new Meeting(
                3, LocalDateTime.of(2022, 3, 28, 14, 0),
                "Kotlin", "SUJET 4",
                "particpant1@email.com, participant2@email.com"
            )
        );

        return meetings;
    }
    // endregion IN

    // region OUT
    public List<MeetingViewStateItem> getExpectedMeetingViewStateItems() {
        List<MeetingViewStateItem> expected = new ArrayList<>();
        expected.add(new MeetingViewStateItem(0, "2022-03-15", "14:00", "Java", "SUJET 1", "particpant1@email.com, participant2@email.com"));
        expected.add(new MeetingViewStateItem(1, "2022-03-23", "14:00", "Swift", "SUJET 2", "particpant1@email.com, participant2@email.com"));
        expected.add(new MeetingViewStateItem(2, "2022-03-22", "14:00", "Dart", "SUJET 3", "particpant1@email.com, participant2@email.com"));
        expected.add(new MeetingViewStateItem(3, "2022-03-28", "14:00", "Kotlin", "SUJET 4", "particpant1@email.com, participant2@email.com"));

        return expected;
    }
    // endregion OUT
}
