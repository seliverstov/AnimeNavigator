package com.animenavigator.details;

import android.content.ContentUris;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;

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
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onItemSelected(int id) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.setData(ContentUris.withAppendedId(Contract.MangaEntry.CONTENT_URI, id));
        startActivity(intent);
    }
}
