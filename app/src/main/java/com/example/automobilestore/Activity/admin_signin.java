package com.example.automobilestore.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.automobilestore.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class admin_signin extends AppCompatActivity {

    public TextInputLayout Email, Password;
    Button login;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Email = findViewById(R.id.admin_email);
        Password = findViewById(R.id.admin_password);
        login = findViewById(R.id.adminlogin);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getEditText().getText().toString();
                String pwd = Password.getEditText().getText().toString();

                
            }
        });
    }
}