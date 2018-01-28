package com.titikcoe.www.bluehacks.Activities;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.titikcoe.www.bluehacks.Adapters.AudioContentAdapter;
import com.titikcoe.www.bluehacks.Models.Course;
import com.titikcoe.www.bluehacks.Models.Playlist;
import com.titikcoe.www.bluehacks.R;
import com.titikcoe.www.bluehacks.TutoApplication;

import java.util.ArrayList;
import java.util.List;

public class PlaylistActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ImageView mHeaderImage;

    private String[] sampleDataSet = {"abc", "def", "ghi", "jkl",
            "abc", "def", "ghi", "jkl",
            "abc", "def", "ghi", "jkl",
            "abc", "def", "ghi", "jkl",
            "abc", "def", "ghi", "jkl",
    };

    private List<Course> mCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        Intent i = getIntent();
        int position = i.getIntExtra("position", -1);
        if (position != -1) {
            TutoApplication app = (TutoApplication) getApplication();
            Playlist currentPlaylist = app.getPlaylists().get(position);

            CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
            ctl.setTitle(currentPlaylist.getPlaylistName());

            mCourses = currentPlaylist.getCourses();

            mRecyclerView = (RecyclerView) findViewById(R.id.content_recycler_view);

            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new AudioContentAdapter(this, mCourses);
            mRecyclerView.setAdapter(mAdapter);

            mHeaderImage = (ImageView) findViewById(R.id.header_image);
            mHeaderImage.setImageResource(currentPlaylist.getResourceId());
        }


    }
}
