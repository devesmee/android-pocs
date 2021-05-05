package ek97.fhict.theapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class Navigation extends AppCompatActivity implements OnMapReadyCallback {
    private Spinner spinner;
    private GoogleMap mMap;
    //LatLng getSavedLocation;
    String savedLocationName;
    LatLng currentUserLocation;
    LatLng savedLocationLatLng;
    public static final String SAVED_LOCATIONS_FILE = "SavedLocationsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        addListenerOnSpinnerItemSelection();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // retrieve info with intent
        //Intent i = getIntent();
        //getSavedLocation = i.getParcelableExtra("longLat_dataProvider");
        //savedLocationName = i.getStringExtra(GoogleMaps.LOCATION_NAME);

        //read data from file
        SharedPreferences readLocations = getSharedPreferences(SAVED_LOCATIONS_FILE, Context.MODE_PRIVATE);
        String extraLocation = readLocations.getString("locName", "");
        String getLatitude = readLocations.getString("latitude", "");
        String getLongitude = readLocations.getString("longitude", "");
        double latitude = Double.valueOf(getLatitude);
        double longitude = Double.valueOf(getLongitude);
        savedLocationLatLng = new LatLng(latitude, longitude);

        //add to the spinner/dropdown menu
        Spinner spinner = findViewById(R.id.locationSpinner);
        final List<String> locationsList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.location_array)));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                this, R.layout.support_simple_spinner_dropdown_item, locationsList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        locationsList.add(extraLocation);
        spinnerArrayAdapter.notifyDataSetChanged();
        savedLocationName = extraLocation;

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }
    // **************** DROP DOWN MENU *************** //
    public void addListenerOnSpinnerItemSelection() {
        spinner = findViewById(R.id.locationSpinner);
    }
    public void startNavigation(View view){
        String theDestination = String.valueOf(spinner.getSelectedItem());
            Bundle destination = new Bundle();
            //switch statement doesnt work if we want to use string savedLocationName; that's why we use if/else
        if ("Home".equals(theDestination)) {
            LatLng homeAddress = new LatLng(51.708050, 5.566060);
            destination.putParcelable("longLat_dataProvider", homeAddress);

        } else if ("School".equals(theDestination)) {
            LatLng schoolAddress = new LatLng(51.451550, 5.481820);
            destination.putParcelable("longLat_dataProvider", schoolAddress);

        } else if ("Work".equals(theDestination)) {
            LatLng workAddress = new LatLng(51.438599, 5.478120);
            destination.putParcelable("longLat_dataProvider", workAddress);

        } else if ("Parents".equals(theDestination)) {
            LatLng parentsAddress = new LatLng(51.480494, 5.650020);
            destination.putParcelable("longLat_dataProvider", parentsAddress);

        } else if (savedLocationName.equals(theDestination)) {
            destination.putParcelable("longLat_dataProvider", savedLocationLatLng);
            if (savedLocationLatLng == null) {
                Toast.makeText(getApplicationContext(), "There's no previous location",
                        Toast.LENGTH_SHORT).show();
            }
        }

        Intent intent = new Intent(this, GoogleMaps.class);
        intent.putExtras(destination);
        startActivity(intent);
    }

    // **************** THE MAP **************** //
    //show current location on the small map
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try{
            final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 0, new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    currentUserLocation = new LatLng(latitude, longitude);
                    if(location.hasAccuracy())
                    {
                        locationManager.removeUpdates(this);
                    }
                    mMap.addMarker(new MarkerOptions().position(currentUserLocation).title("Current location"));
                    float zoomLevel = 16.0f;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentUserLocation, zoomLevel));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }
        catch(SecurityException e)  {
            e.getMessage();
        }
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

