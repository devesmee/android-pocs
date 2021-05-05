package com.example.hearbetter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.hearbetter.QuestionnaireFragments.Question1Fragment;
import com.example.hearbetter.QuestionnaireFragments.Question2Fragment;
import com.example.hearbetter.QuestionnaireFragments.Question3Fragment;
import com.example.hearbetter.QuestionnaireFragments.Question4Fragment;
import com.example.hearbetter.QuestionnaireFragments.QuestionEnding;

public class QuestionnaireActivity extends AppCompatActivity{

    final FragmentManager fm = getSupportFragmentManager();
    final Fragment fragmentQuestion1 = new Question1Fragment();
    final Fragment fragmentQuestion2 = new Question2Fragment();
    final Fragment fragmentQuestion3 = new Question3Fragment();
    final Fragment fragmentQuestion4 = new Question4Fragment();
    final Fragment fragmentQuestionEnding = new QuestionEnding();

    OnDataPass[] passes = new OnDataPass[5];

    int counter = 0;

    Fragment[] fragments = new Fragment[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        Bundle args = new Bundle();
        args.putInt("index", 0);
        fragmentQuestion1.setArguments(args);
        args = new Bundle();
        args.putInt("index", 1);
        fragmentQuestion2.setArguments(args);
        args = new Bundle();
        args.putInt("index", 2);
        fragmentQuestion3.setArguments(args);
        args = new Bundle();
        args.putInt("index", 3);
        fragmentQuestion4.setArguments(args);
        args = new Bundle();
        args.putInt("index", 4);
        fragmentQuestionEnding.setArguments(args);

        //Fragments for each frequency test
        fm.beginTransaction().add(R.id.flTest, fragmentQuestion1, "1").hide(fragmentQuestion1).commit();
        fm.beginTransaction().add(R.id.flTest, fragmentQuestion2, "1").hide(fragmentQuestion2).commit();
        fm.beginTransaction().add(R.id.flTest, fragmentQuestion3, "1").hide(fragmentQuestion3).commit();
        fm.beginTransaction().add(R.id.flTest, fragmentQuestion4, "1").hide(fragmentQuestion4).commit();
        fm.beginTransaction().add(R.id.flTest, fragmentQuestionEnding, "1").hide(fragmentQuestionEnding).commit();
        fragments[0] = fragmentQuestion1;
        fragments[1] = fragmentQuestion2;
        fragments[2] = fragmentQuestion3;
        fragments[3] = fragmentQuestion4;
        fragments[4] = fragmentQuestionEnding;

        passes[0] = (OnDataPass)fragmentQuestion1;
        passes[1] = (OnDataPass)fragmentQuestion2;
        passes[2] = (OnDataPass)fragmentQuestion3;
        passes[3] = (OnDataPass)fragmentQuestion4;

        final Button btnMenu = findViewById(R.id.btnMenu);
        final Button btnNext = findViewById(R.id.btnNext);

        ProgressBar progressBar = findViewById(R.id.progressBar);


        btnMenu.setOnClickListener(v -> {
            if (counter == 0) {
                finish();
            } else {
                fm.beginTransaction().hide(fragments[counter - 1]).commit();
                progressBar.setProgress(progressBar.getProgress() - 20);
                counter--;
                Log.i("Test", "onClick: counter is" + counter);
                if (counter == 0) {
                    btnMenu.setText("Menu");
                }
                if (counter <= 4) {
                    btnNext.setText("Next");
                }
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        btnNext.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            btnMenu.setText("Back");
            if (counter <= 4) {
                fm.beginTransaction().show(fragments[counter]).commit();
            }
            if (counter == 5) {
                if(getIntent().getStringExtra("first_test") != null){
                    Intent i = new Intent(getApplicationContext(), TestActivity.class);
                    Bundle args1 = new Bundle();
                    i.putExtra("first_test", "1");
                    startActivity(i);
                }
                    //prefs.edit().putInt("LASTVERSION", currentVersionCode).apply();
                Profile.SaveToFolder();
                finish();
            }
            if (counter + 1 > 4) {
                btnNext.setText("Finish");
            }
            if(counter > 0 && counter < 5)
                passes[counter-1].onProfileDataPass();
            progressBar.setProgress(progressBar.getProgress() + 20);
            counter++;
            Log.i("Test", "onClick: counter is" + counter);
        });
    }
}
