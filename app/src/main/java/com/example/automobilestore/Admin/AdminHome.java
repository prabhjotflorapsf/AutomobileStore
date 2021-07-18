package com.example.automobilestore.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static android.content.ContentValues.TAG;

public class AdminHome extends AppCompatActivity {


    TextView welcome, logout;
    TextView Post_count,User_count;
    ImageView Imlogout;
    private FirebaseAuth auth;
    FirebaseFirestore db;
    private FirebaseUser curUser;
    LinearLayout addOns,Services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        logout = findViewById(R.id.logout);
        welcome = findViewById(R.id.welcome_admin);
        Imlogout = findViewById(R.id.logout_img);
        User_count=findViewById(R.id.tv_ucount);
        Post_count=findViewById(R.id.tv_pcount);
        addOns=findViewById(R.id.add_ons_btn);
        Services=findViewById(R.id.services_btn);

//        User_count.setText(String.valueOf(getCount("User")+" User"));
//       // Final_Count=0;
//        Post_count.setText(String.valueOf(getCount("Car")+" Post"));
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
            }
        });
        Services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(AdminHome.this, "clicked", Toast.LENGTH_SHORT).show();
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