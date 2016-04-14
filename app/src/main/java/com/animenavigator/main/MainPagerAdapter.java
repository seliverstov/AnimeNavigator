package com.animenavigator.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.animenavigator.R;
import com.animenavigator.common.Const;


/**
 * Created by alexander on 19.03.2016.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {
    public static final int TOP_RATED_TAB = 0;
    public static final int SEARCH_TAB = 1;
    public static final int NEW_TAB = 2;

    private Context mContext;
    public MainPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case TOP_RATED_TAB : return GridFragment.newInstance(Const.TOPRATED_CURSOR_LOADER_ID);
            case SEARCH_TAB : return SearchFragment.newInstance(Const.SEARCH_CURSOR_LOADER_ID);
            case NEW_TAB : return ListFragment.newInstance(Const.NEW_CURSOR_LOADER_ID);
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case TOP_RATED_TAB: return mContext.getString(R.string.tab1);
            case SEARCH_TAB: return mContext.getString(R.string.tab2);
            case NEW_TAB: return mContext.getString(R.string.tab3);
            default: return null;
        }
    }
}
