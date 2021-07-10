package com.example.automobilestore.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.automobilestore.MainActivity;
import com.example.automobilestore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.automobilestore.Activity.CreateAccount.isEmailValid;

public class admin_signup extends AppCompatActivity {

    TextInputLayout admin_name, admin_phone, admin_email, admin_password, admin_confirmPassword;
    Button create_btn, login_btn;
    private FirebaseAuth mFirebaseAuth;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();
        admin_name = findViewById(R.id.admin_create_name);
        admin_phone = findViewById(R.id.admin_create_phone);
        admin_email = findViewById(R.id.admin_create_email);
        admin_password = findViewById(R.id.admin_create_password);
        admin_confirmPassword = findViewById(R.id.admin_create_confirmPassword);
        create_btn = findViewById(R.id.Signup_btn);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        sp = getSharedPreferences("Admindata", Context.MODE_PRIVATE);

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Admin_Name = admin_name.getEditText().getText().toString();
                String Admin_Phone = admin_phone.getEditText().getText().toString();
                String Admin_Email = admin_email.getEditText().getText().toString();
                String Admin_Password = admin_password.getEditText().getText().toString();
                String Admin_ConfirmPassword = admin_confirmPassword.getEditText().getText().toString();

                if (Admin_Name.isEmpty() || Admin_Phone.isEmpty() || Admin_Email.isEmpty() || Admin_Password.isEmpty() || Admin_ConfirmPassword.isEmpty()) {
                    Toast.makeText(admin_signup.this, "Please Fill The Form", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Admin_Password.length() < 6) {

                    Toast.makeText(admin_signup.this, "Password should be 6 characters or long", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((Admin_Phone.length() < 10) || (Admin_Phone.length() > 10)) {
                    Toast.makeText(admin_signup.this, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Admin_Password.equals(Admin_ConfirmPassword)) {
                    Toast.makeText(admin_signup.this, "Confirm password doesn't match with password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isEmailValid(Admin_Email)) {
                    Toast.makeText(admin_signup.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("ADMINEmailID", Admin_Email);
                editor.putString("ADMINPassword", Admin_ConfirmPassword);
                editor.commit();
                final ProgressDialog pd = new ProgressDialog(admin_signup.this);
                pd.setMessage("Loading...");
                pd.show();
                final Map<String, Object> usermap = new HashMap<>();
                usermap.put("Name", Admin_Name);
                usermap.put("Phone", Admin_Phone);
                usermap.put("Email", Admin_Email);

                mFirebaseAuth.createUserWithEmailAndPassword(Admin_Email, Admin_Password).addOnCompleteListener(admin_signup.this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            db.collection("User").document(mFirebaseAuth.getCurrentUser().getUid()).set(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext().getApplicationContext(), "Register Success!", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(i);
                                        finish();
                                        pd.dismiss();
                                    }
                                }
                            });
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(admin_signup.this, "Email id already Exist", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(admin_signup.this, "SignUp Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    }
                });
            }
        });

    }
}