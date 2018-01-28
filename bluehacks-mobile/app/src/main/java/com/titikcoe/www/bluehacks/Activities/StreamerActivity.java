package com.titikcoe.www.bluehacks.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.titikcoe.www.bluehacks.R;
import com.titikcoe.www.bluehacks.client.MediaBrowserAdapter;
import com.titikcoe.www.bluehacks.service.contentcatalogs.MusicLibrary;
import com.titikcoe.www.bluehacks.ui.MediaSeekBar;

public class StreamerActivity extends AppCompatActivity {

    private ImageView mAlbumArt;
    private TextView mTitleTextView;
    private TextView mArtistTextView;
    private ImageView mMediaControlsImage;
    private MediaSeekBar mSeekBarAudio;
    final Intent receivedQueries = getIntent();

    private MediaBrowserAdapter mMediaBrowserAdapter;

    private boolean mIsPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sounds);
        initializeUI();
        mMediaBrowserAdapter = new MediaBrowserAdapter(StreamerActivity.this);
        mMediaBrowserAdapter.addListener(new MediaBrowserListener());
//        Button rec = (Button) findViewById(R.id.btn_record);
//        rec.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent recordIntent = new Intent(MySoundsActivity.this,
//                        RecordActivity.class);
//                startActivity(recordIntent);
//                return;
//            }
//        });
//        Button stream = (Button) findViewById(R.id.btn_stream);
//        stream.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent streamIntent = new Intent(MySoundsActivity.this,
//                        StreamerActivity.class);
//                startActivity(streamIntent);
//                return;
//            }
//        });
    }

    private void initializeUI() {
        mTitleTextView = (TextView) findViewById(R.id.song_title);
        mArtistTextView = (TextView) findViewById(R.id.song_artist);
        mAlbumArt = (ImageView) findViewById(R.id.album_art);
        mMediaControlsImage = (ImageView) findViewById(R.id.media_controls);
        mSeekBarAudio = (MediaSeekBar) findViewById(R.id.seekbar_audio);


        //String title = receivedQueries.getStringExtra("title");
       // mTitleTextView.setText(title);

        final Button buttonPrevious = (Button) findViewById(R.id.button_previous);
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaBrowserAdapter.getTransportControls().skipToPrevious();
            }
        });

        final Button buttonPlay = (Button) findViewById(R.id.button_play);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsPlaying) {
                    mMediaBrowserAdapter.getTransportControls().pause();
                } else {
                    mMediaBrowserAdapter.getTransportControls().play();
                }
            }
        });

        final Button buttonNext = (Button) findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaBrowserAdapter.getTransportControls().skipToNext();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mMediaBrowserAdapter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mSeekBarAudio.disconnectController();
        mMediaBrowserAdapter.onStop();
    }

    private class MediaBrowserListener extends MediaBrowserAdapter.MediaBrowserChangeListener {

        @Override
        public void onConnected(@Nullable MediaControllerCompat mediaController) {
            super.onConnected(mediaController);
            mSeekBarAudio.setMediaController(mediaController);
        }

        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat playbackState) {
            mIsPlaying = playbackState != null &&
                    playbackState.getState() == PlaybackStateCompat.STATE_PLAYING;
            mMediaControlsImage.setPressed(mIsPlaying);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat mediaMetadata) {
            if (mediaMetadata == null) {
                return;
            }
            mTitleTextView.setText(
                    mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
            mArtistTextView.setText(
                    mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
            mAlbumArt.setImageBitmap(MusicLibrary.getAlbumBitmap(
                    StreamerActivity.this,
                    mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)));
        }
    }
}

