package com.example.automobilestore;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class IntroThreeFragment extends Fragment {
    TextView done,back,skip;
    ViewPager viewPager;

    public IntroThreeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro_three,container,false);
        viewPager = getActivity().findViewById(R.id.viewpager);

        done = view.findViewById(R.id.slideThreeFinish);
        back = view.findViewById(R.id.slideThreeBack);
        skip = view.findViewById(R.id.skipThree);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(),MainActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(),MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}