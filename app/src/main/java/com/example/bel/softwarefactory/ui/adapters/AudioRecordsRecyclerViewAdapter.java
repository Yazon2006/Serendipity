package com.example.bel.softwarefactory.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.example.bel.softwarefactory.R;
import com.example.bel.softwarefactory.entities.AudioRecordEntity;
import com.example.bel.softwarefactory.utils.AppConstants;
import com.hugomatilla.audioplayerview.AudioPlayerView;

import java.util.ArrayList;
import java.util.List;

public class AudioRecordsRecyclerViewAdapter extends RecyclerView.Adapter<AudioRecordsRecyclerViewAdapter.ViewHolder> {

    private List<AudioRecordEntity> audioRecordEntities;
    private Context context;
    private ArrayList<AudioPlayerView> audioPlayerViews;

    public AudioRecordsRecyclerViewAdapter(Context context, List<AudioRecordEntity> audioRecordEntities) {
        this.context = context;
        this.audioRecordEntities = audioRecordEntities;
        this.audioPlayerViews = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.audio_record_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AudioRecordEntity currentRecord = audioRecordEntities.get(position);
        holder.title_textView.setText(currentRecord.getFile_name());
        holder.description_textView.setText(currentRecord.getDescription());
        holder.play_audioPlayerView.withUrl(AppConstants.SERVER_ADDRESS + currentRecord.getFile_path());
        audioPlayerViews.add(holder.play_audioPlayerView);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);

        holder.play_audioPlayerView.destroy();
    }

    @Override
    public int getItemCount() {
        return audioRecordEntities.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private AudioPlayerView play_audioPlayerView;
        private TextView title_textView;
        private TextView description_textView;

        public ViewHolder(final View itemView) {
            super(itemView);
            play_audioPlayerView = (AudioPlayerView) itemView.findViewById(R.id.play_audioPlayerView);
            title_textView = (TextView) itemView.findViewById(R.id.title_textView);
            description_textView = (TextView) itemView.findViewById(R.id.description_textView);
        }
    }

    public void deleteViews() {
        Stream.of(audioPlayerViews).forEach(AudioPlayerView::destroy);
    }
}
