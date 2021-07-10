package com.example.automobilestore.Fragment.ui.dashboard;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automobilestore.R;
import com.example.automobilestore.adapter.WishlistAdapter;
import com.example.automobilestore.model.WishlistModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class WishlistFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<WishlistModel> wishlist = new ArrayList<>();
    FirebaseFirestore db;
    /**
     * variable declaration for recyclerview
     */
    RecyclerView wishlistRecycler;
    WishlistAdapter wishAdapter;
    FirebaseStorage storage;
    StorageReference storageReference;
    /**
     * variable declaration authenication object
     */
    private FirebaseAuth auth;
    /**
     * variable declarationfor current user
     */
    private FirebaseUser curUser;

    public WishlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WishlistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WishlistFragment newInstance(String param1, String param2) {
        WishlistFragment fragment = new WishlistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        wishlist.clear();
        return inflater.inflate(R.layout.fragment_wishlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        wishlistRecycler = view.findViewById(R.id.wishlist_recycler);
    }

    private void getWishlistDetails() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        curUser = auth.getCurrentUser();
        String userId = null;
        if (curUser != null) {
            userId = curUser.getUid(); //Do what you need to do with the id
        }
        db.collection("Wishlist")
                .whereEqualTo("UserId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("", document.getId() + " => " + document.getData());
                                System.out.println(document.getId() + " => " + document.getData());
                                String DocumentId = (String) document.getData().get("DocId");
                                Log.d("", "" + DocumentId);
                                getCarDetails(DocumentId, wishlist);
                            }
                            setWishlistRecycler(wishlist);
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void getCarDetails(final String Docid, final ArrayList<WishlistModel> wishlist) {
        DocumentReference docRef = db.collection("Car").document(Docid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        String price, address, model, transmission, Year,condition;
                        price = (String) document.getData().get("Amount");
                        address = (String) document.getData().get("Address");
                        model = (String) document.getData().get("Model");
                        transmission = (String) document.getData().get("Transmission");
                        Year = (String) document.getData().get("Year");
                        condition= (String) document.getData().get("Conditon");

                        getImage(Docid, price, model, transmission, Year, address,condition, wishlist);

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    private void getImage(final String Docid, final String price, final String model, final String transmission, final String Year, final String address,final String condition, final ArrayList<WishlistModel> wishlist) {
        storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("images/" + Docid + "/0").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Log.d("TAG", "image got"+uri);

                wishlist.add(new WishlistModel(Docid, price,model, transmission, Year, address,condition, uri));
                wishAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d("TAG", "image not got");
            }
        });

    }

    private void setWishlistRecycler(ArrayList<WishlistModel> wishlist) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false);
        wishlistRecycler.setLayoutManager(layoutManager);
        wishAdapter = new WishlistAdapter(getActivity().getApplicationContext(), wishlist);
        wishlistRecycler.setAdapter(wishAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        wishlist.clear();
        getWishlistDetails();
    }
}