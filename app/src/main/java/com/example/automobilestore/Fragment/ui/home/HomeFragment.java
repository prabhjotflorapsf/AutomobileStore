package com.example.automobilestore.Fragment.ui.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.utils.Oscillator;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.automobilestore.Activity.CarDialog;
import com.example.automobilestore.Activity.PostAd;
import com.example.automobilestore.Activity.ProfileDetails;
import com.example.automobilestore.Admin.Model_adapter.AdminUserData;
import com.example.automobilestore.R;
import com.example.automobilestore.adapter.Vertical_Car_Adapter;


import com.example.automobilestore.model.VerticalCarData;
import com.example.automobilestore.model.HorizontalCarData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {

    FloatingActionButton add_btn;
    RecyclerView HorizontalRecycler, VerticalRecycler;
    Vertical_Car_Adapter VerticalAdapter;
    StorageReference storageReference;
    private FirebaseAuth auth;
    FirebaseFirestore db;
    private FirebaseUser curUser;
    List<VerticalCarData> VerticalList = new ArrayList<>();
    List<VerticalCarData> newList = new ArrayList<>();
    boolean priceChanged = false;
    boolean passengerChanged = false;
    ImageView filter,imageView3,list,grid;
    float min = 0, max = 100000;
    float minp = 1, maxp = 10;
    String UserId;
    FirebaseFirestore fstore;
    SwipeRefreshLayout swipeContainer;
    EditText search;
    int count=0;
    String LayoutType="List";

    CircleImageView profile_image2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        add_btn=v.findViewById(R.id.add_post_btn);
        profile_image2=v.findViewById(R.id.profile_image2);
        search=v.findViewById(R.id.search_et);
        VerticalRecycler = v.findViewById(R.id.rv_vcar);
        imageView3=v.findViewById(R.id.imageView3);
        list=v.findViewById(R.id.list_btn);
        grid=v.findViewById(R.id.grid_btn);
    profile_image2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            fstore = FirebaseFirestore.getInstance();
            curUser = auth.getCurrentUser();
            if (curUser != null) {
                UserId = curUser.getUid();
            }
            if (curUser != null) {
                UserId = curUser.getUid();
                Intent i = new Intent(getActivity().getApplicationContext(), ProfileDetails.class);
                startActivity(i);
                // RefreshData();
            } else {

                CarDialog alert = new CarDialog();
                alert.showLoginDialog(getActivity());

            }

        }
    });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setVisibility(View.VISIBLE);
                imageView3.setVisibility(View.GONE);
                list.setVisibility(View.GONE);
                grid.setVisibility(View.GONE);
            }
        });
        auth = FirebaseAuth.getInstance();



                   getProfile();

            list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grid.setVisibility(View.VISIBLE);
                    list.setVisibility(View.GONE);
                    LayoutType="List";
                    setVertical();

                }
            });
            grid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.setVisibility(View.VISIBLE);
                    grid.setVisibility(View.GONE);
                    LayoutType="Grid";
                    setVertical();

                }
            });
////        RefreshData();
//         Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                search.setText("");
                RefreshData();
                swipeContainer.setRefreshing(false);
            }
        });


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PopUpClass popUpClass = new PopUpClass();
//                popUpClass.showPopupWindow(v);
                fstore = FirebaseFirestore.getInstance();
                curUser = auth.getCurrentUser();
                if (curUser != null) {
                    UserId = curUser.getUid();
                }
                if (curUser != null) {
                    UserId = curUser.getUid();
                    Intent i = new Intent(getActivity().getApplicationContext(), PostAd.class);
                    startActivity(i);
                    // RefreshData();
                } else {

                    CarDialog alert = new CarDialog();
                    alert.showLoginDialog(getActivity());

                }

            }
        });
        filter = v.findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter(getActivity());
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s=search.getText().toString().toLowerCase();

//                Toast.makeText(getActivity(), ""+count++, Toast.LENGTH_SHORT).show();
                if(s.isEmpty()||s==null) {
                    Log.d(TAG, "onClick: hellooooo");
                    search.setVisibility(View.GONE);
                    imageView3.setVisibility(View.VISIBLE);
                    RefreshData();
                }else {

                    Search(search.getText().toString());
                }
            }

        });


//
//        search.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                String s=search.getText().toString().toLowerCase();
//
////                Toast.makeText(getActivity(), ""+count++, Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "onKey: "+s.isEmpty()+"  "+keyCode);
//                if(keyCode==66||s.isEmpty()&&keyCode==67){
//
//                    search.setText("");
//                    Log.d(TAG, "onClick: hellooooo");
//                    search.setVisibility(View.GONE);
//                    imageView3.setVisibility(View.VISIBLE);
//                    if (LayoutType=="Grid"){
//                        list.setVisibility(View.VISIBLE);
//
//                    }else {
//                        grid.setVisibility(View.VISIBLE);
//                    }
//                    RefreshData();
//                }else {
//
//                    Search(search.getText().toString());
//                }
//                return false;
//            }
//        });


        return v;
    }

    private void getProfile() {
        curUser = auth.getCurrentUser();
        if (curUser != null) {
            UserId = curUser.getUid();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            storageReference.child("images/Profile/" + UserId + ".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Log.d(Oscillator.TAG, "onSuccess: "+uri);
                    Picasso.get().load(uri).fit().into(profile_image2);

                    Log.d(Oscillator.TAG, "onSuccess: "+uri);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors

                }
            });
        }
    }

    private void Search(String search_text) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        curUser = auth.getCurrentUser();


        String userId = null;
        if (curUser != null) {
            userId = curUser.getUid(); //Do what you need to do with the id
        }


        db.collection("Car")
//                .whereGreaterThan("Amount",min).whereLessThan("Amount",max)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String Model = (String) document.getData().get("Model");
                                Float Amount = Float.parseFloat(String.valueOf(document.getData().get("Amount")));
                                String condition = (String) document.getData().get("Conditon");
                                String UserID = document.getId();
                                Log.d("search Data", "search modellll" + Model);
                                setVertical();
//                                if ((Model.toLowerCase()).startsWith(finalSearch_text) || (Address.toLowerCase()).startsWith(finalSearch_text) || (Classification.toLowerCase()).startsWith(finalSearch_text)) {
                                if ((Model.toLowerCase()).contains(search_text)){
                                    Log.d(TAG, "onComplete: "+Model);
                                    getSearch(UserID, Model, Amount.toString(),condition);


                                } else {

                                    VerticalList.clear();

                                    // Toast.makeText(getActivity().getApplicationContext(), "Oops No Cars Found", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                });
    }

    public void RefreshData() {

        VerticalList.clear();
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
                                setVertical();

                                Log.d("modellll", "modellll" + Model);
                                Log.d("amountttt", "amounttttt" +   Amount);


                            }

                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void setVertical() {

        if (LayoutType == "Grid") {
            GridLayoutManager Manager = new GridLayoutManager(getContext(),2);
            VerticalRecycler.setLayoutManager(Manager);
        }else {
            LinearLayoutManager Manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
            VerticalRecycler.setLayoutManager(Manager);
        }

        VerticalAdapter = new Vertical_Car_Adapter(getActivity(), VerticalList,LayoutType);
        VerticalRecycler.setAdapter(VerticalAdapter);

    }
    private void getSearch(final String UserID,final String Model,final String Amount,String condition) {
        storageReference = FirebaseStorage.getInstance().getReference();

        storageReference.child("images/" + UserID + "/0").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

//                VerticalList.clear();
                Log.d(TAG, "getSearch: hellooooo");

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    newList=VerticalList.stream().distinct().collect(Collectors.toList());
//                }

                VerticalList.add(new VerticalCarData(UserID,Model, Amount, uri,condition));
                VerticalAdapter.notifyDataSetChanged();
            }
        });
    }
    private void getImage(final String UserID,final String Model,final String Amount,final String Conditon) {
        storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("images/" + UserID + "/0").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Log.d("TAG", "image got"+Conditon);

                VerticalList.add(new VerticalCarData(UserID,Model, Amount,uri,Conditon));
                    setVertical();
                    VerticalAdapter.notifyDataSetChanged();
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
//        RefreshData();


    }
    @Override
    public void onResume() {
        super.onResume();

        RefreshData();
    }


    //    private void AddPost() {
//        Toast.makeText(getActivity(),"hello",Toast.LENGTH_LONG).show();
//    }
    public void filter(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_filter);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        final RangeSlider price_slider = dialog.findViewById(R.id.price_slider);
        final RangeSlider Seaters_slider = dialog.findViewById(R.id.Seaters_slider);
        final TextView to = dialog.findViewById(R.id.to);
        final TextView from = dialog.findViewById(R.id.from);
        final TextView toSeaters = dialog.findViewById(R.id.toSeaters);
        final TextView fromSeaters = dialog.findViewById(R.id.fromSeaters);
//   dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//       @Override
//       public void onDismiss(DialogInterface dialog) {
//           RefreshData();
//       }
//   });

        price_slider.setValues(min, max);
        to.setText("Min " + min);
        from.setText("Max " + max);
        dialog.show();


        Seaters_slider.setValues(minp, maxp);
        toSeaters.setText("Min " + minp);
        fromSeaters.setText("Max " + maxp);
        dialog.show();

        Button apply = dialog.findViewById(R.id.apply);
        Button reset = dialog.findViewById(R.id.reset);

        price_slider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
                List price = slider.getValues();
                min = (float) price.get(0);
                max = (float) price.get(1);
                to.setText("Min " + min);
                from.setText("Max " + max);
            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                List price = slider.getValues();
                min = (float) price.get(0);
                max = (float) price.get(1);
                to.setText("Min " + min);
                from.setText("Max " + max);
            }
        });

        price_slider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                List price = slider.getValues();
                min = (float) price.get(0);
                max = (float) price.get(1);
                to.setText("Min " + min);
                from.setText("Max " + max);
            }
        });

        Seaters_slider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
                List Passenger = slider.getValues();
                minp = (float) Passenger.get(0);
                maxp = (float) Passenger.get(1);
                toSeaters.setText("Min " + minp);
                fromSeaters.setText("Max " + maxp);

            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                List Passenger = slider.getValues();
                minp = (float) Passenger.get(0);
                maxp = (float) Passenger.get(1);
                toSeaters.setText("Min " + minp);
                fromSeaters.setText("Max " + maxp);

            }
        });

        Seaters_slider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                List Passenger = slider.getValues();
                minp = (float) Passenger.get(0);
                maxp = (float) Passenger.get(1);
                toSeaters.setText("Min " + minp);
                fromSeaters.setText("Max " + maxp);
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List price = price_slider.getValues();
                min = (float) price.get(0);
                max = (float) price.get(1);
                to.setText("Min " + min);
                from.setText("Max " + max);
                Log.d("", min + "");
                priceChanged = true;

                List passenger = Seaters_slider.getValues();
                Log.d("", "VALUE SET " + passenger);
                minp = (float) passenger.get(0);
                Log.d("", "VALUE SET " + minp);
                maxp = (float) passenger.get(1);
                Log.d("", "VALUE SET " + maxp);
                toSeaters.setText("Min " + minp);
                Log.d("", "VALUE SET " + toSeaters);
                fromSeaters.setText("Max " + maxp);
                Log.d("", "VALUE SET " + fromSeaters);
                passengerChanged = true;
                dialog.dismiss();
                FilterData();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                min = 0;
                max = 100000;
                to.setText("Min " + min);
                from.setText("Max " + max);
                priceChanged = false;

                minp = 1;
                maxp = 10;
                toSeaters.setText("Min " + minp);
                fromSeaters.setText("Max " + maxp);
                passengerChanged = false;
                dialog.dismiss();
                RefreshData();
            }
        });

    }
    public void FilterData() {

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        curUser = auth.getCurrentUser();
        String userId = null;
        if (curUser != null) {
            userId = curUser.getUid(); //Do what you need to do with the id
        }
        db.collection("Car")
//                .whereGreaterThan("Amount",min).whereLessThan("Amount",max)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Filterrrrrr Data", "filter calll") ;
                                Log.d("", document.getId() + " => " + document.getData());
                                System.out.println(document.getId() + " => " + document.getData());
                                Log.d("Filterrrrrr Data", "filter calll2") ;
                                String Model = (String) document.getData().get("Model");
                                Float Amount = Float.parseFloat(String.valueOf(document.getData().get("Amount")));
                                String Conditon=(String) document.getData().get("Conditon");
                                Log.d("Filterrrrrr Data", "filter calll3") ;
                                int Seaters=Integer.parseInt(String.valueOf(document.getData().get("Seaters")));
                                Log.d("Filterrrrrr Data", "filter calll4") ;
                                String UserID=document.getId();


                                Log.d("Filterrrrrr Data", "filter modellll" + Model);
                                Log.d("Filterrrrrr Data", "filter amounttttt" +   Amount);
                                if (priceChanged && passengerChanged) {
                                    if (Amount >= min && Amount <= max && Seaters >= minp && Seaters <= maxp) {

                                        VerticalList.clear();
                                        getImage(UserID, Model, Amount.toString(), Conditon);
                                        setVertical();
                                    }
                                    else {
                                        VerticalList.clear();

                                        setVertical();
//                                        Toast.makeText(getActivity().getApplicationContext(),"Oops No Cars Found",Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), "else call", Toast.LENGTH_SHORT).show();
                                }


                            }

                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


}