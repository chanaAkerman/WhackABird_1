package com.perrchick.dbapplication;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String EXTRA_TEXT = "com.example.application.whackABird.EXTRA_TEXT";
    public static final String EXTRA_NUMBER = "com.example.application.whackABird.EXTRA_NUMBER";
    public static final String EXTRA_TIME = "com.example.application.whackABird.EXTRA_TIME";
    public static final String EXTRA_LAT = "com.example.application.whackABird.EXTRA_LAT";
    public static final String EXTRA_LNG= "com.example.application.whackABird.EXTRA_LNG";

    private GoogleMap mMap;

    public String userName;
    public int results;
    public int time;
    public double lat;
    public double lng;
    public LatLng location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent myIntent = getIntent();
        userName = myIntent.getStringExtra(RecordGame.EXTRA_TEXT);
        results = myIntent.getIntExtra(RecordGame.EXTRA_NUMBER, 0);
        time = myIntent.getIntExtra(RecordGame.EXTRA_TIME, 0);

        lat = Double.parseDouble(myIntent.getStringExtra(RecordGame.EXTRA_LAT));
        lng = Double.parseDouble(myIntent.getStringExtra(RecordGame.EXTRA_LNG));

        if(lat!=-100 && lng!=-100.0){
            location = new LatLng(lat,lng);
        }else {
            location=new LatLng(0,0);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.addMarker(new MarkerOptions().position(location).title("Marker"));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location,15);
        mMap.moveCamera(update);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                showPlayer();
                return true;
            }
        });
    }

    private void showPlayer() {
        Intent intent = new Intent(MapsActivity.this, PlayerInfo.class);

        //putting artist name and id to intent
        intent.putExtra(EXTRA_TEXT, userName);
        intent.putExtra(EXTRA_NUMBER, results);
        intent.putExtra(EXTRA_TIME, time);

        //starting the activity with intent
        startActivity(intent);
    }
}
