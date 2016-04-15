package com.animenavigator.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.animenavigator.Application;
import com.animenavigator.R;
import com.animenavigator.common.Const;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

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
                Tracker tracker = ((Application)getActivity().getApplication()).getDefaultTracker();
                switch(position){
                    case Const.TOP_RATED_TAB:
                        tracker.setScreenName(getString(R.string.top_rated_tab_screen_name));
                        break;
                    case Const.SEARCH_TAB:
                        tracker.setScreenName(getString(R.string.search_tab_screen_name));
                        break;
                    case Const.NEW_TAB:
                        tracker.setScreenName(getString(R.string.new_tab_screen_name));
                        break;
                }
                tracker.send(new HitBuilders.ScreenViewBuilder().build());
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
}
