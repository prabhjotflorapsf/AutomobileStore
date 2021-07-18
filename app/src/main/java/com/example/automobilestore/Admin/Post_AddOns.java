package com.example.automobilestore.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.automobilestore.Activity.PostAd;
import com.example.automobilestore.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Post_AddOns extends AppCompatActivity {
    TextInputLayout Comp,addr,link;
    Button post;
    FirebaseFirestore fstore;
    final int GALLERY_REQUEST_CODE = 105;
    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference storageReference;
    ImageView selectedImage, selectedImage1, selectedImage2, selectedImage3, upload;
    ImageView[] image;
    ArrayList<Uri> contenturi = new ArrayList<Uri>();
    int photos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_add_ons);
        Comp= findViewById(R.id.add_c);
        addr=findViewById(R.id.add_a);
        link=findViewById(R.id.add_l);
        post=findViewById(R.id.submit_post);
        upload = findViewById(R.id.add_image);
        image = new ImageView[]{upload, selectedImage1, selectedImage2, selectedImage3};
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostData();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
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
            userMap.put("Comapny", c);
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
}