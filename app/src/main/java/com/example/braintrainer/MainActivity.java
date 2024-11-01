package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button[] buttons;
    TextView timer;
    TextView sum;
    TextView score;
    TextView state;

    int answer;
    int scoreNum;
    int total;

    int secs;
    Handler handler;
    Runnable runnable;

    MediaPlayer hornPlayer;
    MediaPlayer correctPlayer;
    MediaPlayer wrongPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hornPlayer = MediaPlayer.create(this, R.raw.horn);
        correctPlayer = MediaPlayer.create(this, R.raw.correct);
        wrongPlayer = MediaPlayer.create(this, R.raw.wrong);

        timer = (TextView) findViewById(R.id.timer);
        sum = (TextView) findViewById(R.id.sum);
        score = (TextView) findViewById(R.id.score);
        state = (TextView) findViewById(R.id.state);

        buttons = new Button[]{
                (Button) findViewById(R.id.button1),
                (Button) findViewById(R.id.button2),
                (Button) findViewById(R.id.button3),
                (Button) findViewById(R.id.button4)
        };

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                countDown();
            }
        };


    }

    public void countDown() {
        secs--;
        timer.setText(secs+"s");
        handler.postDelayed(runnable, 1000);

        if(secs == 0) {
            gameOver();
        }
    }

    public void gameSetup(View v) {
        Button startBtn = (Button) v;
        startBtn.setText("Restart");

        for(int i = 0; i < buttons.length; i++) {
            buttons[i].setEnabled(true);
        }

        handler.removeCallbacks(runnable);

        generateSum();
        scoreNum = 0;
        total = 0;
        secs = 30;
        handler.post(runnable);

        score.setText("0/0");
        timer.setText("30s");
    }

    public void gameOver() {
        hornPlayer.start();

        handler.removeCallbacks(runnable);
        state.setText("Your score is:\n"+scoreNum+"/"+total);
        sum.setText("Brain Trainer");

        for(int i = 0; i < buttons.length; i++) {
            buttons[i].setEnabled(false);
            buttons[i].setText("0");
        }
    }

    public int getUserAns(View v) {
        int ans = Integer.parseInt(v.getTag().toString());

        return ans;
    }

    public void generateSum() {
        int one = (int) (Math.random() * 100);
        int two = (int) (Math.random() * 100);
        answer = one + two;

        int randNum = (int) (Math.random() * 3);

        for(int i = 0; i < buttons.length; i++) {
            if(i == randNum) {
                buttons[i].setTag(answer);
                buttons[i].setText("" + answer);
            }
            else {
                int wanswer = (int) (Math.random() * 200);
                buttons[i].setTag(wanswer);
                buttons[i].setText("" + wanswer);
            }
        }

        sum.setText(one + "+" + two);
    }

    public void userAnswer(View v) {
        boolean correct = getUserAns(v) == answer;

        if(correct) {
            correctPlayer.start();

            scoreNum++;
            total++;

            state.setText("Correct");
            generateSum();

        }else {
            wrongPlayer.start();
            total++;

            state.setText("Wrong");
        }

        score.setText(scoreNum + "/" + total);
    }




}