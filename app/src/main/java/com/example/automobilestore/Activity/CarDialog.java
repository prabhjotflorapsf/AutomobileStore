package com.example.automobilestore.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.automobilestore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CarDialog extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    SharedPreferences sp;
    public TextInputLayout Email, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_dialog);

        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
    }

    public void showDialog(final Activity activity, final String docId) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_car_dialog);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.y = 150;
        window.setAttributes(wlp);
        ImageView next = (ImageView) dialog.findViewById(R.id.btn_dialog);

        getCarData(dialog, docId);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, CarDetails.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", docId);
                i.putExtras(bundle);
                activity.startActivity(i);
            }
        });
        dialog.show();


    }

    private void getCarData(final Dialog dialog, final String DocId) {
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Car").document(DocId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        TextView title_model = (TextView) dialog.findViewById(R.id.title_model);
                        TextView Tran_type = (TextView) dialog.findViewById(R.id.Tran_type);
                        TextView S_capacity = (TextView) dialog.findViewById(R.id.S_capacity);
                        TextView price = (TextView) dialog.findViewById(R.id.price);
                        title_model.setText("" + document.getData().get("Model"));
                        Tran_type.setText("" + document.getData().get("Transmission"));
                        S_capacity.setText("Capacity:- " + document.getData().get("Seaters"));
                        price.setText(document.getData().get("Amount") + "$");

                       // getImage(dialog, DocId);
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });


    }
}