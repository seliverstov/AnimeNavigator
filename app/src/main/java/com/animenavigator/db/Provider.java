package com.animenavigator.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import static com.animenavigator.db.Contract.*;

/**
 * Created by a.g.seliverstov on 04.04.2016.
 */
public class Provider extends ContentProvider{
    private static final UriMatcher sUriMatcher;

    static final int MANGA = 100;
    static final int MANGA_WITH_ID = 101;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;
        sUriMatcher.addURI(authority, PATH_MANGA, MANGA);
        sUriMatcher.addURI(authority, PATH_MANGA+"/#", MANGA_WITH_ID);
    }

    private DbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db =mDbHelper.getReadableDatabase();
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case MANGA:
                retCursor = db.query(MangaEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case MANGA_WITH_ID:{
                String id = String.valueOf(ContentUris.parseId(uri));
                retCursor = db.query(MangaEntry.TABLE_NAME,projection,MangaEntry._ID+" = ?",new String[]{id},null,null,sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unsupported uri: "+uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch(sUriMatcher.match(uri)){
            case MANGA:
                return MangaEntry.CONTENT_DIR_TYPE;
            case MANGA_WITH_ID:
                return MangaEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unsupported uri: "+uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)){
            case MANGA: {
                long id = db.insertWithOnConflict(MangaEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_NONE);
                if (id != -1)
                    returnUri = ContentUris.withAppendedId(MangaEntry.CONTENT_URI, id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unsupported uri: "+uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db =mDbHelper.getWritableDatabase();
        int deletedRows;

        switch(sUriMatcher.match(uri)){
            case MANGA: {
                deletedRows = db.delete(MangaEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unsupported uri: "+uri);
        }

        if (deletedRows>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int updatedRows;

        switch(sUriMatcher.match(uri)){
            case MANGA:{
                updatedRows = db.update(MangaEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unsupported uri: "+uri);
        }

        if (updatedRows>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return updatedRows;
    }

    @Override
    public void shutdown() {
        mDbHelper.close();
        super.shutdown();
    }
}
