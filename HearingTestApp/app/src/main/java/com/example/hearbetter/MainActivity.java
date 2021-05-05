package com.example.hearbetter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.Equalizer;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private String[] permissions = {"android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE"};
    Button questions;
    Button test;
    Button record;
    Button preview;
    Button start_test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questions = (Button) findViewById(R.id.btnQuestions);
        test = (Button) findViewById(R.id.btnTest);
        record = (Button) findViewById(R.id.btnRecord);
        preview = (Button) findViewById(R.id.btnPreview);
        start_test = (Button) findViewById(R.id.btnStartTest);

        setViews();

        //permission record
        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);

        }
        start_test.setVisibility(View.GONE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int currentVersionCode = BuildConfig.VERSION_CODE;
        if (prefs.getInt("LASTVERSION", 0) < currentVersionCode) {
            questions.setVisibility(View.GONE);
            test.setVisibility(View.GONE);
            record.setVisibility(View.GONE);
            preview.setVisibility(View.GONE);
            start_test.setVisibility(View.VISIBLE);
            prefs.edit().putInt("LASTVERSION", currentVersionCode).apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setViews(){

        final Button btnQuestion = findViewById(R.id.btnQuestions);
        btnQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), QuestionnaireActivity.class);
                startActivity(i);
            }
        });
        final Button btnTest = findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               Intent i = new Intent(getApplicationContext(), TestActivity.class);
               startActivity(i);
            }
        });
        final Button btnRecord = findViewById(R.id.btnRecord);
        btnRecord.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), InstructionRecord.class);
                startActivity(i);
            }
        });
        final Button btnPreview = findViewById(R.id.btnPreview);
        btnPreview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), InstructionPreview.class);
                startActivity(i);
            }
        });
        start_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), QuestionnaireActivity.class);
                Bundle args = new Bundle();
                i.putExtra("first_test", "1");
                startActivity(i);
            }
        });
    }

}
