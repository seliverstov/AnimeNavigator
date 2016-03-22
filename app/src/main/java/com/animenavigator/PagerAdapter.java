package com.animenavigator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by alexander on 19.03.2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0 : return new ListFragment();
            case 1 : return new SearchFragment();
            default: return PageFragment.newInstance(position);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return mContext.getString(R.string.tab1);
            case 1: return mContext.getString(R.string.tab2);
            case 2: return mContext.getString(R.string.tab3);
            default: return "Tab "+position;
        }
    }
}
