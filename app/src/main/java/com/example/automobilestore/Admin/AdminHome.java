package com.example.automobilestore.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automobilestore.Activity.PostAd;
import com.example.automobilestore.MainActivity;
import com.example.automobilestore.R;
import com.google.firebase.auth.FirebaseAuth;

public class AdminHome extends AppCompatActivity {


    TextView welcome, logout;
    FirebaseAuth firebaseAuth;
    ImageView Imlogout,addPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        logout = findViewById(R.id.logout);
        welcome = findViewById(R.id.welcome_admin);
        Imlogout = findViewById(R.id.logout_img);
        addPost = findViewById(R.id.add_post_admin);

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PostAd.class);
                startActivity(i);
            }
        });

        Imlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    public void logout() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        Toast.makeText(getApplicationContext(), "Logout Successfully", Toast.LENGTH_LONG).show();
    }

}