package com.example.automobilestore.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.automobilestore.Activity.PostAd;
import com.example.automobilestore.R;
import com.example.automobilestore.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    FloatingActionButton add_btn;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        add_btn=v.findViewById(R.id.add_post_btn);
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


//    private void AddPost() {
//        Toast.makeText(getActivity(),"hello",Toast.LENGTH_LONG).show();
//    }



}