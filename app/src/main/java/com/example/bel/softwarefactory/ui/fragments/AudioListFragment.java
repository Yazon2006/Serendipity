package com.example.bel.softwarefactory.ui.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.example.bel.softwarefactory.R;

import com.example.bel.softwarefactory.entities.AudioRecordEntity;
import com.example.bel.softwarefactory.preferences.SharedPreferencesManager;
import com.example.bel.softwarefactory.ui.adapters.AudioRecordsRecyclerViewAdapter;
import com.example.bel.softwarefactory.ui.views.DividerItemDecoration;
import com.example.bel.softwarefactory.utils.LocationHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;


@EFragment(R.layout.fragment_list_of_recordings)
public class AudioListFragment extends BaseFragment {

    private AudioRecordsRecyclerViewAdapter audioRecordsRecyclerViewAdapter;

    @ViewById
    protected RecyclerView audioRecords_recyclerView;

    @Bean
    protected SharedPreferencesManager sharedPreferencesManager;

    @AfterViews
    protected void afterViews() {
        List<AudioRecordEntity> audioRecordEntities = Stream.of(sharedPreferencesManager.getAudioRecordsList())
                .filter((audioEntity) -> LocationHelper.isAudioAvailable(audioEntity, sharedPreferencesManager.getLastLocation()))
                .collect(Collectors.toList());

        final LinearLayoutManager audioRecordsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        audioRecordsRecyclerViewAdapter = new AudioRecordsRecyclerViewAdapter(getActivity(), audioRecordEntities);
        audioRecords_recyclerView.setLayoutManager(audioRecordsLayoutManager);
        audioRecords_recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        audioRecords_recyclerView.setAdapter(audioRecordsRecyclerViewAdapter);
    }

    @Override
    public void onDestroy() {
        audioRecordsRecyclerViewAdapter.deleteViews();
        super.onDestroy();
    }

}