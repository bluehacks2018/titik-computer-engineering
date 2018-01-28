package com.titikcoe.www.bluehacks.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.titikcoe.www.bluehacks.Activities.PlaylistActivity;
import com.titikcoe.www.bluehacks.Models.Playlist;
import com.titikcoe.www.bluehacks.R;
import com.titikcoe.www.bluehacks.Utils.RandomPhotoFactory;

import java.util.List;

/**
 * Created by Adrian Mark Perea on 27/01/2018.
 */

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private Context mCtx;
    private List<Playlist> mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mPosterImageView;
        public TextView mTitleTextView;
        public TextView mUploaderTextView;
        public View mLayout;
        public ViewHolder(View v) {
            super(v);

            mLayout = v;
            mPosterImageView = v.findViewById(R.id.playlist_image_view);
            mTitleTextView = v.findViewById(R.id.title_text_view);
            mUploaderTextView = v.findViewById(R.id.uploader_text_view);
        }
    }

    public PlaylistAdapter(Context ctx, List<Playlist> dataSet) {
        mCtx = ctx;
        mDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.playlist_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startPlaylistIntent = new Intent(mCtx, PlaylistActivity.class);

                startPlaylistIntent.putExtra("position", position);
                mCtx.startActivity(startPlaylistIntent);
            }
        });

        holder.mPosterImageView.setImageResource(mDataSet.get(position).getResourceId());
        holder.mTitleTextView.setText(mDataSet.get(position).getPlaylistName());
        holder.mUploaderTextView.setText(mDataSet.get(position).getOwnerName());

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
