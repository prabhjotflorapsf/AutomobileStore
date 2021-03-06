package com.example.automobilestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.automobilestore.adapter.PostListAdapter;
import com.example.automobilestore.model.PostListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class admin_postview extends AppCompatActivity {

    RecyclerView PostListRecycler;

    PostListAdapter postlistAdapter;
    FirebaseStorage storage;
    StorageReference storageReference;
    /**
     * variable declaration authenication object
     */
    FirebaseFirestore db;
    private FirebaseAuth auth;
    /**
     * variable declarationfor current user
     */
    private FirebaseUser curUser;
    String userId = null;
    ArrayList<PostListModel> postlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_postview);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        curUser = auth.getCurrentUser();
        SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.ref_List);
        if (curUser != null) {
            userId = curUser.getUid(); //Do what you need to do with the id
        }
        getCarList();
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                postlist.clear();
                getCarList();
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private void getCarList() {
        db = FirebaseFirestore.getInstance();
        db.collection("Car")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("PostList", document.getId() + " => " + document.getData());
                                String id = document.getId();
                                String Model = (String) document.getData().get("Model");
                                String price = (String) document.getData().get("Amount");
                                String type = (String) document.getData().get("Transmission");
                                String year = (String) document.getData().get("Year");
                                getImage(id, Model,type,year,price);
                            }
                            setPostListRecycler();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void getImage(final String id, final String Model, final String Type, final String Year, final String Price) {
        storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("images/" + id + "/0").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                postlist.add(new PostListModel(id, Model, Type, Year, Price, uri));
                postlistAdapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }


    private void setPostListRecycler() {
        PostListRecycler = findViewById(R.id.admin_postlog);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        PostListRecycler.setLayoutManager(layoutManager);
        postlistAdapter = new PostListAdapter(this, postlist);
        PostListRecycler.setAdapter(postlistAdapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        postlist.clear();
        postlistAdapter.notifyDataSetChanged();
        getCarList();
    }
}
