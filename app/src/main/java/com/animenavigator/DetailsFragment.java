package com.animenavigator;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.animenavigator.model.Anime;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by a.g.seliverstov on 29.03.2016.
 */
public class DetailsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();

        if (args==null){
            return inflater.inflate(R.layout.no_details,container,false);
        }

        final View view = inflater.inflate(R.layout.details_fragment, container, false);

        if (getActivity() instanceof DetailsActivity){
            AppCompatActivity activity = (AppCompatActivity)getActivity();
            Toolbar toolbar = (Toolbar)view.findViewById(R.id.details_toolbar);
            activity.setSupportActionBar(toolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar!=null){
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        final Anime anime = Anime.find(args.getInt("_ID"));

        TextView title = (TextView)view.findViewById(R.id.title);
        TextView rating = (TextView)view.findViewById(R.id.rating);

        if (title!=null)
            title.setText(anime.title);
        if (rating!=null)
            rating.setText(anime.rating);


        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)view.findViewById(R.id.collapsing_toolbar);

        if (collapsingToolbarLayout!=null){
            collapsingToolbarLayout.setTitle(anime.title);
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        }

        final LinearLayout header = (LinearLayout)view.findViewById(R.id.header);

        DetailsPagerAdapter adapter = new DetailsPagerAdapter(getActivity().getSupportFragmentManager(),getActivity());

        ViewPager viewPager = (ViewPager)view.findViewById(R.id.details_viewpager);
        if (viewPager!=null) {
            viewPager.setAdapter(adapter);
        }

        final TabLayout tabLayout = (TabLayout)view.findViewById(R.id.details_tablayout);
        if (tabLayout!=null)
            tabLayout.setupWithViewPager(viewPager);

        final ImageView poster = (ImageView)view.findViewById(R.id.poster);

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        if (poster!=null)
                            poster.setImageBitmap(bitmap);
                        Palette.Swatch swatch = palette.getVibrantSwatch();
                        if (swatch==null) {
                            for (Palette.Swatch s : palette.getSwatches()) {
                                if (s != null) {
                                    swatch = s;
                                    break;
                                }
                            }
                        }
                        if (swatch!=null) {
                            if (header!=null){
                                header.setBackgroundColor(swatch.getRgb());
                            }
                            /*if (collapsingToolbarLayout!=null) {
                                collapsingToolbarLayout.setBackgroundColor(swatch.getRgb());
                                collapsingToolbarLayout.setContentScrimColor(swatch.getRgb());
                            }*/
                            if (tabLayout!=null)
                                tabLayout.setBackgroundColor(swatch.getRgb());
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
        return view;
    }
}
