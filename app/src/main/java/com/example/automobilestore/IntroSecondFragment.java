package com.example.automobilestore;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class IntroSecondFragment extends Fragment {

    TextView next,back,skip;
    ViewPager viewPager;

    public IntroSecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro_second,container,false);
        viewPager = getActivity().findViewById(R.id.viewpager);

        next = view.findViewById(R.id.slideTwoNext);
        back = view.findViewById(R.id.slideTwoBack);
        skip = view.findViewById(R.id.skipTwo);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
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