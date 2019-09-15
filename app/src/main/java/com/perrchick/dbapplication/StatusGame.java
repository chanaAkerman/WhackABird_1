package com.perrchick.dbapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

    public class StatusGame extends AppCompatActivity {
        public static final String EXTRA_TEXT = "com.example.application.whackABird.EXTRA_TEXT";
        public static final String EXTRA_NUMBER = "com.example.application.whackABird.EXTRA_NUMBER";
        public static final String EXTRA_TIME = "com.example.application.whackABird.EXTRA_TIME";
        public static final String EXTRA_RESULTS = "com.example.application.whackABird.EXTRA_RESULTS";
        public static final String EXTRA_FLAG = "com.example.application.whackABird.EXTRA_FLAG";

        public static final int REQUEST_LOCATION=1;
        public DatabaseReference databaseReference;
        public LocationManager locationManager;
        LatLng location = null;
        String latitude,longitude;

        public String userName;
        public int score;
        public int time;
        public int results;

        private Button btn_newGame;
        private Button btn_exit;
        private Button btn_record_table;
        private TextView name_text;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_status_game);

            //fireBase
            databaseReference = FirebaseDatabase.getInstance().getReference("players");

            Intent myIntent = getIntent();
            userName=myIntent.getStringExtra(GameActivity.EXTRA_TEXT);
            score=myIntent.getIntExtra(GameActivity.EXTRA_NUMBER,0);
            time = myIntent.getIntExtra(GameActivity.EXTRA_TIME,0);
            results = myIntent.getIntExtra(GameActivity.EXTRA_RESULTS,0);
            int flag = myIntent.getIntExtra(GameActivity.EXTRA_FLAG,0);

            btn_newGame=(Button)findViewById(R.id.btn_new_game);
            btn_exit=(Button)findViewById(R.id.btn_exit);
            btn_record_table=(Button)findViewById(R.id.btn_record_table);
            name_text=(TextView)findViewById(R.id.the_name) ;

            if(results==0)
                name_text.setText(userName+"\nYou Lost :( try again");
            else
                name_text.setText("Great Job "+userName+":)\nYou earned "+score+" points");

            if(flag==0)
                savePlayer();

            btn_exit.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View view) {
                    finishAffinity();
                }
            });

            btn_newGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openNewGameActivity();
                }
            });

            btn_record_table.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openRecordActivity();
                }
            });
        }

        public void openNewGameActivity() {
            Intent intent  = new Intent(this,GameActivity.class);
            intent.putExtra(EXTRA_TEXT,userName);
            startActivity(intent);

            finish();
        }

        public void openRecordActivity() {
            Intent intent  = new Intent(this,RecordGame.class);
            intent.putExtra(EXTRA_TEXT,userName);
            intent.putExtra(EXTRA_NUMBER,score);
            intent.putExtra(EXTRA_TIME,time);
            intent.putExtra(EXTRA_RESULTS,results);
            startActivity(intent);

            finish();
        }

        public void savePlayer(){

            // in order to save a player , location is needed
            requestPermission();

            if(location!=null) {
                Player player = new Player(userName, score, time,
                        location.latitude + "", location.longitude + "");
                post(player);
            }else{
                //invalid location
                Player player = new Player(userName, score, time,"-100.0","-100.0");
                post(player);
            }
        }

        private void post(Player player) {
            String id = databaseReference.push().getKey();

            //Saving the player
            databaseReference.child(id).setValue(player);
            //Toast.makeText(this, "player added", Toast.LENGTH_LONG).show();


        }

        private void requestPermission(){
            // Add permission
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);

            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                onGPS();
            }
            getLocation();
        }

        private void getLocation(){
            if(ActivityCompat.checkSelfPermission
                    (StatusGame.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission (StatusGame.this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
            }
            else {
                Location locationGPS=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location locationNetWork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Location locationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                if(locationGPS!=null){
                    double lat = locationGPS.getLatitude();
                    double lng = locationGPS.getLongitude();

                    latitude=String.valueOf(lat);
                    longitude=String.valueOf(lng);

                    location=new LatLng(lat,lng);

                }
                else if(locationNetWork!=null){
                    double lat = locationNetWork.getLatitude();
                    double lng = locationNetWork.getLongitude();

                    latitude=String.valueOf(lat);
                    longitude=String.valueOf(lng);

                    location=new LatLng(lat,lng);
                }
                else if(locationPassive!=null){
                    double lat = locationPassive.getLatitude();
                    double lng = locationPassive.getLongitude();

                    latitude=String.valueOf(lat);
                    longitude=String.valueOf(lng);

                    location=new LatLng(lat,lng);
                }else {
                    Toast.makeText(getApplicationContext(),"Can't get your location", Toast.LENGTH_LONG).show();
                    // invalid location
                    latitude=String.valueOf(-100);
                    longitude=String.valueOf(-100);

                    location=new LatLng(-100,-100);
                }
            }
        }

        private void onGPS(){
            final AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
    }

