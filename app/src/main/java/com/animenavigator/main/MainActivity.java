package com.animenavigator.main;

import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

import com.animenavigator.common.Const;
import com.animenavigator.db.Contract;
import com.animenavigator.details.DetailsActivity;
import com.animenavigator.details.DetailsFragment;
import com.animenavigator.common.ItemSelectedCallback;
import com.animenavigator.R;
import com.animenavigator.sync.SyncAdapter;

public class MainActivity extends AppCompatActivity implements ItemSelectedCallback {
    private static final String DETAILS_FRAGMENT_TAG = "DETAILS_FRAGMENT_TAG";
    private DrawerLayout mDrawerLayout;
    private boolean mTwoPane;

    public static final String EXTRA_START_TAB = "EXTRA_START_TAB";

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

        if (navigationView!=null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    item.setChecked(true);
                    switch (item.getItemId()) {
                        case R.id.anime_and_manga:
                            sp.edit().putString(Const.SP_ANIME_TYPE_KEY, null).apply();
                            break;
                        case R.id.anime:
                            sp.edit().putString(Const.SP_ANIME_TYPE_KEY, Const.SP_ANIME_TYPE).apply();
                            break;
                        case R.id.manga:
                            sp.edit().putString(Const.SP_ANIME_TYPE_KEY, Const.SP_MANGA_TYPE).apply();
                            break;
                        default:
                            Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                    mDrawerLayout.closeDrawers();
                    return true;
                }
            });
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            String type = sp.getString(Const.SP_ANIME_TYPE_KEY,null);
            if (Const.SP_ANIME_TYPE.equals(type)) navigationView.setCheckedItem(R.id.anime);
            else if (Const.SP_MANGA_TYPE.equals(type)) navigationView.setCheckedItem(R.id.manga);
            else navigationView.setCheckedItem(R.id.anime_and_manga);
        }

        if (findViewById(R.id.two_pane_layout)!=null){
            mTwoPane = true;
            if (savedInstanceState==null || getSupportFragmentManager().findFragmentByTag(DETAILS_FRAGMENT_TAG)==null){
                getSupportFragmentManager().beginTransaction().replace(R.id.details_container,new DetailsFragment(),DETAILS_FRAGMENT_TAG).commit();
            }
        }else{
            mTwoPane = false;
        }

        SyncAdapter.initializeSyncAdapter(this);

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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras()!=null && intent.getExtras().containsKey(EXTRA_START_TAB)) {
            MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
            if (fragment != null) {
                fragment.setCurrentTab(intent.getIntExtra(EXTRA_START_TAB, fragment.getCurrentTab()));
            }
        }
    }
}
