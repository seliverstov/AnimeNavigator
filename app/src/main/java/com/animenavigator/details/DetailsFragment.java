package com.animenavigator.details;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.animenavigator.common.Const;
import com.animenavigator.common.ImageLoader;
import com.animenavigator.R;
import com.animenavigator.db.Contract;
import com.animenavigator.model.Anime;
import com.animenavigator.utils.ScreenShotUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by a.g.seliverstov on 29.03.2016.
 */
public class DetailsFragment extends Fragment {
    public static final String MANGA_URI_KEY = "MANGA_URI_KEY";
    private static final int REQUEST_WRITE_STORAGE = 100;
    private int MANGA_CURSOR_LOADER_ID = 4;
    private View mView;
    private Uri mMangaUri;
    private String mTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();

        if (args==null || args.getParcelable(MANGA_URI_KEY)==null){
            mView = inflater.inflate(R.layout.no_details,container,false);
            return mView;
        }
        mMangaUri = getArguments().getParcelable(MANGA_URI_KEY);
        mView = inflater.inflate(R.layout.details_fragment, container, false);

        Toolbar toolbar = (Toolbar) mView.findViewById(R.id.details_toolbar);
        if (toolbar!=null){
            toolbar.inflateMenu(R.menu.menu);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch(item.getItemId()){
                        case R.id.action_share: {
                            actionShare();
                            return true;
                        }
                        default:
                            return false;
                    }
                }
            });
        }

        if (getActivity() instanceof DetailsActivity){
            AppCompatActivity activity = (AppCompatActivity)getActivity();
            activity.setSupportActionBar(toolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar!=null){
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
        return mView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof DetailsActivity){
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_share: {
                actionShare();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MANGA_CURSOR_LOADER_ID, null, new CursorLoaderCallback(getActivity()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    sp.edit().putInt(Const.SP_WRITE_EXTERNAL_STORAGE_KEY, PackageManager.PERMISSION_GRANTED).apply();
                    shareScreenShot();
                } else {
                    sp.edit().putInt(Const.SP_WRITE_EXTERNAL_STORAGE_KEY, PackageManager.PERMISSION_DENIED).apply();
                    Toast.makeText(getActivity(), getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    class CursorLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
        private Context mContext;

        public CursorLoaderCallback(Context context){
            mContext = context;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (getArguments()==null || getArguments().getParcelable(MANGA_URI_KEY)==null) return null;
            Uri uri = getArguments().getParcelable(MANGA_URI_KEY);
            return new CursorLoader(
                    mContext,
                    uri,
                    null,
                    null,
                    null,
                    null);

        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data.moveToFirst()) {
                final Anime anime = Anime.fromCursor(data);

                mTitle =anime.title;

                final TextView title = (TextView) mView.findViewById(R.id.title);
                final TextView rating = (TextView) mView.findViewById(R.id.rating);
                final TextView type = (TextView) mView.findViewById(R.id.type);
                final TextView vintage = (TextView) mView.findViewById(R.id.vintage);

                if (title != null)
                    title.setText(anime.title);
                if (rating != null)
                    rating.setText(getContext().getString(R.string.rating_tmp, new DecimalFormat("#.#").format(anime.rating)));
                if (type != null) {
                    type.setText(Character.toUpperCase(anime.type.charAt(0)) + anime.type.substring(1));
                }
                if (vintage!=null){
                    vintage.setText(anime.vintage);
                }

                final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) mView.findViewById(R.id.collapsing_toolbar);

                if (collapsingToolbarLayout != null) {
                    collapsingToolbarLayout.setTitle(anime.title);
                    collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
                }

                final LinearLayout header = (LinearLayout) mView.findViewById(R.id.header);

                final FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.favorite);

                if (fab!=null) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                    List<String> favoriteList = new ArrayList<>(Arrays.asList(sp.getString(Const.SP_FAVORITE_LIST_KEY, "").split(",")));
                    if (!favoriteList.contains(String.valueOf(anime._id))) {
                        fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.btn_star_big_off));
                    } else {
                        fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.btn_star_big_on));
                    }

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                            List<String> favoriteList = new ArrayList<>(Arrays.asList(sp.getString(Const.SP_FAVORITE_LIST_KEY, "").split(",")));
                            if (favoriteList.contains(String.valueOf(anime._id))){
                                fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.btn_star_big_off));
                                favoriteList.remove(String.valueOf(anime._id));
                                sp.edit().putString(Const.SP_FAVORITE_LIST_KEY,getFavoriteString(favoriteList)).apply();
                            }else{
                                fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.btn_star_big_on));
                                favoriteList.add(String.valueOf(anime._id));
                                sp.edit().putString(Const.SP_FAVORITE_LIST_KEY, getFavoriteString(favoriteList)).apply();
                            }
                            getContext().getContentResolver().notifyChange(Contract.MangaEntry.buildFavorite(), null);
                            Intent widgetUpdateIntent = new Intent();
                            widgetUpdateIntent.setAction(Const.ACTION_FAVORITE_UPDATED);
                            getContext().sendBroadcast(widgetUpdateIntent);
                        }
                    });
                }

                Uri uri = getArguments().getParcelable(MANGA_URI_KEY);
                DetailsPagerAdapter adapter = new DetailsPagerAdapter(getActivity().getSupportFragmentManager(), getActivity(), uri);

                ViewPager viewPager = (ViewPager) mView.findViewById(R.id.details_viewpager);
                if (viewPager != null) {
                    viewPager.setAdapter(adapter);
                }

                final TabLayout tabLayout = (TabLayout) mView.findViewById(R.id.details_tablayout);
                if (tabLayout != null)
                    tabLayout.setupWithViewPager(viewPager);

                final ImageView poster = (ImageView) mView.findViewById(R.id.poster);

                Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                if (poster != null)
                                    poster.setImageBitmap(bitmap);
                                Palette.Swatch swatch = palette.getVibrantSwatch();
                                if (swatch == null) {
                                    for (Palette.Swatch s : palette.getSwatches()) {
                                        if (s != null) {
                                            swatch = s;
                                            break;
                                        }
                                    }
                                }
                                if (swatch != null) {
                                    if (header != null) {
                                        header.setBackgroundColor(swatch.getRgb());
                                    }
                                    if (title!=null){
                                        title.setTextColor(swatch.getBodyTextColor());
                                    }
                                    if (rating!=null){
                                        rating.setTextColor(swatch.getTitleTextColor());
                                    }
                                    if (type!=null){
                                        type.setTextColor(swatch.getTitleTextColor());
                                    }
                                    if (vintage!=null){
                                        vintage.setTextColor(swatch.getTitleTextColor());
                                    }
                                    if (tabLayout != null) {
                                        tabLayout.setBackgroundColor(swatch.getRgb());
                                        tabLayout.setTabTextColors(swatch.getTitleTextColor(),swatch.getBodyTextColor());
                                        tabLayout.setSelectedTabIndicatorColor(swatch.getBodyTextColor());
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };

                ImageLoader.loadImageToView(anime.posterUrl, getActivity(), target);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    protected void shareScreenShot(){
        ScreenShotUtils screenShotUtils = new ScreenShotUtils(getContext());
        File file = screenShotUtils.storeScreenShot(screenShotUtils.getScreenShot(mView), getString(R.string.screenshot_file_tmp, ContentUris.parseId(mMangaUri)));
        if (file!=null){
            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, mTitle);
            intent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.ANN_URL, ContentUris.parseId(mMangaUri)));
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
        }else{
            shareLink();
        }
    }

    protected void shareLink(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, mTitle);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.ANN_URL, ContentUris.parseId(mMangaUri)));
        startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
    }

    protected void actionShare(){
        boolean hasPermission = (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
            if (sp.getInt(Const.SP_WRITE_EXTERNAL_STORAGE_KEY, PackageManager.PERMISSION_GRANTED)!=PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            }else{
                Toast.makeText(getActivity(), getString(R.string.permission_remainder), Toast.LENGTH_LONG).show();
                shareLink();
            }
        }else{
            shareScreenShot();
        }
    }

    protected String getFavoriteString(List<String> list){
        StringBuilder sb = new StringBuilder();
        for(String s:list){
            sb.append(s);
            sb.append(",");
        }
        String result =  sb.toString();
        return (result.length()>0)?result.substring(0,result.length()-1):result;
    }
}
