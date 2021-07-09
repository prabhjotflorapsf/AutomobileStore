package com.example.automobilestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.automobilestore.Activity.ForgotPassword;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import static com.example.automobilestore.Activity.CreateAccount.isEmailValid;

public class admin_forgotpassword extends AppCompatActivity {

    TextInputLayout email;
    Button password_reset;
    String emailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_forgotpassword);

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        password_reset = findViewById(R.id.admin_resetpassword);
        email = findViewById(R.id.admin_resetemail);

        password_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailAddress = email.getEditText().getText().toString();

                if(emailAddress.isEmpty()){
                    email.setError("Email is required!");
                    email.requestFocus();
                    return;
                }
                if (!isEmailValid(emailAddress)) {
                    Toast.makeText(admin_forgotpassword.this, "Please enter valid email ", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    System.out.println(emailAddress);
                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(admin_forgotpassword.this, "Reset Password Link has been sent to given registered email", Toast.LENGTH_SHORT).show();
                                        Log.d("", "Email sent.");
                                    } else {
                                        try {
                                            throw task.getException();

                                        } catch (FirebaseAuthInvalidUserException e) {
                                            Toast.makeText(admin_forgotpassword.this, "This Email is not registered", Toast.LENGTH_SHORT).show();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                }
            }
        });

    }
}