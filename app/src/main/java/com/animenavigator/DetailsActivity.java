package com.animenavigator;

import android.content.ContentUris;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.animenavigator.model.Anime;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by a.g.seliverstov on 23.03.2016.
 */
public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        int id = (int)ContentUris.parseId(getIntent().getData());

        final Anime anime = Anime.find(id);

        TextView title = (TextView)findViewById(R.id.title);
        TextView rating = (TextView)findViewById(R.id.rating);

        if (title!=null)
            title.setText(anime.title);
        if (rating!=null)
            rating.setText(anime.rating);

        final Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);

        if (collapsingToolbarLayout!=null){
            collapsingToolbarLayout.setTitle(anime.title);
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        }

        final LinearLayout header = (LinearLayout)findViewById(R.id.header);

        DetailsPagerAdapter adapter = new DetailsPagerAdapter(getSupportFragmentManager(),this);

        ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);
        if (viewPager!=null) {
            viewPager.setAdapter(adapter);
        }

        final TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        if (tabLayout!=null)
            tabLayout.setupWithViewPager(viewPager);

        final ImageView poster = (ImageView)findViewById(R.id.poster);

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

        Picasso.with(this).load(anime.posterUrl).into(target);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
