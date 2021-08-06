package com.example.automobilestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.automobilestore.adapter.IntroAdapter;

public class IntroActivity extends AppCompatActivity {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        viewPager = findViewById(R.id.viewpager);

       IntroAdapter adapter = new IntroAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }
}