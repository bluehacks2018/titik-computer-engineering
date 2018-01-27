package com.titikcoe.www.bluehacks.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.titikcoe.www.bluehacks.R;
import com.titikcoe.www.bluehacks.service.contentcatalogs.MusicLibrary;
import com.titikcoe.www.bluehacks.ui.MediaSeekBar;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RecordActivity extends AppCompatActivity {

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


    private EditText mEditTitle = null;
    private EditText mEditDesc = null;
    private EditText mEditGenre = null;

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
        mFileName +="/"+ mEditTitle.getText().toString().replace(" ", "_").concat(".3gp");
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

    class RecordButton extends android.support.v7.widget.AppCompatButton {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends android.support.v7.widget.AppCompatButton {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Record to the external cache directory for visibility
        mFileName = getExternalFilesDir("").getAbsolutePath();
        mFileName += "/audiorecordtesting.3gp";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);


        setContentView(R.layout.activity_record);
        mPauseButton = (Button) findViewById(R.id.btn_pause);
//        mPlayButton = (Button) findViewById(R.id.btn_play);
//        mStopButton = (Button) findViewById(R.id.btn_stop);
//        mSaveButton = (Button) findViewById(R.id.btn_save);
        mRecordButton = (Button) findViewById(R.id.btn_record);

        timerTextView = (TextView) findViewById(R.id.txv_time);




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
                if(!mRecording)
                {
                    mRecording=true;
                    mRecordButton.setText("Pause");
                    onRecord(true);
                }
                else
                {
                    onRecord(false);
                    mPauseButton.setText("Save");
                    mStopped=true;
                }

                return;
            }
        });
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mStopped)
                {
                    if(!mPaused)
                    {
                        mRecorder.pause();
                        mPauseButton.setText("Continue");
                        mPaused=true;
                        timeSwapBuff += timeInMilliseconds;
                        customHandler.removeCallbacks(updateTimerThread);
                    }
                    else
                    {
                        mRecorder.resume();
                        mPauseButton.setText("Pause");
                        mPaused=false;
                        startHTime = SystemClock.uptimeMillis();
                        customHandler.postDelayed(updateTimerThread, 0);

                    }
                }
                else{
                    Intent doneIntent = new Intent(RecordActivity.this, MainActivity.class);
                    String mediaId = mEditTitle.getText().toString().replace(" ", "_");
                    String title = mEditTitle.getText().toString();
                    String desc = mEditDesc.getText().toString();
                    String genre = mEditGenre.getText().toString();
                    String fileName = mEditTitle.getText().toString().replace(" ", "_").concat(".3gp");
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
                    startActivity(doneIntent);
                    finish();
                    return;


                }
                return;
            }
        });
//        mSaveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String mediaId = mEditTitle.getText().toString().replace(" ", "_");
//                String title = mEditTitle.getText().toString();
//                String desc = mEditDesc.getText().toString();
//                String genre = mEditGenre.getText().toString();
//                String fileName = mEditTitle.getText().toString().replace(" ", "_").concat(".3gp");
//                mLib.createMediaMetadataCompat(
//                        mediaId,
//                        title,
//                        "The 126ers",
//                        desc,
//                        genre,
//                        timeSwapBuff,
//                        TimeUnit.MILLISECONDS,
//                        fileName    ,
//                        R.drawable.album_youtube_audio_library_rock_2,
//                        "album_youtube_audio_library_rock_2");
//                return;
//            }
//        });
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
}
