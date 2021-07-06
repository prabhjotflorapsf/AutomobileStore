package com.example.automobilestore.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.automobilestore.R;

public class PostList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        Toast.makeText(this, "Manage", Toast.LENGTH_SHORT).show();
    }
}