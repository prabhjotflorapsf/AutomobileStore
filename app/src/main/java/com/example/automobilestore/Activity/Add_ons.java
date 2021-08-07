package com.example.automobilestore.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.example.automobilestore.Admin.Model_adapter.AddOns;
import com.example.automobilestore.Admin.Model_adapter.AddOns_Adapter;
import com.example.automobilestore.Admin.Post_AddOns;
import com.example.automobilestore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class Add_ons extends AppCompatActivity {
    SwipeRefreshLayout swipeContainer;
    TextInputLayout Comp,addr,link;
    Button post,update;
    FirebaseFirestore fstore;
    final int GALLERY_REQUEST_CODE = 105;
    FirebaseAuth auth;
    RecyclerView Recycler;
    FirebaseStorage storage;
    StorageReference storageReference;
    ImageView selectedImage, selectedImage1, selectedImage2, selectedImage3, upload,del;
    ImageView[] image;
    ArrayList<Uri> contenturi = new ArrayList<Uri>();
    int photos = 0,internetPhotos = 0;
    List<AddOns> A_List = new ArrayList<>();
    AddOns_Adapter AddOnsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ons);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.refresh);

        image = new ImageView[]{upload, selectedImage1, selectedImage2, selectedImage3};
        Recycler=findViewById(R.id.add_ons_rv);
        // Setup refresh listener which triggers new data loading
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
    private void getData() {
        fstore = FirebaseFirestore.getInstance();

        fstore.collection("AddOns").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String Address = (String) document.getData().get("Address");
                        String Company = (String) document.getData().get("Company");
                        String Link = (String) document.getData().get("Link");
                        String UserID=document.getId();
                        Log.d(TAG, "onComplete: "+UserID);
                        getImage(UserID,Company,Address,Link);

                        setAddOnsAdapter();

                    }
                }
            }
        });
    }
    private void setAddOnsAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        Recycler.setLayoutManager(layoutManager);
        AddOnsAdapter = new AddOns_Adapter(Add_ons.this,A_List,"User");
        Recycler.setAdapter(AddOnsAdapter);
    }
    private void getImage(final String UserID,final String Company,final String Address,final String Link) {
        storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("AddOnsImage/" + UserID + "/0").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                A_List.add(new AddOns(UserID,Company, Address,Link, uri));
                Log.d(TAG, "onSuccess: "+uri);
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