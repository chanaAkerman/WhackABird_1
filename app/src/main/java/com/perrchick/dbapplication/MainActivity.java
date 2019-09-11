package com.perrchick.dbapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.example.application.whackABird.EXTRA_TEXT";

    public EditText name;
    private Button play_game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play_game = (Button) findViewById(R.id.btn_new_game);
        name = (EditText) findViewById(R.id.name);

        addListenerOnButton();
    }

    public void addListenerOnButton() {

        play_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = name.getText().toString();
                Toast.makeText(getApplicationContext(), "Hello " + userName, Toast.LENGTH_LONG).show();
                openGameActivity();
            }
        });
    }

    public void openGameActivity() {
        String userName = name.getText().toString();

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_TEXT, userName);
        startActivity(intent);
    }
}