package com.pubtrivia;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        TextView totalCorrectTextView = (TextView) findViewById(R.id.totalCorrectText);
        TextView totalQuestionsTextview = (TextView) findViewById(R.id.outOfText);
        TextView scoreTextView =  (TextView) findViewById(R.id.percentText);
        Button restartButton = (Button) findViewById(R.id.restartButton);

        MediaPlayer done = MediaPlayer.create(this, R.raw.tada);
        done.start();
        done.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        int totalCorrect;
        int totalQuestions;
        if (getIntent() != null) {
            Bundle extras = getIntent().getExtras();
            totalCorrect = extras.getInt("score");
            totalQuestions = extras.getInt("total");
        } else {
            totalCorrect = 0;
            totalQuestions = 0;
        }

        totalCorrectTextView.setText(String.valueOf(totalCorrect));
        totalQuestionsTextview.setText(String.valueOf(totalQuestions));

        double percent = totalCorrect * 100 / totalQuestions;
        scoreTextView.setText(getString(R.string.percent, String.valueOf(percent)));

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ScoreActivity.this, SettingsActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
