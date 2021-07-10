package com.example.automobilestore.Fragment.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.automobilestore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import static android.content.ContentValues.TAG;

public class overviewFragment extends Fragment {


    private static String Id;
    TextView model, seaters, color, year,condition;
    ImageView gps;
    FirebaseFirestore fstore;


    public static overviewFragment getInstance(String AptId) {
        Id = AptId;
        overviewFragment OverviewFragment = new overviewFragment();
        return OverviewFragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        model = view.findViewById(R.id.d_model);
        seaters = view.findViewById(R.id.d_seaters);

        color = view.findViewById(R.id.d_color);
        year = view.findViewById(R.id.d_year);
        condition=view.findViewById(R.id.d_condition);
        Log.d(TAG, "onComplete:aaaaaaaaa ");

        gps= view.findViewById(R.id.d_gps);

        getdata();

        return view;
    }

    private void getdata() {
        fstore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fstore.collection("Car").document(Id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Map<String, Object> data1 = document.getData();

                        String mt = data1.get("Model").toString();
                        String st = data1.get("Seaters").toString()+" SEATERS.";
                        String ct = data1.get("Color").toString();
                        String yt = data1.get("Year").toString();
                        String gpst = data1.get("Gps").toString();
                        String condt=data1.get("Conditon").toString();
                        //Log.d(TAG, "onComplete:aaaaaaaaa "+mt);


                        model.setText(mt);
                        seaters.setText(st);
                        color.setText(ct);
                        year.setText(yt);
                        condition.setText(condt);

                        if (gpst.equals("Yes")) {
                            gps.setImageResource(R.drawable.rightmark);
                        } else {
                            gps.setImageResource(R.drawable.wrongmark);
                            Log.d(TAG, "onComplete:aaaaaaaaa ");
                        }

                    } else {
                        Log.d("tagvv", "No such document");
                    }
                } else {
                    Log.d("tagvv", "get failed with ", task.getException());
                }
            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
