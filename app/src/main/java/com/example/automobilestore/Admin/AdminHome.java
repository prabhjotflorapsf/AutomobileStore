package com.example.automobilestore.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automobilestore.Activity.PostAd;
import com.example.automobilestore.MainActivity;
import com.example.automobilestore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static android.content.ContentValues.TAG;

public class AdminHome extends AppCompatActivity {


    TextView welcome, logout;

    FirebaseAuth firebaseAuth;
    ImageView Imlogout,Report_admin;

    TextView Post_count,User_count;
    private FirebaseAuth auth;
    FirebaseFirestore db;
    private FirebaseUser curUser;
    LinearLayout addOns, services , userLogs,PostLogs,report_admin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        logout = findViewById(R.id.logout);
        welcome = findViewById(R.id.welcome_admin);
        Imlogout = findViewById(R.id.logout_img);
//        addPost = findViewById(R.id.add_post_admin);
        report_admin=findViewById(R.id.report_admin);
        report_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: reportAdmin");
                Intent i = new Intent(getApplicationContext(), Report_list_Activity.class);
                startActivity(i);
            }
        });

//        addPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent i = new Intent(getApplicationContext(), PostAd.class);
//                startActivity(i);
//            }
//        });

        User_count=findViewById(R.id.tv_ucount);
        Post_count=findViewById(R.id.tv_pcount);
        addOns=findViewById(R.id.add_ons_btn);
        services =findViewById(R.id.services_btn);
        userLogs = findViewById(R.id.userLogs);
        PostLogs = findViewById(R.id.Posts_Logs);

        getCount();


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
        addOns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(AdminHome.this, "clicked", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AdminHome.this, Post_AddOns.class);
                startActivity(i);
            }
        });
        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminHome.this, Post_Services.class);
                startActivity(i);
                // Toast.makeText(AdminHome.this, "clicked", Toast.LENGTH_SHORT).show();
            }
        });

        userLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminHome.this , AdminEditDeleteUser.class);
                startActivity(i);
            }
        });
    }

    public void logout() {
        auth = FirebaseAuth.getInstance();
        auth.signOut();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        Toast.makeText(getApplicationContext(), "Logout Successfully", Toast.LENGTH_LONG).show();
    }

    public void getCount() {
        db = FirebaseFirestore.getInstance();
        //User Count
        db.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                        int count=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

//                                count=task.getResult().size();
                                count++;
                            }
                           User_count.setText(String.valueOf(count)+" User");
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        //post Count
        db.collection("Car")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

//                                count=task.getResult().size();
                                count++;
                            }
                            Post_count.setText(String.valueOf(count)+" Post");
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

         }
}