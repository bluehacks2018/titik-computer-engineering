package com.titikcoe.www.bluehacks.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.titikcoe.www.bluehacks.Adapters.AudioContentAdapter;
import com.titikcoe.www.bluehacks.Models.Course;
import com.titikcoe.www.bluehacks.R;

import java.util.ArrayList;
import java.util.List;

public class PlaylistActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

        mRecyclerView = (RecyclerView) findViewById(R.id.content_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mCourses = new ArrayList<Course>();
        mAdapter = new AudioContentAdapter(mCourses);
        mRecyclerView.setAdapter(mAdapter);
    }
}
