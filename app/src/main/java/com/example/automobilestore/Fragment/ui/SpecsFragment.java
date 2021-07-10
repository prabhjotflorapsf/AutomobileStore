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


public class SpecsFragment extends Fragment {
    TextView power, transmission, fuelType,classification;
    ImageView powerWindow,sunRoof,ac,bluetooth,airBags,parking_sensor,blind_sensor,visualAids;

    FirebaseFirestore fstore;
    static String id;

    public static SpecsFragment getInstance(String Docid) {
        id = Docid;
        SpecsFragment accessibilityFragment = new SpecsFragment();

        return accessibilityFragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_specs, container, false);

        power=view.findViewById(R.id.d_power);
        transmission=view.findViewById(R.id.d_transmission);
        fuelType=view.findViewById(R.id.d_fueltype);
        classification=view.findViewById(R.id.d_classification);

        powerWindow=view.findViewById(R.id.d_powerWindow);
        sunRoof=view.findViewById(R.id.d_sunRoof);  Log.d(TAG, "onCreateView: ssssssssssssssssss");

        ac=view.findViewById(R.id.d_ac);
        bluetooth=view.findViewById(R.id.d_bluetooth);
        airBags=view.findViewById(R.id.d_airBags);

        parking_sensor=view.findViewById(R.id.d_p_sensor);
        blind_sensor=view.findViewById(R.id.d_b_sensor);
        visualAids=view.findViewById(R.id.d_visualAids);


        getdata();

        return view;
    }

    private void getdata() {
        fstore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fstore.collection("Car").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Map<String, Object> data1 = document.getData();

                        String pwt = data1.get("Power_Window").toString();
                        String srt = data1.get("Sun_Roof").toString();
                        String act = data1.get("AC").toString();
                        String bt = data1.get("Bluetooth").toString();
                        String abt = data1.get("AirBags").toString();
                        String pst = data1.get("Parking_Sensor").toString();
                        String bst = data1.get("Blind_Spot_Senser").toString();
                        String vat = data1.get("Visual_Aids").toString();
                        String powert = data1.get("Power").toString();
                        String classificationt = data1.get("Classification").toString();
                        String fueltypet = data1.get("Fuel_type").toString();
                        String transmissiont = data1.get("Transmission").toString();
                        power.setText(powert);
                        classification.setText(classificationt);
                        fuelType.setText(fueltypet);
                        transmission.setText(transmissiont);
                        if (pwt.equals("Yes")) {
                            powerWindow.setImageResource(R.drawable.rightmark);
                        } else {
                            powerWindow.setImageResource(R.drawable.wrongmark);
                        }
                        if (srt.equals("Yes")) {
                            sunRoof.setImageResource(R.drawable.rightmark);
                        } else {
                            sunRoof.setImageResource(R.drawable.wrongmark);
                        }
                        if (act.equals("Yes")) {
                            ac.setImageResource(R.drawable.rightmark);
                        } else {
                            ac.setImageResource(R.drawable.wrongmark);
                        }
                        if (bt.equals("Yes")) {
                            bluetooth.setImageResource(R.drawable.rightmark);
                        } else {
                            bluetooth.setImageResource(R.drawable.wrongmark);
                        }
                        if (abt.equals("Yes")) {
                            airBags.setImageResource(R.drawable.rightmark);
                        } else {
                            airBags.setImageResource(R.drawable.wrongmark);
                        }if (pst.equals("Yes")) {
                            parking_sensor.setImageResource(R.drawable.rightmark);
                        } else {
                            parking_sensor.setImageResource(R.drawable.wrongmark);
                        }if (bst.equals("Yes")) {
                            blind_sensor.setImageResource(R.drawable.rightmark);
                        } else {
                            blind_sensor.setImageResource(R.drawable.wrongmark);
                        }if (vat.equals("Yes")) {
                            visualAids.setImageResource(R.drawable.rightmark);
                        } else {
                            visualAids.setImageResource(R.drawable.wrongmark);
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


}
