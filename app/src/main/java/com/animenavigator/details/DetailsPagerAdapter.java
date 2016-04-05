package com.animenavigator.details;

import android.content.Context;
import android.net.Uri;
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
    Uri mMangaUri;

    public DetailsPagerAdapter(FragmentManager fm, Context context, Uri mangaUri) {
        super(fm);
        mContext =  context;
        mMangaUri = mangaUri;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return SummaryFragment.newInstance(mMangaUri);
            case 1: return PageFragment.newInstance(position);
            case 2: return PageFragment.newInstance(position);
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
            case 0: return mContext.getString(R.string.summary);
            case 1: return mContext.getString(R.string.related);
            case 2: return mContext.getString(R.string.extra);
            default: return "Tab "+position;
        }
    }
}
