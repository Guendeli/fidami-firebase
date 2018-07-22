package com.guendeli.fidami.activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.guendeli.fidami.LoginActivity;
import com.guendeli.fidami.R;

public class SplashActivity extends AppCompatActivity {

    protected ImageView animationImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        animationImage = (ImageView)findViewById(R.id.loading_image);
        AnimationDrawable animationDrawable = (AnimationDrawable) animationImage.getDrawable();
        animationDrawable.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 2000);
    }
}
