package com.example.habithive.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OnBoardingAdapter extends FragmentStateAdapter {
    public OnBoardingAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public Fragment createFragment(int position)
    {
        return position == 0 ? new OnBoardingFragment1() : new OnBoardingFragment2();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
