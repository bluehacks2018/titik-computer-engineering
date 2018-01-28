package com.titikcoe.www.bluehacks.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.titikcoe.www.bluehacks.Adapters.PlaylistAdapter;
import com.titikcoe.www.bluehacks.Decorations.GridSpacingItemDecoration;
import com.titikcoe.www.bluehacks.Models.Course;
import com.titikcoe.www.bluehacks.Models.Playlist;
import com.titikcoe.www.bluehacks.R;
import com.titikcoe.www.bluehacks.TutoApplication;
import com.titikcoe.www.bluehacks.Utils.RandomPhotoFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private String[] sampleDataSet = {"abc", "def", "ghi", "jkl",
            "abc", "def", "ghi", "jkl",
            "abc", "def", "ghi", "jkl",
            "abc", "def", "ghi", "jkl",
            "abc", "def", "ghi", "jkl",
    };

//    private List<Playlist> mPlaylists;

    private static final String[] CATEGORIES = {"All", "Livelihood",
                                              "Technical Skills",
                                              "Self Improvement",
                                              "Group Dynamics",
                                              "Record your Own!",
                                              "My Recordings"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TutoApplication app = (TutoApplication) getApplication();
        ArrayList<Playlist> playlists = app.getPlaylists();


        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitleEnabled(false);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_tool_bar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("All");

        mRecyclerView = (RecyclerView) findViewById(R.id.content_recycler_view);

        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        int spacingInPixels = 12;
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true, 0));

//        mPlaylists = new ArrayList<Playlist>();

        mAdapter = new PlaylistAdapter(this, playlists);
        mRecyclerView.setAdapter(mAdapter);

        getPlaylists();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.nav_list_view);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CATEGORIES));

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDrawerList.setItemChecked(position, true);
                if(CATEGORIES[position].matches("Record your Own!"))
                {
                    Intent uploadIntent = new Intent(MainActivity.this, UploadAudio.class);
                    startActivity(uploadIntent);
                    finish();
                }
                if(CATEGORIES[position].matches("My Recordings"))
                {
                    Intent soundsIntent = new Intent(MainActivity.this, MySoundsActivity.class);
                    startActivity(soundsIntent);
                    finish();
                }
                getSupportActionBar().setTitle(CATEGORIES[position]);
                mDrawerLayout.closeDrawer(findViewById(R.id.nav_layout));
            }
        });
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    public void getPlaylists() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://b225c536.ngrok.io/suggest";
        url += "?user_email=" + ((TutoApplication) getApplication()).getUserEmail();


            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                TutoApplication app = (TutoApplication) getApplication();
                                app.getPlaylists().clear();
                                JSONArray data = response.getJSONArray("data");
                                Log.d("JSON", data.toString(4));
                                for (int i = 0; i < data.length(); i++) {
                                    Playlist playlist;
                                    JSONObject datum = data.getJSONObject(i);
                                    String playlistName = datum.getString("name");
                                    String playlistOwner = datum.getString("owner");
                                    ArrayList<Course> courses = new ArrayList<>();
                                    JSONArray firebase_data = datum.getJSONArray("firebase_data");
                                    for (int j = 0; j < firebase_data.length(); j++) {
                                        JSONObject firebase_datum = firebase_data.getJSONObject(j);

                                        String size = firebase_datum.getString("size");
                                        String url = firebase_datum.getString("url");
//                                        JSONObject metadata = firebase_datum.getJSONObject("metadata");
                                        // TODO: get metadata shit
                                        courses.add(new Course(null, playlistOwner,
                                                null, size, null, url));
                                    }
                                    playlist = new Playlist(RandomPhotoFactory.getRandomPhoto(), playlistOwner, playlistName, courses);

                                    app.addPlaylist(playlist);
//                                            mPlaylists.add(playlist);

                                }
                                mAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                        }
                    });
            // Add the request to the RequestQueue.
            queue.add(jsObjRequest);
            queue.start();



    }



}

