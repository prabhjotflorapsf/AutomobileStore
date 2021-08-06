package com.example.automobilestore.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.automobilestore.IntroActivity;
import com.example.automobilestore.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent=new Intent(SplashScreen.this, IntroActivity.class);
            startActivity(intent);
            finish();
        },1500);
    }
}