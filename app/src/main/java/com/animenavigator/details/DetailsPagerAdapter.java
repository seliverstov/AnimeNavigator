package com.animenavigator.details;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.animenavigator.common.PageFragment;
import com.animenavigator.R;

/**
 * Created by a.g.seliverstov on 23.03.2016.
 */
public class DetailsPagerAdapter extends FragmentStatePagerAdapter {
    Context mContext;

    public DetailsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext =  context;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return mContext.getString(R.string.summary);
            case 1: return mContext.getString(R.string.related);
            case 2: return mContext.getString(R.string.extra);
            default: return "Tab "+position;
        }
    }
}
