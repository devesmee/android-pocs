package ek97.fhict.theapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    // **************** NAVIGATION **************** //
    public void goToDestinations(MenuItem item)
    {
        Intent intent = new Intent(this, Navigation.class);
        startActivity(intent);
    }

    public void goToMap(MenuItem item)
    {
        Intent intent = new Intent(this, GoogleMaps.class);
        startActivity(intent);
    }

    public void goToProfile(MenuItem item)
    {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void goToSettings(MenuItem item)
    {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}
