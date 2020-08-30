package com.swia.iabuilder.views.pageradapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class CollectionPagerAdapter extends FragmentPagerAdapter {

    private int count = 0;
    private final ArrayList<String> titles = new ArrayList<>();
    private final ArrayList<Fragment> fragments = new ArrayList<>();

    public CollectionPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    public void add(Fragment fragment, String title) {
        fragments.add(fragment);
        titles.add(title);
        count++;
    }
}
