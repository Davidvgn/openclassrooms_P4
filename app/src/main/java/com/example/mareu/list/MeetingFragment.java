package com.example.mareu.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mareu.R;
import com.example.mareu.ui.ViewModelFactory;

public class MeetingFragment extends Fragment {

    public static MeetingFragment newInstance() {
        MeetingFragment fragment = new MeetingFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.meetings_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() == null) {
            throw new IllegalStateException("Please use MeetingFragment.newInstance() to build the Fragment");
        }

        MeetingViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MeetingViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.meeting_rv);
        MeetingAdapter adapter = new MeetingAdapter(new OnMeetingClickedListener() {

            @Override
            public void onDeleteMeetingClicked(long meetingId) {
                viewModel.onDeleteMeetingClicked(meetingId);
            }
        });

        recyclerView.setAdapter(adapter);

        viewModel.getMeetingViewStateItemsLiveData().observe(getViewLifecycleOwner(), meetingViewStateItemList ->
                adapter.submitList(meetingViewStateItemList)
        );
    }
}
