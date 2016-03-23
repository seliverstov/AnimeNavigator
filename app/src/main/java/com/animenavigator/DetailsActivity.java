package com.animenavigator;

import android.content.ContentUris;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.animenavigator.model.Anime;
import com.squareup.picasso.Picasso;

/**
 * Created by a.g.seliverstov on 23.03.2016.
 */
public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        int id = (int)ContentUris.parseId(getIntent().getData());

        Anime anime = Anime.find(id);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ImageView poster = (ImageView)findViewById(R.id.poster);

        Picasso.with(this).load(anime.posterUrl).into(poster);

        DetailsPagerAdapter adapter = new DetailsPagerAdapter(getSupportFragmentManager(),this);

        ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);
        if (viewPager!=null) {
            viewPager.setAdapter(adapter);
        }

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        if (tabLayout!=null)
            tabLayout.setupWithViewPager(viewPager);


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
