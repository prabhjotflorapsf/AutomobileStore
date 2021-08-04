package com.example.automobilestore.Activity;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.automobilestore.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class Report_Activity extends AppCompatActivity implements View.OnClickListener {

    StorageReference storageReference;
    SharedPreferences sp;
    FirebaseFirestore fstore;
    TextInputLayout edt_reason;
    public LinearLayout spam, illegal, bullying, scam, false_info, hate_speech, smth_else;
    LinearLayout Main_LL,other_LL;
    Button report_btn;
    String DocId,UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);


        Bundle extras = getIntent().getExtras();
        UserId = extras.getString("userID");
        DocId = extras.getString("PostId");



        spam = findViewById(R.id.ll1);
        illegal = findViewById(R.id.ll2);
        bullying = findViewById(R.id.ll3);
        scam = findViewById(R.id.ll4);
        false_info = findViewById(R.id.ll5);
        hate_speech = findViewById(R.id.ll6);
        smth_else = findViewById(R.id.ll7);
        edt_reason=findViewById(R.id.edt_reason);
        report_btn=findViewById(R.id.submit_report);
        Main_LL=findViewById(R.id.final_LL);
        other_LL=findViewById(R.id.other_LL);

        spam.setOnClickListener(this);
        illegal.setOnClickListener(this);
        bullying.setOnClickListener(this);
        scam.setOnClickListener(this);
        false_info.setOnClickListener(this);
        hate_speech.setOnClickListener(this);
        smth_else.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll1:
                spam.setBackgroundColor(Color.RED);
                Report_Post("It's spam",UserId,DocId);
                break;
            case R.id.ll2:
                illegal.setBackgroundColor(Color.RED);
                Report_Post("Sale of illegal or regulated goods ",UserId,DocId);
                break;
            case R.id.ll3:
                bullying.setBackgroundColor(Color.RED);
                Report_Post("Bullying or harsassment",UserId,DocId);
                break;
            case R.id.ll4:
                scam.setBackgroundColor(Color.RED);
                Report_Post("Scam or fraud",UserId,DocId);
                break;
            case R.id.ll5:
                false_info.setBackgroundColor(Color.RED);
                Report_Post("False information",UserId,DocId);
                break;
            case R.id.ll6:
                hate_speech.setBackgroundColor(Color.RED);
                Report_Post("Hate speech or symbols",UserId,DocId);
                break;
            case R.id.ll7:
                smth_else.setBackgroundColor(Color.RED);
                Main_LL.setVisibility(View.GONE);
                other_LL.setVisibility(View.VISIBLE);

                report_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Report_Post(edt_reason.getEditText().getText().toString(),UserId,DocId);
                    }
                });

                break;
        }
    }

    private void Report_Post(String reason,String uid,String did ){
        Log.d(TAG, "Report_Post: "+reason);
        Log.d(TAG, "Report_Post: "+uid);
        Log.d(TAG, "Report_Post: "+did);
        fstore = FirebaseFirestore.getInstance();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("Reason",reason);
        userMap.put("UserId",uid);
        userMap.put("DocId",did);
        fstore.collection("Report").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(Report_Activity.this, "Reported Successfully", Toast.LENGTH_SHORT).show();
                finish();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String Error = e.getMessage();
                Toast.makeText(Report_Activity.this, " Error:" + Error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}