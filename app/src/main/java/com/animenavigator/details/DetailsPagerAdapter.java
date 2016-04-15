package com.animenavigator.details;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.animenavigator.R;
import com.animenavigator.common.Const;

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
            case Const.SUMMARY_TAB: return SummaryFragment.newInstance(mMangaUri);
            case Const.RELATED_TAB: return RelatedFragment.newInstance(mMangaUri);
            case Const.EXTRA_TAB: return ExtraFragment.newInstance(mMangaUri);
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
            case Const.SUMMARY_TAB: return mContext.getString(R.string.summary);
            case Const.RELATED_TAB: return mContext.getString(R.string.related);
            case Const.EXTRA_TAB: return mContext.getString(R.string.extra);
            default: return null;
        }
    }
}
