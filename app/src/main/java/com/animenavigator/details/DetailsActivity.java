package com.animenavigator.details;

import android.content.ContentUris;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.animenavigator.R;
import com.animenavigator.common.ItemSelectedCallback;
import com.animenavigator.db.Contract;

/**
 * Created by a.g.seliverstov on 23.03.2016.
 */
public class DetailsActivity extends AppCompatActivity implements ItemSelectedCallback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState==null) {
            Bundle args = new Bundle();
            args.putParcelable(DetailsFragment.MANGA_URI_KEY,getIntent().getData());
            DetailsFragment detailsFragment = new DetailsFragment();
            detailsFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.details_container, detailsFragment).commit();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onItemSelected(int id) {
        onItemSelected(id, null);
    }

    @Override
    public void onItemSelected(int id, View view) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.setData(ContentUris.withAppendedId(Contract.MangaEntry.CONTENT_URI, id));
        if (view!=null && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view.findViewById(R.id.item_poster), getString(R.string.poster_transition));
            ActivityCompat.startActivity(this, intent, options.toBundle());
        }else {
            startActivity(intent);
        }
    }
}
