package com.example.hearbetter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class QuestionsPreviewActivity extends AppCompatActivity {
    String name;

    private EditText description;

    private RadioButton yes;
    private RadioButton no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_preview);
        name = getIntent().getStringExtra("record_name");
        setViews();
    }

    private void setViews(){
        final Button btnBack = findViewById(R.id.button_questions_preview_back);
        btnBack.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), ResultRecordActivity.class);
            Bundle args = new Bundle();
            i.putExtra("record_name_back", name);
            startActivity(i);
        });

        final Button btnMenu = findViewById(R.id.button_questions_preview_finish);
        btnMenu.setOnClickListener(v -> {
            if(isValid()){
                Intent i = new Intent(getApplicationContext(), VisitStore.class);
                startActivity(i);
            }

        });

        yes = findViewById(R.id.radioButton_question_preview_yes);
        no = findViewById(R.id.radioButton_question_preview_no);
        description = findViewById(R.id.editTextComments);
    }

    private boolean isValid(){

        if(!yes.isChecked() & !no.isChecked()){
            yes.setError("Please select one of the options");
            no.setError("Please select one of the options");
            return false;
        }
        yes.setError(null);
        no.setError(null);

        if(isNullOrEmpty(description.getText().toString())){
            description.setError("Please fill in this field");
            return false;
        }
        description.setError(null);

        return true;
    }

    public boolean isNullOrEmpty(String input){
        return TextUtils.isEmpty(input);
    }
}
