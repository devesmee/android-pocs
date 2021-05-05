package com.example.hearbetter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class RecordingQuestions extends AppCompatActivity {

    private EditText sittuation;
    private EditText description;

    private RadioButton daily;
    private RadioButton weekly;
    private RadioButton monthly;

    private RadioButton veryImportant;
    private RadioButton important;
    private RadioButton notImportant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_questions);
        setViews();
    }

    private void setViews(){

        final Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), RecordActivity.class);
            startActivity(i);
        });
        final Button btnFinish = findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(v -> {
            if(isValid()){
                Intent i = new Intent(getApplicationContext(), InstructionPreview.class);
                startActivity(i);
            }


        });

        sittuation = findViewById(R.id.ET_Situation);
        description = findViewById(R.id.ET_Description);

        daily = findViewById(R.id.radioButtonDaily);
        weekly = findViewById(R.id.radioButtonWeekly);
        monthly = findViewById(R.id.radioButtonMonthly);

        veryImportant = findViewById(R.id.radioButtonVeryImportant);
        important = findViewById(R.id.radioButtonImportant);
        notImportant = findViewById(R.id.radioButtonNotImportant);

    }

    private boolean isValid(){
        if(isNullOrEmpty(sittuation.getText().toString())){
            sittuation.setError("Please fill in this field");
            return false;
        }
        sittuation.setError(null);

        if(isNullOrEmpty(description.getText().toString())){
            description.setError("Please fill in this field");
            return false;
        }
        description.setError(null);

        if(!daily.isChecked() & !weekly.isChecked() & !monthly.isChecked()){
            daily.setError("Please select one of the options");
            weekly.setError("Please select one of the options");
            monthly.setError("Please select one of the options");
            return false;
        }
        daily.setError(null);
        weekly.setError(null);
        monthly.setError(null);

        if(!veryImportant.isChecked() & !important.isChecked() & !notImportant.isChecked()){
            veryImportant.setError("Please select one of the options");
            important.setError("Please select one of the options");
            notImportant.setError("Please select one of the options");
            return false;
        }
        veryImportant.setError(null);
        important.setError(null);
        notImportant.setError(null);

        return true;
    }

    public boolean isNullOrEmpty(String input){
        return TextUtils.isEmpty(input);
    }
}
