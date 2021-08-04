package com.example.automobilestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.automobilestore.Others.UserLogs;
import com.example.automobilestore.model.AdminPostsModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

public class admin_postview extends AppCompatActivity {

    RecyclerView mFirestoreList;
    FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_postview);

        mFirestoreList = findViewById(R.id.admin_postlog);
        firebaseFirestore = FirebaseFirestore.getInstance();

        Query query = firebaseFirestore.collection("Car");
        FirestoreRecyclerOptions<AdminPostsModel> options = new FirestoreRecyclerOptions.Builder<AdminPostsModel>()
                .setQuery(query, AdminPostsModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<AdminPostsModel,
                PostsViewHolder>(options) {
            @NonNull
            @NotNull
            @Override
            public PostsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_postlist_items,parent,false);
                return new PostsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull @NotNull PostsViewHolder holder, int position, @NonNull @NotNull AdminPostsModel model) {
                holder.Model.setText(model.getModel());
                holder.Engine_type.setText(model.getEngine_type());
                holder.year.setText(model.getYear());
                holder.Amount.setText(model.getAmount());
                holder.image.setImageURI(model.getImage());
            }
        };

        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);
    }

    private class PostsViewHolder extends RecyclerView.ViewHolder{

        private TextView Model,Engine_type,year, Amount;
        private ImageView image;

        public PostsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            Model = itemView.findViewById(R.id.company_name);
            Engine_type = itemView.findViewById(R.id.postlist_Type);
            year = itemView.findViewById(R.id.postlist_Year);
            Amount = itemView.findViewById(R.id.postlist_price);
            image = itemView.findViewById(R.id.postlist_image);
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