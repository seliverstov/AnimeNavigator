package com.animenavigator.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.animenavigator.R;
import com.animenavigator.common.PageFragment;
import com.animenavigator.main.GridFragment;
import com.animenavigator.main.ListFragment;
import com.animenavigator.main.SearchFragment;

/**
 * Created by alexander on 19.03.2016.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    public MainPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0 : return GridFragment.newInstance(position);
            case 1 : return SearchFragment.newInstance(position);
            case 2 : return ListFragment.newInstance(position);
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
