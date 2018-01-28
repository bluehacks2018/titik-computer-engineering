package com.titikcoe.www.bluehacks.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.titikcoe.www.bluehacks.R;

public class UploadAudio extends AppCompatActivity {
    private EditText edtTitle;
    private EditText edtDesc;
    private EditText edtTags;
    private EditText edtPlaylist;
    private Spinner mSpnGenre;
    private Button btnProceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_audio);

        edtTitle = (EditText) findViewById(R.id.edt_title);
        edtDesc = (EditText) findViewById(R.id.edt_desc);
        edtPlaylist = (EditText) findViewById(R.id.edt_playlist);
        mSpnGenre = (Spinner) findViewById(R.id.spn_field);
        edtTags = (EditText) findViewById(R.id.edt_desc);

        btnProceed = (Button) findViewById(R.id.btn_proceed);

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uploadIntent = new Intent(UploadAudio.this, RecordActivity.class);
                uploadIntent.putExtra("title",edtTitle.getText().toString());
                uploadIntent.putExtra("desc",edtDesc.getText().toString());
                uploadIntent.putExtra("playlist",edtPlaylist.getText().toString());
                uploadIntent.putExtra("genre",mSpnGenre.getSelectedItem().toString());
                uploadIntent.putExtra("tags" ,edtTags.getText().toString());
                startActivity(uploadIntent);
                finish();

            }
        });




    }
}
