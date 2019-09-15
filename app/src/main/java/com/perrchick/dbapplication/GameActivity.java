package com.perrchick.dbapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import static com.perrchick.dbapplication.R.drawable.bird2;
import static com.perrchick.dbapplication.R.drawable.bomb;
import static com.perrchick.dbapplication.R.drawable.round_button;
import static com.perrchick.dbapplication.R.drawable.*;

public class GameActivity extends AppCompatActivity {
    // Passing Data
    public static final String EXTRA_TEXT = "com.example.application.whackABird.EXTRA_TEXT";
    public static final String EXTRA_NUMBER = "com.example.application.whackABird.EXTRA_NUMBER";
    public static final String EXTRA_TIME = "com.example.application.whackABird.EXTRA_TIME";
    public static final String EXTRA_RESULTS = "com.example.application.whackABird.EXTRA_RESULTS";
    public static final String EXTRA_FLAG = "com.example.application.whackABird.EXTRA_FLAG";

    public static final int SIZE = 9;
    Random random = new Random();

    // Sounds
    MediaPlayer playerFail;
    MediaPlayer playerSuccess;
    MediaPlayer bombSound;

    // Time counter
    private int counter = 30;

    private TextView miss;
    private TextView score;

    public String userName;
    // lose -0 win-1
    public int results = 1;
    public int scoreCounter = 0;
    public int missCounter = 0;

    private ImageButton buttons[];
    private boolean[] press;
    private boolean[] press_bomb;

    private Animation bounceAnim;
    private Animation myAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent myIntent = getIntent();
        userName = myIntent.getStringExtra(MainActivity.EXTRA_TEXT);

        miss = (TextView) findViewById(R.id.miss_text);
        score = (TextView) findViewById(R.id.score_text);

        press = new boolean[SIZE];
        press_bomb = new boolean[SIZE];

        buttons = new ImageButton[SIZE];
        buttons[0] = (ImageButton) findViewById(R.id.button_00);
        buttons[1] = (ImageButton) findViewById(R.id.button_01);
        buttons[2] = (ImageButton) findViewById(R.id.button_02);
        buttons[3] = (ImageButton) findViewById(R.id.button_03);
        buttons[4] = (ImageButton) findViewById(R.id.button_04);
        buttons[5] = (ImageButton) findViewById(R.id.button_05);
        buttons[6] = (ImageButton) findViewById(R.id.button_06);
        buttons[7] = (ImageButton) findViewById(R.id.button_07);
        buttons[8] = (ImageButton) findViewById(R.id.button_08);

        // animation
        myAnim = AnimationUtils.loadAnimation(this, R.anim.milkshake);

        bounceAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        BounceInterpolator bounceInterpolator = new BounceInterpolator(0.2,20);
        bounceAnim.setInterpolator(bounceInterpolator);


        final TextView count_timer = (TextView) findViewById(R.id.text_timer);

        // count down counter
        CountDownTimer SecondsTimer;
        SecondsTimer = new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long l) {
                count_timer.setText(String.valueOf(counter));
                counter--;
            }

            @Override
            public void onFinish() {
                count_timer.setText("Finished");
                showResults();
            }
        };
        SecondsTimer.start();

        // rand numbers - do job every 1 second
        CountDownTimer jobTimer;
        jobTimer = new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long l) {
                doJob();
            }

            @Override
            public void onFinish() {
                showResults();
            }
        };
        jobTimer.start();

        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            checkButtonPress(view, 0);
            }
        });
        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkButtonPress(view, 1);
            }
        });
        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkButtonPress(view, 2);
            }
        });
        buttons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkButtonPress(view, 3);
            }
        });
        buttons[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkButtonPress(view, 4);
            }
        });
        buttons[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkButtonPress(view, 5);
            }
        });
        buttons[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkButtonPress(view, 6);
            }
        });
        buttons[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkButtonPress(view, 7);
            }
        });
        buttons[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkButtonPress(view, 8);
            }
        });
    }

    public void checkButtonPress(View view, int locate) {
        // stop all kind a sound
        StopFail();
        StopSuccess();
        StopBomb();

        // clear animation
        view.clearAnimation();

        // Bird
        if (press[locate]) {
            view.startAnimation(bounceAnim);
            PlaySuccess(view);

            scoreCounter++;
            UpdateScoreText(scoreCounter);
            goToSleepButton(locate);
            press[locate] = false;
            //Toast.makeText(getApplicationContext(), "Great!", Toast.LENGTH_LONG).show();
        }
        // Bomb
        else if (press_bomb[locate]) {
            view.startAnimation(myAnim);
            PlayBomb(view);

            if (!(scoreCounter - 3 < 0)) {
                scoreCounter -= 3;
                UpdateScoreText(scoreCounter);
            } else {
                UpdateScoreText(0);
            }
            goToSleepButton(locate);
            press_bomb[locate] = false;
            //Toast.makeText(getApplicationContext(), "No!! it's a bomb ", Toast.LENGTH_LONG).show();
        }
        // nothing:(
        else {
            view.startAnimation(myAnim);
            PlayFail(view);

            UpdateMissText(++missCounter);
            //Toast.makeText(getApplicationContext(), "Did you miss again?", Toast.LENGTH_LONG).show();
        }

    }

    public void ResponseBombTime(final int locate) {
        CountDownTimer showTime;

        showTime = new CountDownTimer(2000, 10) {

            @Override
            public void onTick(long l) {
                if (!press_bomb[locate])
                    onFinish();
            }

            @Override
            public void onFinish() {
                // didn't press the button
                goToSleepButton(locate);
            }
        };
        showTime.start();
    }

    public void ResponseTime(final int locate) {
        CountDownTimer showTime;

        showTime = new CountDownTimer(1500, 10) {

            @Override
            public void onTick(long l) {
                if (!press[locate])
                    onFinish();
            }

            @Override
            public void onFinish() {
                // didn't press the button
                goToSleepButton(locate);
            }
        };
        showTime.start();
    }

    public void doJob() {

        // 1- not taken
        // 0- taken

        int number = random.nextInt(9);
        int numbers[] = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            numbers[i] = 1;
        }

        for (int i = 0; i < number; i++) {
            int locate = random.nextInt(9);

            int what = random.nextInt(2);
            if (what == 0) { // bomb
                while (numbers[locate] == 0) {
                    locate = random.nextInt(9);
                }
                numbers[locate] = 0;
                wakeBombButton(locate);
                press_bomb[locate]=true;

            } else { // bird
                while (numbers[locate] == 0) {
                    locate = random.nextInt(9);
                }
                numbers[locate] = 0;
                wakeButton(locate);
                press[locate]=true;
            }
        }
        for(int i=0; i<numbers.length; i++){
            if(numbers[i]==1){
                press[i]=false;
                press_bomb[i]=false;
            }
        }
    }

    public void wakeBombButton(int locate) {
        buttons[locate].setImageResource(bomb);
        press_bomb[locate] = true;
        ResponseBombTime(locate);
    }

    public void wakeButton(int locate) {
        buttons[locate].setImageResource(bird2);
        press[locate] = true;
        ResponseTime(locate);
    }

    public void goToSleepButton(int locate) {
        buttons[locate].setImageResource(round_button);
    }

    public void UpdateScoreText(int newScore) {
        if (newScore == 0) {
            scoreCounter = 0;
            results=0;
            showResults();
            return;
        }
        score.setText("Score: " + scoreCounter);
    }

    public void UpdateMissText(int newMiss) {
        if (newMiss == 3) {
            results=0;
            showResults();
        }
        miss.setText("Miss: " + missCounter);
    }

    public void showResults() {
        //open results activity
        Intent intent = new Intent(this, StatusGame.class);
        intent.putExtra(EXTRA_TEXT, userName);
        intent.putExtra(EXTRA_NUMBER, scoreCounter);
        intent.putExtra(EXTRA_TIME, 30 - counter);
        intent.putExtra(EXTRA_RESULTS, results);
        startActivity(intent);

        finish();
    }


    // sound methods
    public void PlayFail(View v) {
        if (playerFail == null) {
            playerFail = MediaPlayer.create(this, R.raw.systemfault);
            playerFail.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    StopFail();
                }
            });
        }
        playerFail.start();
    }

    public void PlaySuccess(View v) {
        if (playerSuccess == null) {
            playerSuccess = MediaPlayer.create(this, R.raw.accomplished);
            playerSuccess.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    StopSuccess();
                }
            });
        }
        playerSuccess.start();
    }

    public void StopFail() {
        if (playerFail != null) {
            playerFail.release();
            playerFail = null;
        }
    }

    public void StopSuccess() {
        if (playerSuccess != null) {
            playerSuccess.release();
            playerSuccess = null;
        }
    }

    public void PlayBomb(View v) {
        if (bombSound == null) {
            bombSound = MediaPlayer.create(this, R.raw.zombi);
            bombSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    StopBomb();
                }
            });
        }
        bombSound.start();
    }

    public void StopBomb() {
        if (bombSound != null) {
            bombSound.release();
            bombSound = null;
        }
    }
}

