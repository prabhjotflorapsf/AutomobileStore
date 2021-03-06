package com.example.automobilestore.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automobilestore.Fragment.ui.SpecsFragment;
import com.example.automobilestore.Fragment.ui.overviewFragment;
import com.example.automobilestore.R;
import com.example.automobilestore.adapter.ViewImagePagerAdapter;
import com.example.automobilestore.adapter.ViewPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class CarDetails extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView water;
    TextView ModelName, price, address, description;
    ImageView report;
    Button btnbuyCar;
    ImageView like;
    LinearLayout mainLayout;
    ViewPager imageViewPager;
    Boolean wishlisted = false;
    FirebaseFirestore fstore;

    ProgressDialog pd;
    FirebaseStorage storage;
    StorageReference storageReference;
    String UserId;
    String WishlistedId, Uid;
    private FirebaseUser curUser;
    private FirebaseAuth auth;
    WebView browser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);
        ModelName = (TextView) findViewById(R.id.ModelName);
        price = (TextView) findViewById(R.id.price);
        address = (TextView) findViewById(R.id.address);
        description = (TextView) findViewById(R.id.description);
        btnbuyCar = (Button) findViewById(R.id.ContactOwner);
        like = (ImageView) findViewById(R.id.like);
        imageViewPager = findViewById(R.id.imageslider);
        browser = (WebView) findViewById(R.id.browser2);
        report=(ImageView)findViewById(R.id.report);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        browser.getSettings().setJavaScriptEnabled(true);

        ArrayList<Uri> images = new ArrayList<Uri>();

        auth = FirebaseAuth.getInstance();


        final String DocId;
        Bundle extras = getIntent().getExtras();
        DocId = extras.getString("id");
        curUser = auth.getCurrentUser();
        if (curUser != null) {
            UserId = curUser.getUid();
        }
        pd = new ProgressDialog(CarDetails.this);
        pd.setMessage("Loading...");
        pd.show();

        getTabs(DocId);
        getImages(images, DocId);
        getdata(DocId);
        checkWishlist(DocId, like);
        pd.dismiss();
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fstore = FirebaseFirestore.getInstance();
                curUser = auth.getCurrentUser();
                if (curUser != null) {
                    UserId = curUser.getUid();
                }
                if (curUser != null) {
                    UserId = curUser.getUid();

                    Intent i = new Intent(CarDetails.this, Report_Activity.class);
                    i.putExtra("userID",UserId);
                    i.putExtra("PostId",DocId);
                    startActivity(i);
                    // RefreshData();
                } else {

                    CarDialog alert = new CarDialog();
                    alert.showLoginDialog(CarDetails.this);

                }
            }
        });
        btnbuyCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth = FirebaseAuth.getInstance();
                fstore = FirebaseFirestore.getInstance();
                curUser = auth.getCurrentUser();
                if (curUser != null) {
                    Intent i = new Intent(getApplicationContext(), Contact.class);
                    i.putExtra("Uid", Uid);
                    startActivity(i);
                } else {
                    CarDialog alert = new CarDialog();
                    alert.showLoginDialog(CarDetails.this);
                }
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fstore = FirebaseFirestore.getInstance();
                curUser = auth.getCurrentUser();
                if (curUser != null) {
                    UserId = curUser.getUid();
                }
                if (curUser != null) {
                    UserId = curUser.getUid();

                    if (wishlisted) {
                        fstore.collection("Wishlist").document(WishlistedId)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                        Toast.makeText(CarDetails.this, "Removed from Wishlist", Toast.LENGTH_SHORT).show();
                                        like.setImageResource(R.drawable.like);
                                        wishlisted = false;
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("TAG", "Error deleting document", e);
                                    }
                                });
                    } else {
                        String filter = UserId + "_" + DocId;
                        final Map<String, Object> wishlist = new HashMap<>();
                        wishlist.put("UserId", UserId);
                        wishlist.put("DocId", DocId);
                        wishlist.put("Filter", filter);

                        fstore.collection("Wishlist")
                                .add(wishlist)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        CarDialog alert = new CarDialog();
                                        //alert.showLoginDialog(CarDetails.this);
                                        Log.d("TAG", "DocumentSnapshot written with ID: " + documentReference.getId());
                                        Toast.makeText(CarDetails.this, "Added to Wishlist", Toast.LENGTH_SHORT).show();
                                        like.setImageResource(R.drawable.wishlisticon);
                                        wishlisted = true;
                                        WishlistedId = documentReference.getId();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("TAG", "Error adding document", e);
                                    }
                                });
                    }

                } else {

                    CarDialog alert = new CarDialog();
                    alert.showLoginDialog(CarDetails.this);

                }
            }
        });

    }

    private void checkWishlist(String Id, final ImageView like) {
        fstore.collection("Wishlist")
                .whereEqualTo("Filter", UserId + "_" + Id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    Log.d("Pratit", document.getId() + " => " + document.getData());
                                    like.setImageResource(R.drawable.wishlisticon);
                                    wishlisted = true;
                                    WishlistedId = document.getId();
                                    return;
                                } else {
                                    Log.d("Pratit", "Error getting documents: ", task.getException());
                                    like.setImageResource(R.drawable.wishlisticon);
                                }
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void getImages(final ArrayList<Uri> images, final String aptId) {
        StorageReference listRef = FirebaseStorage.getInstance().getReference().child("images/" + aptId);
        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                        }

                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            storageReference = FirebaseStorage.getInstance().getReference();
                            String location = item.toString();
                            String image = location.substring(location.length() - 1);
                            System.out.println(image);
                            storageReference = FirebaseStorage.getInstance().getReference();
                            storageReference.child("images/" + aptId + "/" + image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Got the download URL for 'users/me/profile.png'
                                    System.out.println(images.size() + "maa");
                                    images.add(uri);
                                    ViewImagePagerAdapter viewImagePagerAdapter = new ViewImagePagerAdapter(getApplicationContext(), images);
                                    viewImagePagerAdapter.notifyDataSetChanged();
                                    imageViewPager.setAdapter(viewImagePagerAdapter);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
    }


    private void getdata(String aptId) {

        fstore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fstore.collection("Car").document(aptId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Map<String, Object> data1 = document.getData();

                        String Model = data1.get("Model").toString();
                        String pr = data1.get("Amount").toString();
                        String des = data1.get("Description").toString();
                        String Address = data1.get("Address").toString();
                        Uid = data1.get("UserID").toString();
                        ModelName.setText(Model);
                        price.setText(pr + "$");
                        description.setText(des);
                        address.setText(Address);

                        Log.d("tagvv", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("tagvv", "No such document");
                    }
                } else {
                    Log.d("tagvv", "get failed with ", task.getException());
                }
            }
        });
    }


    public void getTabs(final String docid) {
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG, "run: sssssss");

                viewPagerAdapter.addFragment(SpecsFragment.getInstance(docid), "SPECIFICATION");
                viewPagerAdapter.addFragment(overviewFragment.getInstance(docid), "OVERVIEW");
                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}