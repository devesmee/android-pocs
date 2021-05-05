package com.example.hearbetter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.hearbetter.TestFragments.BassTestFragment;
import com.example.hearbetter.TestFragments.EarphoneTestFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TestActivity extends AppCompatActivity implements setFrequencyVol {

    private static final int PERMISSION_EXTERNAL_WRITE = 0;
    private static final int PERMISSION_EXTERNAL_READ = 1;
    final FragmentManager fm = getSupportFragmentManager();
    final Fragment fragmentEarphoneTest = new EarphoneTestFragment();
    final Fragment VolumeLeftTest = new BassTestFragment();
    final Fragment VolumeRightTest = new BassTestFragment();
    final Fragment fragmentBassTest = new BassTestFragment();
    final Fragment midBassFragment = new BassTestFragment();
    final Fragment fragmentMidTest = new BassTestFragment();
    final Fragment midHighFragment = new BassTestFragment();
    final Fragment fragmentHighTest = new BassTestFragment();
    ProgressBar pb;

    //Equalizer variables
    public Equalizer eq = null;
    public BassBoost bb = null;
    //Levels of volume
    int min_level = 0;
    int max_level = 100;

    //Levels of equalizer
    float[] values;

    Context context = this;
    int counter = 0;

    Fragment[] fragments = new Fragment[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        values = new float[7];

        Bundle args = new Bundle();
        args = new Bundle();
        args.putInt("index", 0);

        VolumeLeftTest.setArguments(args);
        args = new Bundle();
        args.putInt("index", 1);
        VolumeRightTest.setArguments(args);
        args = new Bundle();
        args.putInt("index", 2);
        fragmentBassTest.setArguments(args);
        args = new Bundle();
        args.putInt("index", 3);
        midBassFragment.setArguments(args);
        args = new Bundle();
        args.putInt("index", 4);
        fragmentMidTest.setArguments(args);
        args = new Bundle();
        args.putInt("index", 5);
        midHighFragment.setArguments(args);
        args = new Bundle();
        args.putInt("index", 6);
        fragmentHighTest.setArguments(args);

        //Fragments for each frequency test
        fm.beginTransaction().add(R.id.flTest, fragmentEarphoneTest, "1").hide(fragmentEarphoneTest).commit();
        fm.beginTransaction().add(R.id.flTest, VolumeLeftTest, "1").hide(VolumeLeftTest).commit();
        fm.beginTransaction().add(R.id.flTest, VolumeRightTest, "1").hide(VolumeRightTest).commit();
        fm.beginTransaction().add(R.id.flTest, fragmentBassTest, "1").hide(fragmentBassTest).commit();
        fm.beginTransaction().add(R.id.flTest, midBassFragment, "1").hide(midBassFragment).commit();
        fm.beginTransaction().add(R.id.flTest, fragmentMidTest, "1").hide(fragmentMidTest).commit();
        fm.beginTransaction().add(R.id.flTest, midHighFragment, "1").hide(midHighFragment).commit();
        fm.beginTransaction().add(R.id.flTest, fragmentHighTest, "1").hide(fragmentHighTest).commit();
        fragments[0] = fragmentEarphoneTest;
        fragments[1] = VolumeLeftTest;
        fragments[2] = VolumeRightTest;
        fragments[3] = fragmentBassTest;
        fragments[4] = midBassFragment;
        fragments[5] = fragmentMidTest;
        fragments[6] = midHighFragment;
        fragments[7] = fragmentHighTest;

        final Button btnMenu = findViewById(R.id.btnMenu);
        final Button btnNext = findViewById(R.id.btnNext);
        pb = findViewById(R.id.pbTest);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter == 0){
                    finish();
                }
                else{
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit);
                    transaction.hide(fragments[counter - 1]);
                    transaction.commit();
                    //fm.beginTransaction().hide(fragments[counter - 1]).commit();
                    counter--;
                    pb.setProgress(counter);
                    Log.i("Test", "onClick: counter is" + counter);
                    if(counter == 0){
                        btnMenu.setText("Menu");
                    }
                    if(counter < 7){
                        btnNext.setText("Next");
                    }
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit);
                btnMenu.setText("Back");
                if(counter == 0){
                    fm.beginTransaction().show(fragments[counter]).commit();
                }
                else if(counter <= 7){
                    transaction.show(fragments[counter]);
                    transaction.commit();
                }
                if(counter == 8){
                    EQSettings setting = new EQSettings();
                    eq = setting.CreateSettings(eq, values);
                    SaveToFolder(setting.ToString());
                    if(getIntent().getStringExtra("first_test") != null){
                        Intent i = new Intent(getApplicationContext(), RecordActivity.class);
                        Bundle args = new Bundle();
                        i.putExtra("first_test", "1");
                        startActivity(i);
                    }
                    finish();
                }
                if(counter + 1 > 7){
                    btnNext.setText("Finish");
                }
                counter++;
                pb.setProgress(counter);
                Log.i("Test", "onClick: counter is" + counter);
            }
        });

        eq = new Equalizer(-1, 1);
        if (eq != null)
        {
            short r[] = eq.getBandLevelRange();
            min_level = r[0];
            max_level = r[1];

            for(int i = 2; i < 7; i++){
                values[i] = eq.getBandLevel((short)(i - 2));
            }
        }
        bb = new BassBoost (-1, 1);

        eq.setEnabled(true);
        Log.i("Eq", eq.getBandLevelRange()[0] + ", " + eq.getBandLevelRange()[1]);

        //Check for permissions
        //Check for permission of writing
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            //ask for permission
            ActivityCompat.requestPermissions(TestActivity.this, new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_EXTERNAL_WRITE);
        }//Check for permissions
        //Check for permission of reading
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            //ask for permission
            ActivityCompat.requestPermissions(TestActivity.this, new String[]
                    {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_EXTERNAL_READ);
        }

    }

    public void SaveToFolder(String info) {

        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File (root.getAbsolutePath() + "/HearBetter");
        dir.mkdirs();
        File file = new File(dir, "EqSetting.txt");
        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println(info);
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("Error Dir", "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void SetFrequencyVol(short freqNum, float value) {
        values[freqNum] = value;
        EQSettings setting = new EQSettings();
        eq = setting.CreateSettings(eq, values);
        SaveToFolder(setting.ToString());
    }
}

