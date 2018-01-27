package com.titikcoe.www.bluehacks.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.titikcoe.www.bluehacks.R;

/**
 * Created by Adrian Mark Perea on 27/01/2018.
 */

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private String[] mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitleTextView;
        public TextView mUploaderTextView;
        public View mLayout;
        public ViewHolder(View v) {
            super(v);

            mLayout = v;
            mTitleTextView = v.findViewById(R.id.title_text_view);
            mUploaderTextView = v.findViewById(R.id.uploader_text_view);
        }
    }

    public PlaylistAdapter(String[] dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.playlist_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTitleTextView.setText(mDataSet[position]);
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}
