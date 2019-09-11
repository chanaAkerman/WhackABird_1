package com.perrchick.dbapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlayerInfo extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.example.application.whackABird.EXTRA_TEXT";
    public static final String EXTRA_NUMBER = "com.example.application.whackABird.EXTRA_NUMBER";
    public static final String EXTRA_TIME = "com.example.application.whackABird.EXTRA_TIME";

    public String name;
    public int score;
    public int time;

    public TextView tName;
    public TextView tScore;
    public TextView tTime;

    public Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info);

        Intent myIntent = getIntent();
        name = myIntent.getStringExtra(RecordGame.EXTRA_TEXT);
        score = myIntent.getIntExtra(RecordGame.EXTRA_NUMBER, 0);
        time = myIntent.getIntExtra(RecordGame.EXTRA_TIME, 0);

        tName=(TextView)findViewById(R.id.nameText);
        tScore=(TextView)findViewById(R.id.scoreText);
        tTime=(TextView)findViewById(R.id.timeText);
        back=(Button)findViewById(R.id.backToRecordTable);

        tName.setText( "Name:  "+name);
        tScore.setText("Score: "+score+" points");
        tTime.setText( "Time:  "+time+" sec");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToRecordTeble();
            }
        });
    }

    private void backToRecordTeble() {
        Intent intent = new Intent(this,RecordGame.class);
        intent.putExtra(EXTRA_TEXT,name);
        intent.putExtra(EXTRA_NUMBER,score);
        intent.putExtra(EXTRA_TIME,time);
        startActivity(intent);
    }
}
