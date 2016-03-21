package com.animenavigator;

import android.content.Context;
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
            case 0: return mContext.getString(R.string.top_rated_tab_caption);
            default: return "Tab "+position;
        }
    }
}
