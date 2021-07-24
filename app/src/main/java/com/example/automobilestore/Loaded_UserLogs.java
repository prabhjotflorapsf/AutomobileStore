package com.example.automobilestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.automobilestore.Others.UserLogs;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

import static java.security.AccessController.getContext;

public class Loaded_UserLogs extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mFirestoreList;

    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaded_user_logs);

        mFirestoreList = findViewById(R.id.fireStorelist);

        Query query = firebaseFirestore.collection("User");
        FirestoreRecyclerOptions<UserLogs> options = new FirestoreRecyclerOptions.Builder<UserLogs>()
                .setQuery(query, UserLogs.class)
                .build();
        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<UserLogs, UserLogViewHolder>(options) {
            @NonNull
            @NotNull
            @Override
            public UserLogViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlogs_list, parent, false);
                return new UserLogViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull @NotNull UserLogViewHolder holder, int position, @NonNull @NotNull UserLogs model) {
                holder.list_name.setText(model.getName());
                holder.list_email.setText(model.getEmail());
                holder.list_phone.setText(model.getPhone()+"");
            }
        };

        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);
    }

    private class UserLogViewHolder extends RecyclerView.ViewHolder{

        private TextView list_name;
        private TextView list_email;
        private TextView list_phone;

        public UserLogViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            list_email =itemView.findViewById(R.id.user_email);
            list_name =itemView.findViewById(R.id.user_name);
            list_phone =itemView.findViewById(R.id.user_phoneno);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}