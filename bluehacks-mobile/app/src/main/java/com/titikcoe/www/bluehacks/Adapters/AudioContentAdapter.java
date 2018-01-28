package com.titikcoe.www.bluehacks.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.titikcoe.www.bluehacks.Models.Course;
import com.titikcoe.www.bluehacks.R;

import java.util.List;

/**
 * Created by Adrian Mark Perea on 27/01/2018.
 */

public class AudioContentAdapter extends RecyclerView.Adapter<AudioContentAdapter.ViewHolder> {
    private List<Course> mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mPosterImageView;
        public TextView mTitleTextView;
        public TextView mUploaderTextView;
        public ImageView mAudioImageView;
        public TextView mDurationTextView;
        public ImageView mFileSizeImageView;
        public TextView mFileSizeTextView;
        public TextView mDescriptionTextView;
        public ImageView mActionLikeImageView;
        public ImageView mActionAddToPlaylistImageView;
        public ImageView mActionDownloadImageView;
        public View mLayout;
        public ViewHolder(View v) {
            super(v);

            mLayout = v;
            mPosterImageView = (ImageView) v.findViewById(R.id.poster_image_view);
            mTitleTextView = (TextView) v.findViewById(R.id.title_text_view);
            mUploaderTextView = (TextView) v.findViewById(R.id.uploader_text_view);
            mAudioImageView = (ImageView) v.findViewById(R.id.audio_image_view);
            mDurationTextView = (TextView) v.findViewById(R.id.duration_text_view);
            mFileSizeImageView = (ImageView) v.findViewById(R.id.file_size_image_view);
            mFileSizeTextView = (TextView) v.findViewById(R.id.file_size_text_view);
            mDescriptionTextView = (TextView) v.findViewById(R.id.description_text_view);
            mActionLikeImageView = (ImageView) v.findViewById(R.id.action_like);
            mActionAddToPlaylistImageView = (ImageView) v.findViewById(R.id.action_add_to_playlist);
            mActionDownloadImageView = (ImageView) v.findViewById(R.id.action_download);
        }
    }

    public AudioContentAdapter(List<Course> dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.audio_content_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add music playing shit here
            }
        });
        holder.mTitleTextView.setText(mDataSet.get(position).getTitle());
        holder.mUploaderTextView.setText(mDataSet.get(position).getUploader());
        holder.mDurationTextView.setText(mDataSet.get(position).getDuration());
        holder.mFileSizeTextView.setText(mDataSet.get(position).getFileSize() + "Mb");
        holder.mDescriptionTextView.setText(mDataSet.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
