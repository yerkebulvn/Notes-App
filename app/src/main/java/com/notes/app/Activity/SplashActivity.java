package com.notes.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import com.notes.app.R;


public class SplashActivity extends AppCompatActivity {

    // Экран дисплейінің ұзақтығы (миллисекундпен)
    private static int SPLASH_TIME_OUT = 2000;

    //UI элементтері
    private CardView Logo;
    private Animation animation;
    private TextView Logo_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Қолданба темасын орнату
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Логотипке арналған анимацияны жүктеу
        animation = AnimationUtils.loadAnimation(this, R.anim.smalltobig);

        // UI элементтерін инициализациялау
        Logo = findViewById(R.id.logo);
        Logo.startAnimation(animation);
        Logo_text =  findViewById(R.id.logo_text);
        Logo_text.startAnimation(animation);

        // Қолданба параметрлерін жүктеп, негізгі әрекетке өтіңіз
        loadSettings();
    }

    // Қолданба параметрлерін жүктеу және негізгі әрекетке өту әдісі
    public void loadSettings() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // Негізгі әрекетті бастау intent жасау
                Intent main = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(main);
                finish();   // SplashActivity жабу

            }
        },SPLASH_TIME_OUT);
    }
}