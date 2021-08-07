package com.example.automobilestore.Admin;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automobilestore.Admin.Model_adapter.AdminUserData;
import com.example.automobilestore.Admin.Model_adapter.AdminUserDataAdapter;
import com.example.automobilestore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class AdminEditDeleteUser extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<AdminUserData> userDataArrayList;
    AdminUserDataAdapter adminUserDataAdapter;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_editdeluser);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data......!!!");
        progressDialog.show();

        recyclerView = findViewById(R.id.userList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseFirestore = FirebaseFirestore.getInstance();
        userDataArrayList = new ArrayList<AdminUserData>();
        adminUserDataAdapter = new AdminUserDataAdapter(AdminEditDeleteUser.this , userDataArrayList);
        recyclerView.setAdapter(adminUserDataAdapter);


        EventChangeListener();
    }

    private void EventChangeListener() {
        firebaseFirestore.collection("User")
                .orderBy("Name" , Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("FireStore Error" , error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                           // dc.getDocument().getId();
                            Log.d(TAG, "onEvent: "+ dc.getDocument().getId());
                            String id=dc.getDocument().getId();
                            String Email=(String) dc.getDocument().get("Email");
                            String phone=(String) dc.getDocument().get("Phone");
                            String name=(String) dc.getDocument().get("Name");
                            getProfileImage(id,Email,phone,name);
//                            userDataArrayList.add(new AdminUserData(id,name,Email,phone));
                        }
                        adminUserDataAdapter.notifyDataSetChanged();
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
    }
    private void getProfileImage(String id,String Email,String phone,String name) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("images/Profile/" + id + ".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Log.d(TAG, "onSuccess: "+uri);

                    userDataArrayList.add(new AdminUserData(id, name, Email, phone, uri));
                adminUserDataAdapter.notifyDataSetChanged();
                Log.d(TAG, "onSuccess: "+uri);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                //setting default image in the user profile
                 Uri uri=Uri.parse("https://firebasestorage.googleapis.com/v0/b/automobilestore-782cb.appspot.com/o/images%2FProfile%2Fdo_not_remove_this_file.png?alt=media&token=7d60fbec-3232-4d56-aca8-fcae8f5f713a");
                userDataArrayList.add(new AdminUserData(id, name, Email, phone,uri ));
                adminUserDataAdapter.notifyDataSetChanged();
            }
        });
    }

}
