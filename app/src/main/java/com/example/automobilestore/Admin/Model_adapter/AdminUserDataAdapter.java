package com.example.automobilestore.Admin.Model_adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automobilestore.Activity.PostList;
import com.example.automobilestore.Activity.UpdateAd;
import com.example.automobilestore.Admin.AdminHome;
import com.example.automobilestore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class AdminUserDataAdapter extends RecyclerView.Adapter<AdminUserDataAdapter.AdminUserDataViewHolder> {

    Context context;
    ArrayList<AdminUserData> list;

    public AdminUserDataAdapter(Context context, ArrayList<AdminUserData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public AdminUserDataViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_user , parent , false);
        return new AdminUserDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdminUserDataViewHolder holder, int position) {
        AdminUserData adminUserData = list.get(position);
        holder.Name.setText(adminUserData.getName());
        holder.Email.setText(adminUserData.getEmail());
        holder.Phone.setText(adminUserData.getPhone());
        Log.d(TAG, "onBindViewHolder: "+adminUserData.getId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostList pl=new PostList();
                Intent i = new Intent(context, PostList.class);
                i.putExtra("id",adminUserData.getId());
               context.startActivity(i);

            }
        });
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore fstore = FirebaseFirestore.getInstance();
                DocumentReference docRef = fstore.collection("User").document(adminUserData.getId());
                docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("tagvv", "DocumentSnapshot successfully deleted!");
                        Toast.makeText(context, "User Deleted Successfully", Toast.LENGTH_SHORT).show();

                        PostDelete(adminUserData.getId());


                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("tagvv", "Error deleting document", e);
                            }
                        });
            }
        });
    }

    private void PostDelete(String id) {
        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
        // Create a reference to the cities collection
        CollectionReference docref = fstore.collection("Car");

// Create a query against the collection.
        Query query = docref.whereEqualTo("UserID", id);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                     //docref.document(document.getId()).delete();

                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class AdminUserDataViewHolder extends RecyclerView.ViewHolder {

        TextView Name , Email , Phone;
        ImageView delete_btn;
        public AdminUserDataViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.userName);
            Email = itemView.findViewById(R.id.userEmail);
            Phone = itemView.findViewById(R.id.userPhoneNumber);
            delete_btn=itemView.findViewById(R.id.delete_btn);
        }
    }

}
