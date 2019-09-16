package com.perrchick.dbapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final String EXTRA_TEXT = "com.example.application.whackABird.EXTRA_TEXT";
    public static final String EXTRA_NUMBER = "com.example.application.whackABird.EXTRA_NUMBER";
    public static final String EXTRA_TIME = "com.example.application.whackABird.EXTRA_TIME";
    public static final String EXTRA_LAT = "com.example.application.whackABird.EXTRA_LAT";
    public static final String EXTRA_LNG= "com.example.application.whackABird.EXTRA_LNG";

    private GoogleMap mMap;

    public String userName;
    public int results;

    //our database reference object
    DatabaseReference databasePlayers;
    //a list to store all the players from firebase database
    List<Player> players;

    ArrayList<MarkerOptions> markerList;


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

        //getting the reference of artists node
        databasePlayers = FirebaseDatabase.getInstance().getReference("players");
        players = new ArrayList<>();
        markerList=new ArrayList<>();

        lat = Double.parseDouble(myIntent.getStringExtra(RecordGame.EXTRA_LAT));
        lng = Double.parseDouble(myIntent.getStringExtra(RecordGame.EXTRA_LNG));

        if(lat!=-100 && lng!=-100.0){
            location = new LatLng(lat,lng);
        }else {
            location=new LatLng(0,0);
        }

        databasePlayers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Player player = ds.getValue(Player.class);
                        players.add(player);

                }
                // now we have all the players
                addLocations();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        MarkerOptions markerOptions = new MarkerOptions().position(location).title(userName).snippet("score: "+results);
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
    }

    private void addLocations(){
        for(int i=0;i<players.size();i++) {
            drawMarker(players.get(i).getPlayerLocation(),
                    players.get(i).getName(),
                    players.get(i).getScore());
        }
    }

    private void drawMarker(LatLng location,String name,int score){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(location)
                .title(name)
                .snippet("score: "+score);
        mMap.addMarker(markerOptions);
    }
}


