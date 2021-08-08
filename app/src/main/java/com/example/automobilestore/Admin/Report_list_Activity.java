package com.example.automobilestore.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.automobilestore.Activity.Add_ons;
import com.example.automobilestore.Activity.CarDetails;
import com.example.automobilestore.Admin.Model_adapter.AddOns;
import com.example.automobilestore.Admin.Model_adapter.AddOns_Adapter;
import com.example.automobilestore.Admin.Model_adapter.Report_Adapter;
import com.example.automobilestore.Admin.Model_adapter.Report_List;
import com.example.automobilestore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Report_list_Activity extends AppCompatActivity {

    private SwipeRefreshLayout swipeContainer;
    RecyclerView Recycler;
    List<Report_List> A_List = new ArrayList<>();
    Report_Adapter AddOnsAdapter;
    FirebaseStorage storage;
    StorageReference storageReference;
    ImageView selectedImage, selectedImage1, selectedImage2, selectedImage3, upload,del;
    ImageView[] image;
    FirebaseFirestore fstore;
    String User_Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.ref_List);
        Recycler=findViewById(R.id.report_list);

        getData();
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                A_List.clear();
                getData();
                swipeContainer.setRefreshing(false);
            }
        });
    }
    private void setReportAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        Recycler.setLayoutManager(layoutManager);
        AddOnsAdapter = new Report_Adapter(Report_list_Activity.this,A_List);
        Recycler.setAdapter(AddOnsAdapter);
    }

    private void getData() {
        fstore = FirebaseFirestore.getInstance();

        fstore.collection("Report").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                         String Reason = (String) document.getData().get("Reason");
                        String UserId = (String) document.getData().get("UserId");
                       String Post_Id = (String) document.getData().get("DocId");
                        String document_ID=document.getId();

                        getuserId(UserId,Post_Id,Reason);
//                        Toast.makeText(Report_list_Activity.this, "heyy user id", Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, "onComplete: "+User_Name);


                        setReportAdapter();

                    }
                }
            }
        });
    }



    private void getuserId(String temp,String Post_id,String reason) {
        fstore = FirebaseFirestore.getInstance();
        Log.d(TAG, "getuserId: "+temp);
        DocumentReference docRef = fstore.collection("User").document(temp);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String email= (String) document.getData().get("Email");
                        getImage(email,Post_id,reason);
                        Log.d(TAG, "onComplete: "+User_Name);

                    }
                }
            }
        });
    }

    private void getImage(final String email,final String post_id,final String reason) {
        storageReference = FirebaseStorage.getInstance().getReference();
        Log.d(TAG, "getImage: "+post_id);
        storageReference.child("images/" + post_id + "/0").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                A_List.add(new Report_List(post_id,email, reason,uri));
                //Log.d(TAG, "onSuccess: "+User_Name);
                AddOnsAdapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d("TAG", "image not got");
            }
        });
    }
}