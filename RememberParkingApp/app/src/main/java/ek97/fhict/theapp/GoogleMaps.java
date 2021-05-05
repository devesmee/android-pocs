package ek97.fhict.theapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


public class GoogleMaps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng getDestination;
    LatLng currentUserLocation;
    LatLng savedLocation;
    AlertDialog.Builder dialogBuilder;
    public static final String SAVED_LOCATIONS_FILE = "SavedLocationsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogBuilder = new AlertDialog.Builder(this);
        Intent i = getIntent();
        getDestination = i.getParcelableExtra("longLat_dataProvider");
        setContentView(R.layout.activity_google_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
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
                    if(getDestination == null) {
                        float zoomLevel = 16.0f;
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentUserLocation, zoomLevel));
                    }
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker to the current location
        if(getDestination != null) {
            LatLng theDestination = getDestination;
            mMap.addMarker(new MarkerOptions().position(theDestination).title("Destination Marker"));
            float zoomLevel = 11.0f;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(theDestination, zoomLevel));

            drawCircle(theDestination);
            if(theDestination.latitude == 51.480494 && theDestination.longitude == 5.650020) {
                drawLine(new LatLng(51.451550, 5.481820), theDestination);
            }
        }
    }
    private void drawCircle(LatLng point){

        CircleOptions circleOptions = new CircleOptions();
        // center of the circle
        circleOptions.center(point);
        // radius of the circle
        circleOptions.radius(1000);
        // border color of the circle
        circleOptions.strokeColor(R.color.colorPrimary);
        // rill color of the circle
        circleOptions.fillColor(0x30ff0000);
        // border width of the circle
        circleOptions.strokeWidth(2);
        // adding the circle to the GoogleMap
        mMap.addCircle(circleOptions);
    }

    private void drawLine(LatLng begin, LatLng end){

        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(begin,
                        new LatLng(51.452637, 5.482594),
                        new LatLng(51.452710, 5.483094),
                        new LatLng(51.452453, 5.491541),
                        new LatLng(51.451784, 5.495146),
                        new LatLng(51.450096, 5.497880),
                        new LatLng(51.447030, 5.499716),
                        new LatLng(51.452797, 5.516956),
                        new LatLng(51.454704, 5.524654),
                        new LatLng(51.456909, 5.543441),
                        new LatLng(51.467213, 5.594781),
                        new LatLng(51.470061, 5.602605),
                        new LatLng(51.472533, 5.616863),
                        new LatLng(51.473072, 5.626250),
                        new LatLng(51.473579, 5.629327),
                        new LatLng(51.478360, 5.645370),
                        new LatLng(51.477939, 5.645660),
                        new LatLng(51.477271, 5.647637),
                        new LatLng(51.478214, 5.649093),
                        new LatLng(51.479436, 5.649782),
                        end)
                .width(10)
                .color(R.color.brilliantAzure));
    }

    // **************** SAVE CAR LOCATION **************** //
    public void saveLocation(View view)
    {
        dialogBuilder.setMessage("Save car location") .setTitle("Save car location");
        //Setting message manually and performing action on button click
        dialogBuilder.setMessage("Do you want to save your car location ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        saveLocationName();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = dialogBuilder.create();
        //Setting the title
        alert.setTitle("Save location");
        alert.show();
    }

    public void saveLocationName(){
        dialogBuilder.setMessage("Save location") .setTitle("Save location");
        final EditText inputLocationName = new EditText(GoogleMaps.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        inputLocationName.setLayoutParams(lp);
        dialogBuilder.setView(inputLocationName);
        dialogBuilder.setMessage("What is the name of your location ?")
                .setCancelable(false)
                .setPositiveButton("Save location", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        savedLocation =  currentUserLocation;
                        String currentLatitude = String.valueOf(currentUserLocation.latitude);
                        String currentLongitude = String.valueOf(currentUserLocation.longitude);

                        //use intent to send info
                        //Bundle savedLocationBundle = new Bundle();
                        //savedLocationBundle.putParcelable("longLat_dataProvider", savedLocation);
                        //savedLocationBundle.putString(LOCATION_NAME, inputLocationName.getText().toString());

                        //save data to file
                        SharedPreferences locations = getSharedPreferences(SAVED_LOCATIONS_FILE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = locations.edit();
                        editor.putString("locName",inputLocationName.getText().toString());
                        editor.putString("latitude", currentLatitude);
                        editor.putString("longitude", currentLongitude );
                        //editor.putLong("latitude",savedLocation.latitude);
                        editor.apply();
                        Intent intent = new Intent(GoogleMaps.this, Navigation.class);
                        //intent.putExtras(savedLocationBundle);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = dialogBuilder.create();
        alert.setTitle("Save location name");
        alert.show();
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
