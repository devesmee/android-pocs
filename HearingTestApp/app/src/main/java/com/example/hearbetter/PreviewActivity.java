package com.example.hearbetter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preview);

        setContentView(R.layout.preview_screen_recorded);

    }
}
