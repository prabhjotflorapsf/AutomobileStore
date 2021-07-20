package com.example.automobilestore.Admin;
// Admin Side ADD_ons Page
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.automobilestore.Activity.Add_ons;
import com.example.automobilestore.Activity.PostAd;
import com.example.automobilestore.Activity.UpdateAd;
import com.example.automobilestore.Admin.Model_adapter.AddOns;
import com.example.automobilestore.Admin.Model_adapter.AddOns_Adapter;
import com.example.automobilestore.R;
import com.example.automobilestore.adapter.Horizontal_Car_Adapter;
import com.example.automobilestore.model.HorizontalCarData;
import com.example.automobilestore.model.VerticalCarData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Post_AddOns extends AppCompatActivity {
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
    SwipeRefreshLayout swipeContainer;
    String uID="";
    Boolean changed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_add_ons);

        Comp= findViewById(R.id.add_c);
        addr=findViewById(R.id.add_a);
        link=findViewById(R.id.add_l);
        post=findViewById(R.id.submit_post);
        update=findViewById(R.id.Update_post);
        upload = findViewById(R.id.add_image);
        del=findViewById(R.id.a_delete);
        image = new ImageView[]{upload, selectedImage1, selectedImage2, selectedImage3};
        Recycler=findViewById(R.id.add_rv);
        getData();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.refresh_List);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                A_List.clear();
            getData();
                swipeContainer.setRefreshing(false);
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();

            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    PostData();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedata();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    private void updatedata() {
        fstore = FirebaseFirestore.getInstance();

        final String c = Comp.getEditText().getText().toString().trim();
        final String a = addr.getEditText().getText().toString().trim();
        final String l = link.getEditText().getText().toString().trim();
        if (c.isEmpty()) {
            Toast.makeText(Post_AddOns.this, "Please Enter Company", Toast.LENGTH_LONG).show();
            return;
        } else if (a.isEmpty()) {
            Toast.makeText(Post_AddOns.this, "Please Enter Address", Toast.LENGTH_LONG).show();
            return;
        } else if (l.isEmpty()) {
            Toast.makeText(Post_AddOns.this, "Please Enter Url", Toast.LENGTH_LONG).show();
            return;
        } else if (photos < 1) {
            Toast.makeText(Post_AddOns.this, "Please Select atleast 1 photo", Toast.LENGTH_LONG).show();
        }else {
            final ProgressDialog pd;
            pd = new ProgressDialog(Post_AddOns.this);
            pd.setMessage("Loading...");
            pd.show();

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("Company", c);
            userMap.put("Address", a);
            userMap.put("Link", l);
            fstore.collection("AddOns").document(uID).set(userMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (changed) {
                                uploadImage((uID));
                            }
                            Log.d("TAG", "DocumentSnapshot successfully written!");
                            SystemClock.sleep(3000);
                            Toast.makeText(Post_AddOns.this, "Post Updated Successfully", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error writing document", e);
                        }
                    });

        }
    }
    private void delete() {
        fstore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fstore.collection("AddOns").document(uID);
        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                deleteImages();
                Log.d("tagvv", "DocumentSnapshot successfully deleted!");
//                A_List.clear();
                Toast.makeText(Post_AddOns.this, "Post Deleted Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("tagvv", "Error deleting document", e);
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
    private void deleteImages() {
        Log.d(TAG, "deleteImages: calllll"+uID);
        storageReference = FirebaseStorage.getInstance().getReference();

// Create a reference to the file to delete
        for (int i = 0; i < internetPhotos; i++) {
            StorageReference desertRef = storageReference.child("AddOnsImage/"+ uID + "/0");
// Delete the file
            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                    Log.d(TAG, "onSuccess: deleted");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                    Log.d(TAG, "onFailure: not deleted");
                }
            });
        }
    }

    private void PostData() {
        fstore = FirebaseFirestore.getInstance();
        final String c = Comp.getEditText().getText().toString().trim();
        final String a = addr.getEditText().getText().toString().trim();
        final String l = link.getEditText().getText().toString().trim();
        if (c.isEmpty()) {
            Toast.makeText(Post_AddOns.this, "Please Enter Company", Toast.LENGTH_LONG).show();
            return;
        }else if (a.isEmpty()) {
            Toast.makeText(Post_AddOns.this, "Please Enter Address", Toast.LENGTH_LONG).show();
            return;
        }else if (l.isEmpty()) {
            Toast.makeText(Post_AddOns.this, "Please Enter Url", Toast.LENGTH_LONG).show();
            return;
        }else if (photos < 1) {
            Toast.makeText(Post_AddOns.this, "Please Select atleast 1 photo", Toast.LENGTH_LONG).show();
        }else {
            final ProgressDialog pd;
            pd = new ProgressDialog(Post_AddOns.this);
            pd.setMessage("Loading...");
            pd.show();

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("Company", c);
            userMap.put("Address", a);
            userMap.put("Link", l);
            fstore.collection("AddOns").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d("Demoooooooo1o1", "onSuccess: " + documentReference.getId());
                    uploadImage((String) documentReference.getId());
                    Toast.makeText(Post_AddOns.this, " Post added Successfully ", Toast.LENGTH_SHORT).show();
                    pd.dismiss();

                    finish();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    String Error = e.getMessage();
                    Toast.makeText(Post_AddOns.this, " Error:" + Error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void selectImage() {
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Post_AddOns.this);
        builder1.setTitle("Add Photo!");
        builder1.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose from Gallery")) {
                    contenturi.clear();
                    upload.setImageResource(R.drawable.uploadnew);
                    Intent gallery = new Intent();
                    gallery.setType("image/*");
                    gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    gallery.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(gallery, ""), GALLERY_REQUEST_CODE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder1.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                ClipData clipdata = data.getClipData();

                if (clipdata != null) {
                    photos = clipdata.getItemCount();
                    if (clipdata.getItemCount() > 4) {
                        Toast.makeText(this, "Please select only four items", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (int i = 0; i < clipdata.getItemCount(); i++) {
                        ClipData.Item item = clipdata.getItemAt(i);
                        contenturi.add(item.getUri());
                    }
                } else {
                    contenturi.add(data.getData());
                    photos = 1;
                }
            }
        }
    }
    private void uploadImage(String id) {
        storageReference = FirebaseStorage.getInstance().getReference();
        for (int j = 0; j < contenturi.size(); j++) {

            StorageReference ref = storageReference.child("AddOnsImage").child(id + "/" + j);
            ref.putFile(contenturi.get(j))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(Post_AddOns.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }
    private void setAddOnsAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        Recycler.setLayoutManager(layoutManager);
        AddOnsAdapter = new AddOns_Adapter(Post_AddOns.this,A_List,"Admin");
        Recycler.setAdapter(AddOnsAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();

        uID=intent.getStringExtra("id");
        if(uID!=null){
            update.setVisibility(View.VISIBLE);
            del.setVisibility(View.VISIBLE);
            fstore = FirebaseFirestore.getInstance();
            DocumentReference docRef = fstore.collection("AddOns").document(uID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String Address = (String) document.getData().get("Address");
                            String Company = (String) document.getData().get("Company");
                            String Link = (String) document.getData().get("Link");

                            Comp.getEditText().setText(Company);
                            addr.getEditText().setText(Address);
                            link.getEditText().setText(Link);


                        }
                    }
                }
            });
        }
    }
}