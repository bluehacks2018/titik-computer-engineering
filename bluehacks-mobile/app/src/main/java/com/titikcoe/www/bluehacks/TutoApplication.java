package com.titikcoe.www.bluehacks;

import android.app.Application;

import com.titikcoe.www.bluehacks.Models.Playlist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian Mark Perea on 28/01/2018.
 */

public class TutoApplication extends Application {
    private ArrayList<Playlist> mPlaylists;
    private String mUserEmail;

    @Override
    public void onCreate() {
        super.onCreate();
        mPlaylists = new ArrayList<>();
    }

    public ArrayList<Playlist> getPlaylists() {
        return mPlaylists;
    }

    public void addPlaylist(Playlist playlist) {
        mPlaylists.add(playlist);
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public void setUserEmail(String userEmail) {
        mUserEmail = userEmail;
    }
}
