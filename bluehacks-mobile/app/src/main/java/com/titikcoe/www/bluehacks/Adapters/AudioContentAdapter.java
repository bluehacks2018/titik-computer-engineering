package com.titikcoe.www.bluehacks.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.titikcoe.www.bluehacks.Activities.MainActivity;
import com.titikcoe.www.bluehacks.Activities.MySoundsActivity;
import com.titikcoe.www.bluehacks.Activities.RecordActivity;
import com.titikcoe.www.bluehacks.Activities.StreamerActivity;
import com.titikcoe.www.bluehacks.Activities.UploadAudio;
import com.titikcoe.www.bluehacks.Models.Course;
import com.titikcoe.www.bluehacks.R;
import com.titikcoe.www.bluehacks.service.contentcatalogs.MusicLibrary;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Adrian Mark Perea on 27/01/2018.
 */

public class AudioContentAdapter extends RecyclerView.Adapter<AudioContentAdapter.ViewHolder> {
    private List<Course> mDataSet;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private Context mCtx;

    public static class ViewHolder extends RecyclerView.ViewHolder {




        public ImageView mPosterImageView;
        public TextView mTitleTextView;
        public TextView mUploaderTextView;
        public ImageView mAudioImageView;
        public TextView mDurationTextView;
        public ImageView mFileSizeImageView;
        public TextView mFileSizeTextView;
        public TextView mDescriptionTextView;
        public ImageView mActionLikeImageView;
        public ImageView mActionAddToPlaylistImageView;
        public ImageView mActionDownloadImageView;
        public View mLayout;
        public ViewHolder(View v) {
            super(v);

            mLayout = v;
            mPosterImageView = (ImageView) v.findViewById(R.id.poster_image_view);
            mTitleTextView = (TextView) v.findViewById(R.id.title_text_view);
            mUploaderTextView = (TextView) v.findViewById(R.id.uploader_text_view);
            mAudioImageView = (ImageView) v.findViewById(R.id.audio_image_view);
            mDurationTextView = (TextView) v.findViewById(R.id.duration_text_view);
            mFileSizeImageView = (ImageView) v.findViewById(R.id.file_size_image_view);
            mFileSizeTextView = (TextView) v.findViewById(R.id.file_size_text_view);
            mDescriptionTextView = (TextView) v.findViewById(R.id.description_text_view);
            mActionLikeImageView = (ImageView) v.findViewById(R.id.action_like);
            mActionAddToPlaylistImageView = (ImageView) v.findViewById(R.id.action_add_to_playlist);
            mActionDownloadImageView = (ImageView) v.findViewById(R.id.action_download);
        }
    }

    public AudioContentAdapter(Context ctx, List<Course> dataSet) {
        mDataSet = dataSet;
        mAuth = FirebaseAuth.getInstance();
        mCtx = ctx;
        signInAnonymously();

    }
    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener((Activity) mCtx, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(mCtx, "WAZZAAAAAP", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener((Activity) mCtx, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.audio_content_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("whatever", mDataSet.get(position).getUrl());
                mStorageRef = FirebaseStorage.getInstance().getReference();
                final StorageReference ref = mStorageRef.child(mDataSet.get(position).getUrl());
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Intent uploadIntent = new Intent(mCtx, StreamerActivity.class);
                        String title= "Your Current Lesson";
                        Log.d("Hey",title);
                        uploadIntent.putExtra("title",title);
                        uploadIntent.putExtra("url",uri);
                        MusicLibrary.createMediaMetadataCompat(
                                title.replace(" ","_"),
                                title,
                                mAuth.getCurrentUser().getDisplayName(),
                                "",
                                "",
                                160,
                                TimeUnit.SECONDS,
                                uri.toString(),
                                R.drawable.album_youtube_audio_library_rock_2,
                                "album_youtube_audio_library_rock_2");
                        mCtx.startActivity(uploadIntent);





                        Log.d("URL", uri.toString());
                    }
                });


            }
        });
        holder.mTitleTextView.setText(mDataSet.get(position).getTitle());
        holder.mUploaderTextView.setText(mDataSet.get(position).getUploader());
        holder.mDurationTextView.setText(mDataSet.get(position).getDuration());
        holder.mFileSizeTextView.setText(mDataSet.get(position).getFileSize() + "Mb");
        holder.mDescriptionTextView.setText(mDataSet.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
