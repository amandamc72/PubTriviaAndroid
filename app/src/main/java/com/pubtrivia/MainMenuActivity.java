package com.pubtrivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        RotateAnimation leftAnim = new RotateAnimation(3f, -3f, 90f, 90f);
        leftAnim.setInterpolator(new LinearInterpolator());
        leftAnim.setRepeatCount(Animation.INFINITE);
        leftAnim.setRepeatMode(Animation.REVERSE);
        leftAnim.setDuration(500);

        RotateAnimation rightAnim = new RotateAnimation(-3f, 3f, 90f, 90f);
        rightAnim.setInterpolator(new LinearInterpolator());
        rightAnim.setRepeatCount(Animation.INFINITE);
        rightAnim.setRepeatMode(Animation.REVERSE);
        rightAnim.setDuration(500);

        ImageView leftBeer = (ImageView) findViewById(R.id.leftBeer);
        ImageView rightBeer = (ImageView) findViewById(R.id.rightBeer);
        leftBeer.startAnimation(leftAnim);
        rightBeer.startAnimation(rightAnim);


        ImageButton start = (ImageButton) findViewById(R.id.startButton);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainMenuActivity.this, SettingsActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
