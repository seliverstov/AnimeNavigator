package com.animenavigator.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.animenavigator.common.PagerAdapter;
import com.animenavigator.R;

/**
 * Created by a.g.seliverstov on 29.03.2016.
 */
public class MainFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.main_fragment,container,false);
        PagerAdapter adapter = new PagerAdapter(getActivity().getSupportFragmentManager(),getActivity());

        ViewPager viewPager = (ViewPager)view.findViewById(R.id.main_viewpager);
        if (viewPager!=null) {
            viewPager.setAdapter(adapter);

        }

        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.main_tablayout);
        if (tabLayout!=null)
            tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}
