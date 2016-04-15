package com.animenavigator.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.animenavigator.R;
import com.animenavigator.common.AppTracker;

/**
 * Created by a.g.seliverstov on 29.03.2016.
 */
public class MainFragment extends Fragment{

    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.main_fragment,container,false);
        MainPagerAdapter adapter = new MainPagerAdapter(getActivity().getSupportFragmentManager(),getActivity());

        mViewPager = (ViewPager)view.findViewById(R.id.main_viewpager);
        if (mViewPager !=null) {
            mViewPager.setAdapter(adapter);
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                AppTracker.trackMainScreen(getActivity(), position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.main_tablayout);
        if (tabLayout !=null)
            tabLayout.setupWithViewPager(mViewPager);
        return view;
    }

    public void setCurrentTab(int tab){
        mViewPager.setCurrentItem(tab);
    }

    public int getCurrentTab(){
        return mViewPager.getCurrentItem();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mViewPager!=null) {
            AppTracker.trackMainScreen(getActivity(), mViewPager.getCurrentItem());
        }
    }
}
