package ek97.fhict.theapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /* Create Thread that will sleep for 5 seconds*/
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 1 seconds
                    sleep(500);

                    // After 5 seconds redirect to another intent
                    Intent i=new Intent(getBaseContext(),Navigation.class);
                    startActivity(i);

                    //Remove activity
                    finish();
                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();
    }
}

