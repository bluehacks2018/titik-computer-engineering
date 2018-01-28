package com.titikcoe.www.bluehacks;

import android.app.Application;

import com.titikcoe.www.bluehacks.Models.Playlist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian Mark Perea on 28/01/2018.
 */

public class TutoApplication extends Application {
    ArrayList<Playlist> mPlaylists;

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
}
