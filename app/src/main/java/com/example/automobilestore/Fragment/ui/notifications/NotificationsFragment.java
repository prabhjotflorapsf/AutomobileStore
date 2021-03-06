package com.example.automobilestore.Fragment.ui.notifications;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.automobilestore.Activity.CreateAccount;
import com.example.automobilestore.Activity.ForgotPassword;
import com.example.automobilestore.Admin.AdminHome;
import com.example.automobilestore.Admin.admin_signin;
import com.example.automobilestore.MainActivity;
import com.example.automobilestore.R;

import com.example.automobilestore.Fragment.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import static android.content.ContentValues.TAG;

public class NotificationsFragment extends Fragment {

    public TextInputLayout Email, Password;
    Button create, login, forgot, admin;
    ProgressDialog pd;
    private FirebaseAuth auth;
    private FirebaseUser curUser;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String from = "map";
    String AptId, emailStr, passStr;
    private static final String ARG_PARAM2 = "param2";
    SharedPreferences sp;
    boolean check=false;
    private FirebaseFirestore fstore;


    public NotificationsFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getActivity().getSharedPreferences("Userdata", Context.MODE_PRIVATE);

    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notifications, container, false);
        auth = FirebaseAuth.getInstance();
        Email = v.findViewById(R.id.email);
        Password = v.findViewById(R.id.password);
        create = v.findViewById(R.id.create);
        login = v.findViewById(R.id.login);
        forgot = v.findViewById(R.id.forgotpass);
        admin = v.findViewById(R.id.adminSignin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("tagvv", " hello");
                String email = Email.getEditText().getText().toString();
                String pwd = Password.getEditText().getText().toString();

                checkemail(email);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("USEREmailID", email);
                editor.putString("USERPassword", pwd);
                editor.commit();

                System.out.println(email + "" + pwd);
                if (email.isEmpty() || pwd.isEmpty()) {
                    Toast.makeText(getActivity(), "Please Fill The Form", Toast.LENGTH_SHORT).show();
                    return;
                }
                pd = new ProgressDialog(getActivity());
                pd.setMessage("Loading...");
                pd.show();

                auth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "onComplete: "+check);
                        if (task.isSuccessful()&&check) {
                            curUser = auth.getCurrentUser();
                            Toast.makeText(getActivity().getApplicationContext(), "Login Success!", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getActivity(), MainActivity.class);
                            startActivity(i);
                        }else if(check==false){
                            curUser = auth.getCurrentUser();
                            curUser.delete();
                            auth = FirebaseAuth.getInstance();
                            auth.signOut();
                            Email.getEditText().getText().clear();
                            Password.getEditText().getText().clear();
                            Email.setError("Email not exist!");
                            Email.requestFocus();
                            pd.dismiss();
                        }
                        else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                Toast.makeText(getActivity().getApplicationContext(), "Email not exist!", Toast.LENGTH_LONG).show();
                                Email.getEditText().getText().clear();
                                Password.getEditText().getText().clear();
                                Email.setError("Email not exist!");
                                Email.requestFocus();
                                pd.dismiss();
                                return;
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(getActivity().getApplicationContext(), "Wrong Credential!", Toast.LENGTH_LONG).show();
                                Password.getEditText().getText().clear();
                                Email.requestFocus();
                                pd.dismiss();
                                return;
                            } catch (Exception e) {
                                Toast.makeText(getActivity().getApplicationContext(), "Login Failed!", Toast.LENGTH_LONG).show();
                                pd.dismiss();
                            }
                        }
                    }
                });
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplicationContext(), CreateAccount.class);
                startActivity(i);
            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplicationContext(), admin_signin.class);
//                Intent i = new Intent(getActivity().getApplicationContext(), AdminHome.class);
                startActivity(i);
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplicationContext(), ForgotPassword.class);
                startActivity(i);
            }
        });
        return v;
    }
    public void checkemail(String email){
        fstore = FirebaseFirestore.getInstance();

        fstore.collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int count=0;

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String em= (String) document.getData().get("Email");
                        Log.d(TAG, "onComplete: "+em);
                        if(email.equals(em)){
                            count++;

                        }

                    }
                    Log.d(TAG, "onComplete: "+count);
                    if(count>=1){
                        setBoolean(true);
                    }
                }
            }
        });


    }

    private void setBoolean(boolean b) {
        check=b;
    }
}