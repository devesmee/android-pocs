package com.example.hearbetter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ResultRecordActivity extends AppCompatActivity {
    String name;
    Button play_button_original;
    Button stop_button_original;
    Button play_button_modified;
    Button stop_button_modified;
    Button next;
    Button back;
    MediaPlayer m;

    private Equalizer eq;
    float[] values = new float[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_original_record);
        if(getIntent().getStringExtra("record_name") != null){
            name = getIntent().getStringExtra("record_name");
        }
        if(getIntent().getStringExtra("record_name_back") != null){
            name = getIntent().getStringExtra("record_name_back");
        }
        setupButtons();

        m = new MediaPlayer();
        m.setOnCompletionListener(mp -> {
            play_button_original.setVisibility(View.VISIBLE);
            stop_button_original.setVisibility(View.INVISIBLE);
        });

    }

    public void playOriginal() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
        play_button_modified.setVisibility(View.VISIBLE);
        stop_button_modified.setVisibility(View.INVISIBLE);
        startPlaying(false);
        Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_SHORT).show();
        play_button_original.setVisibility(View.INVISIBLE);
        stop_button_original.setVisibility(View.VISIBLE);
        m.setOnCompletionListener(mp -> {
            play_button_original.setVisibility(View.VISIBLE);
            stop_button_original.setVisibility(View.INVISIBLE);
        });
    }
    private void stopPlayingOriginal() {
        stopPlaying();
        play_button_original.setVisibility(View.VISIBLE);
        stop_button_original.setVisibility(View.INVISIBLE);
    }

    public void playModified() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
        play_button_original.setVisibility(View.VISIBLE);
        stop_button_original.setVisibility(View.INVISIBLE);
        startPlaying(true);

        Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_SHORT).show();
        play_button_modified.setVisibility(View.INVISIBLE);
        stop_button_modified.setVisibility(View.VISIBLE);
        m.setOnCompletionListener(mp -> {
            play_button_modified.setVisibility(View.VISIBLE);
            stop_button_modified.setVisibility(View.INVISIBLE);
        });
    }
    private void stopPlayingModified() {
        stopPlaying();
        play_button_modified.setVisibility(View.VISIBLE);
        stop_button_modified.setVisibility(View.INVISIBLE);
    }

    private void startPlaying(boolean p) throws IOException{
        m.reset();
        File mPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/HearBetter");
        m.setDataSource(mPath + "/" + name);
        m.prepare();
        if(p){
            m.setVolume(values[0], values[1]);
            setupEq();
        }
        else
            if(eq != null)
                eq.setEnabled(false);
        m.start();
    }

    private void stopPlaying(){
        m.stop();
    }

    private void setupEq(){
        try {
            File root = android.os.Environment.getExternalStorageDirectory();
            FileInputStream fileInputStream = new FileInputStream(new File(root.getAbsolutePath() + "/HearBetter/EqSetting.txt"));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineData = bufferedReader.readLine();
            String[] separated = lineData.split(",");
            for(int i = 0; i < 7; i++){
                values[i] = Float.parseFloat(separated[i]);
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), "File not found!" + name, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "File not found!" + name, Toast.LENGTH_SHORT).show();
        }
        eq = new Equalizer(-1, m.getAudioSessionId());
        EQSettings Settings = new EQSettings();
        eq = Settings.CreateSettings(eq, values);
        eq.setEnabled(true);
    }

    private void setupButtons(){
        stop_button_original = findViewById(R.id.button_stop_original_rec);
        stop_button_original.setVisibility(View.INVISIBLE);
        stop_button_original.setOnClickListener(v -> stopPlayingOriginal());

        play_button_original = findViewById(R.id.button_play_original_rec);
        play_button_original.setOnClickListener(v -> {
            try {
                playOriginal();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "File not found!" + name, Toast.LENGTH_SHORT).show();
            }
        });

        stop_button_modified = findViewById(R.id.button_stop_modified_rec);
        stop_button_modified.setVisibility(View.INVISIBLE);
        stop_button_modified.setOnClickListener(v -> stopPlayingModified());

        play_button_modified = findViewById(R.id.button_play_modified_rec);
        play_button_modified.setOnClickListener(v -> {
            try {
                playModified();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "File not found!" + name, Toast.LENGTH_SHORT).show();
            }
        });

        next = findViewById(R.id.button_next_original_record);
        next.setOnClickListener(v -> {
            stopPlaying();
            m.release();
            Intent i = new Intent(ResultRecordActivity.this, QuestionsPreviewActivity.class);
            Bundle args = new Bundle();
            i.putExtra("record_name", name);
            startActivity(i);
        });

        back = findViewById(R.id.button_back_original_record2);
        back.setOnClickListener(v -> {
            stopPlaying();
            m.release();
            Intent i = new Intent(ResultRecordActivity.this, Recording_List_Activity.class);
            startActivity(i);
        });
    }

}

