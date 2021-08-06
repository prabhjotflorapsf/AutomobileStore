package com.example.automobilestore;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class IntroFirstFragment extends Fragment {

    TextView next,skip;
    ViewPager viewPager;


    public IntroFirstFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_intro_first,container,false);
        viewPager = getActivity().findViewById(R.id.viewpager);

        next = view.findViewById(R.id.slideOneNext);
        skip = view.findViewById(R.id.skipOne);

        next.setOnClickListener(new View.OnClickListener() {
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