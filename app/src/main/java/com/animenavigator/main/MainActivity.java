package com.animenavigator.main;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.animenavigator.db.Contract;
import com.animenavigator.details.DetailsActivity;
import com.animenavigator.details.DetailsFragment;
import com.animenavigator.common.ItemSelectedCallback;
import com.animenavigator.R;

public class MainActivity extends AppCompatActivity implements ItemSelectedCallback {
    private static final String DETAILS_FRAGMENT_TAG = "DETAILS_FRAGMENT_TAG";
    private DrawerLayout mDrawerLayout;
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);

        if (navigationView!=null)
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    item.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

        if (findViewById(R.id.two_pane_layout)!=null){
            mTwoPane = true;
            if (savedInstanceState==null || getSupportFragmentManager().findFragmentByTag(DETAILS_FRAGMENT_TAG)==null){
                getSupportFragmentManager().beginTransaction().replace(R.id.details_container,new DetailsFragment(),DETAILS_FRAGMENT_TAG).commit();
            }
        }else{
            mTwoPane = false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onItemSelected(int id) {
        if (mTwoPane){
            Bundle args = new Bundle();
            args.putParcelable(DetailsFragment.MANGA_URI_KEY, ContentUris.withAppendedId(Contract.MangaEntry.CONTENT_URI, id));
            DetailsFragment detailsFragment = new DetailsFragment();
            detailsFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.details_container,detailsFragment,DETAILS_FRAGMENT_TAG).commit();
        }else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.setData(ContentUris.withAppendedId(Contract.MangaEntry.CONTENT_URI, id));
            startActivity(intent);
        }
    }
}
