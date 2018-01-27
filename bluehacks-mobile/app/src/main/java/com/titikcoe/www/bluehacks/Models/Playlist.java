package com.titikcoe.www.bluehacks.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian Mark Perea on 28/01/2018.
 */

public class Playlist {

    private String mOwnerName;
    private String mPlaylistName;
    private List<Course> mCourses;

    public Playlist(String ownerName, String playlistName) {
        mOwnerName = ownerName;
        mPlaylistName = playlistName;
        mCourses = new ArrayList<Course>();
    }

    public Playlist(String ownerName, String playlistName, List<Course> courses) {
        mOwnerName = ownerName;
        mPlaylistName = playlistName;
        mCourses = courses;
    }

    public void addCourse(Course course) {
        mCourses.add(course);
    }

    public String getOwnerName() {
        return mOwnerName;
    }

    public void setOwnerName(String mOwnerName) {
        this.mOwnerName = mOwnerName;
    }

    public String getPlaylistName() {
        return mPlaylistName;
    }

    public void setPlaylistName(String mPlaylistName) {
        this.mPlaylistName = mPlaylistName;
    }
}
