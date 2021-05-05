package com.example.hearbetter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {



    private MediaRecorder myAudioRecorder;
    private String output = null;
    private boolean permissionToRecordAccepted = false;
    private boolean permissionToWriteAccepted = false;
    String date;

    private Button button_start_rec, button_stop_rec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        button_start_rec = (Button) findViewById(R.id.button_start_rec);
        button_stop_rec = (Button) findViewById(R.id.button_stop_rec);
        RecordVar();
        button_stop_rec.setVisibility(View.INVISIBLE);
        setViews();
    }

    private void setViews(){
        final Button btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        final Button btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RecordingQuestions.class);
                Bundle args = new Bundle();
                i.putExtra("name", output);
                startActivity(i);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start_rec:
                RecordVar();
                start(v);
                button_start_rec.setVisibility(View.INVISIBLE);
                button_stop_rec.setVisibility(View.VISIBLE);
                break;
            case R.id.button_stop_rec:
                stop(v);
                button_stop_rec.setVisibility(View.INVISIBLE);
                button_start_rec.setVisibility(View.VISIBLE);
                button_start_rec.setEnabled(true);
                break;
        }
    }

    public void RecordVar(){
        button_start_rec.setOnClickListener((View.OnClickListener) this);
        button_stop_rec.setOnClickListener((View.OnClickListener) this);
        button_stop_rec.setEnabled(false);

        DateFormat df = new SimpleDateFormat("d.MM.yyyy HH mm ss");
        date = df.format(Calendar.getInstance().getTime());
        File mPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/HearBetter");
        if(!mPath.exists()){
            mPath.mkdir();
        }
        output = mPath + "/HearBetter" + date;
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        myAudioRecorder.setAudioEncodingBitRate(16 * 44100);
        myAudioRecorder.setAudioSamplingRate(44100);
        myAudioRecorder.setOutputFile(output);
    }

    public void start(View view) {
        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        button_start_rec.setEnabled(false);
        button_stop_rec.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();
    }

    public void stop(View view) {
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;
        button_start_rec.setEnabled(false);

        Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                permissionToWriteAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) RecordActivity.super.finish();
        if (!permissionToWriteAccepted) RecordActivity.super.finish();
    }
}
