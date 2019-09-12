package com.perrchick.dbapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecordGame extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.example.application.whackABird.EXTRA_TEXT";
    public static final String EXTRA_NUMBER = "com.example.application.whackABird.EXTRA_NUMBER";

    public static final String EXTRA_OTHERNAME = "com.example.application.whackABird.EXTRA_OTHERNAME";
    public static final String EXTRA_SCORE = "com.example.application.whackABird.EXTRA_SCORE";

    public static final String EXTRA_TIME = "com.example.application.whackABird.EXTRA_TIME";
    public static final String EXTRA_RESULTS = "com.example.application.whackABird.EXTRA_RESULTS";
    public static final String EXTRA_FLAG = "com.example.application.whackABird.EXTRA_FLAG";
    public static final String EXTRA_LAT = "com.example.application.whackABird.EXTRA_LAT";
    public static final String EXTRA_LNG = "com.example.application.whackABird.EXTRA_LNG";

    // Location - Google Maps
    public static final int REQUEST_LOCATION=1;
    public LocationManager locationManager;
    public LatLng location =null;
    public String latitude,longitude;

    public String userName;
    public int score;
    public int time;
    public int results;
    //a list to store all the players from firebase database
    List<Player> players;

    //our database reference object
    DatabaseReference databasePlayers;

    private Button btn_back;

    private ListView listViewPlayers;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_record_game);

            Intent myIntent = getIntent();
            userName=myIntent.getStringExtra(GameActivity.EXTRA_TEXT);
            score=myIntent.getIntExtra(GameActivity.EXTRA_NUMBER,0);
            time = myIntent.getIntExtra(GameActivity.EXTRA_TIME,0);
            results = myIntent.getIntExtra(GameActivity.EXTRA_RESULTS,0);

            //getting the reference of artists node
            databasePlayers = FirebaseDatabase.getInstance().getReference("players");
            players = new ArrayList<>();

            btn_back = (Button)findViewById(R.id.btn_back);

            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Intent intent = new Intent(getApplicationContext(), MapsPlayerActivity.class);
                    Intent intent = new Intent(RecordGame.this, StatusGame.class);
                    intent.putExtra(EXTRA_TEXT, userName);
                    intent.putExtra(EXTRA_NUMBER, score);
                    intent.putExtra(EXTRA_TIME, time);
                    intent.putExtra(EXTRA_RESULTS, results);
                    intent.putExtra(EXTRA_FLAG, 1);

                    //starting the activity with intent
                    startActivity(intent);
                }
            });


            listViewPlayers = findViewById(R.id.listViewPlayers);

            databasePlayers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds: dataSnapshot.getChildren())
                    {
                        Player player = ds.getValue(Player.class);
                        if(!isExist(player) && player.getScore()!=0)
                            players.add(player);
                        else
                            deletePlayer(ds.getKey());

                    }

                            // sort the list:
                    Collections.sort(players,new sortByScore());


                    int index=0;
                    for (int i=0; i<players.size(); i++){
                        players.get(i).setRate(i+1);
                    }

                    //creating adapter
                    PlayerList playerAdapter;
                    if(players.size()>10){
                        playerAdapter = new PlayerList(RecordGame.this, players.subList(0,10));
                    }
                    else{
                        playerAdapter = new PlayerList(RecordGame.this, players);
                    }
                    //attaching adapter to the listView
                    listViewPlayers.setAdapter(playerAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            //Show Map when User click on other player- show player location
            listViewPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //getting the selected artist
                    Player player = players.get(i);

                    //creating an intent
                    //Intent intent = new Intent(getApplicationContext(), MapsPlayerActivity.class);
                    Intent intent = new Intent(RecordGame.this, MapsActivity.class);

                    //putting artist name and id to intent
                    intent.putExtra(EXTRA_TEXT, player.getName());
                    intent.putExtra(EXTRA_NUMBER, player.getScore());
                    intent.putExtra(EXTRA_OTHERNAME, userName);
                    intent.putExtra(EXTRA_SCORE, score);
                    intent.putExtra(EXTRA_TIME, player.getTime());
                    intent.putExtra(EXTRA_LAT, player.getLat());
                    intent.putExtra(EXTRA_LNG, player.getLng());

                    //starting the activity with intent
                    startActivity(intent);
                }
            });

        }

    private void deletePlayer(String id) {
        //getting the specified player reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("players").child(id);

        //removing player
        dR.removeValue();
    }

    private boolean isExist(Player player) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).equals(player))
                return true;
        }
        return false;
    }

    private void addPlayer(Player player) {
            String id = databasePlayers.push().getKey();

            //Saving the player
            databasePlayers.child(id).setValue(player);
            Toast.makeText(this, "player added", Toast.LENGTH_LONG).show();
        }


        class sortByScore implements Comparator<Player> {
            // Used for sorting in ascending order of
            // roll number
            public int compare(Player a, Player b)
            {
                return b.getScore() - a.getScore();
            }
        }
    }

