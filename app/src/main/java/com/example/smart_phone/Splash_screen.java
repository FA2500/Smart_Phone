package com.example.smart_phone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;

public class Splash_screen extends AppCompatActivity {

    ImageView Img;
    TextView Txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        Img = findViewById(R.id.logor);
        Txt = findViewById(R.id.textlogor);
        Animation anime = AnimationUtils.loadAnimation(this,R.anim.side_slide);
        Img.startAnimation(anime);
        Txt.startAnimation(anime);

        Intent i = new Intent(this , MainActivity3.class);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i);
                finish();
            }
        }, 3000);

    }
}