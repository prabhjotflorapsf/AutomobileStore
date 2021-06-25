package com.example.automobilestore.ui.home;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    FloatingActionButton add_btn;
    RecyclerView popularRecycler , asiaRecycler;
    Vertical_Car_Adapter asiaFoodAdapter;

    Horizontal_Car_Adapter popularFoodAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        add_btn=v.findViewById(R.id.add_post_btn);
        popularRecycler = v.findViewById(R.id.rv_hcar);
        asiaRecycler = v.findViewById(R.id.rv_vcar);

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
        List<HorizontalCarData> popularFoodList = new ArrayList<>();

        popularFoodList.add(new HorizontalCarData("Honda", "$19000", R.drawable.logo));
        popularFoodList.add(new HorizontalCarData("Hyundai", "$17000", R.drawable.logo));
        popularFoodList.add(new HorizontalCarData("Toyota", "$25000", R.drawable.logo));
        popularFoodList.add(new HorizontalCarData("Ford", "$7000", R.drawable.logo));
        popularFoodList.add(new HorizontalCarData("Mazda", "$17899", R.drawable.logo));
        popularFoodList.add(new HorizontalCarData("Suzuki", "$5670", R.drawable.logo));
        setPopularRecycler(popularFoodList);

        List<VerticalCarData> asiaFoodList = new ArrayList<>();
        asiaFoodList.add(new VerticalCarData("Honda", "$19000", R.drawable.logo,  "OLD"));
        asiaFoodList.add(new VerticalCarData("Hyundai", "$17000", R.drawable.logo,  "OLD"));
        asiaFoodList.add(new VerticalCarData("Toyota", "$25000", R.drawable.logo,  "NEW"));
        asiaFoodList.add(new VerticalCarData("Ford", "$7000", R.drawable.logo, "NEW"));
        asiaFoodList.add(new VerticalCarData("Mazda", "$17899", R.drawable.logo, "OLD"));
        asiaFoodList.add(new VerticalCarData("Suzuki", "$5670", R.drawable.logo, "NEW"));

        setAsiaRecycler(asiaFoodList);

//
//
    }
    private void setPopularRecycler(List<HorizontalCarData> popularFoodList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        popularRecycler.setLayoutManager(layoutManager);
        popularFoodAdapter = new Horizontal_Car_Adapter(getActivity(), popularFoodList);
        popularRecycler.setAdapter(popularFoodAdapter);
    }
    private void setAsiaRecycler(List<VerticalCarData> asiaFoodList) {


        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        asiaRecycler.setLayoutManager(layoutManager);
        asiaFoodAdapter = new Vertical_Car_Adapter(getActivity(), asiaFoodList);
        asiaRecycler.setAdapter(asiaFoodAdapter);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


//    private void AddPost() {
//        Toast.makeText(getActivity(),"hello",Toast.LENGTH_LONG).show();
//    }



}