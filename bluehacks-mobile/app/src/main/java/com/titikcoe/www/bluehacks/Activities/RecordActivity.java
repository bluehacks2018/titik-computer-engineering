package com.titikcoe.www.bluehacks.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.titikcoe.www.bluehacks.R;
import com.titikcoe.www.bluehacks.TutoApplication;
import com.titikcoe.www.bluehacks.service.contentcatalogs.MusicLibrary;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class RecordActivity extends AppCompatActivity {
    private StorageReference mStorageRef;

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mFileName = null;
    private Button mRecordButton = null;
    private MediaRecorder mRecorder = null;
    private MusicLibrary mLib = new MusicLibrary();
//    private Button   mStopButton = null;
    private Button   mPauseButton = null;
//    private Button   mSaveButton = null;
    private MediaPlayer   mPlayer = null;
//    private MediaSeekBar mSeekBarAudio;
    public TextView timerTextView;
    private long startHTime = 0L;
    private long timeHolder = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
//    boolean mStartPlaying = false;
    boolean mRecording = false;
    boolean mStopped = false;
    boolean mPaused = false;
    String title = null;
    String desc = null;
    String genre = null;
    String tags = null;
    String playlist = null;


//    private EditText mEditTitle = null;
//    private EditText mEditDesc = null;
//    private EditText mEditGenre = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        mFileName = getExternalFilesDir("").getAbsolutePath();
        mFileName +="/"+ title.replace(" ", "_").concat(".3gp");
        Log.d("Save Dir", mFileName);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
        startHTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);

        mRecorder = null;
    }

//    class RecordButton extends android.support.v7.widget.AppCompatButton {
//        boolean mStartRecording = true;
//
//        OnClickListener clicker = new OnClickListener() {
//            public void onClick(View v) {
//                onRecord(mStartRecording);
//                if (mStartRecording) {
//                    setText("Stop recording");
//                } else {
//                    setText("Start recording");
//                }
//                mStartRecording = !mStartRecording;
//            }
//        };
//
//        public RecordButton(Context ctx) {
//            super(ctx);
//            setText("Start recording");
//            setOnClickListener(clicker);
//        }
//    }

//    class PlayButton extends android.support.v7.widget.AppCompatButton {
//        boolean mStartPlaying = true;
//
//        OnClickListener clicker = new OnClickListener() {
//            public void onClick(View v) {
//                onPlay(mStartPlaying);
//                if (mStartPlaying) {
//                    setText("Stop playing");
//                } else {
//                    setText("Start playing");
//                }
//                mStartPlaying = !mStartPlaying;
//            }
//        };
//
//        public PlayButton(Context ctx) {
//            super(ctx);
//            setText("Start playing");
//            setOnClickListener(clicker);
//        }
//    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mStorageRef = FirebaseStorage.getInstance().getReference();


        // Record to the external cache directory for visibility
        mFileName = getExternalFilesDir("").getAbsolutePath();
        mFileName += "/audiorecordtesting.3gp";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);


        setContentView(R.layout.activity_record);
//        mPauseButton = (Button) findViewById(R.id.btn_pause);
//        mPlayButton = (Button) findViewById(R.id.btn_play);
//        mStopButton = (Button) findViewById(R.id.btn_stop);
//        mSaveButton = (Button) findViewById(R.id.btn_save);
        mRecordButton = (Button) findViewById(R.id.btn_record);
        TextView txvTitle = (TextView) findViewById(R.id.txv_title);
        timerTextView = (TextView) findViewById(R.id.txv_time);
        final Intent receivedQueries = getIntent();

        title = receivedQueries.getStringExtra("title");
        desc = receivedQueries.getStringExtra("desc");
        genre = receivedQueries.getStringExtra("genre");
        tags = receivedQueries.getStringExtra("tags");
        playlist = receivedQueries.getStringExtra("playlist");

        txvTitle.setText(title);



//        mPauseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                return;
//            }
//        });
//        mPlayButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onPlay(mStartPlaying);
//                if (mStartPlaying) {
//                    mPlayButton.setText("Pause Playing");
//                } else {
//                    mPlayButton.setText("");
//                }
//                mStartPlaying = !mStartPlaying;
//
//                return;
//            }
//        });
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mStopped)
                {
                    Intent doneIntent = new Intent(RecordActivity.this, MainActivity.class);
                    String mediaId = title.replace(" ", "_");
                    String fileName = mediaId.concat(".3gp");
                    Log.d("filename", fileName);
                    mLib.createMediaMetadataCompat(
                            mediaId,
                            title,
                            "The 126ers",
                            desc,
                            genre,
                            timeSwapBuff,
                            TimeUnit.MILLISECONDS,
                            fileName    ,
                            0,
                            "album_youtube_audio_library_rock_2");
                    TutoApplication app = (TutoApplication) getApplication();

                    Uri file = Uri.fromFile(new File(getExternalFilesDir("").getAbsolutePath().concat("/"+fileName).concat(".3gp")));
                    String userName = app.getUserEmail();
                    Log.d("Username", userName);

                    final StorageReference ref = mStorageRef.child(userName+"/"+fileName);

                    try {

                        File savedFile = new File(
                                getExternalFilesDir("").getAbsolutePath().concat("/"+fileName));
                        Log.d("Read Dir", getExternalFilesDir("").getAbsolutePath().concat("/"+fileName));
                        if (savedFile.exists()) {
                            Log.d("SAVEDFILE", "im aliiiiive");
                        } else {
                            Log.d("SAVEDFILE", "wenkwenkwenk");
                        }

                        InputStream stream = new FileInputStream(new File(
                                getExternalFilesDir("").getAbsolutePath().concat("/"+fileName)));
                        UploadTask uploadTask = ref.putStream(stream);
                        Log.d("Noted Firebase", ref.toString());
                        Log.d("Noted Local", ref.toString());
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Log.d("Noted", "NAW ");

                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                Ufload(ref.toString());
                                Log.d("Noted", "YES ");

                            }
                        });
                    } catch (IOException e) {
                        // TODO: Add proper error handling
                        e.printStackTrace();
                    }
                    startActivity(doneIntent);
                }
                else if(!mRecording)
                {
                    mRecording=true;
                    mRecordButton.setText("Stop");
                    onRecord(true);
                }
                else
                {
                    onRecord(false);
                    mStopped=true;
                    mRecordButton.setText("Save");
                }

                return;
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startHTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            if (timerTextView != null)
                timerTextView.setText("" + String.format("%02d", mins) + ":"
                        + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }

    };

    private void Ufload(String fb_url) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "/playlist";
        String name = "playlist_name_here";
        String firebase_url = "firebase_url_here";
        String user_email = "owner_email_here";

        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("firebase_url", fb_url);
            jsonRequest.put("name", name);
            jsonRequest.put("user_email", user_email);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Response Here
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
