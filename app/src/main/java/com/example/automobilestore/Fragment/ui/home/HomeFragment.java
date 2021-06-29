package com.example.automobilestore.Fragment.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automobilestore.Activity.PostAd;
import com.example.automobilestore.R;
import com.example.automobilestore.adapter.Vertical_Car_Adapter;
import com.example.automobilestore.adapter.Horizontal_Car_Adapter;
import com.example.automobilestore.databinding.FragmentHomeBinding;
import com.example.automobilestore.model.VerticalCarData;
import com.example.automobilestore.model.HorizontalCarData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    FloatingActionButton add_btn;
    RecyclerView HorizontalRecycler, VerticalRecycler;
    Vertical_Car_Adapter VerticalAdapter;
    StorageReference storageReference;
    private FirebaseAuth auth;
    FirebaseFirestore db;
    private FirebaseUser curUser;
    List<HorizontalCarData> HorizontalList = new ArrayList<>();
    List<VerticalCarData> VerticalList = new ArrayList<>();

    Horizontal_Car_Adapter HorizontalAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        add_btn=v.findViewById(R.id.add_post_btn);
        HorizontalRecycler = v.findViewById(R.id.rv_hcar);
        VerticalRecycler = v.findViewById(R.id.rv_vcar);

        RefreshData(v);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PopUpClass popUpClass = new PopUpClass();
//                popUpClass.showPopupWindow(v);
                Intent i = new Intent(getActivity().getApplicationContext(), PostAd.class);
                startActivity(i);

            }
        });

        return v;
    }

    private void RefreshData(View v) {

//
//        HorizontalList.add(new HorizontalCarData("Honda", "$19000", R.drawable.logo));
//        HorizontalList.add(new HorizontalCarData("Hyundai", "$17000", R.drawable.logo));
//        HorizontalList.add(new HorizontalCarData("Toyota", "$25000", R.drawable.logo));
//        HorizontalList.add(new HorizontalCarData("Ford", "$7000", R.drawable.logo));
//        HorizontalList.add(new HorizontalCarData("Mazda", "$17899", R.drawable.logo));
//        HorizontalList.add(new HorizontalCarData("Suzuki", "$5670", R.drawable.logo));
//        setHorizontal(HorizontalList);
//
//        List<VerticalCarData> VerticalList = new ArrayList<>();
//        VerticalList.add(new VerticalCarData("Honda", "$19000", R.drawable.logo,  "OLD"));
//        VerticalList.add(new VerticalCarData("Hyundai", "$17000", R.drawable.logo,  "OLD"));
//        VerticalList.add(new VerticalCarData("Toyota", "$25000", R.drawable.logo,  "NEW"));
//        VerticalList.add(new VerticalCarData("Ford", "$7000", R.drawable.logo, "NEW"));
//        VerticalList.add(new VerticalCarData("Mazda", "$17899", R.drawable.logo, "OLD"));
//        VerticalList.add(new VerticalCarData("Suzuki", "$5670", R.drawable.logo, "NEW"));
//
//        setVertical(VerticalList);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        curUser = auth.getCurrentUser();
        String userId = null;
        if (curUser != null) {
            userId = curUser.getUid(); //Do what you need to do with the id
        }
        db.collection("Car")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("", document.getId() + " => " + document.getData());
                                System.out.println(document.getId() + " => " + document.getData());
                                String Model = (String) document.getData().get("Model");
                                String Amount = (String) document.getData().get("Amount");
                                String Conditon=(String) document.getData().get("Conditon");
                                String UserID=document.getId();
                                getImage(UserID,Model,Amount,Conditon);
                                setHorizontal();
                                setVertical();

                                Log.d("modellll", "modellll" + Model);
                                Log.d("amountttt", "amounttttt" +   Amount);


                            }

                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });

//
//
    }
    private void setHorizontal() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        HorizontalRecycler.setLayoutManager(layoutManager);
        HorizontalAdapter = new Horizontal_Car_Adapter(getActivity(), HorizontalList);
        HorizontalRecycler.setAdapter(HorizontalAdapter);
    }
    private void setVertical() {


        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        VerticalRecycler.setLayoutManager(layoutManager);
        VerticalAdapter = new Vertical_Car_Adapter(getActivity(), VerticalList);
        VerticalRecycler.setAdapter(VerticalAdapter);

    }
    private void getImage(final String UserID,final String Model,final String Amount,final String Conditon) {
        storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("images/" + UserID + "/0").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Log.d("TAG", "image got");
                if(Conditon.equals("yes")||Conditon.equals("Yes")){
                    HorizontalList.add(new HorizontalCarData(Model, Amount, uri));
                    HorizontalAdapter.notifyDataSetChanged();
                }
                else{
                    if (Conditon=="yes"||Conditon=="Yes"){
                        VerticalList.add(new VerticalCarData(Model, Amount,uri,"NEW"));
                    }else{
                        VerticalList.add(new VerticalCarData(Model, Amount,uri,"OlD"));
                    }

                    setVertical();
                    VerticalAdapter.notifyDataSetChanged();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d("TAG", "image not got");
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


//    private void AddPost() {
//        Toast.makeText(getActivity(),"hello",Toast.LENGTH_LONG).show();
//    }



}