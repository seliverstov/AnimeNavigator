package com.animenavigator.db;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


/**
 * Created by a.g.seliverstov on 04.04.2016.
 */
public class DbHelper extends SQLiteAssetHelper {
    private static final String DB_NAME = "anime.db";
    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

}
