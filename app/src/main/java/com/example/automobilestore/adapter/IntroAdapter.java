package com.example.automobilestore.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.automobilestore.IntroFirstFragment;
import com.example.automobilestore.IntroSecondFragment;
import com.example.automobilestore.IntroThreeFragment;

import org.jetbrains.annotations.NotNull;

public class IntroAdapter extends FragmentPagerAdapter {


    public IntroAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new IntroFirstFragment();
            case 1: return new IntroSecondFragment();
            case 2: return new IntroThreeFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
